package unice.display;

import unice.livingEntities.LivingEntity;
import unice.map.Map;

import java.util.HashMap;

/**
 * This class collects and provides some statistical data on the state of a
 * field. It is flexible: it will create and maintain a counter for any class of
 * object that is found within the field.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class FieldStats {
    // Counters for each type of entity (person, pigs, chickens, ducks, etc.) in the simulation.
    private HashMap<Class, Counter> counters;
    // Whether the counters are currently up to date.
    private boolean countsValid;

    /**
     * Construct a FieldStats object.
     */
    public FieldStats() {
        // Set up a collection for counters for each type of animal that
        // we might find
        counters = new HashMap<>();
        countsValid = false;
    }

    /**
     * Get details of what is in the field.
     * 
     * @return A string describing what is in the field.
     */
    public String getPopulationDetails(Map theMap) {
        StringBuffer buffer = new StringBuffer();
        if (!countsValid) {
            generateCounts(theMap);
        }
        for (Class key : counters.keySet()) {
            Counter info = counters.get(key);
            buffer.append(info.getName());
            buffer.append(": ");
            buffer.append(info.getCount());
            buffer.append(' ');
        }
        return buffer.toString();
    }

    /**
     * Get the number of individuals in the population of a given class.
     * 
     * @return An int with the number for this class.
     */
    public int getPopulationCount(Map theMap, Class key) {
        if (!countsValid) {
            generateCounts(theMap);
        }

        Counter counter = counters.get(key);
        return counter.getCount();
    }

    /**
     * Invalidate the current set of statistics; reset all counts to zero.
     */
    public void reset() {
        countsValid = false;
        for (Class key : counters.keySet()) {
            Counter count = counters.get(key);
            count.reset();
        }
    }

    /**
     * Increment the count for one class of livingEntity.
     * 
     * @param livingEntityClass
     *            The class of livingEntity to increment.
     */
    public void incrementCount(Class livingEntityClass) {
        Counter count = counters.get(livingEntityClass);
        if (count == null) {
            // We do not have a counter for this species yet.
            // Create one.
            // NB: We use getSimpleName instead of getName to have only the class name, and not the whole package name.
            count = new Counter(livingEntityClass.getSimpleName());
            counters.put(livingEntityClass, count);
        }
        count.increment();
    }

    /**
     * Indicate that an livingEntity count has been completed.
     */
    public void countFinished() {
        countsValid = true;
    }

    /**
     * Determine whether the simulation is still viable. I.e., should it
     * continue to run.
     * 
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Map theMap) {
        // How many counts are non-zero.
        int nonZero = 0;
        if (!countsValid) {
            generateCounts(theMap);
        }
        for (Class key : counters.keySet()) {
            Counter info = counters.get(key);
            if (info.getCount() > 0) {
                nonZero++;
            }
        }
        return nonZero > 1;
    }

    /**
     * Generate counts of the number of people, pigs, ducks, chickens. These are not kept up
     * to date as living entities are placed in the field, but only when a
     * request is made for the information.
     * 
     * @param theMap
     *            The theMap to generate the stats for.
     */
    private void generateCounts(Map theMap) {
        reset();
        for (int y = 0; y < theMap.getDepth(); y++) {
            for (int x = 0; x < theMap.getWidth(); x++) {
                LivingEntity livingEntity = theMap.getEntityAt(x, y);
                if (livingEntity != null) {
                    incrementCount(livingEntity.getClass());
                }
            }
        }
        countsValid = true;
    }
}
