package creatorjames.Basyk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.sun.media.jfxmedia.logging.Logger;

import creatorjames.Basyk.StackObjects.StackBoolean;
import creatorjames.Basyk.StackObjects.StackNumber;import creatorjames.Basyk.StackObjects.StackObject;
import creatorjames.Basyk.StackObjects.StackString;

public class BasykExtension {
	
	private Player selectedPlayer;
	private boolean messageMode = true;
	
	
	
	//Loads a world in if it isn't.
	private World LoadWorld(String worldName)
	{
		return Bukkit.getWorld(worldName);
	}
	
	public boolean GameExtension(String token, Player currentPlayer, BasykParser parser)
	{
		
		if(selectedPlayer == null || !selectedPlayer.isOnline())
		{
			selectedPlayer =currentPlayer;
		}
		
		if(token.equals("setmsgmode") && parser.GetStack().GetSize() != 0 && parser.GetStack().FromTop(0) instanceof StackBoolean )
		{
			messageMode = ((StackBoolean)parser.GetStack().Pop()).GetValue();
		}
		else
		if(token.equals("msgmode"))
		{
			parser.GetStack().Push(new StackBoolean(messageMode));
		}
		else
		if(token.equals("msgoff"))
		{
			messageMode = false;
		}
		else
		if(token.equals("msgon"))
		{
			messageMode = true;
		}
		else
		if(token.equals("load") || token.equals("Load") )
		{
			
			File file = new File(BasykPlugin.GetInstance().getDataFolder().toString() + File.separatorChar + currentPlayer.getUniqueId().toString());
			
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
					//System.out.println(result);
					parser.Parse(result, currentPlayer);
					currentPlayer.sendMessage("Loaded from Save.");
				} catch (FileNotFoundException e) 
				{
					// TODO Auto-generated catch block
					//e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
			else
			{
				currentPlayer.sendMessage("Maybe you should save first?");
			}
			
		}
		else
		if(token.equals("save") || token.equals("Save"))
		{
			HashMap<String,StackObject> o = parser.GetVariables();
			String result = "";
			for(String n : o.keySet())
			{
				result += o.get(n).Serialize() + " =" + n + " pop ";
			}
			
			
			
			File file = new File(BasykPlugin.GetInstance().getDataFolder().toString() + File.separatorChar + currentPlayer.getUniqueId().toString());
			
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

				currentPlayer.sendMessage("Saved your commands and variables.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		else
		if(token.equals("Engine|SetPlayer") || token.equals("Engine|SetPerson"))
		{
			if(parser.GetStack().GetSize() != 0 && parser.GetStack().Peek() instanceof StackString) 
			{
				Object[] arr = Bukkit.getServer().getOnlinePlayers().toArray();
				String name= ((StackString)parser.GetStack().Pop()).GetString();
				for(Object x : arr)
				{
					//currentPlayer.sendMessage(((Player)x).getName());
					if(((Player)x).getName().contains(name))
					{
						selectedPlayer = (Player)x;
						//currentPlayer.sendMessage(((Player)x).getName() + " was selected.");
					}
				}
			}
		}
		else
		if(token.equals("Engine|SetPlayerCurrent") || token.equals("Engine|me"))
		{
			selectedPlayer = currentPlayer;
		}
		else
		if(selectedPlayer != null) 
		{
			if(token.startsWith("Player|"))
			{
				token = token.replace("Player|", "Person|");
			}
			
			if(token.equals("Person|GetLevel" ))
			{
				parser.GetStack().Push(new StackNumber(selectedPlayer.getLevel()));
			}
			else
			if(token.equals("Person|SetLevel"))
			{
				if(parser.GetStack().GetSize() != 0 && parser.GetStack().Peek() instanceof StackNumber)
				selectedPlayer.setLevel((int)((StackNumber)parser.GetStack().Pop()).GetValue());
			}		
			else
			if(token.equals("Person|GetFood"))
			{
				parser.GetStack().Push(new StackNumber(selectedPlayer.getFoodLevel()));
			}
			else
			if(token.equals("Person|SetFood"))
			{
				if(parser.GetStack().GetSize() != 0 && parser.GetStack().Peek() instanceof StackNumber)
				selectedPlayer.setFoodLevel((int)((StackNumber)parser.GetStack().Pop()).GetValue());
			}	
			else
			if(token.equals("Person|SetHealth"))
			{
				if(parser.GetStack().GetSize() != 0 && parser.GetStack().Peek() instanceof StackNumber)
				selectedPlayer.setHealth(((StackNumber)parser.GetStack().Pop()).GetValue());
			}
			else
			if(token.equals("Person|SetGamemode"))
			{
				if(parser.GetStack().GetSize() != 0 && parser.GetStack().Peek() instanceof StackNumber)
				selectedPlayer.setGameMode(GameMode.getByValue((int)((StackNumber)parser.GetStack().Pop()).GetValue()));
			}
			else
			if(token.equals("Person|ToggleFlight"))
			{
				selectedPlayer.setAllowFlight(!selectedPlayer.getAllowFlight());
			}
			else
			if(token.equals("Person|GetHealth"))
			{
				parser.GetStack().Push(new StackNumber(selectedPlayer.getHealth()));
			}
			else
			if(token.equals("Person|GetTime"))
			{
				parser.GetStack().Push(new StackNumber(selectedPlayer.getPlayerTimeOffset()));
			}
			else			
			if(token.equals("Person|GetFlySpeed"))
			{
				parser.GetStack().Push(new StackNumber(selectedPlayer.getFlySpeed()));
			}
			else
			if(token.equals("Person|GetWalkSpeed"))
			{
			
				parser.GetStack().Push(new StackNumber(selectedPlayer.getWalkSpeed()));
			}
			else
			if(token.equals("Person|GetVelocityX"))
			{

				parser.GetStack().Push(new StackNumber(selectedPlayer.getVelocity().getX()));
			}
			else
			if(token.equals("Person|GetVelocityY"))
			{
			
				parser.GetStack().Push(new StackNumber(selectedPlayer.getVelocity().getY()));
			}
			else
			if(token.equals("Person|GetVelocityZ"))
			{
			
				parser.GetStack().Push(new StackNumber(selectedPlayer.getVelocity().getZ()));
			}
			else
			if(token.equals("Person|SetTime"))
			{
				if(parser.GetStack().GetSize() != 0 && parser.GetStack().Peek() instanceof StackNumber)
				selectedPlayer.setPlayerTime((long)((StackNumber)parser.GetStack().Pop()).GetValue(), true);
				
			}
			else
			if(token.equals("Person|AddItem"))
			{
				if(parser.GetStack().GetSize() != 0 && parser.GetStack().Peek() instanceof StackString) 
				{
					String x = ((StackString)parser.GetStack().Pop()).GetString();
					ItemStack i = new ItemStack(Material.getMaterial(x.toUpperCase().replace(' ', '_')), 1);
					selectedPlayer.getInventory().addItem(i);
					
				}
			}
			else
			if(token.equals("Person|SetFlySpeed"))
			{
				if(parser.GetStack().GetSize() != 0 && parser.GetStack().Peek() instanceof StackNumber)
				selectedPlayer.setFlySpeed(((float)((StackNumber)parser.GetStack().Pop()).GetValue()));
			}
			else
			if(token.equals("Person|SetVelocityX"))
			{
				if(parser.GetStack().GetSize() != 0 && parser.GetStack().Peek() instanceof StackNumber)
				selectedPlayer.setVelocity(selectedPlayer.getVelocity().setX(((StackNumber)parser.GetStack().Pop()).GetValue()));
			}
			else
			if(token.equals("Person|SetVelocityY"))
			{
				if(parser.GetStack().GetSize() != 0 && parser.GetStack().Peek() instanceof StackNumber)
				selectedPlayer.setVelocity(selectedPlayer.getVelocity().setY(((StackNumber)parser.GetStack().Pop()).GetValue()));
			}
			else
			if(token.equals("Person|SetVelocityZ"))
			{
				if(parser.GetStack().GetSize() != 0 && parser.GetStack().Peek() instanceof StackNumber)
				selectedPlayer.setVelocity(selectedPlayer.getVelocity().setZ(((StackNumber)parser.GetStack().Pop()).GetValue()));
			}
			else
			if(token.equals("Person|SetWalkSpeed"))
			{
				if(parser.GetStack().GetSize() != 0 && parser.GetStack().Peek() instanceof StackNumber)
				selectedPlayer.setWalkSpeed(((float)((StackNumber)parser.GetStack().Pop()).GetValue()));
			
			}
			else
			if(token.equals("msg"))
			{
				if(parser.GetStack().GetSize() != 0)
				{
					if(messageMode)
					selectedPlayer.sendMessage(parser.GetStack().Peek().toString().replace("[String]", ""));
				}
			}
			else
			if(token.equals("Person|SetX"))
			{
				if(parser.GetStack().GetSize() != 0)
				{
					Location loc = selectedPlayer.getLocation();
					if(parser.GetStack().GetSize() != 0 && parser.GetStack().Peek() instanceof StackNumber)
					loc.setX(((StackNumber)parser.GetStack().Pop()).GetValue());
					selectedPlayer.teleport(loc);
				}
			}
			else
			if(token.equals("Person|GetWorld"))
			{
				parser.GetStack().Push(new StackString(selectedPlayer.getWorld().getName()));
			}
			else
			if(token.equals("Person|SetWorld"))
			{
				if(parser.GetStack().GetSize() != 0)
				{
					if(parser.GetStack().FromTop(0) instanceof StackString)
					{
						String worldName = ((StackString)parser.GetStack().Pop()).GetString();
						
						World z = LoadWorld(worldName);
						//while(!IsWorldLoaded(worldName));
						if(z != null)
						selectedPlayer.teleport(new Location(z,z.getSpawnLocation().getX(),z.getSpawnLocation().getY(),z.getSpawnLocation().getZ()));
					}
				}
			}
			else	
			if(token.equals("Person|SetY"))
			{
				if(parser.GetStack().GetSize() != 0)
				{
					Location loc = selectedPlayer.getLocation();
	
					if(parser.GetStack().GetSize() != 0 && parser.GetStack().Peek() instanceof StackNumber)
					loc.setY(((StackNumber)parser.GetStack().Pop()).GetValue());
					selectedPlayer.teleport(loc);
				}
			}
			else
			if(token.equals("Person|GetX"))
			{
					Location loc = selectedPlayer.getLocation();
					parser.GetStack().Push(new StackNumber(loc.getX()));
			
			}
			else
			if(token.equals("Person|Workbench"))	
			{
				selectedPlayer.openWorkbench(selectedPlayer.getLocation(), true);
			}
			else
			if(token.equals("Person|Enchantment"))	
			{
				selectedPlayer.openEnchanting(selectedPlayer.getLocation(), true);
			}
			else
			if(token.equals("Person|GetY"))
			{
					Location loc = selectedPlayer.getLocation();
					parser.GetStack().Push(new StackNumber(loc.getY()));
			}
			else
			if(token.equals("Person|GetZ"))
			{
					Location loc = selectedPlayer.getLocation();
					parser.GetStack().Push(new StackNumber(loc.getZ()));
				
			}
			else
			if(token.equals("Person|SetZ"))
			{
				if(parser.GetStack().GetSize() != 0)
				{
					Location loc = selectedPlayer.getLocation();
	
					if(parser.GetStack().GetSize() != 0 && parser.GetStack().Peek() instanceof StackNumber)
					loc.setZ(((StackNumber)parser.GetStack().Pop()).GetValue());
					selectedPlayer.teleport(loc);
				}
			}
			else return false;
		}
		else
		{
			return false;
		}
	
		return true;
		
		
		
	}
	
	
}
