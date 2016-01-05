package unice.virus;

import unice.livingEntities.LivingEntity;

import java.util.ArrayList;

/**
 * @author Romain CHuadron , Thomas GILLOT, Loic Rose
 * Represents a Virus
 */
public class Virus {
    private String name;            // Contains the virus name (ex: H5N1, H1N1)
    private int incubationPeriod;   // Time to go from Sick to Contagious
    private int contagiousPeriod;   // Time to go from Contagious to Recovering or Dead
    private int recoveryPeriod;     // Time to go from Recovering to Healthy
    private Double infectionRate;      // Infection Rate (from 0 to 1) representing the chance for a Healthy LivingEntity to become sick when in contact with a Contagious Entity
    private Double mortalityRate;      // Mortality Rate (from 0 to 1) representing the chance for a Contagious LivingEntity to die
    private ArrayList<Class> targetEntities=new ArrayList<>(); // List of species that can be infected by this Virus
    /**
     * Basic constructor
     * @param theName the Virus name
     */
    public Virus(String theName){
        name=theName;
    }

    /**
     * Complete constructor
     * @param theName
     * @param theIncubationPeriod
     * @param theContagiousPeriod
     * @param theRecoveryPeriod
     * @param theInfectionRate
     * @param theMortalityRate
     */
    public Virus(String theName,int theIncubationPeriod, int theContagiousPeriod,int theRecoveryPeriod,Double theInfectionRate, Double theMortalityRate ){
        name=theName;
        setIncubationPeriod(theIncubationPeriod);
        setContagiousPeriod(theContagiousPeriod);
        setRecoveryPeriod(theRecoveryPeriod);
        setInfectionRate(theInfectionRate);
        setMortalityRate(theMortalityRate);
    }

    /**
     * Returns the Virus name
     * @return the Virus name
     */
    public String getName() {
        return name;
    }

    /**
     * Return the incubation period (ie when SICK, time to become CONTAGIOUS)
     * @return the Incubation period (in time unit)
     */
    public int getIncubationPeriod() {
        return incubationPeriod;
    }
    /**
     * Defines a new Incubation Period
     * @param theIncubationPeriod
     */
    protected void setIncubationPeriod(int theIncubationPeriod) {
        incubationPeriod = theIncubationPeriod;
    }

    /**
     * Return the contagious period (ie when CONTAGIOUS, time to either be RECOVERING or DEAD)
     * @return the contagious period (in time unit)
     */
    public int getContagiousPeriod() {
        return contagiousPeriod;
    }
    /**
     * Defines a new Contagious Period
     * @param theContagiousPeriod
     */
    protected void setContagiousPeriod(int theContagiousPeriod) {
        contagiousPeriod = theContagiousPeriod;
    }

    /**
     * Return the recovery period (ie when RECOVERING, time to be HEALTHY again)
     * @return the recovery period (in time unit)
     */
    public int getRecoveryPeriod() {
        return recoveryPeriod;
    }
    /**
     * Defines a new Recovery Period
     * @param theRecoveryPeriod
     */
    protected void setRecoveryPeriod(int theRecoveryPeriod) {
        recoveryPeriod = theRecoveryPeriod;
    }

    /**
     * Return the Infection Rate, ie the probability (between 0 and 1) to infect an HEALTHY living entity
     * @return the Infection Rate
     */
    public Double getInfectionRate() {
        return infectionRate;
    }
    /**
     * Defines a new Infection Rate
     * @param theInfectionRate
     */
    protected void setInfectionRate(Double theInfectionRate) {
        infectionRate = theInfectionRate;
    }

    /**
     * Return the Mortality Rate, ie the probability (between 0 and 1) to die after being CONTAGIOUS
     * @return the Mortality Rate
     */
    public Double getMortalityRate() {
        return mortalityRate;
    }

    /**
     * Defines a new Mortality Rate
     * @param theMortalityRate
     */
    protected void setMortalityRate(Double theMortalityRate) {
        mortalityRate = theMortalityRate;
    }

    /**
     * Add a specific type of LivingEntity to the list of LivingEntities that can be infected by this virus.
     * @param livingEntity
     */
    protected void addTargetEntities(Class livingEntity){
        targetEntities.add(livingEntity);
    }

    /**
     * Check if a specific LivingEntity can be infected by this virus. To be infected, the specified LivingEntity
     * must either be explicitely a target of the Virus (ex: Pig if Pig has been added as a Virus target), or
     * a child of a target of the Virus (ex: Duck if Bird has been specified as a target of the Virus)
     * @param livingEntity
     * @return true if the specified LivingEntity can be infected by the virus. False otherwise.
     */
    public boolean canInfectEntity(LivingEntity livingEntity){
        if (livingEntity==null) return false;

        boolean canInfect=false;
        // Loop on all species that can be infected by the Virus
        for (Class targetEntity: targetEntities) {
            // Check if the specified livingEntity is either identical or a child of the LivingEntities the Virus can target
            canInfect=targetEntity.isAssignableFrom(livingEntity.getClass());

            if (canInfect) break;
        }

        return canInfect;

    }

}
