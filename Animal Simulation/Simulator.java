import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a snake will be created in any given grid position.
    private static final double SNAKE_CREATION_PROBABILITY = 0.04;
    // The probability that a bear will be created in any given grid position.
    private static final double BEAR_CREATION_PROBABILITY = 0.04;
    // The probability that a jaguar will be created in any given grid position.
    private static final double JAGUAR_CREATION_PROBABILITY = 0.04;  
    // The probability that a bird will be created in any given grid position.
    private static final double BIRD_CREATION_PROBABILITY = 0.03;
    // The probability that a mouse will be created in any given grid position.
    private static final double MOUSE_CREATION_PROBABILITY = 0.06;   
    // The rate of spawning for grass.
    private static final int GRASS_SPAWN_RATE = 50;
    // The rate of spawning for daisy.
    private static final int DAISY_SPAWN_RATE = 50;

     

    // List of animals in the field.
    private List<Animal> animals;
    // List of plants in the field.
    private List<Plant> plants;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // The current time of the simulation.
    private int time = 0;
    // A graphical view of the simulation.
    private SimulatorView view;
    // Whether it is raining or not.
    private boolean isRaining;
    // The current rain in the simulation.
    private Rain rain;
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }


    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        animals = new ArrayList<>();
        plants = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Mouse.class, Color.ORANGE);
        view.setColor(Snake.class, Color.BLUE);
        view.setColor(Bear.class, Color.MAGENTA);
        view.setColor(Jaguar.class, Color.GREEN);
        view.setColor(Bird.class, Color.RED);
 
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            // delay(60);   // uncomment this to run more slowly
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of all objects
     */
    public void simulateOneStep()
    {
        step++;
        incrementTime();

        // Simulate rain.
        rain.act();
        // Provide space for newborn animals.
        List<Animal> newAnimals = new ArrayList<>();        
        // Let all animals act.
        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals);
            if(! animal.isAlive()) {
                it.remove();
            }
        }

        // Adds daisies to the space.
        for(int i = 0; i<DAISY_SPAWN_RATE; i++) {
            Plant daisy = new Daisy(this);
            plants.add(daisy);
        }
        // Adds grass to the space.
        for(int i = 0; i<GRASS_SPAWN_RATE; i++) {
            Plant grass = new Grass(this);
            plants.add(grass);
        }
               
        // Add the newly born animals to the main lists.
        animals.addAll(newAnimals);

        view.showStatus(step, field);
    }
    // Increments time with each step.   
    public void incrementTime(){
        time++;
        if (time==24){
            time=0;
        }

    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        time =0;
        animals.clear();
        populate();
        
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    /**
     * Randomly populate the field with animals.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        rain = new Rain(this);
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= SNAKE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Predator snake = new Snake(true, field, location,this);
                    animals.add(snake); // Adds snakes to the field.
                }
                else if(rand.nextDouble() <= BEAR_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Predator bear= new Bear(true, field, location,this);
                    animals.add(bear); // Adds bears to the field.
                }
                else if(rand.nextDouble() <= JAGUAR_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Predator jaguar= new Jaguar(true, field, location, this);
                    animals.add(jaguar); // Adds jaguars to the field.
                }
                else if(rand.nextDouble() <= BIRD_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Prey bird= new Bird(true, field, location, this);
                    animals.add(bird); // Adds birds to the field.
                }
                else if(rand.nextDouble() <= MOUSE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Prey mouse= new Mouse(true, field, location, this);
                    animals.add(mouse); // Adds mice to the field.
                }
                // else leave the location empty.
            }
        }
    }
    

    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds.
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
    // Returns time.
    public int getTime(){
        return time;
    }
    // Returns whether it is raining or not.
    public boolean isRaining(){
        return isRaining;
    }
    // Toggles between whether it is raining on the field or not.
    public void toggleRain(){
        isRaining = !isRaining;
    }

    // Returns a list of plants in the field.
    public List<Plant> getPlants() {
        return plants;
    }   
    // Returns the probability of getting the disease.
    public double getDiseaseProbabilty(){
        return rain.getDiseaseProbabilty();
    }
}