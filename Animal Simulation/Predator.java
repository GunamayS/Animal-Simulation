import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a Predator.
 * Predator age, move, eat Preys, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Predator extends Animal
{
    // Characteristics shared by all predators (class variables).
    
    // The age at which a predator can start to breed.
    private static int BREEDING_AGE;
    // The age to which a predator can live.
    private static int MAX_AGE;
    // The likelihood of a predator breeding.
    private static double BREEDING_PROBABILITY;
    // The maximum number of births.
    private static int MAX_LITTER_SIZE;
    // The food value of a single Prey. In effect, this is the
    // number of steps a predator can go before it has to eat again.
    private static int PREY_FOOD_VALUE;
    // A shared random number generator to control breeding.
    private static Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    // The Predator's age.
    private int age;
    // The timing in which the Predators sleep at.
    private int sleepTime;
    // The duration the predators sleep for
    private int sleepDuration;

    /**
     * Create a predator. A predator can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the predator will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param sim The main simulator object.
     */
    public Predator(boolean randomAge, Field field, Location location,Simulator sim)
    {
        super(field, location,sim);

    }
    
    /**
     * This is what the predator does most of the time: it hunts for
     * Preys. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param newPredators A list to return newly born predatores.
     */
    public void act(List<Animal> newPredators)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newPredators);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
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
     * Increase the age. This could result in the predator's death.
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
     * Look for Preys adjacent to the current location.
     * Only the first live Prey is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Prey) {
                Prey Prey = (Prey) animal;
                if(Prey.isAlive()) { 
                    Prey.setDead();
                    setFoodLevel(PREY_FOOD_VALUE);
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this predator is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newPredators A list to return newly born predatores.
     */
    private void giveBirth(List<Animal> newPredators)
    {
        // New Predators are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
                Predator young = new Predator(false, field, loc,getSimulator());
                newPredators.add(young);                        
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
     * A Predator can breed if it has reached the breeding age.
     */
    protected boolean canBreed()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Predator) {
                Predator predator = (Predator) animal;
                if(!predator.isFemale() && this.isFemale()) { 
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
    
    /** Set the food value of one prey.
    * @param preyFoodValue The food value of one prey.
    */ 
    protected void setPreyFoodValue(int preyFoodValue){
        PREY_FOOD_VALUE = preyFoodValue;
    }

    /**
     * Return the food value of one prey.
     * @return The food value of one prey.
     */
    protected int getPreyFoodValue(){
        return PREY_FOOD_VALUE;
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
