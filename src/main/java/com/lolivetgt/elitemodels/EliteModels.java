package com.lolivetgt.elitemodels;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import com.lolivetgt.elitemodels.comandos.EliteModelsCommand;
import com.lolivetgt.elitemodels.model.BBModelReader;
import com.lolivetgt.elitemodels.model.Model;
import com.lolivetgt.elitemodels.model.Texture;
import com.lolivetgt.elitemodels.util.ModelJSONGenerator;
import com.lolivetgt.elitemodels.util.TextureJSONGenerator;

public class EliteModels extends JavaPlugin {

    private EliteModelsCommand command;

    @Override
    public void onEnable() {
        command = new EliteModelsCommand(this);
        getCommand("elitemodels").setExecutor(command);
        getCommand("emd").setExecutor(command);
        createFolders();
        createConfig();
        processBBModels();
    }

    @Override
    public void onDisable() {
        // Aquí va el código para descargar el plugin
    }

    public void createFolders() {
        File bbModels = new File(getDataFolder() + File.separator + "bbmodels");
        File resources = new File(getDataFolder() + File.separator + "resource pack" + File.separator + "assets"
                + File.separator + "minecraft");
        File elitemodels = new File(getDataFolder() + File.separator + "resource pack" + File.separator + "assets"
                + File.separator + "elitemodels");
        File elitemodelsModels = new File(elitemodels + File.separator + "models");
        File elitemodelsTextures = new File(elitemodels + File.separator + "textures" + File.separator + "entity");
        File atlas = new File(resources + File.separator + "atlases");
        File itemModels = new File(resources + File.separator + "models" + File.separator + "item");

        bbModels.mkdirs();
        elitemodels.mkdirs();
        elitemodelsModels.mkdirs();
        elitemodelsTextures.mkdirs();
        atlas.mkdirs();
        itemModels.mkdirs();
    }

    private void createConfig() {
        // Obtén el archivo config.yml de tu plugin
        File configFile = new File(getDataFolder(), "config.yml");

        // Si no existe, crea una copia desde el archivo default que hayas definido
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }

        // Carga el archivo config.yml utilizando YamlConfiguration
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        // Añade cualquier configuración que quieras tener por defecto
        config.addDefault("opcion1", true);
        config.addDefault("opcion2", "valor");

        // Guarda los cambios en el archivo config.yml
        config.options().copyDefaults(true);
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processBBModels() {
        File bbModelsFolder = new File(getDataFolder() + File.separator + "bbmodels");

        if (bbModelsFolder.exists()) {
            for (File bbModelFile : bbModelsFolder.listFiles()) {
                if (bbModelFile.isFile() && bbModelFile.getName().endsWith(".bbmodel")) {
                    try {
                        // Lee el archivo BBModel y conviértelo en un objeto Model
                        Model model = BBModelReader.readModel(bbModelFile);

                        // Genera los archivos de recurso necesarios
                        String modelJSON = ModelJSONGenerator.generateModelJSON(model);
                        saveJSONToFile(modelJSON,
                                getDataFolder() + File.separator + "resource pack" + File.separator + "assets"
                                        + File.separator + "elitemodels" + File.separator + "models",
                                bbModelFile.getName().replace(".bbmodel", ".json"));

                        for (Texture texture : model.getTextures()) {
                            String textureJSON = TextureJSONGenerator.generateTextureJSON(texture);
                            saveJSONToFile(textureJSON,
                                    getDataFolder() + File.separator + "resource pack" + File.separator + "assets"
                                            + File.separator + "elitemodels" + File.separator + "textures"
                                            + File.separator + "entity",
                                    texture.getName() + ".json");
                        }
                    } catch (IOException e) {
                        getLogger().warning("Error al leer el archivo BBModel: " + bbModelFile.getName());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void saveJSONToFile(String json, String folderPath, String fileName) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File jsonFile = new File(folderPath + File.separator + fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFile))) {
            writer.write(json);
        } catch (IOException e) {
            getLogger().warning("Error al guardar el archivo JSON: " + jsonFile.getName());
            e.printStackTrace();
        }
    }

}
