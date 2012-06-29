package me.Sk8r2K10.carepackage;

import java.util.List;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.item.Items;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Utils {

    public CarePackage plugin;
    public boolean cool = true;
    private Economy econ;

    public Utils(CarePackage inst) {
        this.plugin = inst;
    }

    public Location findEmptyBlock(Location targetloc, World world) {
        Location loc = null;

        int minx = (int) targetloc.getX() - 3;
        int miny = (int) targetloc.getY() - 1;
        int minz = (int) targetloc.getZ() - 3;

        int maxx = (int) targetloc.getX() + 3;
        int maxy = (int) targetloc.getY() + 2;
        int maxz = (int) targetloc.getZ() + 3;

        for (int x = minx; x <= maxx; x++) {
            for (int y = miny; y <= maxy; y++) {
                for (int z = minz; z <= maxz; z++) {
                    Block b = world.getBlockAt(x, y, z);
                    if ((b.isEmpty() && !b.isLiquid()) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.LONG_GRASS)) {
                        if (loc != null) {
                            Location temploc = this.makeLocation(x, y, z, world);

                            if (targetloc.distanceSquared(loc) > targetloc.distanceSquared(temploc) && temploc != loc) {
                                loc = new Location(world, x, y, z);
                            }
                        } else {
                            if (loc != targetloc) {
                                loc = this.makeLocation(x, y, z, world);
                            }
                        }
                    }
                }
            }
        }
        return loc;
    }

    public Location getRoundedLoc(Location targetloc) {

        int nx = (int) targetloc.getX();
        int ny = (int) targetloc.getY();
        int nz = (int) targetloc.getZ();

        Location loc = new Location(targetloc.getWorld(), nx, ny, nz);
        return loc;
    }
    
    public Economy getEcon() {
        
        return this.econ;
    }

    public Location makeLocation(int x, int y, int z, World world) {

        int nx = (int) x;
        int ny = (int) y;
        int nz = (int) z;

        Location loc = new Location(world, nx, ny, nz);
        return loc;
    }

    public boolean isMaterial(String s) {
        if (Items.itemByString(s) != null) {

            return true;
        }
        if (Material.matchMaterial(s) != null && Material.matchMaterial(s) != Material.AIR) {
            plugin.log.warning("[CarePackage] It appears Vault is out of date.");
            plugin.log.warning("[CarePackage] Please update Vault.");
            return false;
        }
        return false;
    }

    public void startCooldown() {
        long c = plugin.getConfig().getLong("options.cooldown") * 20;

        plugin.ID = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Cooldown(plugin), c);

    }
    
    public void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            if (plugin.getConfig().getInt("options.cost") != 0) {
                
                plugin.log.warning("[CarePackage] No Economy plugin found, Setting cost to 0.");
                plugin.getConfig().set("options.cost", 0);
            }
            return;
        }
        econ = rsp.getProvider();
    }
    
    public void setupContents() {
        plugin.cont = (List<String>) plugin.getConfig().getList("chest.contents");
        if (plugin.cont.size() > 27) {
            plugin.log.severe("[CarePackage] Too many items to add to chest!");
            plugin.log.severe("[CarePackage] Check your config.");
            plugin.log.severe("[CarePackage] Disabling plugin.");
            plugin.getPluginLoader().disablePlugin(plugin);
            return;
        }

        if (plugin.cont == null) {
            plugin.log.severe("[CarePackage] Contents of CarePackage have not been setup!");
            plugin.log.severe("[CarePackage] Check your config.");
            plugin.log.severe("[CarePackage] Disabling plugin.");
            plugin.getPluginLoader().disablePlugin(plugin);
            return;
        }
        plugin.log.info("[CarePackage] Chest contents created.");
    }
}