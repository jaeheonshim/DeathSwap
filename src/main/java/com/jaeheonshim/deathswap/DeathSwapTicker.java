package com.jaeheonshim.deathswap;

public class DeathSwapTicker implements Runnable {
    public void run() {
        DeathSwapManager manager = DeathSwapManager.getInstance();

        if(manager.isRunning()) {
            manager.tick();
        }
    }
}
