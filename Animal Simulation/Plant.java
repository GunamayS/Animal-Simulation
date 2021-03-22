import java.util.Random;

public class Plant {
    
    // Whether the plant is alive or not.
    private boolean alive;
    // The plant's food value, which increases the animal food level by eating.
    private int foodValue;
    // A shared random number generator to control breeding.
    private static Random rand = Randomizer.getRandom();
    // The main simulator object
    private Simulator sim;

     /**
     * Create a new plant at location in field.
     * 
     * @param sim The main simulator object.
     */
    public Plant(Simulator sim)
    {
        alive = true;
        this.sim = sim;
    }

    /**
     * Check whether the plant is alive or not.
     * @return true if the plant is still alive.
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the Plant is no longer alive.
     * It is removed from the field.
     */
    public void setDead()
    {
        alive = false;
    }


    /**
     * Returns the food value that the plant is worth.
     * @return The plant's food value.
     */
    public int getFoodValue()
    {
        return foodValue;
    }

     /**
     * Initialises the food value of the plant.
     * @param foodValue The food value of the plant
     */
    public void setFoodValue(int foodValue)
    {
        this.foodValue = foodValue;
        return;
    }

    /**
     * Returns the main simulator object.
    *  @return The main simulator object.
     */
    public Simulator getSimulator(){
        return sim;
    }
    
}
