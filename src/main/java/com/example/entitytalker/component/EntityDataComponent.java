package com.example.entitytalker.component;

import net.minecraft.nbt.NbtCompound;

public class EntityDataComponent implements IEntityData {
    private int friendshipLevel = 0;
    private boolean isFriendly = false;
    private final NbtCompound persistentData = new NbtCompound();

    @Override
    public int getFriendshipLevel() {
        return friendshipLevel;
    }

    @Override
    public void setFriendshipLevel(int level) {
        this.friendshipLevel = Math.max(0, Math.min(100, level));
        if (this.friendshipLevel >= 50) {
            this.isFriendly = true;
        }
    }

    @Override
    public boolean isFriendly() {
        return isFriendly;
    }

    @Override
    public void setFriendly(boolean friendly) {
        this.isFriendly = friendly;
    }

    @Override
    public NbtCompound getPersistentData() {
        return persistentData;
    }

    public void readFromNbt(NbtCompound nbt) {
        this.friendshipLevel = nbt.getInt("FriendshipLevel");
        this.isFriendly = nbt.getBoolean("IsFriendly");
    }

    public void writeToNbt(NbtCompound nbt) {
        nbt.putInt("FriendshipLevel", this.friendshipLevel);
        nbt.putBoolean("IsFriendly", this.isFriendly);
    }
}
