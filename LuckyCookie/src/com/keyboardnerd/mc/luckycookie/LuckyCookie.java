package com.keyboardnerd.mc.luckycookie;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class LuckyCookie extends JavaPlugin {
	public final Logger logger = Logger.getLogger("Minecraft");
	public final LuckyCookieListener listener = new LuckyCookieListener();
	
	@Override
	public void onEnable(){
		this.logger.info(getDescription().getFullName() + ":" + getDescription().getVersion() + " has been ENABLED");
		getServer().getPluginManager().registerEvents(this.listener, this);
	}
	@Override
	public void onDisable(){
		this.logger.info(getDescription().getFullName() + ":" + getDescription().getVersion() + " has been DISABLED.");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel1, final String[] args){
		if (cmd.getName().equals("addToDict") || cmd.getName().equals("toDict")){
			boolean replace = false;
			if (args.length < 2){
				sender.sendMessage(ChatColor.AQUA + "用法：<单词1>，<单词2>，（替换或不替换）");
				return true;
			}else if (args.length == 2){
			}else if (args.length >= 3){
				if (args[2].equalsIgnoreCase("yes") || args[2].equalsIgnoreCase("true")
						|| args[2].equalsIgnoreCase("y") || args[2].equalsIgnoreCase("t")){
					replace = true;
				}
			}
			String feedBack = listener.addItem(args[0], args[1], replace);
			sender.sendMessage(feedBack);
		}else if (cmd.getName().equals("listDictKey")){
			String message = listener.getAllKeys();
			sender.sendMessage(message);
		}
		return true;
	}
}
