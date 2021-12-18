package dev.lukel.silhouette;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;

@Mixin(ClientConnection.class)
public interface ClientConnectionAccessor {
	@Accessor("side")
	public NetworkSide getSide();
}