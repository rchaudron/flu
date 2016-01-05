package unice.display;

import unice.map.Map;

import java.awt.*;

/**
 * A graphical view of the simulation grid. This interface defines all possible
 * different views.
 * 
 * @author Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */
public interface SimulatorView {
    /**
     * Define a color to be used for a given class of living entity.
     * 
     * @param livingEntityClass
     *            The living entity Class object.
     * @param color
     *            The color to be used for the given class.
     */
    void setColor(Class livingEntityClass, Color color);

    /**
     * Determine whether the simulation should continue to run.
     * 
     * @return true If there is more than one species alive.
     */
    boolean isViable(Map theMap);

    /**
     * Show the current status of the theMap.
     * 
     * @param step
     *            Which iteration step it is.
     * @param theMap
     *            The theMap whose status is to be displayed.
     */
    void showStatus(int step, Map theMap);

    /**
     * Prepare for a new run.
     */
    void reset();

}