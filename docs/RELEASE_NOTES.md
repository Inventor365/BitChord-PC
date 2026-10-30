![BitChord Android Banner](https://raw.githubusercontent.com/Inventor365/BitChord-PC/main/Banner.png)

<p align="center">
  <img src="https://raw.githubusercontent.com/Inventor365/BitChord-PC/main/docs/screenshots/macos.png" alt="BitChord for macOS" width="49%" />
  <img src="https://raw.githubusercontent.com/Inventor365/BitChord-PC/main/docs/screenshots/linux.png" alt="BitChord for Linux & Windows" width="49%" />
</p>

## What's Extra Over Official (`Inventor365/BitChord-PC` — `v1.9`)

This release combines the official **`v1.9`** release (`b690dc14`) and **`web-player`** developments from [`kushagrasinghx/BitChord`](https://github.com/kushagrasinghx/BitChord) with **full native desktop builds** and **23+ verified community pull requests** integrated ahead of upstream:

### 1. Native Desktop App Restored & Maintained (Windows, macOS & Linux)
- **Windows (`x64`)**: `.msi` installer, `-setup.exe` installer, and zero-install `-portable.zip` bundle with native WASAPI exclusive/shared output, Windows System Media Transport Controls (`bitchord_smtc.dll`), and DWM Mica/Acrylic window backdrop.
- **macOS (`arm64` Apple Silicon & `x64` Intel)**: `.dmg`, `.pkg`, and `.zip` app bundles with native traffic-light window chrome, CoreAudio output, and Now Playing media keys.
- **Linux (`x86_64`)**: `.AppImage`, `.deb`, and `.rpm` packages with MPRIS2 D-Bus media integration, StatusNotifier/AppIndicator system tray menu, SecretService keyring storage, and HiDPI fractional scaling fix.
- **Desktop Audio & Intelligence**: Bundled `libbitchord_analysis` native DSP + Beat This! (`final0`) ONNX model for local Automix beat/key/downbeat tracking, 10-band parametric EQ, Discord Rich Presence IPC, and full 17-language localization (including the new Korean `ko` desktop translation).
- **Easy Browser Sign-In Link & Universal Session Paste (Google & Spotify)**: Copyable & openable browser sign-in links for **YouTube Music (Google)** and **Spotify**, one-click browser profile import across Windows, macOS (with Keychain decryption), and Linux, plus a **Universal Smart Paste / Paste from Clipboard** button that accepts DevTools Cookies table rows (`Ctrl+A`, `Ctrl+C`), *Copy as cURL* (Bash/CMD), *Copy as PowerShell*, *Copy as fetch*, Cookie-Editor JSON, Netscape `cookies.txt`, or raw cookie headers.

### 2. Integrated Community Pull Requests & Enhancements (Ahead of Official)
- **Turkish Character Normalization for Song Matching (`#1223`)**: Normalizes Turkish characters (`ş→s`, `ğ→g`, `ö→o`, `ü→u`, `ç→c`, `ı→i`) across `TrackMatcher`, `CanvasArtwork`, and `ArtistCredits` for broader cross-catalogue matching.
- **Interactive Quality Badge & Shared Call Hardening (`#1200`)**: Tap the quality badge on the player to toggle the active source name, and drop failed addon calls even when the starter coroutine was cancelled.
- **Open External Music Links (`#1145`)**: Intercepts Spotify, Apple Music, Deezer, and SoundCloud track, album, and playlist links and opens them directly in BitChord, matched automatically on YouTube Music.
- **Save Queue & Radio as Playlist (`#1174`)**: Adds one-tap **Save queue as playlist** and **Save radio as playlist** actions in the queue header and playlist picker.
- **Samsung Galaxy Buds Integration (`#983`)**: Native Bluetooth SPP control panel (`GalaxyBudsManager`) integrated directly into the audio output sheet for Galaxy Buds2 / Buds2 Pro / Buds3 / Buds3 Pro (ANC, Ambient Sound, 360 Audio, touch lock, equalizer presets, and per-earbud/case battery levels).
- **Cast Queue Window & Downloaded Track Casting (`#1186`)**: Sends a bounded castable queue window to Chromecast receivers, streams downloaded tracks via their remote URLs instead of skipping them, and reports honest receiver playback positions.
- **Android TV Low-Memory Performance Mode (`#1044`)**: Adaptive artwork sizing (`TvArtworkSizing`) and memory-aware rendering policies (`TvPerformancePolicy`) alongside the new AMOLED Now Playing backdrop for Android TV and Fire TV sticks.
- **Block Explicit Songs (`#801`)**: Optional explicit content filter (`ExplicitFilter`) that filters AutoPlay and radio stations, skips blocked explicit tracks in the queue, dims explicit rows in lists, and offers an allow-once / disable-blocking prompt.
- **Disable Song Caching & Custom Step Slider (`#985`)**: Allows setting the audio cache limit all the way down to `0 MB` (completely disabled) with a stepped slider while keeping the active playing track safe.
- **Auto-Download Playlist Songs (`#935`)**: Automatically downloads newly added tracks immediately whenever they are added to a downloaded playlist, working alongside `v1.9`'s background `PlaylistSync`.
- **Icon-Only Navigation Bar (`#907`)**: New appearance toggle to hide bottom navigation tab labels for a clean icon-only bar.
- **Anonymous Search & History Privacy (`#939`)**: Ensures all YouTube Music search queries and search pagination continuations run anonymously without logging searches to your Google watch/search history.
- **Local Per-Track Cover Art & Embedded ID3 Lyrics (`#1188`, `#1119`, `#1187`)**: Gives each local audio file its own embedded cover art rather than sharing one cover across an album ID, reads embedded ID3 `USLT`/`SYLT` lyrics from local MP3 files, and keys remote artwork on `(videoId, thumbnailUrl)`.
- **Lyrics, Localization & Playlist Fixes (`#1211`, `#1204`, `#1099`, `#1095`, `#1094`, `#1091`, `#991`, `#932`)**: Complete Portuguese (`pt-BR`) and Turkish (`tr`) localizations across all modules, reloads Android service/notification lyrics when source order or syllable sync changes, decodes astral emoji HTML entities in Enhanced LRC, prevents plain lyrics from beating line-synced lyrics, expands multi-timestamp compressed LRC lines, loads all tracks in the playlist reorder screen, and resumes playback reliably after version swaps.
- **Listen Together Party Hardening (`#997`, `#1089`, `#1088`, `#1087`, `#1086`)**: Publishes the exact playing copy's index when a track appears twice in a party queue, accepts uppercase/mixed-case invite URLs, avoids double-escaping invalid invite codes, writes human-readable activity feed entries, and prevents slow WebSocket listeners from holding party locks.

---

## Upstream `v1.9` Highlights

- **Bit-Perfect Output (`#1194`)**: Lossless tracks reach your DAC unchanged at each track's own sample rate and bit depth — choose Android 14's USB DAC mode, AAudio exclusive for DAPs with a built-in DAC, or BitChord's direct USB driver on any Android version, complete with hardware DAC volume HUD and low-spec DAC format conversion.
- **Video Playback**: Full music video support in the main player — switch between a song and its video from the song menu, select video quality up to 4K, and play live streams.
- **Podcast Playback**: YouTube Music podcast shows now open with their full episode list and stream directly in BitChord.
- **Direct Source Stream & Cache Fixes**: Play tracks directly from your addon from the first note without mid-song switches, decode ALAC through FFmpeg, keep whole addon files in cache, and prevent mid-stream WebM/module collisions.
- **Top Picks for You & Keep Playlists Updated**: New **Top Picks** row on Home (artist stations, new releases, mixes, Mood for You), plus background `PlaylistSync` for downloaded playlists.
- **Automix Loop Mix**: DJ-style beat-matched loop mixing powered by Beat This! (`final0`) and Signalsmith time-stretching, with an interactive Mix View popup.
- **Offline Player & iOS-Style Menus**: Device playlists, Liked playlist, Folders tab, *Open with BitChord*, iOS-style context menus with SF Symbols, custom Sleep Timer (5m–12h), Neon & Monochrome app icons, Vazirmatn Persian lyrics font, and Korean (`ko`) localization.

---

## Installation Guide

### Android Phone & Tablet (Android 8.0 / API 26+)
- **Universal APK (Recommended)**: `BitChord-v1.9.apk` (works on all devices).
- **Per-ABI Splits (Smaller download)**:
  - `BitChord-v1.9_arm64-v8a.apk` — Modern 64-bit ARM phones & tablets.
  - `BitChord-v1.9_armeabi-v7a.apk` — Older 32-bit ARM devices.
  - `BitChord-v1.9_x86_64.apk` — Chromebooks & Android emulators.

### Android TV / Google TV / Fire TV (Android 8.0 / API 26+)
- **Universal TV APK**: `BitChordTV-v1.9.apk`
- **Per-ABI TV Splits**: `BitChordTV-v1.9_armeabi-v7a.apk` (most TVs & Fire TV sticks), `BitChordTV-v1.9_arm64-v8a.apk` (NVIDIA Shield, Google TV Streamer), `BitChordTV-v1.9_x86_64.apk`.

### Wear OS Companion
- **Watch APK**: `BitChordWear-v1.9.apk` (also bundled inside the phone APK for automatic companion installation).

### Windows (`x64`, Windows 10 / 11)
- **Installer (`.exe` / `.msi`)**: `BitChord-1.9-windows-x64-setup.exe` or `BitChord-1.9-windows-x64.msi`
- **Portable (`.zip`)**: `BitChord-1.9-windows-x64-portable.zip` — extract anywhere and run `BitChord.exe` (no installation required).

### macOS (`arm64` Apple Silicon & `x64` Intel, macOS 12+)
- **Apple Silicon (M1 / M2 / M3 / M4)**: `BitChord-1.9-macos-arm64.dmg` (or `.pkg` / `.zip`)
- **Intel Mac (`x86_64`)**: `BitChord-1.9-macos-x64.dmg` (or `.pkg` / `.zip`)

### Linux (`x86_64`)
- **AppImage (Universal)**: `BitChord-1.9-linux-x86_64.AppImage` (`chmod +x` and run directly)
- **Debian / Ubuntu / Mint**: `sudo apt install ./BitChord-1.9-linux-amd64.deb`
- **Fedora / RHEL / openSUSE**: `sudo dnf install ./BitChord-1.9-linux-x86_64.rpm`
