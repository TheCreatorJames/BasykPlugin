package creatorjames.Basyk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import creatorjames.Basyk.StackObjects.StackObject;

public class BasykPlugin extends JavaPlugin 
{
	private String profileFolder;
	private static BasykPlugin instance;
	private BasykListener listener; 
	@Override
	public void onEnable()
	{
		instance = this;
		listener= new BasykListener();
		getServer().getPluginManager().registerEvents(listener, this);
		getDataFolder().mkdirs();
		
		profileFolder = BasykPlugin.GetInstance().getDataFolder().toString() + File.separatorChar + "Profiles";
		
		if(!(new File(profileFolder)).exists())
		{
			new File(profileFolder).mkdir();
		}
		
	}
	
	public static BasykPlugin GetInstance()
	{
		return instance;
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	
	public boolean LoadParser(BasykParser parser, String profileName)
	{
		File file = new File(profileFolder + File.separatorChar + profileName);
		
		if(file.exists())
		{
			FileInputStream fis;
			try 
			{
				fis = new FileInputStream(file);
				GZIPInputStream gzis = new GZIPInputStream(fis);
				BufferedReader reader = new BufferedReader(new InputStreamReader(gzis));
				String result = "";
				String content;
				while ((content = reader.readLine()) != null)
				{
					result += content + " ";
				}
				
				reader.close();
				parser.Parse(result, true, null);
				return true;
			} catch (FileNotFoundException e) 
			{
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			return false;
		}
		else
		{
			return false;
		}
		
		
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
	    if(sender instanceof Player)
	    {
	    	
	    	
		if(command.getName().equalsIgnoreCase("SaveProfile"))
	    {
			Player player = (Player)sender;
	    	
	        HashMap<String,StackObject> o = listener.GetParser(player).GetVariables();
	       
			String result = "";
			for(String n : o.keySet())
			{
				result += o.get(n).Serialize() + " =" + n + " pop ";
			}
			
			
			
			File file = new File( profileFolder + File.separatorChar + args[0]);
		
			if(file.exists())
			{
				file.delete();
			}
			
			try 
			{
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				GZIPOutputStream gsoz = new GZIPOutputStream(fos);
				gsoz.write(result.getBytes());
				gsoz.close();

				sender.sendMessage("Saved commands and variables to a profile.");
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
	        
	    
	    }
		else
		if(command.getName().equalsIgnoreCase("LoadProfile"))
	    {
			Player player = (Player)sender;
	    	
			if(LoadParser(listener.GetParser(player), args[0]))
			{
				sender.sendMessage("Loaded Public Profile!");
			}
			else
			{
				sender.sendMessage("Profile does not appear to exist.");
			}
			return true;
	    }
		
	    }
	    return false;
	}

}
