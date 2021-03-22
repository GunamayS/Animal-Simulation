public class Disease {
    
    // How long the disease lasts for.
    private int duration;
    // The probability of being infected by the disease.
    private double infectProbability;
    // The main simulator object.
    private Simulator sim;
    // The animal that is to be infected.
    private Animal animal;

    /**
     * Creates the disease to infect the animal with.
     * 
     * @param sim The main simulator object.
     * @param animal The animal to be infected.
     */
    public Disease(Simulator sim, Animal animal){
        this.sim = sim;
        this.animal = animal;
        this.duration = 10;
        this.infectProbability = 0.1;
    }

    /**
     * Get the probability of being infected.
     * @return the infect probability.
     */
    protected double getProbabilty(){
        return infectProbability;
    }
    
    /**
     * Assign the probabilty to infectProbabilty of the animal being infected.
     * @param probability The probabilty of the animal being infected.
     */
    protected void setProbabilty(double probability){
        this.infectProbability = probability;
    }
    
    /**
     * The duration of the disease being in an animal.
     * @return duration.
     */
    protected int getDuration() {
        return duration;
    }
    
    /**
     * Set the duration for how long a disease lasts in an animal
     * @param duration The duration of the disease being in an animal.
     */
    protected void setDuration(int duration){
        this.duration = duration;
    }

    /**
     * Return the main simulator object.
     * @return sim The main simulator object.
     */
    protected Simulator getSimulator(){
        return sim;
    }

    /**
     * Set the animal to be infected by the disease
     * @param Animal 
     */
    protected void setAnimal(int Animal){
        this.animal = animal;
    }

    /**
     * Return the infected animal.
     * @return animal The infected animal.
     */
    protected Animal getAnimal(){
        return animal;
    }
}
