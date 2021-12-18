package dev.lukel.silhouette.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OtherClientPlayerEntity.class)
public class OtherClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public OtherClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "shouldRender", at = @At("RETURN"))
    public void shouldRender(double distance, CallbackInfoReturnable<Boolean> cir) {
        // shouldRender never ends up being false. because the entity just doesn't exist.
//        SilhouetteClientMod.LOGGER.info("shouldRender?={} distance={}", cir.getReturnValue(), distance);
    }
}
