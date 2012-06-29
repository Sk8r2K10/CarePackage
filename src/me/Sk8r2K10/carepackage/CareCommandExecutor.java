package me.Sk8r2K10.carepackage;

import net.milkbowl.vault.item.Items;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CareCommandExecutor implements CommandExecutor {

    public CarePackage plugin;

    public CareCommandExecutor(CarePackage inst) {

        this.plugin = inst;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("carepackage.send")) {
            sender.sendMessage(plugin.pre + ChatColor.RED + "You don't have permission for that command");
            return false;
        }
        
        if (!plugin.util.cool) {
            sender.sendMessage(plugin.pre + ChatColor.RED + "Cooldown period active, Please wait.");
            return false;
        }
        
        if (commandLabel.equalsIgnoreCase("carepackage") || commandLabel.equalsIgnoreCase("cp")) {
            if (args.length != 1) {
                sender.sendMessage(plugin.pre + ChatColor.RED + "Invalid arguments.");
                sender.sendMessage(plugin.pre + ChatColor.GRAY + "/carepackage [player]");
                return false;
            }

            if (args.length == 1 && Bukkit.getPlayer(args[0]) != null && sender.hasPermission("carepackage.send")) {
                Player target = Bukkit.getPlayer(args[0]);
                World world = target.getWorld();
                Location targetloc = plugin.util.getRoundedLoc(target.getLocation());
                
                if (targetloc.getY() > 255 || targetloc.getY() < 1) {
                    sender.sendMessage(plugin.pre + ChatColor.RED + "No area to place chest!");
                    return false;
                }
                
                Location loc = plugin.util.findEmptyBlock(targetloc, world);
                if (loc == null) {
                    sender.sendMessage(plugin.pre + ChatColor.RED + "No area to place chest!");
                    return false;
                }
                loc.getBlock().setType(Material.CHEST);
                Chest chestblock = (Chest) loc.getBlock().getState();
                
                loc.setY(loc.getY() + 1);
                
                if (loc.getBlock().isEmpty()) loc.getBlock().setType(Material.FENCE);
                loc.setZ(loc.getZ() - 1);
               
                if (loc.getBlock().isEmpty()) {
                    byte n = (byte) 0x2;                    
                    loc.getBlock().setType(Material.WALL_SIGN);
                    
                    Sign sign = (Sign) loc.getBlock().getState();
                    sign.getData().setData(n); //set direction
                    
                    sign.setLine(0, "===============");
                    sign.setLine(1, "Care Package");
                    sign.setLine(2, sender.getName());
                    sign.setLine(3, "===============");
                    sign.update();
                }
                loc.setZ(loc.getZ() + 1);
                loc.setY(loc.getY() + 1);
                loc.getBlock().setType(Material.REDSTONE_TORCH_ON);
                
                target.sendMessage(plugin.pre + ChatColor.YELLOW + "You have been sent a care package from " + sender.getName());
                target.sendMessage(plugin.pre + ChatColor.YELLOW + "Make sure you don't miss it, It should be nearby!");
                
                for (String s : plugin.cont) {
                    if (plugin.util.isMaterial(s)) {
                        ItemStack i = Items.itemByString(s).toStack();
                        i.setAmount(plugin.getConfig().getInt("chest.amounts." + Items.itemByStack(i).getName().replace(" ", "_")));
                        chestblock.getBlockInventory().addItem(i);
                    } else {
                        plugin.log.severe("[CarePackage] There appears to be problems with your Config.");
                        plugin.log.severe("[CarePackage] Please check your config for errors.");
                        plugin.log.severe("[CarePackage] If the problem persists, Contact Sk8r2K9");
                        plugin.log.severe("[CarePackage] Note: Vault could be out of date.");
                        continue;
                    }
                }
                sender.sendMessage(plugin.pre + ChatColor.GREEN + "The Care Package has been sent.");
                plugin.util.cool = false;
                plugin.util.startCooldown();
                return true;
                
            }
            
            if (Bukkit.getPlayer(args[0]) == null) {
                sender.sendMessage(plugin.pre + ChatColor.RED + "That player doesn't exist");
                return false;
            }
        }
        return false;
    }
}