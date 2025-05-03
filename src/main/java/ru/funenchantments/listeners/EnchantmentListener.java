package ru.funenchantments.listeners;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import ru.funenchantments.enchantments.BulldozerEnchantment;
import ru.funenchantments.enchantments.CustomEnchantment;
import ru.funenchantments.enchantments.EnchantmentManager;

public class EnchantmentListener implements Listener {
    
    private final EnchantmentManager enchantmentManager;
    
    public EnchantmentListener(EnchantmentManager enchantmentManager) {
        this.enchantmentManager = enchantmentManager;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        
        // Проверяем зачарование Бульдозер
        CustomEnchantment bulldozerEnchantment = enchantmentManager.getEnchantment("bulldozer");
        if (bulldozerEnchantment != null && CustomEnchantment.hasEnchantment(tool, bulldozerEnchantment)) {
            int level = CustomEnchantment.getLevel(tool, bulldozerEnchantment);
            BulldozerEnchantment.handleBlockBreak(event, level);
        }
    }
} 