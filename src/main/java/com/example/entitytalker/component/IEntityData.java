package com.example.entitytalker.component;

import net.minecraft.nbt.NbtCompound;

public interface IEntityData {
    int getFriendshipLevel();
    void setFriendshipLevel(int level);
    boolean isFriendly();
    void setFriendly(boolean friendly);
    NbtCompound getPersistentData();
}
