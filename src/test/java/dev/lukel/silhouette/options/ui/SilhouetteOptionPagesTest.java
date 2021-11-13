package dev.lukel.silhouette.options.ui;

import dev.lukel.silhouette.SilhouetteClientMod;
import dev.lukel.silhouette.options.SilhouetteOptions;
import dev.lukel.silhouette.options.ui.options.OptionPage;
import dev.lukel.silhouette.options.ui.options.OptionUi;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

class SilhouetteOptionPagesTest {

    @Test
    void general_shouldCreateGroupForEachSilhouetteOption() {
        try (MockedStatic<SilhouetteClientMod> mockedClientMod = mockStatic(SilhouetteClientMod.class)) {
            SilhouetteOptions defaultOptions = new SilhouetteOptions();
            mockedClientMod.when(SilhouetteClientMod::options).thenReturn(defaultOptions);
            OptionPage optionPage = SilhouetteOptionPages.general();
            List<Field> optionFields = List.of(defaultOptions.getClass().getDeclaredFields());
            List<OptionUi<?>> optionGroupOptions = optionPage.getGroups().get(0).getOptions();
            optionFields.forEach(field -> {
                String fieldName = field.getName();
                String translatedName = OptionsTranslatableTextMap.map.get(fieldName);
                // this check fixes error when running tests with code coverage
                if (!fieldName.contains("lineHits")) {
                    assertTrue(optionGroupOptions.stream()
                            .anyMatch(option -> option.getName().getString().contains(translatedName)));
                }
            });
        }
    }

    @Test
    void general_allOptionsShouldHaveTranslatableText() {
        try (MockedStatic<SilhouetteClientMod> mockedClientMod = mockStatic(SilhouetteClientMod.class)) {
            Field[] fields1 = SilhouetteOptions.class.getDeclaredFields();
            Field[] fields2 = OptionsTranslatableTextMap.class.getDeclaredFields();
            for (int i = 0; i < fields1.length; i++) {
                if (fields2[i].getName().equals("map")) {
                    continue;
                }
                assertEquals(fields1[i].getName(), fields2[i].getName());
            }
        }
    }
}