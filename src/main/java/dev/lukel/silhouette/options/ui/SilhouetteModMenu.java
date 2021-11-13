package dev.lukel.silhouette.options.ui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * modmenu integration
 */
public class SilhouetteModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SilhouetteOptionsGui::new;
    }
}
