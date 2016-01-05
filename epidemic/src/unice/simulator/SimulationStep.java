package unice.simulator;

import java.util.TimerTask;

/**
 * @author Romain
 */
public class SimulationStep extends TimerTask {

    private Simulator simulator;

    public SimulationStep(Simulator theSimulator) {
        simulator=theSimulator;
    }

    @Override
    public void run() {
        simulator.simulateOneStep();
    }
}
