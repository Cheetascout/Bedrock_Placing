/*
 * Licensed under the Apache License, Version 2.0
 * See https://www.apache.org/licenses/LICENSE-2.0
 */
package org.bedrock.placing.bedrock_placing;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import org.bedrock.placing.bedrock_placing.client.BedrockPlacingConfig;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("Bedrock Placing"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));

            general.addEntry(entryBuilder
                    .startBooleanToggle(Text.literal("Mod Enabled"), BedrockPlacingConfig.modEnabled)
                    .setDefaultValue(true)
                    .setTooltip(Text.literal("Enable or disable Bedrock Placing"))
                    .setSaveConsumer(val -> BedrockPlacingConfig.modEnabled = val)
                    .build());

            general.addEntry(entryBuilder
                    .startBooleanToggle(Text.literal("Creative Mode Only"), BedrockPlacingConfig.creativeOnly)
                    .setDefaultValue(false)
                    .setTooltip(Text.literal("Only activate in creative mode"))
                    .setSaveConsumer(val -> BedrockPlacingConfig.creativeOnly = val)
                    .build());

            builder.setSavingRunnable(BedrockPlacingConfig::save);
            return builder.build();
        };
    }
}