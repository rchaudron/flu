package unice;

import unice.display.Begining;
import unice.map.NeighbourhoodType;
import unice.simulator.Simulator;

import java.util.Timer;

public class Main {

    private static Timer timer;
    private static Simulator simulator;

    public static void main(String[] args) throws InterruptedException {
        Begining begining = new Begining();

        // Create a new simlation

        Boolean simulation = begining.isSimuation_begin();


        while (!simulation) {
            /**
             * While we don t push the enter button we do nothing
             * We add a System.out.println() to jump some lines in the terminal
             */
            Thread.sleep(500);
            System.out.println();
            simulation = begining.isSimuation_begin();
        }

        if (begining.getNeighbourhood().equals("FOUR")) {
            simulator = new Simulator(50, 50, NeighbourhoodType.FOUR);
        } else {
            simulator = new Simulator(50, 50, NeighbourhoodType.HEIGHT);
        }
        simulator.simulate(begining.getSteps());
        begining.close();
    }

}



