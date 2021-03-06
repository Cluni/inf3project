package environment;

import java.util.Vector;

import util.Bitmask;
import environment.entity.Entity;
/**
 * {@link MapCell} of which a {@link ServerMap} consists.
 * @author Daniel
 */
public class MapCell {
	protected Vector<Entity> entities;
	protected Vector<Property> properties;
	protected Bitmask propertyMap;
	protected int x,y;
	protected long tickAccu;
	protected Map map;
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	protected MapCell() {
		this.entities = new Vector<Entity>();
		this.properties = new Vector<Property>();
		this.propertyMap = new Bitmask();
	}
	
	public void setMap(Map m) {
		this.map = m;
	}

	/**
	 * Constructor
	 * @param x the x-coordinate of the {@link MapCell} in the {@link Map}
	 * @param y the y-coordinate of the {@link MapCell} in the {@link Map}
	 */
	public MapCell(Map map, int x, int y) {
		this();
		this.map = map;
		this.x = x;
		this.y = y;
	}

	/**
	 * Collects the direct neighbours of the cell
	 * @return array of {@link MapCell}s that are directly adjacent to the current cell (4 elements in total, vertically and horizontally) That is:<br>
	 * 0: left<br>
	 * 1: top<br>
	 * 2: right<br>
	 * 3: down<br>
	 * Be aware that if a cell does not have a neighbour to any side the value at this position is NULL! For example: 
	 * if a cell does not have a top neighbour, getNeighbours()[1] will be NULL. So additional checking is needed.
	 */
	public MapCell[] getNeighbours() {
		MapCell[] n = new MapCell[4];
		if(map != null) {
			n[0] = map.getCellAt(this.x - 1, this.y);
			n[1] = map.getCellAt(this.x, this.y - 1);
			n[2] = map.getCellAt(this.x + 1, this.y);
			n[3] = map.getCellAt(this.x, this.y + 1);
		}
		return n;
	}
	
	/**
	 * Add an {@link Entity} to the {@link MapCell}
	 * @param e {@link Entity} to add
	 */
	public void addEntity(Entity e) {
		this.entities.add(e);
	}
	
	/**
	 * Remove an {@link Entity} from the {@link MapCell}
	 * @param e {@link Entity} to add
	 */
	public void removeEntity(Entity e) {
		this.entities.remove(e);
	}
	
	/**
	 * List {@link Entity}s
	 * @return a list of {@link Entity}s
	 */
	public Vector<Entity> getEntities() {
		return this.entities;
	}
	
	/**
	 * Add a {@link Property} to the {@link MapCell} (no duplicates possible)
	 * @param p the {@link Property} to add
	 * @return true, if the value was added, false if not (value was already in list)
	 */
	public boolean addProperty(Property p) {
		boolean added = false;
		if(p != null && !hasProperty(p)) {
			added = properties.add(p);
			if(added) {
				propertyMap.add(p.getValue());
			}
		}
		return added;
	}
	
	/**
	 * Add a whole list of {@link Property}s to the cell
	 * @param p new {@link Property}s
	 */
	public void addProperties(Property[] p) {
		if(p != null) {
			for(Property prop : p) {
				addProperty(prop);
			}
		}
	}
	
	/**
	 * Remove a {@link Property} from the {@link MapCell}
	 * @param p the {@link Property} to remove
	 * @return true, if the value was removed, false, if it wasn't in the list
	 */
	public boolean removeProperty(Property p) {
		boolean removed = properties.remove(p);
		if(removed) {
			propertyMap.remove(p.getValue());
		}
		return removed;
	}
	
	/**
	 * Checks whether the {@link MapCell} has a given {@link Property}
	 * @param p the {@link Property} to check for
	 * @return true, if it is contained in the list of {@link Property}s
	 */
	public boolean hasProperty(Property p) {
		return propertyMap.has(p.getValue());
	}
	
	/**
	 * The list of properties this {@link MapCell} holds
	 * @return
	 */
	public Vector<Property> getProperties() {
		return properties;
	}
	
	/**
	 * Two {@link MapCell}s are the same if they have the same coordinate in a {@link Map}
	 * @param other other {@link MapCell}
	 * @return true, if the cells share the same coords
	 */
	public boolean equals(MapCell other) {
		return this.x == other.x && this.y == other.y;
	}
	
	/**
	 * Two {@link MapCell}s are similar if they differ at least <threshold> {@link Property}
	 * @param other other {@link MapCell}
	 * @return true, if the two cells have at least <threshold> {@link Property} NOT in common
	 */
	public boolean isSimilar(MapCell other, int threshold) {
		int i = 0,j = 0,counter = 0;
		while(counter < threshold && i < properties.size()) {
			while(counter < threshold && j < other.getProperties().size()) {
				if(!properties.get(i).equals(other.getProperties().get(j))) {
					counter++;
				}
				j++;
			}
			i++;
		}
		return counter < threshold;
	}
	
	@Override
	public String toString() {
		return String.format("MapCell(%d|%d)",this.x, this.y);
	}
}
