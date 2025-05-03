package ru.funenchantments.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EnchantmentManager {
    
    private final JavaPlugin plugin;
    private final Map<String, CustomEnchantment> enchantments = new HashMap<>();
    
    public EnchantmentManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void registerEnchantments() {
        try {
            // Разрешаем редактирование списка зачарований через рефлексию
            Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNew.setAccessible(true);
            acceptingNew.set(null, true);
            
            // Регистрируем зачарование Бульдозер
            registerEnchantment(new BulldozerEnchantment(plugin));
            
            acceptingNew.set(null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void registerEnchantment(CustomEnchantment enchantment) {
        try {
            Enchantment.registerEnchantment(enchantment);
            enchantments.put(enchantment.getKey().getKey(), enchantment);
        } catch (IllegalArgumentException e) {
            // Если зачарование уже зарегистрировано, получаем его
            enchantments.put(enchantment.getKey().getKey(), enchantment);
        }
    }
    
    public void unregisterEnchantments() {
        try {
            Field byKeyField = Enchantment.class.getDeclaredField("byKey");
            byKeyField.setAccessible(true);
            
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) byKeyField.get(null);
            
            Field byNameField = Enchantment.class.getDeclaredField("byName");
            byNameField.setAccessible(true);
            
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) byNameField.get(null);
            
            for (CustomEnchantment enchantment : enchantments.values()) {
                byKey.remove(enchantment.getKey());
                byName.remove(enchantment.getName());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Map<String, CustomEnchantment> getEnchantments() {
        return enchantments;
    }
    
    public CustomEnchantment getEnchantment(String key) {
        return enchantments.get(key);
    }
} 