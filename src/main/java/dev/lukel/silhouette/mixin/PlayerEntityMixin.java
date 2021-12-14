package dev.lukel.silhouette.mixin;

import dev.lukel.silhouette.render.SilhouettePlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    private final SilhouettePlayerEntity impl;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
        this.impl = new SilhouettePlayerEntity((PlayerEntity) (Object) this);
    }

    @Override
    public boolean isGlowing() {
        return impl.isGlowing();
    }

    @Override
    public boolean shouldRenderName() {
        return impl.shouldRenderName();
    }
}
