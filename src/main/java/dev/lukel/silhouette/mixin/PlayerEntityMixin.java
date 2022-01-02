package dev.lukel.silhouette.mixin;

import dev.lukel.silhouette.SilhouetteClientMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isGlowing() {
        return SilhouetteClientMod.options().silhouette.isEnabled;
    }

    @Override
    public boolean shouldRenderName() {
        return SilhouetteClientMod.options().silhouette.displayGamertags;
    }
}
