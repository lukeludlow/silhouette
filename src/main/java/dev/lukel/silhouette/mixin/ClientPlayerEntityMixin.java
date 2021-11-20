package dev.lukel.silhouette.mixin;

import dev.lukel.silhouette.SilhouetteClientMod;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends LivingEntity {


    protected ClientPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isGlowing() {
        return SilhouetteClientMod.options().isEnabled && SilhouetteClientMod.options().shouldOutlineSelf;
    }

    @Override
    public boolean shouldRenderName() {
        return SilhouetteClientMod.options().displayGamertags;
    }

    // TODO inject and invert control using an "Impl" object that i can unit test

}
