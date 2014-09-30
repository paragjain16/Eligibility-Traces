/* Author: Mingcheng Chen */

import java.util.Random;
import java.util.ArrayList;

public class QLearningAgent implements Agent {
    public QLearningAgent() {
        this.rand = new Random();
    }

    public void initialize(int numOfStates, int numOfActions) {
        this.qValue = new double[numOfStates][numOfActions];
        this.eValue = new double[numOfStates][numOfActions];
        this.numOfStates = numOfStates;
        this.numOfActions = numOfActions;

        for (int i = 0; i < numOfStates; i++) {
            for (int j = 0; j < numOfActions; j++) {
                this.qValue[i][j] = 0.0;
            }
        }
    }

    private double bestUtility(int state) {
        double result = this.qValue[state][0];
        for (int i = 1; i < this.numOfActions; i++) {
            if (this.qValue[state][i] > result) {
                result = this.qValue[state][i];
            }
        }

        return result;
    }

    public int chooseAction(int state) {
        if (this.rand.nextDouble() <= epsilon) {
            greedy = false;
            return this.rand.nextInt(numOfActions);
        }
        greedy = true;
        double maxQ = this.bestUtility(state);

        // System.out.println("maxQ = " + maxQ);

        ArrayList<Integer> candidates = new ArrayList<Integer>();
        for (int i = 0; i < this.numOfActions; i++) {

            // System.out.println("qValue: " + this.qValue[state][i]);

            if (this.qValue[state][i] == maxQ) {
                candidates.add(i);
            }
        }

        // System.out.println("candidates = " + candidates.size());
        // return candidates.get(0);
        return candidates.get(this.rand.nextInt(candidates.size()));
    }

    public void updatePolicy(double reward, int action,
                             int oldState, int newState) {
        qValue[oldState][action] = (1.0 - rate) * qValue[oldState][action] + rate * ( reward + discount * bestUtility(newState));
    }

    public void updatePolicyET(double reward, int action, int bestActionFromNewState,
                             int oldState, int newState, boolean isGreedy) {

        double update = reward + discount * qValue[newState][bestActionFromNewState] - qValue[oldState][action];
        eValue[oldState][action] += 1;

        for(int i = 0; i< numOfStates; i++){
            for(int j =0; j<numOfActions; j++){
                qValue[i][j] += rate * update * eValue[i][j];
                if(isGreedy)
                    eValue[i][j] = discount * lambda * eValue[i][j];
                else
                    eValue[i][j] = 0;
            }
        }
    }

    public Policy getPolicy() {
        int[] actions = new int[this.numOfStates];

        for (int state = 0; state < this.numOfStates; state++) {
            double maxQ = this.bestUtility(state);

            for (int i = 0; i < this.numOfActions; i++) {
                if (this.qValue[state][i] == maxQ) {
                    actions[state] = i;
                    break;
                }
            }
        }

        return new Policy(actions);
    }

    public int bestAction(int state){
        double[] qValueForState = qValue[state];
        int bestAction = -1;
        double maxQValue = Double.NEGATIVE_INFINITY;
        int i = 0;
        for(double aQValue : qValueForState){
            if(Double.compare(aQValue, maxQValue) > 0){
                bestAction = i;
                maxQValue = aQValue;
            }
            i++;
        }
        return bestAction;
    }

    private static final double discount = 0.9;
    private static final double rate = 0.05;
    private static final double epsilon = 0.01;
    private static final double lambda = 0.9;

    private int numOfStates;
    private int numOfActions;
    private double[][] qValue;
    private double[][] eValue;
    private Random rand;
    private boolean greedy = false;
}

/* Author: Mingcheng Chen and Parag Jain
*  Date: 09/20/14
*

import java.util.Random;
import java.util.ArrayList;

public class QLearningAgent implements Agent {
  public QLearningAgent() {
    this.rand = new Random();
  }

  public void initialize(int numOfStates, int numOfActions) {
    this.qValue = new double[numOfStates][numOfActions];
    this.numOfStates = numOfStates;
    this.numOfActions = numOfActions;
  }

  private double bestUtility(int state) {
    double maxQValue = Double.NEGATIVE_INFINITY;
    for(double aQValue : qValue[state]){
        if(Double.compare(aQValue, maxQValue) > 0)
            maxQValue = aQValue;
    }
    return maxQValue;
  }

  public int chooseAction(int state) {
    double prob = rand.nextDouble();
    ArrayList<Integer> actionsWithMaxQValue = new ArrayList<Integer>();
    // Choose random action with epsilon probability
    if(prob <= epsilon){
        return rand.nextInt(this.numOfActions);
    }else{ //Be greedy with (1-epsilon) probability
        double maxQValue = Double.NEGATIVE_INFINITY;
        double[] actionsForThisState = qValue[state];
        for(double aQValue : actionsForThisState){
            if(Double.compare(aQValue, maxQValue) > 0){
                maxQValue = aQValue;
            }
        }
        int i = 0;
        for(double aQValue : actionsForThisState){
            if(Double.compare(aQValue, maxQValue) == 0){
                actionsWithMaxQValue.add(i);
            }
            i++;
        }
        // When there are multiple max values, choose one randomly
        return actionsWithMaxQValue.get(rand.nextInt(actionsWithMaxQValue.size()));
    }
  }

  public void updatePolicy(double reward, int action,
                           int oldState, int newState) {
    qValue[oldState][action] = (1.0 - rate) * qValue[oldState][action] + rate * ( reward + discount * bestUtility(newState));
  }

  public Policy getPolicy() {
      int[] actions = new int[this.numOfStates];
      for (int i = 0; i < this.numOfStates; i++) {
          actions[i] = bestAction(qValue[i]);
      }
      return new Policy(actions);
  }

  private int bestAction(double[] qValueForState){
      int bestAction = -1;
      double maxQValue = Double.NEGATIVE_INFINITY;
      int i = 0;
      for(double aQValue : qValueForState){
          if(Double.compare(aQValue, maxQValue) > 0){
              bestAction = i;
              maxQValue = aQValue;
          }
          i++;
      }
      return bestAction;
  }

  public void setEpsilon(double epsilon){
      this.epsilon = epsilon;
  }

  public double getEpsilon(){
      return epsilon;
  }

    public static double getRate() {
        return rate;
    }

    public static void setRate(double rate) {
        QLearningAgent.rate = rate;
    }

    private static final double discount = 0.9;
  public static double rate = 1.0;
  public static double epsilon = 1.0;

  private int numOfStates;
  private int numOfActions;
  private double[][] qValue;
  private Random rand;
}*/
