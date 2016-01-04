package unice;

import unice.map.NeighbourhoodType;
import unice.simulator.SimulationStep;
import unice.simulator.Simulator;

import java.util.Timer;

public class Main {

    private static Timer timer;
    private static Simulator simulator;

    public static void main(String[] args) {

        // Instanciate new timer to run the simulator
        timer=new Timer();
        // Create a new simlation
        simulator=new Simulator(50,50, NeighbourhoodType.HEIGHT);
        // Start Simulator after 100ms, and execute one step every 200ms
        timer.scheduleAtFixedRate(new SimulationStep(simulator),100,50);
    }
}
