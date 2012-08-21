package me.pineabe.modernmc;
import java.io.File;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class MineCraftLotto extends JavaPlugin {
	/* This is an open source plugin created by PineAbe/nggmc/TickleNinja
	 * However that does not mean you can release edits without permission from the official owner
	 * Feel free to make edits and keep them contained within your machine/server machine
	 * Thank You!*/

		public final Logger logger = Logger.getLogger("Minecraft");
	    public static Economy econ = null;
		
	    //ENABLE METHOD
	    public void createConfig() {
	    	  File file = new File(getDataFolder() + File.separator + "config.yml");
	    	  if (!file.exists()) {
	    	   this.getLogger().info("You don't have a config file!!!");
	    	   this.getLogger().info("Generating config.yml.....");
	    	   getConfig().options().copyDefaults(true);
	    	   this.saveConfig();
	    	  }
	    	  }
	    
		public void onEnable() {
			PluginDescriptionFile pdffile = this.getDescription();
			if (getServer().getPluginManager().getPlugin("Vault") == null) {
				this.logger.info("ERROR: " + pdffile.getName() + "requires Vault as a dependancy. MCLotto disabled!");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
			else if (!setupEconomy() ) {
				this.logger.info(Level.SEVERE + "ERROR: " + pdffile.getName() + "requires Vault economy plugin as a dependancy. MCLotto disabled!");
				getServer().getPluginManager().disablePlugin(this);
				};
			this.logger.info(pdffile.getName() + " " + pdffile.getVersion() + " hooked on with " + econ.getName());
	   		createConfig();		
			getConfig().set("LottoEnabled", "false");
		}
		
		//DISABLE METHOD
		
		public void onDisable() {
			PluginDescriptionFile pdffile = this.getDescription();
			this.logger.info(pdffile.getName() + " " + pdffile.getVersion() + " has successfully stopped!");
		    }
		
	    private boolean setupEconomy() {
	        if (getServer().getPluginManager().getPlugin("Vault") == null) {
	            return false;
	        }
	        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
	        if (rsp == null) {
	            return false;
	        }
	        econ = rsp.getProvider();
	        return econ != null;
	   } 

	    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]){
	    	Player player = (Player) sender;
            EconomyResponse r = econ.withdrawPlayer(player.getName(), getConfig().getInt("TicketFare"));
            int wn = new Random().nextInt(getConfig().getInt("TicketsForSale"));
            int jackpot = getConfig().getInt("Jackpot");
            int fare = getConfig().getInt("TicketFare");
	    	if(commandLabel.equalsIgnoreCase("lotto") && !player.isOp()){
	    		if(args.length == 0) {
		    		
	    			if(player.hasPermission("lotto.admin")){

		        		player.sendMessage(ChatColor.GREEN + "------(MCLotto Help)------");
		        		player.sendMessage(ChatColor.DARK_GREEN + "/lotto buyticket (1 - " + getConfig().getInt("TicketsForSale") + ") " + ChatColor.WHITE + "|" + ChatColor.AQUA + " Purchase a lotto ticket");	
		        		player.sendMessage(ChatColor.DARK_GREEN + "/lotto jackpot " + ChatColor.WHITE + "|" + ChatColor.AQUA + " Shows the current jackpot and ticketfare");
		        		player.sendMessage(ChatColor.DARK_GREEN + "/lotto start " + ChatColor.WHITE + "|" + ChatColor.AQUA +  " Selects a random number and allows tickets to be bought");
		        		player.sendMessage(ChatColor.DARK_GREEN + "/lotto stop " + ChatColor.WHITE + "|" + ChatColor.AQUA + " Stop ticket sales and disallow the winner");
		        		player.sendMessage(ChatColor.DARK_GREEN + "/lotto setjackpot " + ChatColor.WHITE + "|" + ChatColor.AQUA + " Set the jackpot using in-game commands");
		        		player.sendMessage(ChatColor.DARK_GREEN + "/lotto setfare " + ChatColor.WHITE + "|" + ChatColor.AQUA + " Set the ticket fare with in-game commands");
		        		player.sendMessage(ChatColor.DARK_GREEN + "/lotto setmaxtickets " + ChatColor.WHITE + "|" + ChatColor.AQUA + " Set the max ticket number with in-game commands");
		    		}
	    			
	    		else if(player.hasPermission("lotto.user")){
	        		player.sendMessage(ChatColor.GREEN + "------(MCLotto Help)------");
	        		player.sendMessage(ChatColor.DARK_GREEN + "/lotto buyticket (1 - " + getConfig().getInt("TicketsForSale") + ") " + ChatColor.WHITE + "|" + ChatColor.AQUA + " Purchase a lotto ticket");
	        		player.sendMessage(ChatColor.DARK_GREEN + "/lotto jackpot " + ChatColor.WHITE + "|" + ChatColor.AQUA + " Shows the current jackpot and ticketfare");
	    		}

	    	}
	    		else if(args.length == 1 ){
	    			if(args[0].equalsIgnoreCase("buyticket")){
    		   			player.sendMessage(ChatColor.RED + "Syntax Error: Please define a ticket value");
	    			}
	    			else if(args[0].equalsIgnoreCase("stop")){
    	        				if(getConfig().getString("LottoEnabled") == "true"){
    	        		   		if(player.hasPermission("lotto.admin")){
    	        		   			getConfig().set("LottoEnabled", "false");
    	        				Bukkit.broadcastMessage(ChatColor.GOLD + "The lotto has been" + ChatColor.RED + " stopped");
    	        				}
    	        				else if(getConfig().getString("LottoEnabled") == "false") {
    	            				player.sendMessage(ChatColor.RED + "The lotto has not started yet!");
    	            			}
    	        			}
    		    		}
	    			else if(args[0].equalsIgnoreCase("jackpot")){
	    				if(player.hasPermission("lotto.user") || player.hasPermission("lotto.admin")){
	    					if(getConfig().getString("LottoEnabled") == "false") {
	            				player.sendMessage(ChatColor.RED + "The lotto has not started yet!");
	            			}
	    					else if(getConfig().getString("LottoEnabled") == "true"){
	    						player.sendMessage(ChatColor.YELLOW + "The jackpot is: " + jackpot + " " + econ.currencyNamePlural());
	    						player.sendMessage(ChatColor.YELLOW + "The ticket fare is: " + fare + " " + econ.currencyNamePlural());
	    						}
	    				}
	    			}
	    			if(args[0].equalsIgnoreCase("start")){
                        if(player.hasPermission("lotto.admin")){
        				if(getConfig().getString("LottoEnabled") == "false"){
        		   		getConfig().set("LottoEnabled", "true");
        		   		getConfig().set("WinningNumber", wn);
        		   		saveConfig();
        				player.sendMessage(ChatColor.YELLOW + "You have now started a lotto with the winning number as " + wn);
        				Bukkit.broadcastMessage(ChatColor.GOLD + "A lotto has now commenced (/lotto)!");
        				}
        				else if(getConfig().getString("LottoEnabled") == "true") {
            				player.sendMessage(ChatColor.RED + "The Lotto has already started");
            			}
        				}
	    			}
	    			}
	    		else if(args.length == 2){
	    			if(args[0].equalsIgnoreCase("buyticket")){
	    				
	    				if(player.hasPermission("lotto.user") || player.hasPermission("lotto.admin")){
	    					
	    					if(getConfig().getString("LottoEnabled") == "false"){
    						player.sendMessage(ChatColor.RED +"The lotto has not started yet!");
    					}
	    					else if(Integer.parseInt(args[1]) > getConfig().getInt("MaxTicketsForSale")){
	    						player.sendMessage(ChatColor.RED + "This ticket is not for sale!");
	    					}    						
	    					
	    					else if(r.transactionSuccess()) {
	    						if(!args[1].equalsIgnoreCase(getConfig().getString("WinningNumber"))){
	            		   		player.sendMessage(ChatColor.GREEN + "You have purchased lotto ticket #" + args[1]);
	    						}
	    						if(args[1].equalsIgnoreCase(getConfig().getString("WinningNumber"))){
	            		   			econ.depositPlayer(player.getName(), jackpot);
	            		   			getConfig().set("LottoEnabled", "false");
	            		   			saveConfig();
	            		   			Bukkit.broadcastMessage(ChatColor.GREEN + player.getDisplayName() + " has won the lotto with the jackpot of " + jackpot + " " + econ.currencyNamePlural() );
	            		   		}
	    					}
    	    					else {
    	    		                player.sendMessage(String.format(ChatColor.RED + "An error occured: %s", r.errorMessage));
    	    		            }
	    					
	    					
	    					
	    				}
	    			}
	    			else if(args[0].equalsIgnoreCase("setjackpot")){
	    				if(player.hasPermission("lotto.set")){
    		   			player.sendMessage(ChatColor.YELLOW + "Jackpot set to " + args[1]);
    		   			getConfig().set("Jackpot", Integer.parseInt(args[1]));
    		   			saveConfig();
	    				}
	    			}
	    			else if(args[0].equalsIgnoreCase("setfare")){
	    				if(player.hasPermission("lotto.set")){
    		   			player.sendMessage(ChatColor.YELLOW + "Ticket fare set to " + args[1]);
    		   			getConfig().set("TicketFare", Integer.parseInt(args[1]));
    		   			saveConfig();
	    			}
	    			}
	    			else if(args[0].equalsIgnoreCase("setmaxtickets")){
	    				if(player.hasPermission("lotto.set")){
    		   			player.sendMessage(ChatColor.YELLOW + "Max ticket number set to " + args[1]);
    		   			getConfig().set("TicketsForSale", Integer.parseInt(args[1]));
    		   			saveConfig();
	    				}
	    			}
	    		}
	    		else if(args.length >= 3) {
	    		player.sendMessage(ChatColor.RED + "Syntax Error: Unrecognized argument " + args[2]);	
	    		}
	    		}
			return false;
       }
}