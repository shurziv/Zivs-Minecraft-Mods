package com.example.entitytalker.screen;

import com.example.entitytalker.ai.AIChatService;
import com.example.entitytalker.component.EntityDataComponent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TalkScreen extends Screen {
    private final Entity entity;
    private int friendshipLevel = 0;
    private boolean isFriendly = false;
    private TextFieldWidget inputField;
    private Text lastResponse = Text.literal("Start a conversation...");
    private boolean waitingForResponse = false;

    public TalkScreen(Entity entity) {
        super(Text.literal("Talk with " + entity.getName().getString()));
        this.entity = entity;
    }

    @Override
    protected void init() {
        // Get friendship data from component
        EntityDataComponent component = EntityDataComponent.get(entity);
        if (component != null) {
            this.friendshipLevel = component.getFriendshipLevel();
            this.isFriendly = component.isFriendly();
        }

        int buttonWidth = 150;
        int buttonHeight = 20;
        int centerX = width / 2;
        int startY = height / 3;

        // Input field for custom messages
        inputField = new TextFieldWidget(textRenderer, centerX - 150, startY - 30, 300, 20, Text.literal("Type your message"));
        inputField.setMaxLength(200);
        addDrawableChild(inputField);

        // Send button
        addDrawableChild(ButtonWidget.builder(Text.literal("Send"), btn -> {
            sendMessage(inputField.getText());
            inputField.setText("");
        }).dimensions(centerX + 160, startY - 30, 40, 20).build());

        // Quick action buttons
        addDrawableChild(ButtonWidget.builder(Text.literal("Greet (+5)"), btn -> {
            sendDialogOption("greet");
        }).dimensions(centerX - buttonWidth / 2, startY + 20, buttonWidth, buttonHeight).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Compliment (+10)"), btn -> {
            sendDialogOption("compliment");
        }).dimensions(centerX - buttonWidth / 2, startY + 50, buttonWidth, buttonHeight).build());

        if (friendshipLevel >= 75) {
            addDrawableChild(ButtonWidget.builder(Text.literal("Ask for Gift"), btn -> {
                sendGiftRequest();
            }).dimensions(centerX - buttonWidth / 2, startY + 80, buttonWidth, buttonHeight).build());
        }

        if (friendshipLevel < 50 && entity.getType().isIn(net.minecraft.registry.tag.EntityTypeTags.MONSTER)) {
            addDrawableChild(ButtonWidget.builder(Text.literal("Try to Befriend"), btn -> {
                sendDialogOption("befriend");
            }).dimensions(centerX - buttonWidth / 2, startY + 80, buttonWidth, buttonHeight).build());
        }

        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), btn -> {
            close();
        }).dimensions(centerX - buttonWidth / 2, height - 40, buttonWidth, buttonHeight).build());
    }

    private void sendMessage(String message) {
        if (message.trim().isEmpty()) return;
        
        waitingForResponse = true;
        lastResponse = Text.literal("Waiting for response...").formatted(Formatting.GRAY);
        
        AIChatService.chatWithEntity(entity, message, friendshipLevel)
            .thenAccept(response -> {
                client.execute(() -> {
                    lastResponse = Text.literal(response).formatted(Formatting.GREEN);
                    waitingForResponse = false;
                    
                    // Update friendship based on AI response sentiment
                    updateFriendshipFromResponse(response);
                });
            })
            .exceptionally(ex -> {
                client.execute(() -> {
                    lastResponse = Text.literal("Error getting response.").formatted(Formatting.RED);
                    waitingForResponse = false;
                });
                return null;
            });
    }

    private void sendDialogOption(String option) {
        String message;
        int friendshipChange = 0;
        
        switch (option) {
            case "greet":
                message = "Hello there!";
                friendshipChange = 5;
                break;
            case "compliment":
                message = "You're amazing!";
                friendshipChange = 10;
                break;
            case "befriend":
                message = "I want to be your friend!";
                friendshipChange = 15;
                break;
            default:
                message = option;
        }
        
        sendMessage(message);
        
        // Apply immediate friendship change
        if (friendshipChange > 0) {
            EntityDataComponent component = EntityDataComponent.get(entity);
            if (component != null) {
                component.addFriendship(friendshipChange);
                this.friendshipLevel = component.getFriendshipLevel();
                this.isFriendly = component.isFriendly();
            }
        }
    }

    private void sendGiftRequest() {
        if (friendshipLevel < 75) {
            lastResponse = Text.literal("They don't trust you enough yet! (Need 75+ friendship)").formatted(Formatting.RED);
            return;
        }
        
        sendMessage("Can I have something?");
        
        // Chance to get gift based on friendship
        if (friendshipLevel >= 90 || Math.random() < (friendshipLevel - 75) / 25.0) {
            EntityDataComponent component = EntityDataComponent.get(entity);
            if (component != null) {
                component.requestGift(entity);
                lastResponse = Text.literal("They're giving you a gift!").formatted(Formatting.GOLD);
            }
        }
    }

    private void updateFriendshipFromResponse(String response) {
        // Simple sentiment analysis based on keywords
        String lowerResponse = response.toLowerCase();
        int change = 0;
        
        if (lowerResponse.contains("happy") || lowerResponse.contains("like") || 
            lowerResponse.contains("friend") || lowerResponse.contains("love") ||
            lowerResponse.contains("nice") || lowerResponse.contains("great")) {
            change = 5;
        } else if (lowerResponse.contains("angry") || lowerResponse.contains("hate") || 
                   lowerResponse.contains("bad") || lowerResponse.contains("go away")) {
            change = -5;
        }
        
        if (change != 0) {
            EntityDataComponent component = EntityDataComponent.get(entity);
            if (component != null) {
                component.addFriendship(change);
                this.friendshipLevel = component.getFriendshipLevel();
                this.isFriendly = component.isFriendly();
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        
        // Draw title
        context.drawCenteredTextWithShadow(textRenderer, "Talking with " + entity.getName().getString(), 
            width / 2, height / 6 - 20, 0xFFFF00);
        
        // Draw friendship info
        String friendshipText = "Friendship: " + friendshipLevel + "/100" + 
            (isFriendly ? " (Friendly!)" : "") +
            (friendshipLevel >= 50 && entity.getType().isIn(net.minecraft.registry.tag.EntityTypeTags.MONSTER) ? " (No longer hostile)" : "");
        context.drawCenteredTextWithShadow(textRenderer, friendshipText, 
            width / 2, height / 6, 0x00FF00);
        
        // Draw response box
        int boxX = width / 4;
        int boxY = height / 3 + 30;
        int boxWidth = width / 2;
        int boxHeight = 100;
        
        context.fill(boxX - 2, boxY - 2, boxX + boxWidth + 2, boxY + boxHeight + 2, 0xFF000000);
        context.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0xDD000000);
        
        // Draw response text with wrapping
        context.drawTextWrapped(textRenderer, lastResponse, boxX + 5, boxY + 5, boxWidth - 10, 0xFFFFFF);
        
        if (waitingForResponse) {
            context.drawCenteredTextWithShadow(textRenderer, "...", width / 2, boxY + boxHeight - 15, 0xFFFF00);
        }
        
        // Draw instructions
        context.drawCenteredTextWithShadow(textRenderer, "Type a message or use quick actions below", 
            width / 2, startY - 50, 0xAAAAAA);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // ESC
            close();
            return true;
        }
        if (keyCode == 257 && !waitingForResponse) { // Enter
            sendMessage(inputField.getText());
            inputField.setText("");
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void updateFriendship(int level, boolean friendly) {
        this.friendshipLevel = level;
        this.isFriendly = friendly;
    }
}
