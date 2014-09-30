/* Author: Mingcheng Chen */

public interface Agent {
  public void initialize(int numOfStates, int numOfActions);
  public int chooseAction(int state);
  public void updatePolicy(double reward, int action,
                           int oldState, int newState);
  public void updatePolicyET(double reward, int action, int bestActionFromNewState,
                             int oldState, int newState, boolean isGreedy);
  public Policy getPolicy();
}
