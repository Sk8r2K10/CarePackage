
package me.Sk8r2K10.carepackage;

import java.util.List;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class CarePackage extends JavaPlugin {

    public int ID;
    public List<String> cont;
    public Utils util = new Utils(this);
    public static final Logger log = Logger.getLogger("Minecraft");
    public String pre = ChatColor.GREEN + "[" + ChatColor.YELLOW + "CarePackage" + ChatColor.GREEN + "] " + ChatColor.RESET;

	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public void onEnable() {
		getCommand("carepackage").setExecutor(new CareCommandExecutor(this));
        
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            
            log.severe("[CarePackage] Vault is missing! Disabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
        }
        
        getConfig().options().copyDefaults(true);
        saveConfig();
        
        setupContents();
	}
    
    public void setupContents() {
        cont = (List<String>) this.getConfig().getList("chest.contents");
        if (cont.size() > 27) {
            this.log.severe("[CarePackage] Too many items to add to chest!");
            this.log.severe("[CarePackage] Check your config.");
            this.log.severe("[CarePackage] Disabling plugin.");
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        if (cont == null) {
            this.log.severe("[CarePackage] Contents of CarePackage have not been setup!");
            this.log.severe("[CarePackage] Check your config.");
            this.log.severe("[CarePackage] Disabling plugin.");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        this.log.info("[CarePackage] Chest contents created.");
    }	
}
