import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class Mouse extends Prey {

    // A shared random number generator to control breeding.
    private static Random rand = Randomizer.getRandom();

    // Fields to handle disease.
    private Disease disease;
    private int diseaseCount;
    private Field f;
    
    /**
     * Create a new mouse. A mouse may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the mouse will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param sim The main simulator object.
     */    
    public Mouse(boolean randomAge, Field field, Location location,Simulator sim){
        super(randomAge, field, location,sim);
        f = field;
        setBreedingAge(5);
        setMaxAge(60);
        setBreedingProbability(0.80);
        setMaxLitterSize(5);
        setSleepTime(8);
        setSleepDuration(14);
        setAge(0);
        setFoodLevel(10);
        if(randomAge) {
            setAge(rand.nextInt(getMaxAge()));
        }
    }

    //Returns an array of hours during which the animal is asleep.
    public ArrayList sleepHours(){
        ArrayList<Integer> hours = new ArrayList();
        for(int i=0; i<=getSleepDuration();i++){
            hours.add((getSleepTime()+i)%24);
        }
        return hours;
    }

    /**
     * This is what the mouse does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newPreys A list to return newly born Preys.
     */
    public void act(List<Animal> newPreys)
    {
        if(!sleepHours().contains(getSimulator().getTime())){
            incrementAge();
            incrementHunger();
            if(isAlive()) {
                giveBirth(newPreys);
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
    
    //When this method is called it signals that the animal has been infected with a disease and handles neccesary operations.
    public void infect(){
        setDiseased(true);
        disease = new Disease(getSimulator(),this); 
        diseaseCount = disease.getDuration();
    }

    //Attempt to spread disease to nearby animals.
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
     * Check whether or not this mouse is to give birth at this step.
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
                Prey young = new Mouse(false, field, loc, getSimulator());
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
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()+ 1);
        }
        return births;
    }

    /**
     * A mouse can breed if it has reached the breeding age.
     * @return true if the mouse can breed, false otherwise.
     */
    public boolean canBreed()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Mouse) {
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

        /**
     * Look for Plants to eat.
     * Only the first plant is eaten.
     */
    protected void findFood(){
        List<Plant> plants = getSimulator().getPlants();
        Iterator<Plant> it = plants.iterator();
        while(it.hasNext()) {
            Object plant = it.next();
            if(plant instanceof Grass){
                Plant p = (Plant) plant;
                if(p.isAlive()) { 
                    p.setDead();
                    setFoodLevel(getFoodLevel() + p.getFoodValue());
                    return;
                }  
            } 
        }
        return;
    }   
}
