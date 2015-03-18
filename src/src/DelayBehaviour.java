package src;


/***
 * Behavior for creating delay
 * @author eh222mk, js222xt
 *
 */
public class DelayBehaviour implements Behaviour {

	//Fields
	/***
	 * Delay time
	 */
	private int delayTime;
	/***
	 * If all providers should be affected
	 */
	private boolean affectsAll = false;
	/***
	 * Current affected providers
	 */
	private ServiceProvider affectedProviders[] = new ServiceProvider[2];
	
	
	/***
	 * Set delay time
	 */
	public void setDelay(int time){
		delayTime = time;
	}
	
	/***
	 * Set the behavior to affect between two provider
	 */
	public void setAffects(ServiceProvider from, ServiceProvider to){
		affectedProviders[0] = from;
		affectedProviders[1] = to;
	}
	
	/***
	 * Set if behavior affects all providers
	 */
	public void setAffects(boolean all){
		affectsAll = all;
	}
	
	/***
	 * Executes the behavior
	 */
	@Override
	public void enforceBehaviour(ServiceProvider from, ServiceProvider to) {
		if(isRelevant(from, to)){
			try {
				Thread.sleep(delayTime);
			} catch (InterruptedException e) {
			}
		}
	}

	/***
	 * Returns true if the behavior works between set providers or affects all
	 */
	@Override
	public boolean isRelevant(ServiceProvider one, ServiceProvider two){
		if(affectsAll){
			return true;
		}
		if(affectedProviders[0] == null ||
		   affectedProviders[1] == null ){
			return false;
		}
		ActiveMQProvider p1 = (ActiveMQProvider) affectedProviders[0];
		ActiveMQProvider p2 = (ActiveMQProvider) affectedProviders[1];
		
		ActiveMQProvider p3 = (ActiveMQProvider) one;
		ActiveMQProvider p4 = (ActiveMQProvider) two;
		
		if(affectsAll ||				
				p1.getNumber().equals(p3.getNumber()) && p2.getNumber().equals(p4.getNumber()) ||
				p1.getNumber().equals(p4.getNumber()) && p2.getNumber().equals(p3.getNumber())){
			return true;
		}
		return false;
	}

	
	
}
