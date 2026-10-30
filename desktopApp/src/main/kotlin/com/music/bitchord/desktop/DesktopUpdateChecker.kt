package com.music.bitchord.desktop

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Looks at the repo's "latest release" on GitHub once per launch and says whether it is newer
 * than the running build. The installer is not run from here: the dialog opens the asset in the
 * browser, so the user installs it the way they installed the first one.
 *
 * GitHub's "latest" skips pre-releases and drafts, so a `-beta` desktop build is only ever
 * nudged toward a published release.
 */
internal object DesktopUpdateChecker {

    data class UpdateInfo(
        val version: String,
        val releaseUrl: String,
        /** The installer for this platform, or null when the release has none. */
        val downloadUrl: String?,
        val notes: String?,
    )

    private const val DEFAULT_RELEASE_PAGE =
        "https://github.com/Inventor365/BitChord-PC/releases/latest"

    private const val OTA_TOKEN_URL =
        "https://raw.githubusercontent.com/Inventor365/Bitchord-OTA/refs/heads/main/ota.json?token=GHSAT0AAAAAADUIC3ZQ4LG6AXEKYTMTOL2I2XE3AZA"

    private const val OTA_CLEAN_URL =
        "https://raw.githubusercontent.com/Inventor365/Bitchord-OTA/refs/heads/main/ota.json"

    private const val REPO_OTA_URL =
        "https://raw.githubusercontent.com/Inventor365/BitChord-PC/main/ota.json"

    private const val LATEST_RELEASE_URL =
        "https://api.github.com/repos/Inventor365/BitChord-PC/releases/latest"

