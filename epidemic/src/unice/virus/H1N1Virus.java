package unice.virus;

import unice.livingEntities.Human;
import unice.livingEntities.Pig;

/**
 * @author Romain Chuadron
 * Class representing the H1N1 Virus
 */
public class H1N1Virus extends Virus {
    public H1N1Virus(){

        super("H1N1");
        setIncubationPeriod(5);
        setContagiousPeriod(10);
        setRecoveryPeriod(3);
        setInfectionRate(0.3);
        setMortalityRate(0.15);
        addTargetEntities(Pig.class);
        addTargetEntities(Human.class);
    }
}
