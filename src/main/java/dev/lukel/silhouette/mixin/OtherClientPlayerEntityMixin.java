package dev.lukel.silhouette.mixin;

import dev.lukel.silhouette.SilhouetteClientMod;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OtherClientPlayerEntity.class)
public abstract class OtherClientPlayerEntityMixin extends LivingEntity {


    protected OtherClientPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isGlowing() {
        return SilhouetteClientMod.options().isEnabled;
    }

    @Override
    public boolean shouldRenderName() {
        return SilhouetteClientMod.options().displayGamertags;
    }
}
