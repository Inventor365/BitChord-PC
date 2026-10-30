package com.music.bitchord.desktop

import java.security.SecureRandom
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DesktopBrowserCookiesTest {
    @Test
    fun `recognises a YouTube signing cookie`() {
        assertTrue(DesktopBrowserCookies.hasSigningSecret("SID=x; __Secure-3PAPISID=secret; other=y"))
    }

    @Test
    fun `reads Chromium profile display names from Local State`() {
        val state = """
            {
              "profile": {
                "info_cache": {
                  "Default": { "name": "Kushagra" },
                  "Profile 26": { "name": "Music account" }
                }
              }
            }
        """.trimIndent()

        assertEquals(
            mapOf("Default" to "Kushagra", "Profile 26" to "Music account"),
            DesktopBrowserCookies.chromiumProfileNames(state),
        )
    }

    @Test
    fun `decrypts Chromium Windows AES GCM cookies`() {
        val random = SecureRandom()
        val key = ByteArray(32).also(random::nextBytes)
        val nonce = ByteArray(12).also(random::nextBytes)
        val value = "youtube-session-value"
        val host = ".youtube.com"
        val hostDigest = MessageDigest.getInstance("SHA-256").digest(host.toByteArray())
        val encrypted = Cipher.getInstance("AES/GCM/NoPadding").run {
            init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), GCMParameterSpec(128, nonce))
            doFinal(hostDigest + value.toByteArray())
        }
        val sealed = "v10".toByteArray() + nonce + encrypted

        assertEquals(value, DesktopBrowserCookies.decryptWindows(sealed, key, host))
        assertNull(DesktopBrowserCookies.decryptWindows(sealed, null))
    }

    @Test
    fun `extracts YouTube cookies from cURL, PowerShell, JSON, DevTools table and Netscape formats`() {
        val curl = """curl 'https://music.youtube.com/youtubei/v1/browse' -H 'Accept: */*' -H 'Cookie: SID=abc; SAPISID=secret123/xyz; LOGIN_INFO=1'"""
        assertEquals("SID=abc; SAPISID=secret123/xyz; LOGIN_INFO=1", DesktopBrowserCookies.extractYouTubeCookie(curl))

        val powershell = """
            ${'$'}session = New-Object Microsoft.PowerShell.Commands.WebRequestSession
            ${'$'}session.Cookies.Add((New-Object System.Net.Cookie("SID", "abc", "/", ".youtube.com")))
            ${'$'}session.Cookies.Add((New-Object System.Net.Cookie("__Secure-3PAPISID", "psSecret/999", "/", ".youtube.com")))
        """.trimIndent()
        assertEquals("SID=abc; __Secure-3PAPISID=psSecret/999", DesktopBrowserCookies.extractYouTubeCookie(powershell))

        val jsonExport = """[{"name":"VISITOR_INFO1_LIVE","value":"v1"},{"name":"SAPISID","value":"jsonSecret"}]"""
        assertEquals("VISITOR_INFO1_LIVE=v1; SAPISID=jsonSecret", DesktopBrowserCookies.extractYouTubeCookie(jsonExport))

        val devToolsTable = "Name\tValue\tDomain\nSAPISID\ttableSecret\t.youtube.com\nSID\tsidVal\t.youtube.com"
        assertEquals("SAPISID=tableSecret; SID=sidVal", DesktopBrowserCookies.extractYouTubeCookie(devToolsTable))

        val netscape = ".youtube.com\tTRUE\t/\tTRUE\t1800000000\tSAPISID\tnetscapeSecret"
        assertEquals("SAPISID=netscapeSecret", DesktopBrowserCookies.extractYouTubeCookie(netscape))
    }

    @Test
    fun `extracts Spotify sp_dc token from bare token, cURL, PowerShell, JSON and DevTools table`() {
        val bare = "AQB_valid_spotify_sp_dc_token_value_123456"
        assertEquals(bare, DesktopBrowserCookies.extractSpotifyToken(bare))

        val curl = """curl 'https://open.spotify.com/' -b 'sp_t=123; sp_dc=AQB_from_curl_987654321; sp_landing=x'"""
        assertEquals("AQB_from_curl_987654321", DesktopBrowserCookies.extractSpotifyToken(curl))

        val devToolsRow = "sp_dc\tAQB_from_devtools_table_111222\t.spotify.com\t/\t2027-01-01"
        assertEquals("AQB_from_devtools_table_111222", DesktopBrowserCookies.extractSpotifyToken(devToolsRow))
    }
}
