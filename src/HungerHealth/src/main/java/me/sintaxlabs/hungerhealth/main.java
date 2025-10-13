package me.sintaxlabs.hungerhealth;

/*
    MavenWare Development
    Version: 1.0.2
    Date: October 12, 2025

    Follow applicable laws for Apache 2.0.

    @jammingcat21 - Discord
    Github.com/MavenWare
*/

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin implements Listener
{

    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        Global.configToggleHardMode = this.getConfig().getBoolean("HardMode");
    }

    public static class Global
    {
        public static boolean configToggleHardMode;
    }

    // Food level can change from exhaustion, hunger, potions, etc.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void foodEvent(FoodLevelChangeEvent e)
    {
        Entity entity = e.getEntity();
        if (entity instanceof Player)
        {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(this.getClass()), () -> ConvertHealthToHunger(e.getEntity()), 1L);
        }
    }


    // Got Hurt? Set hunger to health.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void healthEvent1(EntityDamageEvent e)
    {
        Entity entity = e.getEntity();
        if (entity instanceof Player)
        {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(getClass()), () -> ConvertHungerToHealth(e.getEntity()), 1L);
        }
    }

    //Healing? Set hunger to health.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void healthEvent2(EntityRegainHealthEvent e)
    {
        Entity entity = e.getEntity();
        if (entity instanceof Player)
        {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(getClass()), () -> ConvertHungerToHealth(e.getEntity()), 2L);
        }
    }

    private static void ConvertHealthToHunger(Entity e)
    {
        Player player = (Player)e;
        if (player.getHealth() <= 0 || player.getHealth() > 20) return;

        float hunger = player.getFoodLevel();
        player.setHealth(hunger);
        if (Global.configToggleHardMode) player.setSaturation(0);
    }

    private static void ConvertHungerToHealth(Entity e)
    {
        Player player = (Player) e;
        if (player.getHealth() <= 0) return;

        float health = (float) player.getHealth();
        player.setFoodLevel((int) health);
        if (Global.configToggleHardMode) player.setSaturation(0);
    }
    
    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
    }
}