    val currentVersion: String = System.getProperty("bitchord.version") ?: "1.9"

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private val http = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 15_000
            connectTimeoutMillis = 10_000
        }
    }

    suspend fun check(): UpdateInfo? {
        val customUrl = System.getProperty("bitchord.ota.url")?.trim()?.takeIf { it.isNotEmpty() }
            ?: System.getenv("OTA_JSON_URL")?.trim()?.takeIf { it.isNotEmpty() }
        val endpoints = listOfNotNull(
            customUrl,
            OTA_TOKEN_URL,
            OTA_CLEAN_URL,
            REPO_OTA_URL,
            LATEST_RELEASE_URL,
        ).distinct()

        for (endpoint in endpoints) {
            val info = runCatching { fetchUpdateFrom(endpoint) }.getOrNull()
            if (info != null) {
                return if (isNewer(info.version, currentVersion)) info else null
            }
        }
        return null
    }

    private suspend fun fetchText(url: String): String? = runCatching {
        val response = http.get(url) {
            if (url.contains("api.github.com")) {
                header("Accept", "application/vnd.github+json")
            }
        }
        if (!response.status.isSuccess()) null else response.bodyAsText()
    }.getOrNull()

    private suspend fun fetchUpdateFrom(endpoint: String): UpdateInfo? {
        val body = fetchText(endpoint) ?: return null
        val obj = runCatching { json.parseToJsonElement(body) as? JsonObject }.getOrNull() ?: return null
        val externalNotes = obj.string("changelogUrl")?.takeIf { it.startsWith("http") }?.let { fetchText(it) }
        return parseOtaPayload(obj, externalNotes)
    }

    internal fun parseOtaPayload(
        obj: JsonObject,
        externalNotes: String? = null,
    ): UpdateInfo? {
        val rawVersion = obj.string("version")
            ?: obj.string("tag_name")
            ?: obj.string("latestVersion")
            ?: return null
        val latest = rawVersion.trim().removePrefix("v")
        if (latest.isEmpty()) return null

        val url = obj.string("releaseUrl")
            ?: obj.string("html_url")
            ?: obj.string("url")
            ?: DEFAULT_RELEASE_PAGE

        val inlineNotes = extractChangelogText(obj)
        val ext = externalNotes?.trim()?.takeIf { it.isNotEmpty() }
        val notes = when {
            !inlineNotes.isNullOrBlank() && !ext.isNullOrBlank() && inlineNotes != ext -> "$inlineNotes\n\n$ext"
            !inlineNotes.isNullOrBlank() -> inlineNotes
            else -> ext
        }

        return UpdateInfo(
            version = latest,
            releaseUrl = url,
            downloadUrl = resolveDesktopDownloadUrl(obj),
            notes = notes,
        )
    }

    private fun extractChangelogText(obj: JsonObject): String? {
        for (key in listOf("changelog", "changelogs", "notes", "body")) {
            val el = obj[key] ?: continue
            runCatching { el.jsonPrimitive.contentOrNull?.trim() }.getOrNull()
                ?.takeIf { it.isNotEmpty() }
                ?.let { return it }
            runCatching {
                el.jsonArray.mapNotNull { item -> item.jsonPrimitive.contentOrNull?.trim() }
                    .filter { it.isNotEmpty() }
                    .joinToString("\n")
            }.getOrNull()?.takeIf { it.isNotEmpty() }?.let { return it }
        }
        return null
    }

    private fun resolveDesktopDownloadUrl(obj: JsonObject): String? {
        val downloads = obj["downloads"] as? JsonObject
        val osKey = when {
            DesktopPlatform.isWindows -> "windows"
            DesktopPlatform.isMac -> "macos"
            DesktopPlatform.isLinux -> "linux"
            else -> null
        }
        if (osKey != null) {
            val node = downloads?.get(osKey) ?: obj[osKey]
            when (node) {
                is JsonObject -> {
                    when {
                        DesktopPlatform.isWindows ->
                            node.string("exe") ?: node.string("msi") ?: node.string("portable") ?: node.string("url") ?: node.string("downloadUrl")
                        DesktopPlatform.isMac -> {
                            val arch = System.getProperty("os.arch").lowercase()
                            val isArm = arch == "aarch64" || arch == "arm64"
                            if (isArm) node.string("arm64") ?: node.string("dmg") ?: node.string("pkg") ?: node.string("url")
                            else node.string("x64") ?: node.string("intel") ?: node.string("dmg") ?: node.string("pkg") ?: node.string("url")
                        }
                        DesktopPlatform.isLinux ->
                            node.string("appimage") ?: node.string("deb") ?: node.string("rpm") ?: node.string("url") ?: node.string("downloadUrl")
                        else -> null
                    }
                }
                else -> runCatching { node?.jsonPrimitive?.contentOrNull?.trim() }.getOrNull()
            }?.takeIf { it.isNotEmpty() }?.let { return it }
        }
        return installerUrl(obj)
    }

    private fun JsonObject.string(key: String): String? =
        runCatching { this[key]?.jsonPrimitive?.contentOrNull?.trim() }.getOrNull()?.takeIf { it.isNotEmpty() }

    /**
     * The release names its files `BitChord-<version>-windows-x64-setup.exe` and
     * `BitChord-<version>-linux-x86_64.AppImage` (see .github/workflows/release.yml); match on the
     * platform part so a version change does not matter.
     */
    private fun installerUrl(release: JsonObject): String? {
        val suffixes = when {
            DesktopPlatform.isWindows -> listOf("-windows-x64-setup.exe")
            DesktopPlatform.isLinux -> listOf(".AppImage", "-linux-amd64.deb")
            DesktopPlatform.isMac -> {
                val arch = System.getProperty("os.arch").lowercase()
                val isArm = arch == "aarch64" || arch == "arm64"
                if (isArm) listOf("-macos-arm64.dmg", "-macos-arm64.pkg", "-macos-arm64.zip")
                else listOf("-macos-x64.dmg", "-macos-x64.pkg", "-macos-x64.zip")
            }
            else -> return null
        }
        val assets = release["assets"]?.jsonArray?.mapNotNull { it as? JsonObject }.orEmpty()
        for (suffix in suffixes) {
            val hit = assets.firstOrNull { asset ->
                asset["name"]?.jsonPrimitive?.contentOrNull?.endsWith(suffix, ignoreCase = true) == true &&
                    asset["state"]?.jsonPrimitive?.contentOrNull == "uploaded"
            }
            hit?.get("browser_download_url")?.jsonPrimitive?.contentOrNull?.let { return it }
        }
        return null
    }

    private class Parsed(val parts: List<Int>, val preRelease: Boolean)

    private fun parse(raw: String): Parsed {
        val dash = raw.indexOf('-')
        val base = if (dash >= 0) raw.substring(0, dash) else raw
        return Parsed(base.split('.').map { it.toIntOrNull() ?: 0 }, dash >= 0)
    }

    /** Numeric comparison, with a `-betaN` build counted as older than the plain release it leads up to. */
    internal fun isNewer(latest: String, current: String): Boolean {
        val l = parse(latest)
        val c = parse(current)
        for (i in 0 until maxOf(l.parts.size, c.parts.size)) {
            val a = l.parts.getOrElse(i) { 0 }
            val b = c.parts.getOrElse(i) { 0 }
            if (a != b) return a > b
        }
        return c.preRelease && !l.preRelease
    }
}
