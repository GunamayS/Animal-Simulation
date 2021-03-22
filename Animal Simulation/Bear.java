import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Bear extends Predator {
    
    private static Random rand = Randomizer.getRandom();

    // Fields to handle disease
    private Disease disease;
    private int diseaseCount;
    private Field f;

    /**
     * Create a Bear. A bear can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the predator will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param sim The main simulator object.
     */
    public Bear(boolean randomAge, Field field, Location location,Simulator sim){
        super(randomAge, field, location,sim);
        f = field;
        setBreedingAge(15);
        setMaxAge(40);
        setBreedingProbability(0.60);
        setMaxLitterSize(2);
        setPreyFoodValue(10);
        setSleepTime(20);
        setSleepDuration(11);
        if(randomAge) {
            setAge(rand.nextInt(getMaxAge()));
            setFoodLevel(rand.nextInt(getPreyFoodValue()));
        }
        else {
            setAge(0);
            setFoodLevel(getPreyFoodValue());
        }
    }

    // Returns an array of hours during which the animal is asleep.
    public ArrayList sleepHours(){
        ArrayList<Integer> hours = new ArrayList();
        for(int i=0; i<=getSleepDuration();i++){
            hours.add((getSleepTime()+i)%24);
        }
        return hours;
    }

    /**
     * This is what the bear does most of the time: it hunts for
     * Prey. In the process, it might breed, die of hunger or
     * die of old age
     * @param newPredators A list to return newly born predators.
     */
    public void act(List<Animal> newPredators)
    {
        if(!sleepHours().contains(getSimulator().getTime())){
            incrementAge();
            incrementHunger();
            if(isAlive()) {
                giveBirth(newPredators);            
                if(!getSimulator().isRaining()){           
                    // Try to move into a free location.
                    Location newLocation = getField().freeAdjacentLocation(getLocation());
                    findFood();
                    if(newLocation != null) {
                        setLocation(newLocation);
                    }
                    else {
                        // Overcrowding.
                        setDead();
                    }
                    //Disease Handling.
                    if(!getDiseased()){
                        if(rand.nextDouble() < getSimulator().getDiseaseProbabilty()){
                            infect();
                        }
                        
                    }
                    else if(diseaseCount > 0){
                            diseaseCount--;
                            incrementAge();
                            incrementHunger();
                            spreadDisease();
                    }
                    else{
                        setDiseased(false);
                        disease = null;
                    }   
                }
            }
        }
    }

    // When this method is called it signals that the animal has been infected with a disease and handles neccesary operations.
    public void infect(){
        setDiseased(true);
        disease = new Disease(getSimulator(),this); 
        diseaseCount = disease.getDuration();
    }

    // Attempt to spread disease to nearby animals.
    private void spreadDisease(){
        if(getLocation() != null){
            List<Location> adjacent = f.adjacentLocations(getLocation());
            Iterator<Location> it = adjacent.iterator();
            while(it.hasNext()) {
                Location where = it.next();
                Object a = f.getObjectAt(where);
                Animal animal = (Animal) a;
                if(animal != null) {
                    if(rand.nextDouble()<disease.getProbabilty()){
                        animal.infect();
                        return;           
                    }               
                }
            }
            return;
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
            if(animal instanceof Mouse) {
                Prey Prey = (Prey) animal;
                if(Prey.isAlive()) { 
                    Prey.setDead();
                    setFoodLevel(getPreyFoodValue());
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Check whether or not this bear is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newPredators A list to return newly born predators.
     */
    protected void giveBirth(List<Animal> newPredators)
    {
        // New Predators are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
                Predator young = new Bear(false, field, loc,getSimulator());
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
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }

    /**
     * A Predator can breed if it has reached the breeding age.
     */
    public boolean canBreed()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Bear) {
                Predator predator = (Predator) animal;
                if(!predator.isFemale() && this.isFemale() && getFoodLevel()>=getPreyFoodValue()-6) { 
                    return true;
                }
                else{
                    return false;
                }
            }
        } 
        return false;
    }
}

