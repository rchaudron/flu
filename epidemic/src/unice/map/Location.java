package unice.map;

/**
 * @author Loïc
 * Represents the coordinates of a specific location on the map, ie X and Y values
 */
public class Location {
    private int x;
    private int y;

    /**
     * Constructor
     * @param theX X coordinate
     * @param theY Y coordinate
     */
    public Location(int theX, int theY){
        setX(theX);
        setY(theY);
    }

    /**
     * Returns the X coordinate of the location
     * @return X coordinate of the location
     */
    public int getX() {
        return x;
    }

    /**
     * Set the X coordinate of a location
     * @param theX
     */
    public void setX(int theX) {
        x = theX;
    }

    /**
     * Returns the Y coordinate of the location
     * @return Y coordinate of the location
     */
    public int getY() {
        return y;
    }
    /**
     * Set the Y coordinate of a location
     * @param theY
     */
    public void setY(int theY) {
        y = theY;
    }

    /**
     * Defines the comparison rules an instance of a Location class and an instance of another type of Object.
     * If other object is a Location, then X and Y coordinates are compared.
     * @param obj the object to compare with current instance
     * @return true if objet is a Location, and X and Y coordinates are equals. False otherwise.
     */
    public boolean equals(Object obj)
    {
        if(obj instanceof Location) {
            Location other = (Location) obj;
            return x == other.getX() && y == other.getY();
        }
        else {
            return false;
        }
    }

    /**
     * Returns a String representation of the current instance
     * @return String representation of the current instance
     */
    public String toString()
    {
        return x + "," + y;
    }

    /**
     * Use the top 16 bits for the x value and the bottom for
     * the y. Except for very big grids, this should give a
     * unique hash code for each (x,y) pair.
     * @return A hashcode for the location.
     */
    public int hashCode()
    {
        return (x << 16) + y;
    }


}
