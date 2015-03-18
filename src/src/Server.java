package src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/***
 * Static server class. Keeps track of the active providers and routes messages to the correct receiver
 * @author eh222mk, js222xt
 *
 */
public abstract class Server {

	//Map with the active providers, the key is the "number" the providers received
	private static Map<String,ActiveMQProvider> _activeMqProviders = new HashMap<String,ActiveMQProvider>();
	private static int ids = 0;
	
	/***
	 * Creates a key for the provider, registers it on the server and tells it that it can start listening for messages
	 * @param provider to be registered
	 * @return the registered provider
	 */
	public static ActiveMQProvider registerNewProvider(ActiveMQProvider provider){
		String number = gererateNewNumber();
		_activeMqProviders.put(number, provider);
		Channel.startListening(provider,String.valueOf(number), new MessageView());
		return provider;
	}
	
	/***
	 * removes a providers from list of active providers
	 * @param number to the provider (key)
	 */
	public static void removeProvider(String number){
		_activeMqProviders.remove(number);
	}

	/***
	 * Generates a number for a new provider
	 * @return a number as a string
	 */
	public static String gererateNewNumber() {
		return String.valueOf(ids++);
	}

	/***
	 * Takes a message and routes it to the correct receiver
	 * @param message
	 * @param endPoint
	 * @param from
	 */
	public static void sendMessage(String message, String endPoint, String from) {
		ActiveMQProvider provider = _activeMqProviders.get(endPoint);
		ActiveMQProvider fromProvider = _activeMqProviders.get(from);
		Channel.sendMessageToProvider(message, provider, fromProvider);
	}

	/***
	 * Returns a specific active provider
	 * @param endPoint
	 * @return ActiveMQProvider
	 */
	public static ActiveMQProvider getProvider(String endPoint) {
		return _activeMqProviders.get(endPoint);
		
	}
	
	/***
	 * Returns all providers, used in testing.
	 * @return Map with providers
	 */
	public static ArrayList<ActiveMQProvider> getActiveProviders(){
		ArrayList<ActiveMQProvider> providerList = new ArrayList();
		
		Collection<ActiveMQProvider> coll = _activeMqProviders.values();
		
		Iterator<ActiveMQProvider> it = coll.iterator();
		
		while(it.hasNext()){
			providerList.add(it.next());
		}
				
		return providerList;
	}
	
	/***
	 * Clear providers
	 */
	public static void clearAllProviders(){
		_activeMqProviders.clear();
	}
}
