package ru.funenchantments.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public abstract class CustomEnchantment extends Enchantment {
    
    private final String name;
    private final int maxLevel;
    
    public CustomEnchantment(NamespacedKey key, String name, int maxLevel) {
        super(key);
        this.name = name;
        this.maxLevel = maxLevel;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public int getMaxLevel() {
        return maxLevel;
    }
    
    public static boolean hasEnchantment(ItemStack item, Enchantment enchantment) {
        if (item == null) return false;
        return item.getEnchantments().containsKey(enchantment);
    }
    
    public static int getLevel(ItemStack item, Enchantment enchantment) {
        if (!hasEnchantment(item, enchantment)) return 0;
        return item.getEnchantmentLevel(enchantment);
    }
} 