import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A simple model of a Prey.
 * Preys age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Prey extends Animal
{
    // Characteristics shared by all Preys (class variables).

    // The age at which a Prey can start to breed.
    private static int BREEDING_AGE;
    // The age to which a Prey can live.
    private static int MAX_AGE;
    // The likelihood of a Prey breeding.
    private static double BREEDING_PROBABILITY;
    // The maximum number of births.
    private static int MAX_LITTER_SIZE;
    // A shared random number generator to control breeding.
    private static Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    // The Prey's age.
    private int age;    
    // The timing in which the prey sleep at.
    private int sleepTime;
    // The duration the prey sleep for
    private int sleepDuration;

    /**
     * Create a new Prey. A Prey may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the Prey will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param sim The main simulator object.
     */
    public Prey(boolean randomAge, Field field, Location location, Simulator sim)
    {
        super(field, location, sim);

    }
    
    /**
     * This is what the Prey does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newPreys A list to return newly born Preys.
     */
    public void act(List<Animal> newPreys)
    {
        incrementAge();
        if(isAlive()) {
            giveBirth(newPreys);            
            // Try to move into a free location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Increase the age.
     * This could result in the Prey's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this predator more hungry. This could result in the predator's death.
     */
    protected void incrementHunger()
    {
        setFoodLevel(getFoodLevel()-1);
        if(getFoodLevel() <= 0) {
            setDead();
        }
    }

    /**
     * Look for Plants to eat.
     * Only the first plant is eaten.
     */
    protected void findFood(){
        List<Plant> plants = getSimulator().getPlants();
        Iterator<Plant> it = plants.iterator();
        while(it.hasNext()) {
            Plant plant = it.next();
            if(plant.isAlive()) { 
                plant.setDead();
                setFoodLevel(getFoodLevel() + plant.getFoodValue());
                return;
            }   
        }
        return;
    }

    /**
     * Check whether or not this Prey is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newPreys A list to return newly born Preys.
     */
    protected void giveBirth(List<Animal> newPreys)
    {
        // New Preys are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
                Prey young = new Prey(false, field, loc,getSimulator());
                newPreys.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A Prey can breed if it has reached the breeding age.
     * @return true if the Prey can breed, false otherwise.
     */
    protected boolean canBreed()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Prey) {
                Prey prey = (Prey) animal;
                if(!prey.isFemale() && this.isFemale()) { 
                    return true;
                }
                else{
                    return false;
                }
            }
        } 
        return false;
    }

    /** Set the min breeding age .
    * @param breedingAge The min breeding age.
    */   
    protected void setBreedingAge(int breedingAge){
        BREEDING_AGE = breedingAge;
    }

    /**
     * Return the breeding age.
     * @return The breeding age.
     */
    protected int getBreedingAge(){
        return BREEDING_AGE;
    }

    /** Set the max age .
    * @param maxAge The max age.
    */    
    protected void setMaxAge(int maxAge){
        MAX_AGE = maxAge;
    }

    /**
     * Return the max age.
     * @return The max age.
     */
    protected int getMaxAge(){
        return MAX_AGE;
    }

    /** Set the breeding probability.
    * @param breedingProbability The breeding probability.
    */       
    protected void setBreedingProbability(double breedingProbability){
        BREEDING_PROBABILITY = breedingProbability;
    }

    /**
     * Return the breeding probability.
     * @return The breeding probability.
     */    
    protected double getBreedingProbability(){
        return BREEDING_PROBABILITY;
    }

    /** Set the max litter size.
    * @param maxLitterSize The max litter size.
    */ 
    protected void setMaxLitterSize(int maxLitterSize){
        MAX_LITTER_SIZE = maxLitterSize;
    }

    /**
     * Return the max litter size.
     * @return The max litter size.
     */
    protected int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }
    
    /** Set the age.
    * @param age The age.
    */ 
    protected void setAge(int age){       
        this.age = age;
    }

    /**
     * Return the age of the predator.
     * @return Age.
     */
    protected int getAge(){
        return age;
    }

    /** Set the time the predator sleeps.
    * @param sleepTime the time the predator sleeps.
    */ 
    protected void setSleepTime(int sleepTime){       
        this.sleepTime = sleepTime;
    }

    /**
     * Return the time the predator sleeps.
     * @return The time the predator sleeps.
     */
    protected int getSleepTime(){
        return sleepTime;
    }

    /** Set the duration the predator sleeps.
    * @param sleepDuration the duration the predator sleeps.
    */
    protected void setSleepDuration(int sleepDuration){       
        this.sleepDuration = sleepDuration;
    }

    /**
     * Return the duration the predator sleeps.
     * @return The duration the predator sleeps.
     */
    protected int getSleepDuration(){
        return sleepDuration;
    }
}

