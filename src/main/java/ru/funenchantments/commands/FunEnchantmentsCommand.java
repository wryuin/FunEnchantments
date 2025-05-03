package ru.funenchantments.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import ru.funenchantments.enchantments.CustomEnchantment;
import ru.funenchantments.enchantments.EnchantmentManager;

import java.util.ArrayList;
import java.util.List;

public class FunEnchantmentsCommand implements CommandExecutor {
    
    private final EnchantmentManager enchantmentManager;
    
    public FunEnchantmentsCommand(EnchantmentManager enchantmentManager) {
        this.enchantmentManager = enchantmentManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Использование: /funenchantments give <book/pickaxe> <enchantment> [material]");
            return true;
        }
        
        if (!"give".equalsIgnoreCase(args[0])) {
            sender.sendMessage(ChatColor.RED + "Неизвестная подкоманда: " + args[0]);
            return true;
        }
        
        String itemType = args[1].toLowerCase();
        String enchantmentKey = args[2].toLowerCase();
        
        CustomEnchantment enchantment = enchantmentManager.getEnchantment(enchantmentKey);
        if (enchantment == null) {
            sender.sendMessage(ChatColor.RED + "Зачарование не найдено: " + enchantmentKey);
            return true;
        }
        
        if (!(sender instanceof Player)) {
            if (args.length < 4) {
                sender.sendMessage(ChatColor.RED + "Укажите игрока, которому нужно выдать предмет.");
                return true;
            }
            
            Player targetPlayer = Bukkit.getPlayer(args[3]);
            if (targetPlayer == null) {
                sender.sendMessage(ChatColor.RED + "Игрок не найден: " + args[3]);
                return true;
            }
            
            giveItem(targetPlayer, itemType, enchantment, args.length > 4 ? args[4] : null);
            sender.sendMessage(ChatColor.GREEN + "Предмет выдан игроку " + targetPlayer.getName());
        } else {
            Player player = (Player) sender;
            giveItem(player, itemType, enchantment, args.length > 3 ? args[3] : null);
            player.sendMessage(ChatColor.GREEN + "Вы получили зачарованный предмет!");
        }
        
        return true;
    }
    
    private void giveItem(Player player, String itemType, CustomEnchantment enchantment, String material) {
        ItemStack item;
        
        if ("book".equalsIgnoreCase(itemType)) {
            item = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            meta.addStoredEnchant(enchantment, 1, true);
            
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + enchantment.getName() + " I");
            meta.setLore(lore);
            
            item.setItemMeta(meta);
        } else if ("pickaxe".equalsIgnoreCase(itemType)) {
            Material pickaxeMaterial = Material.DIAMOND_PICKAXE;
            
            if (material != null) {
                if ("netherite".equalsIgnoreCase(material)) {
                    pickaxeMaterial = Material.NETHERITE_PICKAXE;
                } else if ("diamond".equalsIgnoreCase(material)) {
                    pickaxeMaterial = Material.DIAMOND_PICKAXE;
                }
            }
            
            item = new ItemStack(pickaxeMaterial);
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(enchantment, 1, true);
            
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + enchantment.getName() + " I");
            meta.setLore(lore);
            
            item.setItemMeta(meta);
        } else {
            player.sendMessage(ChatColor.RED + "Неизвестный тип предмета: " + itemType);
            return;
        }
        
        player.getInventory().addItem(item);
    }
} 