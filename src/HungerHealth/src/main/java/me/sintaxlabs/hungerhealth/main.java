package me.sintaxlabs.hungerhealth;

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
        getServer().getPluginManager().registerEvents(this, this);
    }

    // Get exhausted? Set health to hunger.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void hungerEvent(EntityExhaustionEvent e)
    {
        Entity entity = e.getEntity();
        if (entity instanceof Player)
        {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(getClass()), () -> ConvertHealthToHunger1(e), 1L);
        }
    }

    // Eating? Set health to hunger.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void painEvent(PlayerItemConsumeEvent e)
    {
        Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(getClass()), () -> ConvertHealthToHunger2(e), 1L);
    }



    // Get hurt by mob? Set hunger to health.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void painEvent(EntityDamageEvent e)
    {
        Entity entity = e.getEntity();
        if (entity instanceof Player)
        {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(getClass()), () -> ConvertHungerToHealth(e.getEntity()), 1L);
        }
    }

    //Healing? Set hunger to health.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void painEvent(EntityRegainHealthEvent e)
    {
        Entity entity = e.getEntity();
        if (entity instanceof Player)
        {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(getClass()), () -> ConvertHungerToHealth(e.getEntity()), 1L);
        }
    }

    private static void ConvertHealthToHunger1(EntityExhaustionEvent e)
    {
        Player player = (Player) e.getEntity();
        float hunger = player.getFoodLevel();
        player.setHealth(hunger);
        player.setSaturation(0);
    }
    private static void ConvertHealthToHunger2(PlayerItemConsumeEvent e)
    {
        Player player = e.getPlayer();
        float hunger = player.getFoodLevel();
        player.setHealth(hunger);
        player.setSaturation(0);
    }

    private static void ConvertHungerToHealth(Entity e)
    {
        Player player = (Player) e;
        //player.setSaturatedRegenRate(0);
        float health = (float) player.getHealth();
        player.setFoodLevel((int) health);
        player.setSaturation(0);
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
