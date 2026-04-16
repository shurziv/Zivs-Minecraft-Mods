package com.example.entitytalker.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

public class TalkScreen extends Screen {
    private final Entity entity;
    private int friendshipLevel = 0;
    private boolean isFriendly = false;

    public TalkScreen(Entity entity) {
        super(Text.literal("Talk with Entity"));
        this.entity = entity;
    }

    @Override
    protected void init() {
        int buttonWidth = 200;
        int buttonHeight = 20;
        int centerX = width / 2;
        int startY = height / 4;

        addDrawableChild(ButtonWidget.builder(Text.literal("Greet"), btn -> {
            sendDialogOption("greet");
        }).dimensions(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Compliment"), btn -> {
            sendDialogOption("compliment");
        }).dimensions(centerX - buttonWidth / 2, startY + 30, buttonWidth, buttonHeight).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Ask for Gift"), btn -> {
            sendGiftRequest();
        }).dimensions(centerX - buttonWidth / 2, startY + 60, buttonWidth, buttonHeight).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), btn -> {
            close();
        }).dimensions(centerX - buttonWidth / 2, startY + 120, buttonWidth, buttonHeight).build());
    }

    private void sendDialogOption(String option) {
        // Network packet sending will be implemented
        System.out.println("Sending dialog: " + option + " to entity " + entity.getId());
    }

    private void sendGiftRequest() {
        System.out.println("Requesting gift from entity " + entity.getId());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        
        // Draw friendship info
        String friendshipText = "Friendship Level: " + friendshipLevel + (isFriendly ? " (Friendly)" : "");
        context.drawCenteredTextWithShadow(textRenderer, friendshipText, width / 2, height / 4 - 20, 0xFFFFFF);
        
        context.drawCenteredTextWithShadow(textRenderer, "Talk with " + entity.getName().getString(), width / 2, height / 4 - 40, 0xFFFF00);
    }

    public void updateFriendship(int level, boolean friendly) {
        this.friendshipLevel = level;
        this.isFriendly = friendly;
    }
}
