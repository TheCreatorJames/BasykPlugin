package creatorjames.Basyk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.material.Redstone;

public class BasykListener implements Listener
{
	
	HashMap<UUID, BasykParser> parsers = new HashMap<UUID, BasykParser>();
	HashMap<UUID, Long> lastBook = new HashMap<UUID, Long>();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent ev)
	{
		createParser(ev.getPlayer());
			
	}
	
	 @EventHandler
	 public void onPlayerInteractEntity(PlayerInteractEntityEvent event) 
	 {
		 createParser(event.getPlayer());
		 
		 
		 
		
	 }
	 
	 public BasykParser GetParser(Player player)
	 {
		 createParser(player);
		 return parsers.get(player.getUniqueId());
	 }
	 
	 
	 private void createParser(Player player)
	 {
		 if(!parsers.containsKey(player.getUniqueId()))
			{
				parsers.put(player.getUniqueId(), new BasykParser());
			}
	 }
	 
	 @EventHandler
	 public void onPlayerInteract(PlayerInteractEvent event) 
	 {
		 createParser(event.getPlayer());
		 
		 
		 if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
		 {

			 if(event.getClickedBlock().getState() instanceof Sign)
			 {
				
				 Sign sign = (Sign)event.getClickedBlock().getState();
				 String[] meh = sign.getLines();
				 String result = "";
				 for(int i =0; i < meh.length; i++)
				 {
					 result += meh[i] + " ";
					 
				 }
				 
				 String word = "";
				 if(result.startsWith("Code|"))
				 {
					word = result.split(" ")[0].substring(5);
					result = result.replace("Code|" + word, "");
				 }
				 
				
				 if(word.length() > 0)
				 {
					BasykParser signParser = new BasykParser();
					BasykPlugin.GetInstance().LoadParser(signParser, word);
				 	signParser.Parse(result, event.getPlayer());
				 }
			 }
		 }
		 
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) 
		{
			if(event.getPlayer().getItemInHand().getItemMeta() instanceof BookMeta)
			{
				if(!lastBook.containsKey(event.getPlayer().getUniqueId()))
				{
					lastBook.put(event.getPlayer().getUniqueId(), 0l);
				}
				
				BookMeta bookMeta = (BookMeta)event.getPlayer().getItemInHand().getItemMeta();
				String bookText = "";
				for(int i =0; i < bookMeta.getPageCount(); i++) 
				{
					bookText += bookMeta.getPage(1 + i) + " ";
				}
				
				
				
				if(lastBook.get(event.getPlayer().getUniqueId()) - System.currentTimeMillis() < -3500)
				{
					parsers.get(event.getPlayer().getUniqueId()).Parse(bookText, event.getPlayer());
					lastBook.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
				}
				else
				{
					event.getPlayer().sendMessage("Books need time to cooldown.");
				}
				
				
				
			
				
			}
		}
		 
		
	 }
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		createParser(event.getPlayer());
	
		if(event.getMessage().equals("Heh"))
		{
			 if(event.getPlayer().getTargetBlock((HashSet<Byte>)null, 100).getState() instanceof Sign)
			 {
				 Bukkit.getServer().broadcastMessage("Howdy!");
				 
			 }
			 
		}
		
		if(event.getMessage().toLowerCase().equals("#code"))
		{
			event.getPlayer().sendMessage("Hey there! Write some code!");
			event.getPlayer().getInventory().addItem(new ItemStack(Material.BOOK_AND_QUILL, 1));
			event.setCancelled(true);
		}
		else
		if(event.getMessage().toLowerCase().equals("#run") || event.getMessage().toLowerCase().equals("#learn"))
		{
			if(event.getPlayer().getItemInHand().getItemMeta() instanceof BookMeta)
			{
				
				BookMeta bookMeta = (BookMeta)event.getPlayer().getItemInHand().getItemMeta();
				String bookText = "";
				for(int i =0; i < bookMeta.getPageCount(); i++) 
				{
					bookText += bookMeta.getPage(1 + i) + " ";
				}
				
				parsers.get(event.getPlayer().getUniqueId()).Parse(bookText, event.getPlayer());
				
				event.getPlayer().sendMessage("Book successfully used.");
				event.setCancelled(true);
				
			}	
			
		}
		else
		if(event.getMessage().toLowerCase().equals("#copy"))
		{
			if(event.getPlayer().getItemInHand().getItemMeta() instanceof BookMeta)
			{
				
				BookMeta bookMeta = (BookMeta)event.getPlayer().getItemInHand().getItemMeta();
				bookMeta.setDisplayName("Code Book");
		
				ItemStack copiedBook = new ItemStack(Material.BOOK_AND_QUILL, 1);
				BookMeta copiedBookMeta = ((BookMeta)copiedBook.getItemMeta());
				copiedBookMeta.setPages(bookMeta.getPages());
				
				copiedBook.setItemMeta(copiedBookMeta);
				event.getPlayer().getInventory().addItem(copiedBook);
				
				event.getPlayer().sendMessage("Book copied!");
				event.setCancelled(true);
				
			}	
			
		}
		else
		if(event.getMessage().startsWith("#"))
		{
			
			try 
			{
				parsers.get(event.getPlayer().getUniqueId()).Parse(event.getMessage().substring(1), event.getPlayer());
			} catch(Exception ex)
			{
				event.getPlayer().sendMessage("Your last command had an issue.");
			}
			event.setCancelled(true);
		}
			
		
	}

}
