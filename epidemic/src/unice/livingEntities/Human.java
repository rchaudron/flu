package unice.livingEntities;

import unice.map.Location;
import unice.map.Map;


import java.util.List;


/**
 * @author Loïc
 * Abstract class representing a Human living entity.
 * A human has some additional characteristics, he can move within the map
 */
public abstract class Human extends LivingEntity {

    public Human(Map theMap) {
        super(theMap);
    }

    @Override
    public void act() {
        // Call the LivingEntity base class method, to determine the new health status
        super.act();

        // A human can MOVE
        if (getHealthStatus()!=HealthStatusType.DEAD) { move();}

    }

    /**
     * Ask a human to move to one of the free adjacent locations
     */
    protected void move(){
       if (getHealthStatus()==HealthStatusType.DEAD) return;
        // Get the list of Free locations around the current location
        List<Location> locations=getMap().getFreeNeigbourhoodLocations(getLocation());
        // If some locations are available...
        if (!locations.isEmpty()){
            // Move to the first location available!
            setLocation(locations.get(0));
        }
    }


    /**
     * We need to override the base method as humans can only infect other humans, and not animals.
     * We have to keep only the locations that contains humans, before calling the base class method.
     * @param locations
     */
    @Override
    protected void tryInfectNeighbourhood(List<Location> locations){
        // Filter the list of locations to keep only the ones occupied by Humans
        List<Location> humanLocations =getMap().findSpeciesLocations(locations, Human.class);
        // Call the method of the base class to spread the virus
        super.tryInfectNeighbourhood(humanLocations);
    }


}
