package ru.funenchantments.enchantments;

import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class BulldozerEnchantment extends CustomEnchantment {
    
    public BulldozerEnchantment(JavaPlugin plugin) {
        super(new NamespacedKey(plugin, "bulldozer"), "Бульдозер", 1);
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().toString().contains("PICKAXE");
    }

    @Override
    public @NotNull Component displayName(int level) {
        return null;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public @NotNull EnchantmentRarity getRarity() {
        return null;
    }

    @Override
    public float getDamageIncrease(int level, @NotNull EntityCategory entityCategory) {
        return 0;
    }

    @Override
    public @NotNull Set<EquipmentSlot> getActiveSlots() {
        return new HashSet<>();
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }
    
    @Override
    public int getStartLevel() {
        return 1;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    public static void handleBlockBreak(BlockBreakEvent event, int level) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        
        if (tool.getType().toString().contains("PICKAXE")) {
            Block centerBlock = event.getBlock();
            
            // Если блок не может быть добыт киркой, не применяем эффект
            if (!isMineable(centerBlock.getType())) {
                return;
            }
            
            // Получаем направление взгляда игрока
            Set<Block> blocks = getBlocksAround(centerBlock);
            
            // Ломаем блоки вокруг центрального блока
            for (Block block : blocks) {
                if (block.getType() != Material.AIR && isMineable(block.getType())) {
                    // Проверяем, что блок можно сломать
                    BlockBreakEvent breakEvent = new BlockBreakEvent(block, player);
                    player.getServer().getPluginManager().callEvent(breakEvent);
                    
                    // Если событие не отменено (т.е. блок можно сломать)
                    if (!breakEvent.isCancelled()) {
                        block.breakNaturally(tool);
                    }
                }
            }
        }
    }
    
    private static Set<Block> getBlocksAround(Block center) {
        Set<Block> blocks = new HashSet<>();
        
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Пропускаем центральный блок
                    blocks.add(center.getRelative(x, y, z));
                }
            }
        }
        
        return blocks;
    }
    
    private static boolean isMineable(Material material) {
        return material.toString().contains("STONE") || 
               material.toString().contains("ORE") || 
               material == Material.NETHERRACK || 
               material == Material.OBSIDIAN || 
               material == Material.CRYING_OBSIDIAN ||
               material == Material.ANCIENT_DEBRIS ||
               material.toString().contains("CONCRETE") ||
               material.toString().contains("TERRACOTTA") ||
               material == Material.END_STONE;
    }
} 