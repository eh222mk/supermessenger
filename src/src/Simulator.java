package src;


/***
 * Simulation executing the program
 * Works with a main method
 * @author eh222mk, js222xt
 */
public class Simulator {
	
	/***
	 * Main method executing the program
	 * @param args
	 */
	public static void main(String[] args) {
	
		//Creating providers
		ActiveMQProvider p1 = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		ActiveMQProvider p2 = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		ActiveMQProvider p3 = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		ActiveMQProvider p4 = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		
		//Registering them at the server
		p1 = Server.registerNewProvider(p1);
		p2 = Server.registerNewProvider(p2);
		p3 = Server.registerNewProvider(p3);
		p4 = Server.registerNewProvider(p4);
		
		//Creates behaviours for the channel
		DelayBehaviour be1 = new DelayBehaviour();
		MessageLossBehaviour be2 = new MessageLossBehaviour();
		
		be1.setDelay(0);
		be1.setAffects(p1, p2);
		be2.setAffects(p1, p2);
		be2.setMessageLossChances(5,5,5);
		
		//Adds behaviours to channel
		Channel.addBehaviour(be1);
		Channel.addBehaviour(be2);
		
		//loops through the providers, sending messages
		for(int i = 0; i < 20; i++){
			p1.sendMessage("Hej"+i, p2.getNumber());
		}
		
	}
	
	
	
}
