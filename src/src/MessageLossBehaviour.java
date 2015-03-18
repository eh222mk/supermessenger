package src;

import java.util.Random;

/***
 * Behaviors for creating messageloss
 * @author eh222mk, js222xt
 *
 */
public class MessageLossBehaviour implements Behaviour {

	//Fields
	/***
	 * How often messages should be lost, first loss chance
	 */
	private int messageLossChanceOne = 0;
	/***
	 * How often messages should be lost, second loss chance
	 */
	private int messageLossChanceTwo = 0;
	/***
	 * Current message loss
	 */
	private int currentMsgLoss = 0;
	/***
	 * How often it should switch from first loss chance to the second
	 */
	private int switchEvery = 0;
	/***
	 * Counter to determine if message should be lost
	 */
	private int lossCounter = 0;
	/***
	 * Counter to determine if a switch should be performed
	 */
	private int switchlossCounter = 0;
	/***
	 * Should it affect all providers
	 */
	private boolean affectsAll = false;
	/***
	 * Current affected providers
	 */
	private ServiceProvider affectedProviders[] = new ServiceProvider[2];
	
	/***
	 * Sets first and second loss chance and how often it should switch between them
	 */
	public void setMessageLossChances(int chanceOne, int chanceTwo, int switchEvery){
		if(chanceOne >= 0 && chanceTwo >= 0){
			currentMsgLoss = chanceOne;
			messageLossChanceOne = chanceOne;
			messageLossChanceTwo = chanceTwo;
			this.switchEvery = switchEvery;
			lossCounter = 0;
			switchlossCounter = 0;
		}
	}
	
	/***
	 * Set the behavior to affect between two provider
	 */
	@Override
	public void setAffects(ServiceProvider from, ServiceProvider to){
		affectedProviders[0] = from;
		affectedProviders[1] = to;
	}
	
	/***
	 * Set if behavior affects all
	 */
	@Override
	public void setAffects(boolean all){
		affectsAll = all;
	}
	
	/***
	 * Returns true if the behavior works between set providers or affects all
	 */
	@Override
	public boolean isRelevant(ServiceProvider one, ServiceProvider two) {
		if(affectsAll){
			return true;
		}
		if(affectedProviders[0] == null ||
		   affectedProviders[1] == null ){
			return false;
		}
		//Get providers set in behavior
		ActiveMQProvider p1 = (ActiveMQProvider) affectedProviders[0];
		ActiveMQProvider p2 = (ActiveMQProvider) affectedProviders[1];
		
		ActiveMQProvider p3 = (ActiveMQProvider) one;
		ActiveMQProvider p4 = (ActiveMQProvider) two;
		
		//Checks if the behavior should be used, if it affects all or between the set providers
		if(affectsAll ||				
				p1.getNumber().equals(p3.getNumber()) && p2.getNumber().equals(p4.getNumber()) ||
				p1.getNumber().equals(p4.getNumber()) && p2.getNumber().equals(p3.getNumber())){
			return true;
		}
		return false;
	}

	/***
	 * Executes the behavior and calculates message loss and switch
	 */
	@Override
	public void enforceBehaviour(ServiceProvider from, ServiceProvider to) {
		//if it is relevant
		if(isRelevant(from, to)){
			if(switchlossCounter >= switchEvery){
				currentMsgLoss = (currentMsgLoss == messageLossChanceOne ? messageLossChanceTwo : messageLossChanceOne);
				switchlossCounter = 1;
			}
			else{
				switchlossCounter++;
			}
			
			if(currentMsgLoss != 0 && lossCounter >= currentMsgLoss){
				Channel.looseNextMessage = true;
				lossCounter = 1;
			}
			else
				lossCounter++;
		}
	}

}
