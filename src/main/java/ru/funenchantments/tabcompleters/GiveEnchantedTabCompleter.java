package ru.funenchantments.tabcompleters;

import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class GiveEnchantedTabCompleter implements TabCompleter {

    private static final List<String> ARGS = Arrays.asList("book", "diamond_pickaxe", "netherite_pickaxe");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            return ARGS;
        }
        return null;
    }
}