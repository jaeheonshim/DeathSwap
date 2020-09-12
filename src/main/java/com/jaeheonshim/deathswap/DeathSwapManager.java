package com.jaeheonshim.deathswap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class DeathSwapManager {
    private boolean running;
    private long interval;
    private long elapsed;
    private long start;

    private static DeathSwapManager instance = new DeathSwapManager();
    private BossBar bossBar;

    public static DeathSwapManager getInstance() {
        return instance;
    }

    public void start() {
        this.running = true;
        this.start = System.currentTimeMillis();
    }

    public void stop() {
        this.running = false;
    }

    public void tick() {
        this.elapsed = System.currentTimeMillis() - start;

        if(bossBar == null) {
            bossBar = Bukkit.getServer().createBossBar("Swapping In: ", BarColor.RED, BarStyle.SOLID, new BarFlag[0]);
            bossBar.setVisible(true);
            for(Player player : Bukkit.getOnlinePlayers()) {
                bossBar.addPlayer(player);
            }
        }

        bossBar.setProgress((double) this.elapsed / this.interval);
        bossBar.setTitle("Swapping In " + (interval - elapsed) / 1000 + " seconds!");

        if(elapsed >= interval) {
            swap();
        }

        if((interval - elapsed) <= 10000) {
            Bukkit.getServer().broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Swapping in " + (interval - elapsed) / 1000 + " seconds!");
        }
    }

    public void swap() {
        this.start = System.currentTimeMillis();

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Player extraPlayer = null;

        Collections.shuffle(players);
        if(players.size() % 2 != 0) {
            extraPlayer = players.get(players.size() - 1);
            players.remove(players.size() - 1);
        }

        int splitPoint = players.size() / 2;

        List<Player> l1 = players.subList(0, splitPoint);
        List<Player> l2 = players.subList(splitPoint, players.size());

        l2 = rotRight(l2, 1);

        for(int i = 0; i < l1.size(); i++) {
            performSwap(l1.get(i), l2.get(i));
        }

        if(extraPlayer != null) {
            performSwap(players.get(0), extraPlayer);
        }
    }

    public static void performSwap(Player player1, Player player2) {
        Location player1To = player2.getLocation();
        Location player2To = player1.getLocation();

        Bukkit.getLogger().log(Level.INFO, player1To + " <-> " + player2To);

        player1.teleport(player1To);
        player2.teleport(player2To);
    }

    public static <T> List<T> rotRight(List<T> list, int places) {
        List<T> overrun = list.subList(list.size() - places ,list.size());
        List<T> newList = new ArrayList<T>(overrun);
        newList.addAll(list.subList(0, places));

        return newList;
    }

    public boolean isRunning() {
        return running;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval * 1000;
    }

    public long getElapsed() {
        return elapsed;
    }

    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }
}
