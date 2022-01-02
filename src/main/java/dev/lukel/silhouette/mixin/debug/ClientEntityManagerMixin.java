package dev.lukel.silhouette.mixin.debug;

import net.minecraft.client.world.ClientEntityManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.entity.EntityChangeListener;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientEntityManager.class)
public abstract class ClientEntityManagerMixin<T extends EntityLike> {

    @Inject(method = "addEntity(Lnet/minecraft/world/entity/EntityLike;)V", at = @At("HEAD"))
    public void addEntity(T entity, CallbackInfo ci) {
//        SilhouetteClientMod.LOGGER.info("ClientEntityManagerMixin addEntity");
    }

//    @Mixin(ClientEntityManager.Listener.class)
    @Mixin(targets = "net/minecraft/client/world/ClientEntityManager$Listener")
    abstract static class ListenerMixin implements EntityChangeListener {
//    @Override
//    public void updateEntityPosition() {
//
//    }
//
//    @Override
    @Inject(method = "remove", at = @At("HEAD"))
    public void remove(Entity.RemovalReason reason, CallbackInfo ci) {
//        SilhouetteClientMod.LOGGER.info("ClientEntityManagerMixin$Listener remove");
    }

}
}
