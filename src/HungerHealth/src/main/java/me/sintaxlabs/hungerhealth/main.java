package me.sintaxlabs.hungerhealth;

//Date: Oct 5, 2025
//Vers: 1.0.1


import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerItemConsumeEvent;
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

    // Get exhausted? Set health to hunger.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void hungerEvent(EntityExhaustionEvent e)
    {
        Entity entity = e.getEntity();
        if (entity instanceof Player)
        {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(getClass()), () -> ConvertHealthToHunger1(e), 2L);
            //ConvertHealthToHunger1(e);
        }
    }

    // Eating? Set health to hunger.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void painEvent(PlayerItemConsumeEvent e)
    {
        Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(getClass()), () -> ConvertHealthToHunger2(e), 2L);
        //ConvertHealthToHunger2(e);
    }


    // Got Hurt? Set hunger to health.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void painEvent(EntityDamageEvent e)
    {
        Entity entity = e.getEntity();
        if (entity instanceof Player)
        {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(getClass()), () -> ConvertHungerToHealth(e.getEntity()), 1L);
            //ConvertHungerToHealth(e.getEntity());
        }
    }

    //Healing? Set hunger to health.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void painEvent(EntityRegainHealthEvent e)
    {
        Entity entity = e.getEntity();
        if (entity instanceof Player)
        {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(getClass()), () -> ConvertHungerToHealth(e.getEntity()), 2L);
            //ConvertHungerToHealth(e.getEntity());
        }
    }

    private static void ConvertHealthToHunger1(EntityExhaustionEvent e)
    {
        Player player = (Player) e.getEntity();
        if (player.getHealth() <= 0) return;
        if (player.getHealth() > 20) return;

        float hunger = player.getFoodLevel();
        player.setHealth(hunger);
        if (Global.configToggleHardMode) player.setSaturation(0);

    }
    private static void ConvertHealthToHunger2(PlayerItemConsumeEvent e)
    {
        Player player = e.getPlayer();
        if (player.getHealth() <= 0) return;
        if (player.getHealth() > 20) return;

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
