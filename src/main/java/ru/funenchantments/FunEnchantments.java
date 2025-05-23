package ru.funenchantments;

import ru.funenchantments.commands.GiveEnchantedCommand;
import ru.funenchantments.enchantments.BulldozerEnchantment;
import ru.funenchantments.tabcompleters.GiveEnchantedTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

public class FunEnchantments extends JavaPlugin {

    private static final Enchantment BULLDOZER = new BulldozerEnchantment();

    @Override
    public void onEnable() {
        if (Enchantment.getByKey(BULLDOZER.getKey()) == null) {
            Enchantment.registerEnchantment(BULLDOZER);
        }

        getCommand("giveenchanted").setExecutor(new GiveEnchantedCommand());
        getCommand("giveenchanted").setTabCompleter(new GiveEnchantedTabCompleter());

        Bukkit.getPluginManager().registerEvents(new ru.funenchantments.enchantments.BulldozerEnchantment(), this);
    }

    @Override
    public void onDisable() {
//        Enchantment.unregisterEnchantment(BULLDOZER);
    }
}