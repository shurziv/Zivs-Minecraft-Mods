package com.example.entitytalker.mixin;

import com.example.entitytalker.component.EntityDataComponent;
import com.example.entitytalker.component.IEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements IEntityData {
    @Unique
    private final EntityDataComponent entityData = new EntityDataComponent();

    @Override
    public int getFriendshipLevel() {
        return entityData.getFriendshipLevel();
    }

    @Override
    public void setFriendshipLevel(int level) {
        entityData.setFriendshipLevel(level);
    }

    @Override
    public boolean isFriendly() {
        return entityData.isFriendly();
    }

    @Override
    public void setFriendly(boolean friendly) {
        entityData.setFriendly(friendly);
    }

    @Override
    public NbtCompound getPersistentData() {
        return entityData.getPersistentData();
    }

    @Unique
    public void writeCustomDataToNbt(NbtCompound nbt) {
        entityData.writeToNbt(nbt);
    }

    @Unique
    public void readCustomDataFromNbt(NbtCompound nbt) {
        entityData.readFromNbt(nbt);
    }
}
