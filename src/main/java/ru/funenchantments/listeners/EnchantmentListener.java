package ru.funenchantments.listeners;

import java.util.HashMap;
import java.util.Map;

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

    private final Map<String, Boolean> accessCache = new HashMap<>();

    public EnchantmentListener(EnchantmentManager enchantmentManager, FunEnchantments plugin) {
        this.enchantmentManager = enchantmentManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (tool == null || tool.getType().isAir()) {
            return;
        }

        CustomEnchantment bulldozerEnchantment = enchantmentManager.getEnchantment("bulldozer");
        if (bulldozerEnchantment == null || !CustomEnchantment.hasEnchantment(tool, bulldozerEnchantment)) {
            return;
        }

        int level = CustomEnchantment.getLevel(tool, bulldozerEnchantment);

        if (plugin.isWorldGuardEnabled() && !canBreakWithBulldozer(player, event.getBlock().getLocation(), level)) {
            return;
        }

        BulldozerEnchantment.handleBlockBreak(event, level);
    }

    /**
     * Метод проверяет права игрока на разрушение блока с учётом уровня зачарования.
     */
    private boolean canBreakWithBulldozer(Player player, org.bukkit.Location location, int level) {
        String cacheKey = location.toString();
        if (accessCache.containsKey(cacheKey)) {
            return accessCache.get(cacheKey);
        }

        try {
            com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(location);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();

            ApplicableRegionSet regions = query.getApplicableRegions(loc);
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

            boolean result = regions.testState(localPlayer, Flags.BLOCK_BREAK);
            accessCache.put(cacheKey, result);
            return result;
        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка при проверке регионов WorldGuard.");
            return true;
        }
    }
}