package unice.map;

/**
 * @author Loïc
 * Defines the way the neighbourhood of a location of the map is defined
 */
public enum NeighbourhoodType {
    FOUR,   // Only locations located on the N,S,E and W  of a specific location are considered
    HEIGHT  // All locations around a specific location are considered
}
