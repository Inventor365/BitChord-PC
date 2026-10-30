package com.music.bitchord

import com.music.bitchord.data.AppUpdateChecker
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class OtaUpdateCheckerTest {

    @Test
    fun `parses root ota json for Android phone and Android TV`() {
        val otaFile = File("ota.json").takeIf { it.exists() } ?: File("../ota.json")
        assertTrue("ota.json must exist at repository root", otaFile.exists())
        val payload = Json.parseToJsonElement(otaFile.readText()).jsonObject

        val phoneInfo = AppUpdateChecker.parseOtaPayload(
            payload = payload,
            isTv = false,
            supportedAbis = listOf("arm64-v8a"),
            fetchExternalText = { "Build date: 30/10/2026\n- Verified OTA" },
        )
        assertNotNull(phoneInfo)
        assertEquals("1.9", phoneInfo!!.version)
        assertEquals("https://github.com/Inventor365/BitChord-PC/releases/download/v1.9/BitChord-v1.9.apk", phoneInfo.apkUrl)
        assertTrue(phoneInfo.notes.orEmpty().contains("Bit-Perfect Output"))
        assertTrue(phoneInfo.notes.orEmpty().contains("Verified OTA"))

        val tvInfo = AppUpdateChecker.parseOtaPayload(
            payload = payload,
            isTv = true,
            supportedAbis = listOf("armeabi-v7a"),
        )
        assertNotNull(tvInfo)
        assertEquals("1.9", tvInfo!!.version)
        assertEquals("https://github.com/Inventor365/BitChord-PC/releases/download/v1.9/BitChordTV-v1.9.apk", tvInfo.apkUrl)
    }

    @Test
    fun `parses minimal single-link ota json`() {
        val minimal = Json.parseToJsonElement(
            """
            {
              "version": "v2.0",
              "downloadUrl": "https://example.com/BitChord-v2.0.apk",
              "changelog": "- Faster startup\n- New equalizer"
            }
            """.trimIndent(),
        ).jsonObject

        val info = AppUpdateChecker.parseOtaPayload(minimal, isTv = false)
        assertNotNull(info)
        assertEquals("2.0", info!!.version)
        assertEquals("https://example.com/BitChord-v2.0.apk", info.apkUrl)
        assertEquals("- Faster startup\n- New equalizer", info.notes)
    }
}
