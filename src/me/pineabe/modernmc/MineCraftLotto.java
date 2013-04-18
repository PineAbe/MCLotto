package me.pineabe.modernmc;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;

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
	    	   this.getLogger().info(Level.WARNING + "You don't have a config file!!!");
	    	   this.getLogger().info(Level.WARNING + "Generating config.yml.....");
	    	   getConfig().options().copyDefaults(true);
	    	   this.saveConfig();
	    	  }
	    }
	    @Override
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
			CommandExecutor listener = new Commands(this);
	        getCommand("lotto").setExecutor(listener);
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

}
