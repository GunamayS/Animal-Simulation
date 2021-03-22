import java.util.Random;

public class Weather {

    private static Random rand = Randomizer.getRandom();
    // Duration of the weather condition
    private int duration;
    // Probability for this weather condition to trigger
    private double probability;
    // Probability for this weather condition to cause disease
    private double diseaseProbability;
    // The main simulator object
    private Simulator sim;

    /**
     * Create a weather condition in the field.
     * 
     * @param sim The main simulator object.
     */
    public Weather(Simulator sim){
        this.sim = sim;
    }
    
    /**
     * Return the weather probability.
     * @return The weather probability.
     */
    protected double getProbabilty(){
        return probability;
    }

    /** Set the weather probability.
    * @param probability The weather probability.
    */   
    protected void setProbabilty(double probability){
        this.probability = probability;
    }
    
    /**
     * Return the weather duration.
     * @return The weather duration.
     */
    protected int getDuration() {
        return duration;
    }
    
    /** Set the weather duration.
    * @param duration The weather duration.
    */ 
    protected void setDuration(int duration){
        this.duration = duration;
    }

    /**
     * Return the main simulator object.
     * @return The main simulator object.
     */
    protected Simulator getSimulator(){
        return sim;
    }

    /** Set the disease probability.
    * @param diseaseProbability The disease probability.
    */ 
    protected void setDiseaseProbabilty(double diseaseProbability){
        this.diseaseProbability = diseaseProbability;
    }

    /**
     * Return the disease probability.
     * @return The disease probability.
     */    
    protected double getDiseaseProbabilty(){
        return diseaseProbability;
    }
}
