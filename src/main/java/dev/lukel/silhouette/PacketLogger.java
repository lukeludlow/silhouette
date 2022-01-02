package dev.lukel.silhouette;

import dev.lukel.silhouette.mixin.CustomPayloadS2CPacketAccessor;
import net.fabricmc.fabric.mixin.networking.accessor.CustomPayloadC2SPacketAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerSpawnS2CPacket;
import net.minecraft.util.Identifier;

import java.util.Locale;

public class PacketLogger {

	private static Identifier getChannel(Packet<?> packet) {
		if (packet instanceof CustomPayloadC2SPacketAccessor) {
			return ((CustomPayloadC2SPacketAccessor) packet).getChannel();
		} else if (packet instanceof CustomPayloadS2CPacketAccessor) {
			return ((CustomPayloadS2CPacketAccessor) packet).getChannel();
		}
		return null;
	}

	private static String getSideName(NetworkSide side) {
		if (side == NetworkSide.CLIENTBOUND) return "client";
		if (side == NetworkSide.SERVERBOUND) return "server";

		return side.name().toLowerCase(Locale.ROOT);
	}

	public static void logSentPacket(Packet<?> packet, NetworkSide side) {
		String sideName = PacketLogger.getSideName(side);

		Identifier channel = PacketLogger.getChannel(packet);
		if (channel != null) {
			SilhouetteClientMod.LOGGER.info("Sending packet with channel '{}' ({})", channel, sideName);
			return;
		}

//		SilhouetteClientMod.LOGGER.info("Sending packet with name '{}' ({})", packet.getClass().getName(), sideName);
	}

	public static void logReceivedPacket(Packet<?> packet, NetworkSide side) {
		String sideName = PacketLogger.getSideName(side);

		Identifier channel = PacketLogger.getChannel(packet);
		if (channel != null) {
//			SilhouetteClientMod.LOGGER.info("Received packet with channel '{}' ({})", channel, sideName);
			return;
		}

		if (packet instanceof PlayerSpawnS2CPacket playerSpawnPacket) {
//			SilhouetteClientMod.LOGGER.info("playerSpawnPacket entityid={} uuid={}", playerSpawnPacket.getId(), playerSpawnPacket.getPlayerUuid());
		}

		if (packet instanceof EntityS2CPacket entityPacket) {
//			SilhouetteClientMod.LOGGER.info("EntityS2CPacket packet type is {}", packet.getClass().getName());
			Entity e = entityPacket.getEntity(MinecraftClient.getInstance().world);
//			SilhouetteClientMod.LOGGER.info("EntityS2CPacket entity is {}. packet type is {}", (e == null ? "" : e.getId()), packet.getClass().getName());
			if (e instanceof OtherClientPlayerEntity otherClientPlayer) {
//				SilhouetteClientMod.LOGGER.info("EntityS2CPacket entity is {}. packet type is {}", e.toString(), packet.getClass().getName());
			}


		}


		if (packet instanceof EntityTrackerUpdateS2CPacket entityTrackerPacket) {
//			SilhouetteClientMod.LOGGER.info("EntityTrackerUpdateS2CPacket id is {}. packet type is {}. packet={}", entityTrackerPacket.id(), packet.getClass().getName(), entityTrackerPacket);
			for (DataTracker.Entry<?> entry : entityTrackerPacket.getTrackedValues()) {
//				SilhouetteClientMod.LOGGER.info("    entry={}", entry.getData());
			}
//			SilhouetteClientMod.LOGGER.info("    tracked values {}", entityTrackerPacket.getTrackedValues().get(0));
		}


//		String s = String.format("received packet with name %s", packet.getClass().getName());
//		if (s.toLowerCase().contains("player")) {
//			SilhouetteClientMod.LOGGER.info("yes. Received packet with name '{}' ({})", packet.getClass().getName(), sideName);
//			if (packet instanceof PlayerSpawnS2CPacket playerSpawnPacket) {
//				SilhouetteClientMod.LOGGER.info("playerSpawnPacket entityid={} uuid={}", playerSpawnPacket.getId(), playerSpawnPacket.getPlayerUuid());
//			}
//		}
//		SilhouetteClientMod.LOGGER.info("Received packet with name '{}' ({})", packet.getClass().getName(), sideName);

//		if (packet instanceof EntityS2CPacket entityPacket) {
//			Entity e = entityPacket.getEntity(MinecraftClient.getInstance().world);
//			if (e instanceof OtherClientPlayerEntity otherClientPlayer) {
//				SilhouetteClientMod.LOGGER.info("here. entity is {}. packet type is {}", e.toString(), packet.getClass().getName());
//			}
//		}


	}
}