package ru.funenchantments.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ru.funenchantments.enchantments.EnchantmentManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FunEnchantmentsTabCompleter implements TabCompleter {
    
    private final EnchantmentManager enchantmentManager;
    
    public FunEnchantmentsTabCompleter(EnchantmentManager enchantmentManager) {
        this.enchantmentManager = enchantmentManager;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // Первый аргумент - подкоманда
            completions.add("give");
        } else if (args.length == 2 && "give".equalsIgnoreCase(args[0])) {
            // Второй аргумент - тип предмета
            completions.add("book");
            completions.add("pickaxe");
        } else if (args.length == 3 && "give".equalsIgnoreCase(args[0])) {
            // Третий аргумент - название зачарования
            completions.addAll(enchantmentManager.getEnchantments().keySet());
        } else if (args.length == 4 && "give".equalsIgnoreCase(args[0]) && "pickaxe".equalsIgnoreCase(args[1])) {
            // Четвертый аргумент - материал кирки
            completions.add("diamond");
            completions.add("netherite");
        }
        
        // Фильтруем предложения по тому, что уже ввел игрок
        String lastArg = args[args.length - 1].toLowerCase();
        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(lastArg))
                .collect(Collectors.toList());
    }
} 