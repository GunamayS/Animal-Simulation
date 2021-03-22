import java.util.Random;

public class Rain extends Weather {
    
    private Random rand = new Random();
    // The Rain counter.
    private int counter = 0;

    /**
     * Create a rain simulation in the field.
     * 
     * @param sim The main simulator object.
     */
    public Rain(Simulator sim){
        super(sim);
        setDuration(10);
        setProbabilty(0.1);
        setDiseaseProbabilty(0.05);
    }
    /** 
     * Simulate the rain: it checks if it is raining,
     * and if it is not raining, it gets a random number and checks whether
     * the number is less than the set probabilty of it raining.
     * If the number is less, it starts raining for the number of steps
     * equivalent to the count variable.
     * If is is raining, the counter variable decrements with each step in 
     * the simulation, until it reaches 0 and stops raining.
     */
    protected void act(){
        if(!getSimulator().isRaining()){
            if(rand.nextDouble()<getProbabilty()){
                getSimulator().toggleRain();
            }
            counter = getDuration();
        }
        else{
            counter--;
            if(counter<=0){
                getSimulator().toggleRain();
            }
        }
    }

}
