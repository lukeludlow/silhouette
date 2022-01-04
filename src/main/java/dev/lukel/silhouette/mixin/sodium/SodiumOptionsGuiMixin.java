package dev.lukel.silhouette.mixin.sodium;

import dev.lukel.silhouette.SilhouetteClientMod;
import dev.lukel.silhouette.options.SilhouetteGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SodiumOptionsGUI.class)
public abstract class SodiumOptionsGuiMixin extends Screen {

    @Final
    @Shadow
    private List<OptionPage> pages;
    @Shadow
    private OptionPage currentPage;

    private final OptionPage silhouetteOptionPage = SilhouetteGameOptionPages.silhouette();

    // make compiler happy
    protected SodiumOptionsGuiMixin(Text title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(Screen previousScreen, CallbackInfo info) {
        pages.add(silhouetteOptionPage);
    }

    @Override
    public void renderBackground(MatrixStack matrices, int vOffset) {
        if (this.client != null && this.client.world != null) {
            // sodium uses 0 for both of these values
            int startX = 0;
            int startY = 0;
            if (this.currentPage == silhouetteOptionPage) {
                // modify these two values so that there's no gray overlay when viewing silhouette options page
                startX = this.width;
                startY = this.height;
            }
            // these two values are just copy pasted from the source code idk what they do
            int colorStart = -1072689136;
            int colorEnd = -804253680;
            this.fillGradient(matrices, startX, startY, this.width, this.height, colorStart, colorEnd);
        } else {
            this.renderBackgroundTexture(vOffset);
        }
    }

}


