package ru.funenchantments.commands;

import ru.funenchantments.EnchantmentStorageMeta;
import ru.funenchantments.enchantments.BulldozerEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveEnchantedCommand implements CommandExecutor {

    private final Enchantment bulldozer = new BulldozerEnchantment();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("Usage: /giveenchanted <player> [book|diamond_pickaxe|netherite_pickaxe]");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found!");
            return true;
        }

        String type = args[1];
        ItemStack item;

        switch (type.toLowerCase()) {
            case "book":
                item = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta meta = item.getItemMeta();
                ((EnchantmentStorageMeta) meta).addStoredEnchant(bulldozer, 1, true);
                item.setItemMeta(meta);
                break;
            case "diamond_pickaxe":
                item = new ItemStack(Material.DIAMOND_PICKAXE);
                item.addUnsafeEnchantment(bulldozer, 1);
                break;
            case "netherite_pickaxe":
                item = new ItemStack(Material.NETHERITE_PICKAXE);
                item.addUnsafeEnchantment(bulldozer, 1);
                break;
            default:
                sender.sendMessage("Invalid type. Use book/diamond_pickaxe/netherite_pickaxe");
                return true;
        }

        target.getInventory().addItem(item);
        sender.sendMessage("Выдан предмет с зачарованием Бульдозер!");
        return true;
    }
}