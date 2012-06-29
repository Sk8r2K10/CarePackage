package me.Sk8r2K10.carepackage;

import net.milkbowl.vault.item.Items;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Utils {

    public CarePackage plugin;
    public boolean cool = true;

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
}