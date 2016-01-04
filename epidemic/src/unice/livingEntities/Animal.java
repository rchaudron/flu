package unice.livingEntities;

import unice.map.Location;
import unice.map.Map;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Loïc
 * Abstract class representing an Animal living entity.
 * Animal do not recover from a Virus, they are always dying.
 * Animals can potentially infect humans in their neighbourhood
 */
public abstract class Animal extends LivingEntity {

    /**
     * Constructor
     *
     * @param theMap Map the animal will live in.
     */
    public Animal(Map theMap) {
        super(theMap);
    }

    /**
     *
     */
    @Override
    public void act() {

        // Call the LivingEntity base class method, to determine the new health status of the animal
        super.act();

        switch (getHealthStatus()){

            case RECOVERING:
                // Specific rule for animals: they CAN'T recover...
                // ... virus always win...
                setHealthStatus(HealthStatusType.DEAD);
                return;
        }

    }


}
