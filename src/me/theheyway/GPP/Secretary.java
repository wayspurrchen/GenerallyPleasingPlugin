package me.theheyway.GPP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.theheyway.GPP.Exceptions.NoMorePagesException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * The Secretary class is the global message system used for lines of text that need to be displayed to a player.
 * Its primary use is to enable pagination of information--i.e., pulling up a list of some set of static, noninteractive
 * information and then enabling the player to scroll through it (usually with the /next command).
 * 
 * 
 * @author Way
 *
 */
public class Secretary {

	public static Map<Player, Secretary> secretaries = new HashMap<Player, Secretary>();
	
	Player player;
	int pointer = 0;
	int linesLeft = 0;
	int pages = 0;
	ArrayList<String> lines = new ArrayList<String>(100);

	public Secretary(Player player) {
		this.player = player;
		secretaries.put(player, this);
	}
	
	public static boolean hasSecretary(Player player) {
		if (secretaries.containsKey(player)) return true;
		else return false;
	}
	
	public static Secretary getSecretary(Player player) {
		return Secretary.secretaries.get(player);
	}

	public void addLine(String line) {
		lines.add(line);
		calculateLineCount();
		calculatePageCount();
	}
	
	public void removeLine(int index) {
		lines.remove(index);
		calculateLineCount();
		calculatePageCount();
	}
	
	public void removeLine(String line) {
		lines.remove(line);
		calculateLineCount();
		calculatePageCount();
	}
	
	public int getCurrentPage() {
		return (pointer / Constants.SECRETARY_LINES_PER_PAGE) + 1;
	}
	
	public void calculatePageCount() {
		int tmp = lines.size() % Constants.SECRETARY_LINES_PER_PAGE;
		if (tmp > 0) {
			pages = (lines.size() / Constants.SECRETARY_LINES_PER_PAGE) + 1;
		} else {
			pages = lines.size() / Constants.SECRETARY_LINES_PER_PAGE;
		}
	}
	
	public void calculateLineCount() {
		linesLeft = lines.size();
	}
	
	public void clearLines() {
		lines.clear();
		pages = 0;
	}
	
	public void firstPage() {
		pointer = 0;
		linesLeft = lines.size();
	}
	
	public void changePage(int page) {
		if (page >= 0) {
			pointer = (page - 1) * (Constants.SECRETARY_LINES_PER_PAGE);
			// + 1 because pointer uses index starting at 0, whereas linesLeft and linesRead use index starting at 1
			linesLeft = lines.size() - pointer;
		} else { // safety
			pointer = 0;
			linesLeft = lines.size();
		}
	}
	
	public ArrayList<String> getLines() {
		return lines;
	}
	
	public boolean hasMoreLines() {
		boolean hasMore = pointer + 1 >= lines.size() ? false : true;
		return hasMore;
	}
	
	/**
	 * Displays the page info at the current pointer.
	 * 
	 * @throws NoMorePagesException 
	 * 
	 */
	public void sendPage() throws NoMorePagesException {
		if (hasMoreLines()) {
			int currentPage = getCurrentPage();

			int linesThisPage = (linesLeft > Constants.SECRETARY_LINES_PER_PAGE) ? Constants.SECRETARY_LINES_PER_PAGE : linesLeft;
			for (int i=0; i < linesThisPage; i++) {
				player.sendMessage(lines.get(pointer+i));
			}
			
			if (pages > 1) {
				String message = ChatColor.YELLOW + "Page " + ChatColor.WHITE + currentPage + ChatColor.YELLOW + " of " + pages + ". ";
				
				if (currentPage == 1) {
					message += "Use " + ChatColor.WHITE + "/next" + ChatColor.YELLOW + " to go forward a page.";
				} else if (currentPage > 0 && currentPage < pages) {
					message += "Use " + ChatColor.WHITE + "/prev" + ChatColor.YELLOW + " and " + ChatColor.WHITE + "/next" + ChatColor.YELLOW + " to navigate.";
				} else {
					message += "Use " + ChatColor.WHITE + "/prev" + ChatColor.YELLOW + " to go back a page.";
				}
				player.sendMessage(message);
			}
		} else throw new NoMorePagesException();
	}
	
	/**
	 * Does not work if there are more than 14 lines to display.
	 */
	public void simplePrint() {
		if (lines.size() <= 14) {
			for (int i=0; i < lines.size(); i++, pointer++) {
				player.sendMessage(lines.get(i));
			}
		} else GPP.consoleWarn("[GPP] Secretary tried to simplePrint() but failed: more than 14 lines.");
	}

	public void nextPage() throws NoMorePagesException {
		if (getCurrentPage() != pages) {
			changePage(getCurrentPage() + 1);
			sendPage();
		} else throw new NoMorePagesException();
	}

	public void previousPage() throws NoMorePagesException {
		if (getCurrentPage() != 1) {
			changePage(getCurrentPage() - 1);
			sendPage();
		} else throw new NoMorePagesException();
	}
	
	public void getPage(int page) throws NoMorePagesException {
		if (page >= pages && page <= pages) {
			changePage(page);
			sendPage();
		} else throw new NoMorePagesException(ChatColor.DARK_RED + "Invalid page number.");
	}

}
