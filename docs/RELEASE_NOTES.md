![BitChord Android Banner](https://raw.githubusercontent.com/Inventor365/BitChord-PC/main/Banner.png)

<p align="center">
  <img src="https://raw.githubusercontent.com/Inventor365/BitChord-PC/main/docs/screenshots/macos.png" alt="BitChord for macOS" width="49%" />
  <img src="https://raw.githubusercontent.com/Inventor365/BitChord-PC/main/docs/screenshots/linux.png" alt="BitChord for Linux & Windows" width="49%" />
</p>

## What's Extra Over Official (`Inventor365/BitChord-PC`)

This release combines the latest upstream **`v1.8.2`** (`77634fb6`, `#1109`, `#1194`) and **`web-player`** (`#1157`) developments from [`kushagrasinghx/BitChord`](https://github.com/kushagrasinghx/BitChord) with **full native desktop builds** and **22+ verified community pull requests** integrated ahead of upstream:

### 1. Native Desktop App Restored & Maintained (Windows, macOS & Linux)
- **Windows (`x64`)**: `.msi` installer, `-setup.exe` installer, and zero-install `-portable.zip` bundle with native WASAPI exclusive/shared output, Windows System Media Transport Controls (`bitchord_smtc.dll`), and DWM Mica/Acrylic window backdrop.
- **macOS (`arm64` Apple Silicon & `x64` Intel)**: `.dmg`, `.pkg`, and `.zip` app bundles with native traffic-light window chrome, CoreAudio output, and Now Playing media keys.
- **Linux (`x86_64`)**: `.AppImage`, `.deb`, and `.rpm` packages with MPRIS2 D-Bus media integration, StatusNotifier/AppIndicator system tray menu, SecretService keyring storage, and HiDPI fractional scaling fix.
- **Desktop Audio & Intelligence**: Bundled `libbitchord_analysis` native DSP + Beat This! (`final0`) ONNX model for local Automix beat/key/downbeat tracking, 10-band parametric EQ, Discord Rich Presence IPC, and full 17-language localization (including the new Korean `ko` desktop translation).

### 2. Integrated Community Pull Requests & Playback Fixes (Ahead of Official)
- **Sequential Source Fallback & Interactive Quality Badge (`#1200`)**: Queries sources in rank order instead of triggering duplicate parallel downloads, falls back to the next source when an auditioned stream is rejected or stalls (`4s` stall detection), drops failed addon calls even when the starter coroutine was cancelled, and lets you tap the quality badge on the player to toggle the active source name.
- **Open External Music Links (`#1145`)**: Intercepts Spotify, Apple Music, Deezer, and SoundCloud track, album, and playlist links and opens them directly in BitChord, matched automatically on YouTube Music.
- **Save Queue & Radio as Playlist (`#1174`)**: Adds one-tap **Save queue as playlist** and **Save radio as playlist** actions in the queue header and playlist picker.
- **Samsung Galaxy Buds Integration (`#983`)**: Native Bluetooth SPP control panel (`GalaxyBudsManager`) integrated directly into the audio output sheet for Galaxy Buds2 / Buds2 Pro / Buds3 / Buds3 Pro (ANC, Ambient Sound, 360 Audio, touch lock, equalizer presets, and per-earbud/case battery levels).
- **Cast Queue Window & Downloaded Track Casting (`#1186`)**: Sends a bounded castable queue window to Chromecast receivers, streams downloaded tracks via their remote URLs instead of skipping them, and reports honest receiver playback positions.
- **Android TV Low-Memory Performance Mode (`#1044`)**: Adaptive artwork sizing (`TvArtworkSizing`) and memory-aware rendering policies (`TvPerformancePolicy`) for smooth navigation on resource-constrained Android TV and Fire TV sticks.
- **Block Explicit Songs (`#801`)**: Optional explicit content filter (`ExplicitFilter`) that filters AutoPlay and radio stations, skips blocked explicit tracks in the queue, dims explicit rows in lists, and offers an allow-once / disable-blocking prompt.
- **Disable Song Caching & Custom Step Slider (`#985`)**: Allows setting the audio cache limit all the way down to `0 MB` (completely disabled) with a stepped slider while keeping the active playing track safe.
- **Auto-Download Playlist Songs (`#935`)**: Automatically downloads newly added tracks whenever they are added to a playlist already marked as downloaded.
- **Icon-Only Navigation Bar (`#907`)**: New appearance toggle to hide bottom navigation tab labels for a clean icon-only bar.
- **Anonymous Search & History Privacy (`#939`)**: Ensures all YouTube Music search queries and search pagination continuations run anonymously without logging searches to your Google watch/search history.
- **Local Per-Track Cover Art & Embedded ID3 Lyrics (`#1188`, `#1119`, `#1187`)**: Gives each local audio file its own embedded cover art rather than sharing one cover across an album ID, reads embedded ID3 `USLT`/`SYLT` lyrics from local MP3 files, and keys remote artwork on `(videoId, thumbnailUrl)`.
- **Lyrics, Localization & Playlist Fixes (`#1211`, `#1204`, `#1099`, `#1095`, `#1094`, `#1091`, `#991`, `#932`)**: Complete Portuguese (`pt-BR`) and Turkish (`tr`) localizations across all modules, reloads Android service/notification lyrics when source order or syllable sync changes, decodes astral emoji HTML entities in Enhanced LRC, prevents plain lyrics from beating line-synced lyrics, expands multi-timestamp compressed LRC lines, loads all tracks in the playlist reorder screen, and resumes playback reliably after version swaps.
- **Listen Together Party Hardening (`#997`, `#1089`, `#1088`, `#1087`, `#1086`)**: Publishes the exact playing copy's index when a track appears twice in a party queue, accepts uppercase/mixed-case invite URLs, avoids double-escaping invalid invite codes, writes human-readable activity feed entries, and prevents slow WebSocket listeners from holding party locks.

---

## Upstream `v1.8.2` Highlights

- **Bit-Perfect Audio Output (`#1194`)**: Three bit-perfect modes — Android 14 bit-perfect USB mixer bypass, built-in DAP AAudio exclusive mode, and a custom direct USB Audio Class 1.0/2.0 driver with hardware DAC volume control and an iOS-style frosted volume HUD.
- **Automix Tempo Ease & Late Handoff Fixes**: Eases beat-matched tempo back one beat at a time and handles the outgoing player reaching the next track before handoff without stopping playback.
- **Apple-Fidelity Liquid Glass & Typography**: Liquid Glass edge refraction and plus-darker end crescents, adaptive dark/light glass glyphs over bright backdrops, progressive status-bar blur setting, squircle segmented tabs, iOS-style context menus (`SongActionsMenu`) with SF Symbols, and SF Pro typography tuned to Apple Music proportions.
- **Full Music Video Support**: Watch music videos directly in the player with selectable video quality up to 4K, YouTube's official version time-map alignment, 3:4 motion covers on mobile, and music-video radio continuation.
- **Automix Loop Mix**: Beat-matched DJ loop mixing powered by the updated Beat This! (`final0`) model and Signalsmith time-stretching, with an interactive Mix View popup and vocal/lyric loop guard.
- **Top Picks for You & Offline Player**: New **Top Picks** hero carousel on Home, dedicated Offline Player mode (device playlists, Liked songs, Folders tab, Open With), custom Sleep Timer (5 minutes to 12 hours), and **Remove from history** action on the History screen.
- **New Icons, Fonts & Languages**: Neon app icons (Apple, Samsung, and Pixel shapes), Monochrome icon, Vazirmatn font option for Persian lyrics, and full Korean (`ko`) localization.

---

## Installation Guide

### Android Phone & Tablet (Android 8.0 / API 26+)
- **Universal APK (Recommended)**: `BitChord-v1.8.2.apk` (works on all devices).
- **Per-ABI Splits (Smaller download)**:
  - `BitChord-v1.8.2_arm64-v8a.apk` — Modern 64-bit ARM phones & tablets.
  - `BitChord-v1.8.2_armeabi-v7a.apk` — Older 32-bit ARM devices.
  - `BitChord-v1.8.2_x86_64.apk` — Chromebooks & Android emulators.

### Android TV / Google TV / Fire TV (Android 8.0 / API 26+)
- **Universal TV APK**: `BitChordTV-v1.8.2.apk`
- **Per-ABI TV Splits**: `BitChordTV-v1.8.2_armeabi-v7a.apk` (most TVs & Fire TV sticks), `BitChordTV-v1.8.2_arm64-v8a.apk` (NVIDIA Shield, Google TV Streamer), `BitChordTV-v1.8.2_x86_64.apk`.

### Wear OS Companion
- **Watch APK**: `BitChordWear-v1.8.2.apk` (also bundled inside the phone APK for automatic companion installation).

### Windows (`x64`, Windows 10 / 11)
- **Installer (`.exe` / `.msi`)**: `BitChord-1.8.2-windows-x64-setup.exe` or `BitChord-1.8.2-windows-x64.msi`
- **Portable (`.zip`)**: `BitChord-1.8.2-windows-x64-portable.zip` — extract anywhere and run `BitChord.exe` (no installation required).

### macOS (`arm64` Apple Silicon & `x64` Intel, macOS 12+)
- **Apple Silicon (M1 / M2 / M3 / M4)**: `BitChord-1.8.2-macos-arm64.dmg` (or `.pkg` / `.zip`)
- **Intel Mac (`x86_64`)**: `BitChord-1.8.2-macos-x64.dmg` (or `.pkg` / `.zip`)

### Linux (`x86_64`)
- **AppImage (Universal)**: `BitChord-1.8.2-linux-x86_64.AppImage` (`chmod +x` and run directly)
- **Debian / Ubuntu / Mint**: `sudo apt install ./BitChord-1.8.2-linux-amd64.deb`
- **Fedora / RHEL / openSUSE**: `sudo dnf install ./BitChord-1.8.2-linux-x86_64.rpm`
