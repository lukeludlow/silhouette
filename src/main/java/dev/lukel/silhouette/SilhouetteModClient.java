package dev.lukel.silhouette;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.impl.client.indigo.renderer.IndigoRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;

public class SilhouetteModClient implements ClientModInitializer {
//    private static BlockEntityType<DemoBlockEntity> DEMO_BLOCK_ENTITY;

    @Override
    public void onInitializeClient() {
//        DEMO_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "silhouette:demo_block_entity", FabricBlockEntityTypeBuilder.create(DemoBlockEntity::new, DEMO_BLOCK).build(null));
//        BlockEntityRendererRegistry.register(DEMO_BLOCK_ENTITY, DemoBlockEntityRenderer::new);


//        IndigoRenderer renderer = IndigoRenderer.INSTANCE;
//        renderer.

//        HudRenderCallback.EVENT.register(e -> {
//            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
//            textRenderer.draw
//        })
    }
}
