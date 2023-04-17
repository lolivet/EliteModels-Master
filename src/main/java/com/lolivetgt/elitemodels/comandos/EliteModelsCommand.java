package com.lolivetgt.elitemodels.comandos;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.lolivetgt.elitemodels.EliteModels;

public class EliteModelsCommand implements CommandExecutor {

    private final EliteModels plugin;

    public EliteModelsCommand(EliteModels plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            plugin.createFolders();
            sender.sendMessage(ChatColor.GREEN + "EliteModels config reloaded.");
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reload") && args[1].equalsIgnoreCase("models")) {
            // Aquí va el código para recargar los archivos .bbmodel
            sender.sendMessage(ChatColor.GREEN + "EliteModels models reloaded.");
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reload") && args[1].equalsIgnoreCase("config")) {
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "EliteModels config reloaded.");
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
            sender.sendMessage(ChatColor.BLUE + "EliteModels version " + plugin.getDescription().getVersion());
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("zip")) {
            // Aquí va el código para recargar el archivo .zip del resource pack
            sender.sendMessage(ChatColor.BLUE + "EliteModels zip reloaded.");
            return true;
        }
        return false;

    }

}