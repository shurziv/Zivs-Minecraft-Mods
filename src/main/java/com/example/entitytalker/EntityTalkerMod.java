package com.example.entitytalker;

import net.fabricmc.api.ModInitializer;
import com.example.entitytalker.network.ModMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class EntityTalkerMod implements ModInitializer {
    public static final String MOD_ID = "entity-talker";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static String apiKey = null;
    
    @Override
    public void onInitialize() {
        LOGGER.info("Entity Talker initialized!");
        ModMessages.registerC2SPackets();
        loadApiKey();
    }
    
    private static void loadApiKey() {
        try {
            Path configPath = FabricLoader.getInstance().getConfigDir().resolve("entity-talker.properties");
            if (Files.exists(configPath)) {
                Properties props = new Properties();
                props.load(Files.newInputStream(configPath));
                apiKey = props.getProperty("openai_api_key", "YOUR_API_KEY_HERE");
                
                if ("YOUR_API_KEY_HERE".equals(apiKey)) {
                    LOGGER.warn("Please set your OpenAI API key in config/entity-talker.properties");
                } else {
                    LOGGER.info("OpenAI API key loaded successfully!");
                }
            } else {
                // Create default config file
                Properties props = new Properties();
                props.setProperty("openai_api_key", "YOUR_API_KEY_HERE");
                props.store(Files.newOutputStream(configPath), "Entity Talker Configuration - Add your OpenAI API key here");
                LOGGER.info("Created default config file at config/entity-talker.properties. Please add your API key!");
                apiKey = "YOUR_API_KEY_HERE";
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load config: {}", e.getMessage());
            apiKey = "YOUR_API_KEY_HERE";
        }
    }
    
    public static String getAIApiKey() {
        return apiKey;
    }
    
    public static String getEntityTypeKey(Entity entity) {
        return entity.getType().toString().replace("EntityType[", "").replace("]", "").split("/")[0].trim();
    }
}
