package me.sintaxlabs.hungerhealth;

/*
    MavenWare Development
    Version: 1.0.3
    Date: November 5, 2025

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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
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
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(getClass()), () -> ConvertHungerToHealth(e.getEntity()), 1L);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerJoin(EntityExhaustionEvent e)
    {
        Entity entity = e.getEntity();
        if (entity instanceof Player)
        {
            Player player = (Player)e.getEntity();
            if (Global.configToggleHardMode) player.setSaturation(0);
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerRespawn(PlayerRespawnEvent e)
    {
        Player player = e.getPlayer();
        if (Global.configToggleHardMode) player.setSaturation(0);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();
        if (Global.configToggleHardMode) player.setSaturation(0);
    }

    private static void ConvertHealthToHunger(Entity e)
    {
        Player player = (Player) e;
        if (Global.configToggleHardMode) player.setSaturation(1);

        if (player.getHealth() <= 0)
        {
            player.setFoodLevel(0);
            return;
        }

        if (player.getHealth() > 20)
        {
            player.setHealth(20);
            player.setFoodLevel(20);
            return;
        }

        double hunger = player.getFoodLevel();
        player.setHealth(hunger);

        double health = player.getHealth();
        player.setFoodLevel((int) health);
    }

    private static void ConvertHungerToHealth(Entity e)
    {
        Player player = (Player) e;
        if (Global.configToggleHardMode) player.setSaturation(1);

        if (player.getHealth() <= 0)
        {
            player.setFoodLevel(0);
            return;
        }

        if (player.getFoodLevel() > 20)
        {
            player.setHealth(20);
            player.setFoodLevel(20);
            return;
        }

        double health = player.getHealth();
        player.setFoodLevel((int) health);

        double hunger = player.getFoodLevel();
        player.setHealth(hunger);
    }
    
    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
    }
}
