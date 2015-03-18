package src;

import java.util.ArrayList;


/***
 * A Channel connecting the devices (providers and server)
 * Can be manipulated with behaviors
 * Is static so all devices use the same channel
 * @author eh222mk, js222xt
 */
public abstract class Channel {

	//List of behaviors
	public static ArrayList<Behaviour> _currentBehaviours = new ArrayList<Behaviour>();
	//Field keeping track if a message is to be lost and if it was lost
	public static boolean looseNextMessage = false;
	public static boolean lostMessage = false;
	
	/***
	 * sends a message forward to the server unless behavior tells otherwise
	 * @param message
	 * @param endPoint
	 * @param from
	 */
	public static void sendMessageToServer(String message, String endPoint, String from){
		ActiveMQProvider toPro = Server.getProvider(endPoint);
		ActiveMQProvider fromPro = Server.getProvider(from);
		checkBehaviours(toPro,fromPro);
		
		if(!looseNextMessage){
			Server.sendMessage(message, endPoint, from);
		}
		else{
			System.out.println("Lost message");
			looseNextMessage = false;
		}
	}
	
	/***
	 * Checks the behaviors and tries to enforce them 
	 * @param provider
	 * @param fromProvider
	 */
	private static void checkBehaviours(ActiveMQProvider provider, ActiveMQProvider fromProvider) {
		for (Behaviour behaviour : _currentBehaviours) {
			behaviour.enforceBehaviour(provider, fromProvider);
		}
		
	}

	/***
	 * Adds a new behavior
	 * @param be
	 */
	public static void addBehaviour(Behaviour be){
		_currentBehaviours.add(be);
	}

	/***
	 * Deletes all behaviors
	 */
	public static void clearBehaviours(){
		_currentBehaviours.clear();
	}
	
	/***
	 * Sends a message forward to a specific provider unless it is lost
	 * @param message
	 * @param provider
	 * @param fromProvider
	 */
	public static void sendMessageToProvider(String message, ActiveMQProvider provider, ActiveMQProvider fromProvider) {
		checkBehaviours(provider,fromProvider);
		
		if(!looseNextMessage){
			provider.receiveMessage(message);
			lostMessage = false;
		}
		else{
			System.out.println("Lost message");
			looseNextMessage = false;
			lostMessage = true;
		}
	}
	
	/***
	 * Passes a request to the server to remove a provider
	 * @param _number
	 */
	public static void removeProvider(String _number) {
		Server.removeProvider(_number);
		
	}

	/***
	 * Tells a provider it can start listening
	 * @param provider
	 * @param valueOf
	 * @param messageView
	 */
	public static void startListening(ActiveMQProvider provider, String valueOf, MessageView messageView) {
		provider.startListening(valueOf, messageView);
	}
}
