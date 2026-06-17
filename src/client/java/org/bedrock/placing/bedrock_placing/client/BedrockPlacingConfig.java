/*
 * Licensed under the Apache License, Version 2.0
 * See https://www.apache.org/licenses/LICENSE-2.0
 */
package org.bedrock.placing.bedrock_placing.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;

public class BedrockPlacingConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("bedrock_placing.json");

    public static boolean modEnabled = true;
    public static boolean creativeOnly = false;

    public static void load() {
        if (!CONFIG_PATH.toFile().exists()) {
            save();
            return;
        }
        try (Reader reader = new FileReader(CONFIG_PATH.toFile())) {
            Data data = GSON.fromJson(reader, Data.class);
            if (data != null) {
                modEnabled = data.modEnabled;
                creativeOnly = data.creativeOnly;
            }
        } catch (Exception e) {
            save();
        }
    }

    public static void save() {
        try (Writer writer = new FileWriter(CONFIG_PATH.toFile())) {
            Data data = new Data();
            data.modEnabled = BedrockPlacingConfig.modEnabled;
            data.creativeOnly = BedrockPlacingConfig.creativeOnly;
            GSON.toJson(data, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Data {
        boolean modEnabled = true;
        boolean creativeOnly = false;
    }
}