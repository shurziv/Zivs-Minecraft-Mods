package com.example.entitytalker;

import net.fabricmc.api.ModInitializer;
import com.example.entitytalker.network.ModMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityTalkerMod implements ModInitializer {
    public static final String MOD_ID = "entity-talker";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Entity Talker initialized!");
        ModMessages.registerC2SPackets();
    }
}
