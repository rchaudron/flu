package unice.simulator;

import unice.*;
import unice.display.*;
import unice.livingEntities.LivingEntity;
import unice.map.Location;
import unice.map.Map;
import unice.livingEntities.*;
import unice.map.NeighbourhoodType;
import unice.virus.H1N1Virus;
import unice.virus.H5N1Virus;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A simple epidemic simulator, based on a rectangular map containing
 * humans, chickens, ducks and pigs.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class Simulator {
    // Constants representing configuration information for the simulation.
    // The default width for the map.
    private static final int DEFAULT_WIDTH = 50;
    // The default depth of the map.
    private static final int DEFAULT_DEPTH = 50;
    // The probability that a chicken will be created in any given map position.
    private static final double CHICKEN_CREATION_PROBABILITY = 0.06;
    // The probability that a duck will be created in any given map position.
    private static final double DUCK_CREATION_PROBABILITY = 0.04;
    // The probability that a pig will be created in any given map position.
    private static final double PIG_CREATION_PROBABILITY = 0.15;
    // The probability that a person will be created in any given map position.
    // If we want to have no empty space, we must put 1
    private static final double PERSON_CREATION_PROBABILITY = 0.6;

    private static final double PIG_INITIALINFECTION_PROBABILITY = 0.5;
    private static final double DUCK_INITIALINFECTION_PROBABILITY = 0.5;
    private static final double CHICKEN_INITIALINFECTION_PROBABILITY = 0.5;

    // List of living entities in the map.
    private List<LivingEntity> livingEntities;
    // The current state of the map.
    private Map map;
    // The current step of the simulation.
    private int step;
    // The graphical views of the simulation.
    private List<SimulatorView> views;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH,NeighbourhoodType.FOUR);
    }

    /**
     * Create a simulation field with the given size.
     * 
     * @param depth
     *            Depth of the field. Must be greater than zero.
     * @param width
     *            Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width, NeighbourhoodType theNeighbourhoodType) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        livingEntities = new ArrayList<>();
        map = new Map(depth, width,theNeighbourhoodType);

        views = new ArrayList<>();

        SimulatorView view = new GridView(depth, width);
        view.setColor(Person.class, Color.ORANGE);
        view.setColor(Pig.class, Color.PINK);
        view.setColor(Chicken.class, Color.BLUE);
        view.setColor(Duck.class, Color.BLACK);
        views.add(view);

        view = new GraphView(500, 150, 500);
        view.setColor(Person.class, Color.ORANGE);
        view.setColor(Pig.class, Color.PINK);
        view.setColor(Chicken.class, Color.BLUE);
        view.setColor(Duck.class, Color.BLACK);
        views.add(view);

        // Setup a valid starting point.
        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation() {
        simulate(4000);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * 
     * @param numSteps
     *            The number of steps to run for.
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && views.get(0).isViable(map); step++) {
            simulateOneStep();
        }
    }

    /**
     * Run the simulation from its current state for a single step. Iterate over
     * the whole field updating the state of each fox and rabbit.
     */
    public void simulateOneStep() {
        step++;

        // Let all LivingEntities act.
        for (LivingEntity livingEntity:livingEntities) {
            livingEntity.act();
        }

        updateViews();
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        livingEntities.clear();
        for (SimulatorView view : views) {
            view.reset();
        }

        populate();
        updateViews();
    }

    /**
     * Update all existing views.
     */
    private void updateViews() {
        for (SimulatorView view : views) {
            view.showStatus(step, map);
        }
    }

    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate() {
        Random rand = new Random();
        H5N1Virus h5n1=new H5N1Virus();
        H1N1Virus h1n1=new H1N1Virus();
        map.clear();
        for (int y = 0; y < map.getDepth(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {

                if (rand.nextDouble() <= PIG_CREATION_PROBABILITY) {
                    Pig pig=new Pig(map);
                    if (rand.nextDouble() <= PIG_INITIALINFECTION_PROBABILITY) pig.becomeSick(h1n1);
                    placeLivingEntity(pig,x,y);
                } else if (rand.nextDouble() <= CHICKEN_CREATION_PROBABILITY) {
                    Chicken chicken=new Chicken(map);
                    if (rand.nextDouble() <= CHICKEN_INITIALINFECTION_PROBABILITY) chicken.becomeSick(h5n1);
                    placeLivingEntity(chicken,x,y);
                } else if (rand.nextDouble() <= DUCK_CREATION_PROBABILITY) {
                    Duck duck=new Duck(map);
                    if (rand.nextDouble() <= DUCK_INITIALINFECTION_PROBABILITY) duck.becomeSick(h5n1);
                    placeLivingEntity(duck,x,y);
                } else if (rand.nextDouble() <= PERSON_CREATION_PROBABILITY) {
                    Person person=new Person(map);
                    placeLivingEntity(person,x,y);
                }
            }
        }
    }

    private void placeLivingEntity(LivingEntity theLivingEntity, int x, int y){
        livingEntities.add(theLivingEntity);
        theLivingEntity.setLocation(new Location(x,y));
    }
}
