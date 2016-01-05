package unice.livingEntities;

import unice.map.Location;
import unice.map.Map;
import unice.virus.Virus;

import java.util.List;
import java.util.Random;

/**
 * @author Loïc, Thomas
 */
public abstract class LivingEntity {

    private HealthStatusType healthStatus=HealthStatusType.HEALTHY;
    private Location location;
    private Map map;
    private Virus virus;        // Contains the virus infecting the entity, or null if the entity is healthy
    private int sicknessCounter;   // Nb of days since the entity is Sick or Contagious


    /**
     * Constructor
     * @param theMap Map where the enity is going to live...and die !
     */
    public LivingEntity(Map theMap){
        map = theMap;
    }


    /**
     * Returns the entity health status
     * @return : entity health status
     */
    public HealthStatusType getHealthStatus() {
        return healthStatus;
    }

    /**
     * Set a new Health Status, and reset the counter used to check the various 
     * infection phases (incubation, contagous, recovery)
     * @param theHealthStatus : the new health status of the entity
     */
    protected void setHealthStatus(HealthStatusType theHealthStatus) {
        // If no change, do nothing...
        if (theHealthStatus==healthStatus) return;
        //System.out.println(this.getClass().getSimpleName()+ "=> " + theHealthStatus.toString());

        // Set the new Status...
        healthStatus = theHealthStatus;
        // Reset the counter for the period
        sicknessCounter=0;
        // If entity is Healthy again, virus is dead !
        if (healthStatus==HealthStatusType.HEALTHY){
            virus=null;
            return;
        }
        if(healthStatus==HealthStatusType.DEAD){
            virus=null;
            // Remove entity from the map
            map.clear(location);
            location=null;
        }
    }

    /**
     * Returns the virus currently infecting the living entity
     * @return virus currently infecting the living entity
     */
    public Virus getVirus() {
        return virus;
    }

    /**
     * Defines the virus that infects the entity. if previously healthy, the entity will become
     * sick, otherwise, entity is either already dead, or already infected.
     * @param theVirus virus infecting the entity
     */
    public void becomeSick(Virus theVirus){
        // If Dead, cannot become sick again...
        if (healthStatus==HealthStatusType.DEAD) return;
        // If already sick, do nothing...
        if (virus !=null) return;
        // Got the virus, too bad !
        virus=theVirus;
        // Set the new status
        setHealthStatus(HealthStatusType.SICK);
    }

    /**
     * returns the location of the entity on the Map
     * @return location of the entity on the map.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Defines the new location of the entity on the Map
     * @param newLocation
     */
    public void setLocation(Location newLocation) {
        // Check if we try to move to thye same place !
        if (newLocation.equals(location)) return;
        // Cleanup the previous location
        if(location != null) {
            map.clear(location);
        }
        // Assign the entity to the new location on the Map.
        location = newLocation;
        map.place(this, newLocation);
    }

    /**
     * Returns the Map where the entity is living.
     * @return the Map
     */
    public Map getMap() {
        return map;
    }

    /**
     * Update the health status of the entity, based on duration of the infection, 
     * and on the Mortality rate of the virus
     */
    protected void updateHealthStatus(){
        switch (healthStatus){

            case SICK:
                // Check if entity is sick since enough time to become Contagious;
                if (sicknessCounter == virus.getIncubationPeriod()){
                    setHealthStatus(HealthStatusType.CONTAGIOUS);
                }
                break;

            case CONTAGIOUS:
                // Check if entity is sick since enough time to become Contagious;
                if (sicknessCounter == virus.getContagiousPeriod()){

                    // Decide if entity is either recovering or dying, based on the 
                    // virus Mortality rate.
                    Random rand = new Random();
                    if (rand.nextDouble()< virus.getMortalityRate()){
                        setHealthStatus(HealthStatusType.DEAD);
                    }else{
                        setHealthStatus(HealthStatusType.RECOVERING);
                    }
                }
                break;

            case RECOVERING:
                // Check if entity is sick since enough time to become Contagious;
                if (sicknessCounter == virus.getRecoveryPeriod()){
                    // Determine is entity either Recover or Die from the virus.
                    setHealthStatus(HealthStatusType.HEALTHY);
                }
                break;

            default:
                break;
        }

    }

    /**
     * Provides the most basic action for an entity, by incrementing the time counter, and checking
     * the new Health Status of the entity.
     * If some health status do not exists for some entity (for example, the animals are not recovering
     * but all dying, the child class can apply some additional logic by overriding that method.
     */
    public void act(){
        // If Dead then... stay dead !
        if (healthStatus==HealthStatusType.DEAD) return;

        // If Living Entity has a virus, increment sickness time counter...
        if (virus!=null) sicknessCounter++;

        // Check if health status has evolved
        updateHealthStatus();
        // If Contagious, check if entity is infecting its neighbourhood
        if (healthStatus==HealthStatusType.CONTAGIOUS) {
            tryInfectNeighbourhood(getNeighbourhood());
        }

    }

    /**
     * Try to infect living entities on the map with the virus of the current entity
     * @param locations list of location on the map to look for Lioving Entities to infect
     */
    protected void tryInfectNeighbourhood(List<Location> locations){
        // If not contagious, entity cannot infect others
        if (healthStatus!=HealthStatusType.CONTAGIOUS)return;

        // Loop on all location
        for (Location loc : locations) {
            // Get the living entity on the current location
            LivingEntity entity=(LivingEntity) getMap().getEntityAt(loc);
            // Check if virus is dangerous for that type of entity
            if (virus.canInfectEntity(entity)){
                Random rand =new Random();
                if (rand.nextDouble()<virus.getInfectionRate()){
                    entity.becomeSick(virus);
                }

            }
        }

    }

    /**
     * Returns the entity's neighbourhood as a list of locations sorted randomly
     * @return list of locations sorted randomly
     * @throws Exception
     */
    public List<Location> getNeighbourhood()  {
        return map.getNeighbourhoodLocations(location);
    }




}
