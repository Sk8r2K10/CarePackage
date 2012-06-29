package me.Sk8r2K10.carepackage;

public class Cooldown implements Runnable {

    public CarePackage plugin;
    
    public Cooldown(CarePackage inst) {
        this.plugin = inst;
    }
    
    @Override
    public void run() {
        
        plugin.util.cool = true;
    }
}
