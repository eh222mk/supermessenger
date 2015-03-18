package tests;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.Test;

import src.ActiveMQProvider;
import src.Server;
import src.ServiceProviderFactory;
/***
* @author eh222mk, js222xt
*/
public class ServerTests {

	/***
	 * See if a provider can be registered
	 */
	@Test 
	public void registerNewProviderTest(){
		ActiveMQProvider provider = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		ActiveMQProvider provider1 = Server.registerNewProvider(provider);
		assertEquals(provider, provider1);
		assertEquals(String.valueOf(Integer.valueOf(provider.getNumber()) +1) , Server.gererateNewNumber());
	}
	
	/***
	 * See if a provider can be removed from the server
	 */
	@Test 
	public void removeProviderTest(){
		Server.clearAllProviders();
		ActiveMQProvider provider1 = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		ActiveMQProvider provider2 = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		
		Server.registerNewProvider(provider1);
		Server.registerNewProvider(provider2);
		
		Server.removeProvider(provider1.getNumber());
		ArrayList<ActiveMQProvider> apRemoved = Server.getActiveProviders();
		assertEquals(apRemoved.size(), 1);		
	}
	
	/***
	 * See if numbers are generated correctly
	 */
	@Test
	public void gererateNewNumberTest(){
		Server.clearAllProviders();
		String first = Server.gererateNewNumber();
		
		ActiveMQProvider provider1 = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		ActiveMQProvider provider2 = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		
		Server.registerNewProvider(provider1);
		Server.registerNewProvider(provider2);
		
		assertEquals(String.valueOf((Integer.valueOf(first) + 1)), provider1.getNumber());
		assertEquals(String.valueOf((Integer.valueOf(first) + 2)), provider2.getNumber());
	}

	/***
	 * See if getProvider works
	 */
	@Test
	public void getProviderTest(){
		ActiveMQProvider provider = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		Server.registerNewProvider(provider);
		assertEquals(provider, Server.getProvider(provider.getNumber()));
	}
}
