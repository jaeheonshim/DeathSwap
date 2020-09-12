package com.jaeheonshim.deathswap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathSwapPlugin extends JavaPlugin {
    public void onEnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new DeathSwapTicker(), 0, 20);
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("startswap")) {
            if(strings.length < 1) {
                return false;
            }

            int interval;
            try {
                interval = Integer.parseInt(strings[0]);
            } catch(NumberFormatException e) {
                return false;
            }

            DeathSwapManager manager = DeathSwapManager.getInstance();
            manager.setInterval(interval);
            manager.start();

            return true;
        } else if(command.getName().equalsIgnoreCase("swap")) {
            DeathSwapManager.getInstance().swap();
            return true;
        } else if(command.getName().equalsIgnoreCase("stopswap")) {
            DeathSwapManager.getInstance().stop();
            return true;
        }

        return false;
    }
}
