# Quick Setup Guide for Entity Talker Mod

## Step-by-Step Instructions to Build and Install

### 1. Build the JAR File

Open a terminal in the `/workspace` directory and run:

```bash
./gradlew build
```

If you get "Permission denied", make the script executable first:
```bash
chmod +x gradlew
./gradlew build
```

If `gradlew` doesn't exist, install Gradle and run:
```bash
gradle wrapper
./gradlew build
```

**Success!** You'll see: `BUILD SUCCESSFUL`
The JAR file will be at: `build/libs/entity-talker-1.0.0.jar`

### 2. Install Minecraft Fabric

1. Download Fabric Installer: https://fabricmc.net/use/installer/
2. Run it and select:
   - Minecraft version: **1.20.4**
   - Loader version: **0.15.6** (or latest)
3. Click "Install"

### 3. Get Required Files

Download these and put them in your mods folder:

1. **Your mod**: Copy `build/libs/entity-talker-1.0.0.jar`
2. **Fabric API**: Download from https://modrinth.com/mod/fabric-api/version/1.20.4

Location of mods folder:
- **Windows**: Press `Win+R`, type `%appdata%\.minecraft\mods`
- **Mac**: `~/Library/Application Support/minecraft/mods`
- **Linux**: `~/.minecraft/mods`

(Create the `mods` folder if it doesn't exist)

### 4. Set Up Your API Key

1. Launch Minecraft with the Fabric profile once
2. Close Minecraft
3. Open `config/entity-talker.properties`
4. Replace the placeholder with your actual key:
   ```
   openai_api_key=sk-proj-your-actual-key-here
   ```

**Get an API key**: https://platform.openai.com/api-keys

⚠️ **Note**: OpenAI API costs money! Check pricing at https://openai.com/pricing
- Without an API key, the mod still works with basic fallback responses

### 5. Play!

1. Launch Minecraft with Fabric profile
2. Load a world
3. **Sneak (Shift) + Right-click** on any entity
4. Start talking!

---

## Troubleshooting

### "gradlew: command not found"
Run: `gradle wrapper` first, then `./gradlew build`

### Build fails with Java error
Make sure you have Java 17 or higher:
```bash
java --version
```

### Mod doesn't show up in game
- Verify both entity-talker.jar AND fabric-api.jar are in mods folder
- Make sure you're using Minecraft 1.20.4
- Check if Fabric Loader is installed correctly

### "Waiting for response..." forever
- Check internet connection
- Verify API key in config/entity-talker.properties
- Make sure you have credits in your OpenAI account

---

That's it! Enjoy talking to mobs! 🎮
