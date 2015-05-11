package com.keyboardnerd.mc.luckycookie;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class LuckyCookieListener implements Listener {
	private Random random;
	private Properties dictionary;
	private List<Object> keys;
	private ChatColor[] list = {ChatColor.AQUA, ChatColor.BLUE, ChatColor.DARK_AQUA, ChatColor.DARK_BLUE, ChatColor.DARK_GRAY, ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE, ChatColor.DARK_PURPLE
			,ChatColor.DARK_RED, ChatColor.GOLD, ChatColor.GRAY, ChatColor.GREEN, ChatColor.RED};
	public static final String DEFAULT_FILE = "dictionary.properties";
	public LuckyCookieListener(){
		setInit();
		random = new Random();
		dictionary = new Properties();
		load();
	}
	synchronized public void load(){
		try {
			dictionary.load(new FileInputStream(DEFAULT_FILE));
			keys = new ArrayList<>(dictionary.keySet());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	synchronized public String getAllKeys(){
		load(); // synchronize before access.
		return keys.toString();
	}
	synchronized public String addItem(String key, String value, boolean replace){
		load(); // synchronize before access.
		if (dictionary.containsKey(key) && !replace){
			return ChatColor.RED + key + "已经存在啦";
		}
		OutputStream out = null;
		String output = null;
		try{
			if (dictionary.containsKey(key)){
				output = "成功替换：";
			}else{
				output = "成功添加：";
			}
			out = new FileOutputStream(DEFAULT_FILE);
			dictionary.setProperty(key, value);
			dictionary.store(out, null);
			keys.add(key);
		}catch(IOException io){
			io.printStackTrace();
			return ChatColor.RED + key + ":" + value + " 添加失败";
		}finally{
			if (out != null){
				try{
					out.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		return ChatColor.GREEN + output + key + " : " + value;
	}
	private int[] randomNumber(int biggest, int size){
		int[] result = new int[5];
		Set<Integer> integer = new HashSet<>();
		int currentIndex = 0;
		while (currentIndex < 5){
			int i = random.nextInt(biggest);
			if (integer.contains(i)) continue;
			result[currentIndex] = i;
			currentIndex++;
		}
		return result;
	}
	private ChatColor randomColor(){
		return list[random.nextInt(list.length)];
	}
	private String takeString(){
		load();// synchronize before access.
		if (keys.size() == 0) return "还没人添加词汇！";
		String key = (String) keys.get(random.nextInt(keys.size()));
		if (key == null) return "King Ke La";
		String str =  dictionary.getProperty(key);
		if (str == null) return "King ke La";
		int[] luckyNumber = randomNumber(100, 5);
		StringBuilder builder = new StringBuilder();
		builder.append("跟我学中文：" + ChatColor.BLUE + key + " : " + ChatColor.AQUA + str 
				+ "\n今日幸运数字:");
		for (int i : luckyNumber){
			if (i != luckyNumber.length - 1){
				builder.append(randomColor() + " " + i + ",");
			}else{
				builder.append(randomColor() + " " + i);
			}
		}
		return builder.toString();
	}
	public static boolean isFileExists(String fileName){
		File f = new File(fileName);
		return f.exists() && !f.isDirectory();
	}
	@EventHandler
	public void onPlayerEat(PlayerItemConsumeEvent event){
		if (event.getItem().getType() == Material.COOKIE){
			event.getPlayer().sendMessage(takeString());
		}
	}
	private void setInit(){
		if (isFileExists(DEFAULT_FILE)) return;
		OutputStream out = null;
		try{
			out = new FileOutputStream(DEFAULT_FILE);
		}catch(Exception io){
			io.printStackTrace();
		}finally{
			if (out != null){
				try{
					out.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}	
		}
	}
}
