package unice.display;

import unice.livingEntities.*;
import unice.map.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * A graphical view of the simulation grid. The view displays a colored
 * rectangle for each location representing its contents. Colors for each type
 * of species can be defined using the setColor method.
 * 
 * @author Michael Kölling and David J. Barnes
 * @author Loic Rose , Thomas Gillot, Romain Chaudron
 * @version 2011.07.31
 */
public class GridView extends JFrame implements SimulatorView {
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;

    /**
     * The differents colors for all Entities
     * More the color is bold , more the entity is sick
     */

    private static final Color[] HUMANS_COLORS = {new Color(173,176,230),new Color(30,144,255),new Color(0,0,141),new Color(0,0,200)};
    private static final Color[] CHICKENS_COLORS = {new Color(250,128,114),new Color(255,0,0),new Color(139,0,0),new Color(198,0,0)};
    private static final Color[] DUCKS_COLORS = {new Color(144,238,144),new Color(0,255,0),new Color(0,100,0),new Color(0,180,0)};
    private static final Color[] PIGS_COLORS = {new Color(255,239,213),new Color(255,192,203),new Color(255,105,180),new Color(255,200,178)};

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel stepLabel, population;
    private FieldView fieldView;

    // A map for storing colors for Living Entities in the simulation
    private java.util.Map<Class, Color> colors;
    // A statistics object computing and storing simulation information
    private FieldStats stats;

    private String speed;

    private JComboBox combo = new JComboBox();
    private JLabel label = new JLabel("Simulation speed");

    /**
     * HashMap to know which color at which state and entity
     */
    private HashMap<HealthStatusType,Integer> indice_state;
    private HashMap<Class,Integer> indice_lb;


    /**
     * Create a view of the given width and height.
     * 
     * @param height
     *            The simulation's height.
     * @param width
     *            The simulation's width.
     */
    public GridView(int height, int width) {

        indice_state = new HashMap<>();
        indice_lb = new HashMap<>();

        speed = "MEDIUM";
        stats = new FieldStats();
        colors = new HashMap<>();

        setTitle("Epidemic Simulator");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);

        setLocation(20, 50);

        /**
         * Adding of the speed modification
         */
        JPanel top =  new JPanel();
        combo.setPreferredSize(new Dimension(100,20));
        combo.addItem("SLOW");
        combo.addItem("MEDIUM");
        combo.addItem("FAST");
        combo.setSelectedIndex(1);
        combo.addActionListener(new SpeedListener());
        top.add(label);
        top.add(combo);

        JLabel label1 = new JLabel("BLUE: Human,");
        JLabel label2 = new JLabel("RED: CHICKEN,");
        JLabel label3 = new JLabel("GREEN: DUCK,");
        JLabel label4 = new JLabel("PINK: PIG");

        top.add(label1);
        top.add(label2);
        top.add(label3);
        top.add(label4);

        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        contents.add(top,BorderLayout.EAST);
        contents.add(stepLabel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);

        /**
         * We add the different states and entity for each colors
         */
        indice_state.put(HealthStatusType.HEALTHY,0);
        indice_state.put(HealthStatusType.SICK,1);
        indice_state.put(HealthStatusType.CONTAGIOUS,2);
        indice_state.put(HealthStatusType.RECOVERING,3);

        indice_lb.put(Person.class,0);
        indice_lb.put(Chicken.class,1);
        indice_lb.put(Duck.class,2);
        indice_lb.put(Pig.class,3);
    }

    /**
     * Define a color to be used for a given class of Living Entity.
     * 
     * @param livingEntityClass
     *            The living Entity's Class object.
     * @param color
     *            The color to be used for the given class.
     */
    public void setColor(Class livingEntityClass, Color color) {
        colors.put(livingEntityClass, color);
    }

    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class livingEntityClass) {
        Color col = colors.get(livingEntityClass);
        if (col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        } else {
            return col;
        }
    }


    /**
     * Show the current status of the field.
     * Each color is for one state of one entity
     *
     * @param step
     *            Which iteration step it is.
     * @param field
     *            The field whose status is to be displayed.
     */
    public void showStatus(int step, Map field) {
        if (!isVisible()) {
            setVisible(true);
        }

        stepLabel.setText(STEP_PREFIX + step);
        stats.reset();

        fieldView.preparePaint();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                LivingEntity livingBeing = field.getEntityAt(row, col);
                if (livingBeing != null) {
                    stats.incrementCount(livingBeing.getClass());
                    fieldView.drawMark(col, row, getColorState(livingBeing));
                } else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX
                + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Check the state and recup the corresponding color
     * @param livingBeing
     * @return
     */
    public Color getColorState (Object livingBeing){
        Color[] tabColor = new Color[4];
        int indice_ste = 0;
        if (livingBeing instanceof LivingEntity){
            int lb = indice_lb.get(livingBeing.getClass());
            switch (lb){
                case 0:
                    tabColor=HUMANS_COLORS;
                    break;
                case 1:
                    tabColor=CHICKENS_COLORS;
                    break;
                case 2:
                    tabColor = DUCKS_COLORS;
                    break;
                case 3:
                    tabColor = PIGS_COLORS;
                    break;
            }
            indice_ste = indice_state.get(((LivingEntity) livingBeing).getHealthStatus());
        }
        return tabColor[indice_ste];
    }

    /**
     * Determine whether the simulation should continue to run.
     * 
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Map theMap) {
        return stats.isViable(theMap);
    }

    /**
     * Prepare for a new run.
     */
    public void reset() {
    }

    public String getSpeed() {
        return speed;
    }

    private class SpeedListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            speed = combo.getSelectedItem().toString();
        }
    }

    /**
     * Provide a graphical view of a rectangular field. This is a nested class
     * (a class defined inside a class) which defines a custom component for the
     * user interface. This component displays the field. This is rather
     * advanced GUI stuff - you can ignore this for your project if you like.
     */
    private class FieldView extends JPanel {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width) {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                    gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component may be
         * resized, compute the scaling factor again.
         */
        public void preparePaint() {
            if (!size.equals(getSize())) { // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if (xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if (yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the internal
         * image to screen.
         */
        public void paintComponent(Graphics g) {
            if (fieldImage != null) {
                Dimension currentSize = getSize();
                if (size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                } else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width,
                            currentSize.height, null);
                }
            }
        }
    }
}
