package unice.virus;

import unice.livingEntities.Bird;
import unice.livingEntities.Human;

/**
 * @author Romain Chaudron
 * Class representing the H5N1 Virus
 */
public class H5N1Virus extends Virus {
    public H5N1Virus(){
        super("H5N1");
        setIncubationPeriod(10);
        setContagiousPeriod(3);
        setRecoveryPeriod(5);
        setInfectionRate(0.2);
        setMortalityRate(0.9);
        addTargetEntities(Bird.class);
        addTargetEntities(Human.class);
    }
}
