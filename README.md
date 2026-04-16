# Entity Talker - Minecraft Fabric Mod

A Minecraft Fabric mod that lets you have **AI-powered conversations** with any entity (mobs, villagers, creatures, etc.) and build friendships with them!

## Features

### 🗣️ AI-Powered Conversations
- Talk with ANY entity in Minecraft using OpenAI's GPT API
- Each creature type has its own unique personality:
  - **Zombies**: Slow, groaning, think about brains
  - **Creepers**: Shy, nervous, hiss when startled
  - **Villagers**: Business-minded, say "Hrrrm", love emeralds
  - **Wolves**: Loyal, playful, expressive
  - **Cats**: Independent, sassy, aloof but caring
  - **Endermen**: Mysterious, repeat words, teleport references
  - **Skeletons**: Sarcastic, dry humor, witty
  - And many more!

### 💕 Friendship System
- Build friendship levels (0-100) through conversations
- **Greet** (+5 friendship)
- **Compliment** (+10 friendship)
- **Custom messages** via text input
- Friendship affects how entities respond to you

### ⚔️ Make Hostile Mobs Friendly
- Reach 50+ friendship with hostile mobs to make them non-aggressive
- Special "Try to Befriend" option for monsters
- Watch as creepers stop exploding and zombies stop attacking!

### 🎁 Get Free Items from Villagers
- Reach 75+ friendship to ask for gifts
- Higher friendship = better chance of receiving items
- At 90+ friendship, villagers will almost always give gifts!

## Installation

### Prerequisites
1. **Minecraft Java Edition 1.20.4**
2. **Fabric Loader** for 1.20.4
3. **Fabric API** mod

### Step 1: Install Fabric Loader
1. Download Fabric Installer from: https://fabricmc.net/use/installer/
2. Run the installer
3. Select Minecraft version **1.20.4**
4. Click "Install"

### Step 2: Build the Mod JAR

#### Option A: Using Command Line (Recommended)
```bash
cd /workspace
./gradlew build
```

The mod JAR will be created at: `build/libs/entity-talker-1.0.0.jar`

#### Option B: If gradlew doesn't exist
```bash
# Generate gradle wrapper first
gradle wrapper

# Then build
./gradlew build
```

### Step 3: Install the Mod
1. Navigate to your Minecraft directory:
   - Windows: `%appdata%\.minecraft`
   - Mac: `~/Library/Application Support/minecraft`
   - Linux: `~/.minecraft`
2. Open the `mods` folder (create it if it doesn't exist)
3. Copy `build/libs/entity-talker-1.0.0.jar` to the `mods` folder
4. Also download and copy **Fabric API** to the mods folder:
   - Get it from: https://modrinth.com/mod/fabric-api
   - Choose version for Minecraft 1.20.4

### Step 4: Configure API Key
1. Launch Minecraft with Fabric profile
2. The mod will create a config file at: `config/entity-talker.properties`
3. Close Minecraft
4. Open `config/entity-talker.properties` in a text editor
5. Replace `YOUR_API_KEY_HERE` with your OpenAI API key:
   ```
   openai_api_key=sk-your-actual-api-key-here
   ```
6. Save the file

**Get an OpenAI API Key:**
- Visit: https://platform.openai.com/api-keys
- Sign up or log in
- Create a new API key
- **Note:** API usage costs money! Check pricing at https://openai.com/pricing

### Step 5: Play!
1. Launch Minecraft with the Fabric profile
2. Load a world
3. **Sneak + Right-click** on any entity to start talking!

## How to Use In-Game

### Starting a Conversation
1. **Sneak** (hold Shift)
2. **Right-click** on any entity
3. The talk screen will open!

### Talking with Entities
- **Type custom messages** in the text field and press Enter or click Send
- **Quick actions**: Use preset buttons for common interactions
  - "Greet (+5)" - Say hello and gain friendship
  - "Compliment (+10)" - Give a compliment for more friendship
  - "Ask for Gift" - Available at 75+ friendship
  - "Try to Befriend" - Available for hostile mobs under 50 friendship

### Building Friendship
- Every conversation can increase or decrease friendship
- Positive responses from AI = +5 friendship
- Negative responses = -5 friendship
- Quick actions give immediate friendship boosts

### Making Hostile Mobs Friendly
1. Find a hostile mob (zombie, creeper, skeleton, etc.)
2. Start talking (you may need to be quick!)
3. Use "Try to Befriend" or send friendly messages
4. Reach 50+ friendship
5. The mob will no longer attack you!

### Getting Free Items
1. Build friendship with a villager to 75+
2. Click "Ask for Gift"
3. Chance of receiving items increases with friendship level
4. At 90+ friendship, gifts are almost guaranteed!

## Troubleshooting

### Mod doesn't appear in game
- Make sure Fabric Loader is installed correctly
- Verify both the mod JAR and Fabric API are in the `mods` folder
- Check Minecraft version matches (1.20.4)

### "Waiting for response..." forever
- Check your internet connection
- Verify your OpenAI API key is correct in `config/entity-talker.properties`
- Make sure you have credits in your OpenAI account

### Entities still attack me
- You need 50+ friendship to make hostile mobs friendly
- Keep talking to them to build friendship
- Some mobs may need multiple conversations

### Can't get gifts from villagers
- You need 75+ friendship level
- Check the friendship bar at the top of the talk screen
- Keep building friendship through conversations

## Technical Details

### How It Works
1. When you talk to an entity, the mod sends your message to OpenAI's API
2. The AI receives a system prompt defining the entity's personality
3. The AI responds in character as that specific mob type
4. Friendship levels are stored per-entity using Fabric's component system
5. All data persists between game sessions

### Personality System
Each entity type has a custom personality prompt that influences:
- Speaking style and vocabulary
- Attitude toward the player
- Topics they care about
- Emotional responses

### Requirements
- Minecraft 1.20.4
- Fabric Loader 0.15.6+
- Fabric API 0.96.0+
- Java 17+
- OpenAI API key (optional - fallback responses work without it)

## Building from Source

```bash
# Clone or navigate to the project directory
cd /workspace

# Build the mod
./gradlew build

# The JAR file will be in build/libs/
```

## Configuration

Edit `config/entity-talker.properties`:
```properties
# Your OpenAI API key (get from https://platform.openai.com/api-keys)
openai_api_key=sk-your-key-here
```

## Credits

- Built with FabricMC
- Uses OkHttp for HTTP requests
- Powered by OpenAI GPT API

## License

MIT License - Feel free to modify and distribute!

## Support

If you encounter issues:
1. Check the troubleshooting section above
2. Look at Minecraft logs in `logs/latest.log`
3. Verify your API key and internet connection
4. Make sure all dependencies are installed

---

**Enjoy making friends with all the creatures in Minecraft!** 🎮✨
