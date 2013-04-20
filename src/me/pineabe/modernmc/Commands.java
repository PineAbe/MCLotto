package me.pineabe.modernmc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
//import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Commands implements CommandExecutor {
    public final Plugin plugin;
    public static Economy econ = null;
    public final HashMap<Player, Integer> hm = new HashMap<Player, Integer>();
    public final HashMap<String, Integer> map = new HashMap<String, Integer>();
    
	public Commands(Plugin plugin){
    	this.plugin = plugin;
    }
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]){
    	Player p = (Player) sender;
    	RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();
        EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("TicketFare"));
        int wn = new Random().nextInt(plugin.getConfig().getInt("TicketsForSale"));
        int jackpot = plugin.getConfig().getInt("Jackpot");
        int fare = plugin.getConfig().getInt("TicketFare");
    	if(commandLabel.equalsIgnoreCase("lotto")){
    		if(args.length == 0) {
	    		
    			if(p.hasPermission("lotto.admin")){

	        		p.sendMessage(ChatColor.GREEN + "------(MCLotto Help)------");
	        		p.sendMessage(ChatColor.DARK_GREEN + "/lotto buyticket (1 - " + plugin.getConfig().getInt("TicketsForSale") + ") " + ChatColor.WHITE + "|" + ChatColor.AQUA + " Purchase a lotto ticket");	
	        		p.sendMessage(ChatColor.DARK_GREEN + "/lotto jackpot " + ChatColor.WHITE + "|" + ChatColor.AQUA + " Shows the current jackpot and ticketfare");
	        		p.sendMessage(ChatColor.DARK_GREEN + "/lotto start " + ChatColor.WHITE + "|" + ChatColor.AQUA +  " Selects a random number and allows tickets to be bought");
	        		p.sendMessage(ChatColor.DARK_GREEN + "/lotto stop " + ChatColor.WHITE + "|" + ChatColor.AQUA + " Stop ticket sales and disallow the winner");
	        		p.sendMessage(ChatColor.DARK_GREEN + "/lotto setjackpot " + ChatColor.WHITE + "|" + ChatColor.AQUA + " Set the jackpot using in-game commands");
	        		p.sendMessage(ChatColor.DARK_GREEN + "/lotto setfare " + ChatColor.WHITE + "|" + ChatColor.AQUA + " Set the ticket fare with in-game commands");
	        		p.sendMessage(ChatColor.DARK_GREEN + "/lotto setmaxtickets " + ChatColor.WHITE + "|" + ChatColor.AQUA + " Set the max ticket number with in-game commands");
	    		}
    			
    		else if(p.hasPermission("lotto.user")){
        		p.sendMessage(ChatColor.GREEN + "------(MCLotto Help)------");
        		p.sendMessage(ChatColor.DARK_GREEN + "/lotto buyticket (1 - " + plugin.getConfig().getInt("TicketsForSale") + ") " + ChatColor.WHITE + "|" + ChatColor.AQUA + " Purchase a lotto ticket");
        		p.sendMessage(ChatColor.DARK_GREEN + "/lotto jackpot " + ChatColor.WHITE + "|" + ChatColor.AQUA + " Shows the current jackpot and ticketfare");
    		}
    	}
    		else if(args.length == 1 ){
    			if(args[0].equalsIgnoreCase("buyticket")){
		   			p.sendMessage(ChatColor.RED + "Syntax Error: Please define a ticket value");
    			}
    			
    			else if(args[0].equalsIgnoreCase("stop")){
    				if(map.containsKey("le")){
        		   		if(p.hasPermission("lotto.admin")){
	        		   		map.remove("le");
	        		   		Bukkit.broadcastMessage(ChatColor.GOLD + "The lotto has been" + ChatColor.RED + " stopped");
	        				}
    				}
        		   	else{
            		   		p.sendMessage(ChatColor.RED + "The lotto has not started yet!");
	            		}
		    	}
    			
    			else if(args[0].equalsIgnoreCase("jackpot")){
    				if(p.hasPermission("lotto.user") || p.hasPermission("lotto.admin")){
    					if(!map.containsKey("le")){
    	            		p.sendMessage(ChatColor.RED + "The lotto has not started yet!");
            			}
    					else{
    						if(!map.containsKey("il")){
    						   p.sendMessage(ChatColor.YELLOW + "The jackpot is: " + jackpot + " " + econ.currencyNamePlural());
    						}
    						else{
    							p.sendMessage(ChatColor.GREEN + p.getDisplayName() + "The jackpot is " + ChatColor.AQUA + "Item: " + ChatColor.GREEN + String.valueOf(map.get("i")) + ChatColor.AQUA + " Amount: " + ChatColor.GREEN + String.valueOf(map.get("ia")));
    						}
    						p.sendMessage(ChatColor.YELLOW + "The ticket fare is: " + fare + " " + econ.currencyNamePlural());
    						}
    					}
    			}
    			else if(args[0].equalsIgnoreCase("start")){
                    if(p.hasPermission("lotto.admin")){
    				if(!map.containsKey("le")){
    					map.put("le", null);
    					map.put("wn", wn);
    					p.sendMessage(ChatColor.YELLOW + "You have now started a lotto with the winning number as " + wn);
    					Bukkit.broadcastMessage(ChatColor.GOLD + "A lotto has now commenced! (/lotto)");
    					Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable(){
    						public void run(){
    							if(map.containsKey("le")){
    								Bukkit.broadcastMessage(ChatColor.GOLD + "A lotto is currently occuring (/lotto)");
    							}
    						}
    					}, 1200L, 1200L);
    				}
    				else{
        				p.sendMessage(ChatColor.RED + "The Lotto has already started");
        			}
    			}
    		}
    	}
    		else if(args.length == 2){
    			if(args[0].equalsIgnoreCase("buyticket")){
    				
    				if(p.hasPermission("lotto.user") || p.hasPermission("lotto.admin")){
    					if(!map.containsKey("le")){
						p.sendMessage(ChatColor.RED +"The lotto has not started yet!");
					}
    					else if(hm.containsKey(p)){
    					p.sendMessage(ChatColor.RED + "Error: You have already purchased a ticket");	
    					}
    					else if(r.transactionSuccess()) {
    						List<Player> onlinePlayers = Arrays.asList(Bukkit.getServer().getOnlinePlayers());
        		   			Iterator<Player> iterator = onlinePlayers.iterator();
        		   			while(iterator.hasNext()){
        		   				Player onlinePlayer = iterator.next();
        		   				if(args[1].equals(hm.get(onlinePlayer))){
        		   					p.sendMessage(ChatColor.RED + onlinePlayer.getName() + " is in ownership of this ticket");
        		   				}
        		   			}
        		   			if(!(Integer.parseInt(args[1]) == (map.get("wn")))){
                		   		Bukkit.broadcastMessage(ChatColor.GREEN + p.getName() + " has purchased lotto ticket #" + args[1]);
                		   		hm.put(p, Integer.parseInt(args[1]));
        						}
        		   			else{
        		   				if(!map.containsKey("il")){
        		   				    econ.depositPlayer(p.getName(), jackpot);
            		   			    map.remove("le");
            		   			    Bukkit.broadcastMessage(ChatColor.GREEN + p.getDisplayName() + " has won the lotto with the jackpot of " + jackpot + " " + econ.currencyNamePlural() );
        		   				}
        		   				else{
        		   					PlayerInventory pi = p.getInventory();
        							ItemStack stack = new ItemStack(map.get("i"), map.get("ia"));
        							pi.addItem(stack);
        							Bukkit.broadcastMessage(ChatColor.GREEN + p.getDisplayName() + " has won the lotto with the jackpot " + ChatColor.AQUA + "Item: " + ChatColor.GREEN + String.valueOf(map.get("i")) + ChatColor.AQUA + " Amount: " + ChatColor.GREEN + String.valueOf(map.get("ia")));
        							map.remove("il");
        							map.remove("le");
        		   				}
        		   			}
    					}
	    					else {
	    		               p.sendMessage(String.format(ChatColor.RED + "An error occured: %s", r.errorMessage));
	    		           }
    				}
    			}
    			else if(args[0].equalsIgnoreCase("setjackpot")){
    				if(p.hasPermission("lotto.set")){
		   			p.sendMessage(ChatColor.YELLOW + "Jackpot set to " + args[1]);
		   			plugin.getConfig().set("Jackpot", Integer.parseInt(args[1]));
		   			plugin.saveConfig();
    				}
    			}
    			else if(args[0].equalsIgnoreCase("setfare")){
    				if(p.hasPermission("lotto.set")){
		   			p.sendMessage(ChatColor.YELLOW + "Ticket fare set to " + args[1]);
		   			plugin.getConfig().set("TicketFare", Integer.parseInt(args[1]));
		   			plugin.saveConfig();
    			}
    			}
    			else if(args[0].equalsIgnoreCase("setmaxtickets")){
    				if(p.hasPermission("lotto.set")){
		   			p.sendMessage(ChatColor.YELLOW + "Max ticket number set to " + args[1]);
		   			plugin.getConfig().set("TicketsForSale", Integer.parseInt(args[1]));
		   			plugin.saveConfig();
    				}
    			}
    		}
    		else if(args.length >= 3) {
    			if(args[0].equalsIgnoreCase("itemlotto")){
    				if(p.hasPermission("lotto.set")){
    					if(isInt(args[1]) && isInt(args[2])){
    						if(!map.containsKey("il")) {
    							map.put("i", Integer.parseInt(args[1]));
    							map.put("ia", Integer.parseInt(args[2]));
                                p.sendMessage(ChatColor.AQUA + "ItemLotto" + ChatColor.YELLOW + " mode activated. /lotto start to continue");
    				            map.put("il", 1);
    						}
    						/*else if(!isInt(args[1]) && isInt(args[2])){
    							String material = args[1];
    							if(Material.matchMaterial(material) != null) {
    								map.put("i", Integer.parseInt("material"));
    								map.put("ia", Integer.parseInt(args[2]));
    								}
    							else{
    								p.sendMessage(ChatColor.RED + "Unknown material: " + args[1]);
    							}	 
    						}*/
    						else{
    							p.sendMessage(ChatColor.RED + "Syntax Error: /lotto itemlotto <itemid> <amount>");
    						}
    			    }
    			}
    		}	
    	}
    		else if(args.length >= 4) {
    			p.sendMessage(ChatColor.RED + "Unknown argument" + args[3]);
    		}
    }
		return false;
   }

    	public static boolean isInt(String s) {
    	    try {
    	        Integer.parseInt(s);
    	    } catch (NumberFormatException nfe) {
    	        return false;
    	    }
    	    return true;
    	}
}
