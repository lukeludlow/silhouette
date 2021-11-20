package dev.lukel.silhouette.options.ui;

import dev.lukel.silhouette.options.ui.control.Control;
import dev.lukel.silhouette.options.ui.control.ControlElement;
import dev.lukel.silhouette.options.ui.control.Dim2i;
import dev.lukel.silhouette.options.ui.options.OptionFlag;
import dev.lukel.silhouette.options.ui.options.OptionGroup;
import dev.lukel.silhouette.options.ui.options.OptionImpact;
import dev.lukel.silhouette.options.ui.options.OptionPage;
import dev.lukel.silhouette.options.ui.options.OptionUi;
import dev.lukel.silhouette.options.ui.storage.OptionStorage;
import dev.lukel.silhouette.options.ui.widgets.FlatButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class SilhouetteOptionsGui extends Screen {

    private final List<OptionPage> pages = new ArrayList<>();

    private final List<ControlElement<?>> controls = new ArrayList<>();

    private final Screen prevScreen;

    private OptionPage currentPage;

//    private FlatButtonWidget applyButton;
    private FlatButtonWidget closeButton;
//    private FlatButtonWidget undoButton;

    private boolean hasPendingChanges;
    private ControlElement<?> hoveredElement;

    public SilhouetteOptionsGui(Screen prevScreen) {
        super(new TranslatableText("Silhouette Options"));

        this.prevScreen = prevScreen;

        this.pages.add(SilhouetteOptionPages.general());
    }

    public void setPage(OptionPage page) {
        this.currentPage = page;

        this.rebuildGUI();
    }

    @Override
    protected void init() {
        super.init();

        this.rebuildGUI();
    }

    protected void rebuildGUI() {
        this.controls.clear();

        this.clearChildren();

        if (this.currentPage == null) {
            if (this.pages.isEmpty()) {
                throw new IllegalStateException("No pages are available?!");
            }

            // Just use the first page for now
            this.currentPage = this.pages.get(0);
        }

        this.rebuildGUIPages();
        this.rebuildGUIOptions();

//        this.undoButton = new FlatButtonWidget(new Dim2i(this.width - 211, this.height - 30, 65, 20), new TranslatableText("silhouette.options.buttons.undo"), this::undoChanges);
//        this.applyButton = new FlatButtonWidget(new Dim2i(this.width - 142, this.height - 30, 65, 20), new TranslatableText("silhouette.options.buttons.apply"), this::applyChanges);
        this.closeButton = new FlatButtonWidget(new Dim2i(this.width - 73, this.height - 30, 65, 20), new TranslatableText("gui.done"), this::onClose);

//        this.addDrawableChild(this.undoButton);
//        this.addDrawableChild(this.applyButton);
        this.addDrawableChild(this.closeButton);
    }

    private void rebuildGUIPages() {
        int x = 6;
        int y = 6;

        for (OptionPage page : this.pages) {
            int width = 12 + this.textRenderer.getWidth(page.getName());

            FlatButtonWidget button = new FlatButtonWidget(new Dim2i(x, y, width, 18), page.getName(), () -> this.setPage(page));
            button.setSelected(this.currentPage == page);

            x += width + 6;

            this.addDrawableChild(button);
        }
    }

    private void rebuildGUIOptions() {
        int x = 6;
        int y = 28;

        for (OptionGroup group : this.currentPage.getGroups()) {
            // Add each option's control element
            for (OptionUi<?> option : group.getOptions()) {
                Control<?> control = option.getControl();
                ControlElement<?> element = control.createElement(new Dim2i(x, y, 200, 18));

                this.addDrawableChild(element);

                this.controls.add(element);

                // Move down to the next option
                y += 18;
            }

            // Add padding beneath each option group
            y += 4;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
//        SilhouetteClientMod.LOGGER.info("silhouette gui render. super.getClass()=" + super.getClass().toString());
//        super.renderBackground(matrixStack);

        this.renderBackground(matrixStack, 69);

        this.updateControls();

        super.render(matrixStack, mouseX, mouseY, delta);

        if (this.hoveredElement != null) {
            this.renderOptionTooltip(matrixStack, this.hoveredElement);
        }
    }

    @Override
    public void renderBackground(MatrixStack matrices, int vOffset) {
//        SilhouetteClientMod.LOGGER.info("silhouette gui renderBackground");
        if (this.client.world != null) {
            // original values
            int colorStart = -1072689136;
            int colorEnd = -804253680;
//            int colorStart = -1000;
//            int colorEnd = -8000;
            // modified these values so that there's no gray overlay
            int startX = this.width;
            int startY = this.height;
            this.fillGradient(matrices, startX, startY, this.width, this.height, colorStart, colorEnd);
        } else {
            this.renderBackgroundTexture(vOffset);
        }
    }

    private void updateControls() {
        ControlElement<?> hovered = this.getActiveControls()
                .filter(ControlElement::isHovered)
                .findFirst()
                .orElse(null);

        boolean hasChanges = this.getAllOptions()
                .anyMatch(OptionUi::hasChanged);

        for (OptionPage page : this.pages) {
            for (OptionUi<?> option : page.getOptions()) {
                if (option.hasChanged()) {
                    hasChanges = true;
                }
            }
        }


//        this.applyButton.setEnabled(hasChanges);
//        this.undoButton.setVisible(hasChanges);
        this.closeButton.setEnabled(!hasChanges);

        this.hasPendingChanges = hasChanges;
        this.hoveredElement = hovered;
        // auto apply changes
        this.applyChanges();
    }

    private Stream<OptionUi<?>> getAllOptions() {
        return this.pages.stream()
                .flatMap(s -> s.getOptions().stream());
    }

    private Stream<ControlElement<?>> getActiveControls() {
        return this.controls.stream();
    }

    private void renderOptionTooltip(MatrixStack matrixStack, ControlElement<?> element) {
        Dim2i dim = element.getDimensions();

        int textPadding = 3;
        int boxPadding = 3;

        int boxWidth = 200;

        int boxY = dim.y();
        int boxX = dim.getLimitX() + boxPadding;

        OptionUi<?> option = element.getOption();
        List<OrderedText> tooltip = new ArrayList<>(this.textRenderer.wrapLines(option.getTooltip(), boxWidth - (textPadding * 2)));

        OptionImpact impact = option.getImpact();

        if (impact != null) {
            tooltip.add(Language.getInstance().reorder(new TranslatableText("silhouette.options.performance_impact_string", impact.getLocalizedName()).formatted(Formatting.GRAY)));
        }

        int boxHeight = (tooltip.size() * 12) + boxPadding;
        int boxYLimit = boxY + boxHeight;
        int boxYCutoff = this.height - 40;

        // If the box is going to be cutoff on the Y-axis, move it back up the difference
        if (boxYLimit > boxYCutoff) {
            boxY -= boxYLimit - boxYCutoff;
        }

        this.fillGradient(matrixStack, boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0xE0000000, 0xE0000000);

        for (int i = 0; i < tooltip.size(); i++) {
            this.textRenderer.draw(matrixStack, tooltip.get(i), boxX + textPadding, boxY + textPadding + (i * 12), 0xFFFFFFFF);
        }
    }

    private void applyChanges() {
        final HashSet<OptionStorage<?>> dirtyStorages = new HashSet<>();
        final EnumSet<OptionFlag> flags = EnumSet.noneOf(OptionFlag.class);

        this.getAllOptions().forEach((option -> {
            if (!option.hasChanged()) {
                return;
            }

            option.applyChanges();

            flags.addAll(option.getFlags());
            dirtyStorages.add(option.getStorage());
        }));

        MinecraftClient client = MinecraftClient.getInstance();

        if (flags.contains(OptionFlag.REQUIRES_RENDERER_RELOAD)) {
            client.worldRenderer.reload();
        }

        if (flags.contains(OptionFlag.REQUIRES_ASSET_RELOAD)) {
            client.setMipmapLevels(client.options.mipmapLevels);
            client.reloadResourcesConcurrently();
        }

        for (OptionStorage<?> storage : dirtyStorages) {
            storage.save();
        }
    }

    private void undoChanges() {
        this.getAllOptions()
                .forEach(OptionUi::reset);
    }

    private void openDonationPage() {
        Util.getOperatingSystem()
                .open("https://caffeinemc.net/donate");
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_P && (modifiers & GLFW.GLFW_MOD_SHIFT) != 0) {
            // FIXME does this work?
            MinecraftClient.getInstance().setScreen(new VideoOptionsScreen(this.prevScreen, MinecraftClient.getInstance().options));
//            MinecraftClient.getInstance().openScreen(new VideoOptionsScreen(this.prevScreen, MinecraftClient.getInstance().options));

            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !this.hasPendingChanges;
    }

    @Override
    public void onClose() {
        // FIXME does this work?
        this.client.setScreen(this.prevScreen);
//        this.client.openScreen(this.prevScreen);
    }
}