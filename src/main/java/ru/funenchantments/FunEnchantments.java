package ru.funenchantments;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import ru.funenchantments.commands.FunEnchantmentsCommand;
import ru.funenchantments.commands.FunEnchantmentsTabCompleter;
import ru.funenchantments.enchantments.EnchantmentManager;
import ru.funenchantments.listeners.EnchantmentListener;

public final class FunEnchantments extends JavaPlugin {

    private EnchantmentManager enchantmentManager;

    @Override
    public void onEnable() {
        // Инициализация менеджера зачарований
        enchantmentManager = new EnchantmentManager(this);
        enchantmentManager.registerEnchantments();
        
        // Регистрация обработчика событий
        getServer().getPluginManager().registerEvents(new EnchantmentListener(enchantmentManager), this);
        
        // Регистрация команды
        PluginCommand command = getCommand("funenchantments");
        if (command != null) {
            FunEnchantmentsCommand commandExecutor = new FunEnchantmentsCommand(enchantmentManager);
            FunEnchantmentsTabCompleter tabCompleter = new FunEnchantmentsTabCompleter(enchantmentManager);
            
            command.setExecutor(commandExecutor);
            command.setTabCompleter(tabCompleter);
        }
        
        getLogger().info("FunEnchantments плагин успешно загружен!");
    }

    @Override
    public void onDisable() {
        // Отключение зачарований
        if (enchantmentManager != null) {
            enchantmentManager.unregisterEnchantments();
        }
        
        getLogger().info("FunEnchantments плагин отключен!");
    }
}
