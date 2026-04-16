package com.example.entitytalker.ai;

import com.example.entitytalker.EntityTalkerMod;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.WolfEntity;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AIChatService {
    private static final OkHttpClient client = new OkHttpClient();
    private static final Map<String, String> entityPersonalities = new HashMap<>();
    
    static {
        // Define personalities for different entity types
        entityPersonalities.put("minecraft:zombie", "You are a zombie in Minecraft. You speak slowly, groan occasionally, and think about brains. You're not very smart but you're friendly if the player is nice to you. Keep responses short (1-2 sentences).");
        entityPersonalities.put("minecraft:creeper", "You are a Creeper in Minecraft. You are shy and nervous, sometimes hissing when startled. You don't mean to be hostile, you just explode when stressed. Speak in whispers and short sentences.");
        entityPersonalities.put("minecraft:skeleton", "You are a Skeleton in Minecraft. You are a bit sarcastic and dry-humored. You like bones and archery. You're actually quite witty but come off as cold.");
        entityPersonalities.put("minecraft:spider", "You are a Spider in Minecraft. You speak with multiple perspectives (you have 8 eyes!). You're curious about players and like dark places. You're not evil, just misunderstood.");
        entityPersonalities.put("minecraft:enderman", "You are an Enderman in Minecraft. You speak mysteriously, often repeating words. You get upset when looked at directly. You love blocks and teleporting. Use *static noises* occasionally.");
        entityPersonalities.put("minecraft:villager", "You are a Villager in Minecraft. You speak in a merchant-like manner, always thinking about trades and emeralds. You say 'Hrrrm' often. You're friendly but business-minded.");
        entityPersonalities.put("minecraft:wolf", "You are a Wolf in Minecraft. You're loyal, playful, and expressive. You bark, wag your tail, and show emotions clearly. You love bones and protecting your friends.");
        entityPersonalities.put("minecraft:cat", "You are a Cat in Minecraft. You're independent, sassy, and sometimes ignore the player. You love fish and sleeping. Act aloof but secretly care.");
        entityPersonalities.put("minecraft:cow", "You are a Cow in Minecraft. You're calm, gentle, and simple. You love grass and peaceful days. Speak slowly and peacefully.");
        entityPersonalities.put("minecraft:pig", "You are a Pig in Minecraft. You're cheerful, optimistic, and love food (especially carrots). You oink happily and are very friendly.");
        entityPersonalities.put("minecraft:sheep", "You are a Sheep in Minecraft. You're timid but kind, love wool and grass. You're a bit shy but warm-hearted.");
        entityPersonalities.put("minecraft:chicken", "You are a Chicken in Minecraft. You're energetic, scatterbrained, and cluck excitedly. You love seeds and are very jumpy.");
        entityPersonalities.put("minecraft:iron_golem", "You are an Iron Golem in Minecraft. You're protective, stoic, and speak formally. You defend villages and children. You're strong but gentle.");
        entityPersonalities.put("minecraft:witch", "You are a Witch in Minecraft. You're mysterious, slightly mischievous, and knowledgeable about potions. You speak cryptically but aren't necessarily evil.");
        
        // Default personality for unknown entities
        entityPersonalities.put("default", "You are a creature in Minecraft. You have feelings and thoughts. Respond naturally to the player based on your nature as this type of entity. Keep it immersive and in-character.");
    }
    
    public static CompletableFuture<String> chatWithEntity(Entity entity, String userMessage, int friendshipLevel) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return performChatRequest(entity, userMessage, friendshipLevel);
            } catch (Exception e) {
                EntityTalkerMod.LOGGER.error("AI Chat error: {}", e.getMessage());
                return getFallbackResponse(entity, friendshipLevel);
            }
        });
    }
    
    private static String performChatRequest(Entity entity, String userMessage, int friendshipLevel) throws IOException {
        String apiKey = EntityTalkerMod.getAIApiKey();
        
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("YOUR_API_KEY_HERE")) {
            return getFallbackResponse(entity, friendshipLevel);
        }
        
        String entityType = EntityTalkerMod.getEntityTypeKey(entity);
        String personality = entityPersonalities.getOrDefault(entityType, entityPersonalities.get("default"));
        
        String systemPrompt = personality + 
            " Current friendship level with player: " + friendshipLevel + "/100. " +
            "If friendship is high (75+), be very friendly and warm. " +
            "If friendship is low (below 30), be more distant or cautious. " +
            "If friendship is medium, be neutral but open to friendship. " +
            "Respond as if you are this Minecraft entity talking to a player.";
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-3.5-turbo");
        requestBody.addProperty("max_tokens", 150);
        requestBody.addProperty("temperature", 0.8);
        
        JsonArray messagesArray = new JsonArray();
        
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", systemPrompt);
        messagesArray.add(systemMessage);
        
        JsonObject userMessageObj = new JsonObject();
        userMessageObj.addProperty("role", "user");
        userMessageObj.addProperty("content", userMessage);
        messagesArray.add(userMessageObj);
        
        requestBody.add("messages", messagesArray);
        
        RequestBody body = RequestBody.create(
            requestBody.toString(),
            MediaType.parse("application/json; charset=utf-8")
        );
        
        Request request = new Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer " + apiKey)
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                EntityTalkerMod.LOGGER.error("AI API error: {}", response.message());
                return getFallbackResponse(entity, friendshipLevel);
            }
            
            String responseBody = response.body().string();
            JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
            
            return json.getAsJsonArray("choices")
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("message")
                .get("content")
                .getAsString()
                .trim();
        }
    }
    
    private static String getFallbackResponse(Entity entity, int friendshipLevel) {
        String entityType = EntityTalkerMod.getEntityTypeKey(entity);
        
        // Fallback responses based on entity type and friendship
        if (entity instanceof ZombieEntity) {
            if (friendshipLevel > 75) return "*groans happily* Me... like... you! Brain... friend!";
            if (friendshipLevel > 50) return "*groans* Hrrrm... hello...";
            return "*groans menacingly* Braaaains...";
        }
        
        if (entity instanceof CreeperEntity) {
            if (friendshipLevel > 75) return "*soft hissing* Oh! It's you! I'll try not to explode around you...";
            if (friendshipLevel > 50) return "*nervous hissing* Sss... please don't hurt me...";
            return "*startled hissing* SSSSS!";
        }
        
        if (entity instanceof VillagerEntity) {
            if (friendshipLevel > 75) return "Hrrrm! My good friend! Would you like a special discount?";
            if (friendshipLevel > 50) return "Hrrrm. Hello there. Interested in trading?";
            return "Hrrrm? *suspicious noise*";
        }
        
        if (entity instanceof WolfEntity) {
            if (friendshipLevel > 75) return "*wags tail excitedly and barks happily*";
            if (friendshipLevel > 50) return "*wags tail cautiously* Woof?";
            return "*growls softly*";
        }
        
        if (entity instanceof CatEntity) {
            if (friendshipLevel > 75) return "*purrs loudly and rubs against you* Meow~";
            if (friendshipLevel > 50) return "*meows curiously*";
            return "*hisses and arches back*";
        }
        
        if (entity instanceof EndermanEntity) {
            if (friendshipLevel > 75) return "*static noises* Fffriend... *teleports closer* Don't... look... directly...";
            if (friendshipLevel > 50) return "*quiet static* ...hello...";
            return "*angry static noises*";
        }
        
        // Generic fallback based on friendship
        if (friendshipLevel > 75) {
            return "*makes friendly sounds and approaches you warmly*";
        } else if (friendshipLevel > 50) {
            return "*makes curious noises*";
        } else if (friendshipLevel > 30) {
            return "*observes you cautiously*";
        } else {
            return "*makes wary sounds and keeps distance*";
        }
    }
}
