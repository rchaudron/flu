package unice.map;

import unice.livingEntities.LivingEntity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Loïc
 * Represents a map where Living Entities of various species will live and cohabit.
 */
public class Map {

    private Random rand = new Random(); // A random number for providing random locations.
    private int width;             // Width of the map
    private int depth;             // Depth of the map
    private LivingEntity[][] map;  // Array to store all living entities
    private NeighbourhoodType neighbourhoodType;

    /**
     * Constructor with a defaut neighbourhood of FOUR
     * @param theWidth Width of the map (X axis)
     * @param theDepth Depth of the map (Y axis)
     */
    public Map(int theWidth, int theDepth) {
        this(theWidth, theDepth, NeighbourhoodType.FOUR);
    }

    /**
     * Main constructor
     * @param theWidth Width of the map (X axis)
     * @param theDepth Depth of the map (Y axis)
     * @param theNeighbourhoodType Type of neighbourhood (FOUR of HEIGHT)
     */
    public Map(int theWidth, int theDepth, NeighbourhoodType theNeighbourhoodType) {
        width = theWidth;
        depth = theDepth;
        neighbourhoodType = theNeighbourhoodType;
        // Instantiate the Map
        map = new LivingEntity[width][depth];
    }


    /**
     * Return the type of neighbourhood used to get a list of adjacent locations
     * @return the type of neighbourhood
     */
    public NeighbourhoodType getNeighbourhoodType() {
        return neighbourhoodType;
    }

    /**
     * Defines the type of neighbourhood (FOUR cases, ie N,S,E,W closest locations, or HEIGHT cases, ie all surrounding
     * cases).
     * @param theNeighbourhoodType type of Neighbourhood (FOUR or HEIGHT)
     */
    public void setNeighbourhoodType(NeighbourhoodType theNeighbourhoodType) {
        neighbourhoodType = theNeighbourhoodType;
    }

    /**
     * Return the map's width
     * @return the map's width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Return the map's depth
     * @return the map's depth
     */
    public int getDepth() {
        return depth;
    }


    // Empty the map
    public void clear() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getDepth(); y++) {
                map[x][y] = null;
            }
        }
    }

    /**
     * Clear a specific place in the map
     *
     * @param location : coordinates (X,Y) of the place to clear
     */
    public void clear(Location location) {
        map[location.getX()][location.getY()] = null;
    }


    /**
     * Place an entity at the given place, and replace the existing one if any
     *
     * @param entity   The living entity to be placed.
     * @param location : Location (X,Y) of the location.
     */
    public void place(LivingEntity entity, Location location) {
        place(entity, location.getX(), location.getY());
    }

    /**
     * Place an entity at the given place, and replace the existing one if any
     *
     * @param entity The living entity to be placed.
     * @param x      : Location X of the location.
     * @param y      : Location Y of the location.
     */
    public void place(LivingEntity entity, int x, int y) {
        map[x][y] = entity;
    }

    /**
     * Return the living entity at the given place, if any.
     *
     * @param location: coordinate within the map
     * @return The entity at the given coordinate, or null if there is none.
     */
    public LivingEntity getEntityAt(Location location) {
        return getEntityAt(location.getX(), location.getY());
    }

    /**
     * Return the living entity at the given place, if any.
     *
     * @param x : Location X of the location.
     * @param y : Location Y of the location.
     * @return The entity at the given coordinate, or null if there is none.
     */
    public LivingEntity getEntityAt(int x, int y) {

        try {
            return map[x][y];
        } catch (Exception theE) {
            theE.printStackTrace();
            return null;
        }
    }


    /**
     * Return the list of locations adjacent to the given one. The list
     * will not include the location itself, and only locations within the map's limits are returned.
     * Also, the list is sorted randomly !!!
     *
     * @param location : The location from which to generate adjacencies.
     * @return A list of locations adjacent to the one given
     */
    public List<Location> getNeighbourhoodLocations(Location location) {

        // The list of locations to be returned.
        List<Location> locations = new LinkedList<>();
        try {
            int x = location.getX();
            int y = location.getY();
            switch (neighbourhoodType)

            {
                case HEIGHT:
                    for (int xOffset = -1; xOffset <= 1; xOffset++) {
                        int nextX = x + xOffset;
                        if (nextX >= 0 && nextX < width) {
                            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                                int nextY = y + yOffset;
                                // Exclude invalid locations and the original location.
                                if (nextY >= 0 && nextY < depth
                                        && (xOffset != 0 || yOffset != 0)) {
                                    locations.add(new Location(nextX, nextY));
                                }
                            }
                        }
                    }
                    break;

                default:
                    if (x > 0) {
                        locations.add(new Location(x - 1, y));
                    }
                    if (x < (width - 1)) {
                        locations.add(new Location(x + 1, y));
                    }
                    if (y > 0) {
                        locations.add(new Location(x, y - 1));
                    }
                    if (y < (depth - 1)) {
                        locations.add(new Location(x, y + 1));
                    }
                    break;

            }

            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        } catch (Exception theE) {
            theE.printStackTrace();
        }

        return locations;
    }

    /**
     * Returns the list of free locations within the neighbourhood
     * The list is sorted randomly !!!
     * @param location the location for which we look for free adjacent locations
     * @return list of free locations within the neighbourhood
     */
    public List<Location> getFreeNeigbourhoodLocations(Location location) {

        List<Location> freeLocations=new LinkedList<>();
        // Loop on all locations
        for (Location loc: getNeighbourhoodLocations(location)) {
            LivingEntity livingEntity = getEntityAt(loc);
            if (livingEntity == null ){
                freeLocations.add(loc);
            }
        }
        return freeLocations;
    }

    /**
     * Returns the subset of a list of locations containing living entities of
     * a specific species
     * @param locations List of locations to analyse
     * @param speciesClass Class of the specific species (ex: Pig, Birds..)
     * @return list of locations containing the specified species
     */
    public List<Location> findSpeciesLocations(List<Location> locations, Class speciesClass){
        List<Location> speciesLocations= new LinkedList<>();
        // Loop on neighbours
        for (Location loc:locations) {
            LivingEntity livingEntity = getEntityAt(loc);
            if ((livingEntity != null) && (speciesClass.isAssignableFrom(livingEntity.getClass()))){
                speciesLocations.add(loc);
            }
        }
        return speciesLocations;
    }


}
