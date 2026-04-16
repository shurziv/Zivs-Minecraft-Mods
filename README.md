# Entity Talker - Minecraft Fabric Mod

A Minecraft Fabric mod that allows you to talk with entities (mobs, creatures, villagers, etc.) and become friends with them!

## Features

- **Talk with Entities**: Right-click while sneaking on any entity to open a dialog screen
- **Friendship System**: Build friendship levels with entities through conversations
- **Make Hostile Mobs Friendly**: Increase friendship with hostile mobs to make them non-aggressive
- **Free Items from Villagers**: Convince villagers to give you items for free when friendship is high enough
- **Dialog Options**: 
  - Greet (+5 friendship)
  - Compliment (+10 friendship)
  - Ask for Gift (requires 75+ friendship and friendly status)

## How to Use

1. **Approach any entity** (mob, villager, animal, etc.)
2. **Sneak and right-click** to open the talk screen
3. **Choose dialog options** to increase friendship
4. **Reach friendship level 50** to make the entity friendly
5. **Reach friendship level 75+** to ask for gifts

## Building

### Requirements
- Java 17 or higher
- Gradle (included via wrapper)

### Build Commands
```bash
# Build the mod
./gradlew build

# The built JAR will be in build/libs/
```

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/)
2. Install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Place the mod JAR in your `mods` folder

## Project Structure

```
src/main/
├── java/com/example/entitytalker/
│   ├── EntityTalkerMod.java          # Main mod class
│   ├── EntityTalkerClient.java       # Client-side initialization
│   ├── component/
│   │   ├── IEntityData.java          # Interface for entity data
│   │   └── EntityDataComponent.java  # Friendship data implementation
│   ├── mixin/
│   │   ├── LivingEntityMixin.java              # Adds friendship data to entities
│   │   ├── ClientPlayerInteractionManagerMixin.java  # Handles right-click interaction
│   │   └── ServerPlayInteractionManagerMixin.java    # Server-side interaction
│   ├── network/
│   │   └── ModMessages.java          # Network packet handlers
│   └── screen/
│       └── TalkScreen.java           # Dialog UI screen
└── resources/
    ├── fabric.mod.json               # Mod metadata
    ├── entity-talker.mixins.json     # Mixin configuration
    └── assets/entity-talker/         # Mod assets
```

## Technical Details

- **Minecraft Version**: 1.20.4
- **Loader**: Fabric 0.15.6+
- **Fabric API**: Required
- **Java Version**: 17+

## Customization

You can modify the friendship values in `ModMessages.java`:
- Greet: +5 friendship
- Compliment: +10 friendship
- Friendly threshold: 50 friendship
- Gift threshold: 75 friendship

## License

MIT License - See LICENSE file for details

## Contributing

Feel free to submit issues and pull requests!