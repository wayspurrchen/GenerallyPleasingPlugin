package me.theheyway.GPP.Places;

import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.theheyway.GPP.GPP;

/**
 * Represents the data held within the Places database; temporary object used as the middle man between database and function input.
 * Some clarification: Setting/getting only sets/gets the information on the Place object, not the database. Updating/querying sets
 * and gets information to/from the database.
 * 
 * @author Way
 *
 */
public class Place {
	
	String name;
	String description;
	String type;
	String world;
	String owner;
	String shape;
	boolean discoverable;
	int expreward;
	boolean searchable;
	boolean pointable;
	double x1;
	double y1;
	double z1;
	double x2;
	double y2;
	double z2;
	double yaw;
	
	public Place() {
		
	}
	
	/**
	 * Constructs a Place object right at the player's location with x1, y1, z1, owner, world, and yaw values, with the shape defined as 'point'.
	 * Does not give it a name, type, or other flag information.
	 * 
	 * @param player
	 */
	public Place(Player player) {
		Location loc = player.getLocation();
		owner = player.getName();
		world = player.getWorld().getName();
		shape = "point";
		x1 = loc.getX();
		y1 = loc.getY();
		z1 = loc.getZ();
		yaw = loc.getYaw();
	}
	
	/**
	 * Check whether or not this Place exists within the Places database by its name, type, world, and owner.
	 * @return
	 */
	public boolean exists() {
		return PlaceUtil.placeExists(name, type, world, owner);
	}
	
	/**
	 * Loads all of the information of a Place database row into this Place object, based on the object's name, type, world, and owner.
	 * 
	 * @param name
	 * @param type
	 * @param world
	 * @param owner
	 */
	public void load(String name, String type, String world, String owner) {
		if (exists()) PlaceUtil.loadPlace(this, name, type, world, owner);
		else GPP.logger.info("Place does not exist!");
	}
	
	/**
	 * Inserts this place newly into the Places database. Makes sure that it has no conflicts first.
	 * @throws SQLException 
	 * 
	 */
	public void insert() throws SQLException {
		if (!exists()) {
			PlaceUtil.insertPlace(this);
		}
	}
	
	public Location generateLocation() {
		Location loc = new Location(GPP.server.getWorld(world), x1, y1, z1);
		loc.setY(yaw);
		return loc;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getType() {
		return type;
	}

	public String getWorld() {
		return world;
	}

	public String getOwner() {
		return owner;
	}

	public String getShape() {
		return shape;
	}

	public boolean isDiscoverable() {
		return discoverable;
	}

	public int getExpReward() {
		return expreward;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public boolean isPointable() {
		return pointable;
	}

	public double getX1() {
		return x1;
	}

	public double getY1() {
		return y1;
	}

	public double getZ1() {
		return z1;
	}

	public double getX2() {
		return x2;
	}

	public double getY2() {
		return y2;
	}

	public double getZ2() {
		return z2;
	}

	public double getYaw() {
		return yaw;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public void setDiscoverable(boolean discoverable) {
		this.discoverable = discoverable;
	}

	public void setExpReward(int expreward) {
		this.expreward = expreward;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public void setPointable(boolean pointable) {
		this.pointable = pointable;
	}

	public void setX1(double x1) {
		this.x1 = x1;
	}

	public void setY1(double y1) {
		this.y1 = y1;
	}

	public void setZ1(double z1) {
		this.z1 = z1;
	}

	public void setX2(double x2) {
		this.x2 = x2;
	}

	public void setY2(double y2) {
		this.y2 = y2;
	}

	public void setZ2(double z2) {
		this.z2 = z2;
	}

	public void setYaw(double yaw) {
		this.yaw = yaw;
	}

}
