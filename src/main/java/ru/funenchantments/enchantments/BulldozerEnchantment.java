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

import java.util.*;

public class BulldozerEnchantment extends CustomEnchantment {

    public BulldozerEnchantment(JavaPlugin plugin) {
        super(new NamespacedKey(plugin, "bulldozer"), "Бульдозер", 1);
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().toString().contains("PICKAXE"); // Только инструменты-кирки
    }

    @Override
    public @NotNull Component displayName(int level) {
        return Component.text("Бульдозер " + level);
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
        return EnchantmentRarity.COMMON;
    }

    @Override
    public float getDamageIncrease(int level, @NotNull EntityCategory entityCategory) {
        return 0f;
    }

    @Override
    public @NotNull Set<EquipmentSlot> getActiveSlots() {
        return EnumSet.of(EquipmentSlot.HAND);
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
    public boolean conflictsWith(@NotNull Enchantment other) {
        return false;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 3; // Максимально три уровня зачарования
    }

    public static void handleBlockBreak(BlockBreakEvent event, int level) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (tool == null || !tool.getType().toString().contains("PICKAXE")) {
            return; // Ничего не делаем, если инструмент не кирка
        }

        Block centerBlock = event.getBlock();

        // Определяем размер зоны захвата в зависимости от уровня зачарования
        int radius = Math.min(level, 3); // Максимальный радиус захвата ограничен уровнем зачарования

        // Фиксированная сетка сканирования по направлению
        for (int x = -radius; x <= radius; ++x) {
            for (int y = -radius; y <= radius; ++y) {
                for (int z = -radius; z <= radius; ++z) {
                    if (x == 0 && y == 0 && z == 0) continue; // Исключаем центральный блок

                    Block currentBlock = centerBlock.getRelative(x, y, z);

                    if (currentBlock.getType() != Material.AIR) {
                        // Генерируем событие взлома блока
                        BlockBreakEvent breakEvent = new BlockBreakEvent(currentBlock, player);
                        player.getServer().getPluginManager().callEvent(breakEvent);

                        // Если событие не отменено, выполняем взлом
                        if (!breakEvent.isCancelled()) {
                            currentBlock.breakNaturally(tool);
                        }
                    }
                }
            }
        }
    }

    private static List<Block> getBlocksWithinRadius(Block center, int radius) {
        List<Block> blocks = new ArrayList<>();

        // Собираем блоки в пределах заданного радиуса
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    blocks.add(center.getRelative(x, y, z));
                }
            }
        }

        return blocks;
    }
}