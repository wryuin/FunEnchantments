package ru.funenchantments.listeners;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import ru.funenchantments.FunEnchantments;
import ru.funenchantments.enchantments.BulldozerEnchantment;
import ru.funenchantments.enchantments.CustomEnchantment;
import ru.funenchantments.enchantments.EnchantmentManager;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.flags.StateFlag;

public class EnchantmentListener implements Listener {
    
    private final EnchantmentManager enchantmentManager;
    private final FunEnchantments plugin;
    
    public EnchantmentListener(EnchantmentManager enchantmentManager, FunEnchantments plugin) {
        this.enchantmentManager = enchantmentManager;
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        
        // Проверяем зачарование Бульдозер
        CustomEnchantment bulldozerEnchantment = enchantmentManager.getEnchantment("bulldozer");
        if (bulldozerEnchantment != null && CustomEnchantment.hasEnchantment(tool, bulldozerEnchantment)) {
            int level = CustomEnchantment.getLevel(tool, bulldozerEnchantment);
            
            // Если WorldGuard включен, проверяем разрешения
            if (plugin.isWorldGuardEnabled() && !canBreakWithBulldozer(player, event.getBlock().getLocation())) {
                return;
            }
            
            BulldozerEnchantment.handleBlockBreak(event, level);
        }
    }
    
    private boolean canBreakWithBulldozer(Player player, org.bukkit.Location location) {
        try {
            com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(location);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            
            // Проверяем наличие доступа к региону
            ApplicableRegionSet regions = query.getApplicableRegions(loc);
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
            
            // Проверяем флаг BLOCK_BREAK, если игрок не может ломать блоки в этом регионе, 
            // то и бульдозер не должен разрушать блоки
            return regions.testState(localPlayer, Flags.BLOCK_BREAK);
        } catch (Exception e) {
            plugin.getLogger().warning("Ошибка при проверке регионов WorldGuard: " + e.getMessage());
            return true; // В случае ошибки разрешаем использование бульдозера
        }
    }
} 