import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animal
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    // The animal's food level, which is increased by eating food.
    private int foodLevel;
    // The animal's gender
    private boolean female;
    // A shared random number generator to control breeding.
    private static Random rand = Randomizer.getRandom();
    // The main simulator object
    private Simulator sim;
    // If the animal is diseased or not
    private boolean diseased;

    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param sim The main simulator object.
     */
    public Animal(Field field, Location location, Simulator sim)
    {
        alive = true;
        this.field = field;
        this.sim = sim;
        setLocation(location);
        if (rand.nextInt(2)==0){
            female = true;
        }
        else{
            female = false;
        }
        diseased=false;
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals);

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's food level.
     * @return The animal's food level.
     */
    protected int getFoodLevel()
    {
        return foodLevel;
    }

    /**
     * The starting food level of the animal.
     * @param foodLevel The animal's food level.
     */
    protected void setFoodLevel(int foodLevel)
    {
        this.foodLevel = foodLevel;
        return;
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }

    /**
     * Return whether the animal is female or not.
     * @return The animal's gender.
     */
    public boolean isFemale(){
        return female;
    }

    /**
     * Return the main simulator object.
     * @return The main simulator object.
     */
    protected Simulator getSimulator(){
        return sim;
    }

    /**
     * Give the animal the disease.
     * @param diseased The animal's disease.
     */
    public void setDiseased(boolean diseased){
        this.diseased = diseased;
    }

    /**
     * Return whether the animal is diseased or not.
     * @return The animal's disease.
     */
    public boolean getDiseased(){
        return diseased;
    }

    // Method to be overwritten in the subclasses.
    protected void infect(){        
    }
}
