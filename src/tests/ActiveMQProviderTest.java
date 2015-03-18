package tests;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import src.ActiveMQProvider;
import src.MessageView;
import src.Server;
import src.ServiceProviderFactory;

/***
* @author eh222mk, js222xt
*/
public class ActiveMQProviderTest {
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	}
	

	/***
	 * See if a provider can start and stop listening
	 */
	@Test
	public void startStopListeningTest()
	{
		ActiveMQProvider pro = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		pro.startListening("3", new MessageView());
		assertEquals("3", pro.getNumber());
		pro.stopListening();
		Server.getProvider(pro.getNumber());
		assertEquals(Server.gererateNewNumber(), "0");
	}
	
	/***
	 * See if a provider can receive a message
	 */
	@Test
	public void sendAndRecieveMessageTest(){
		ActiveMQProvider pro = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		ActiveMQProvider pro2 = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		
		Server.registerNewProvider(pro);
		Server.registerNewProvider(pro2);
		
		pro.startListening("1", new MessageView());
		pro2.startListening("2", new MessageView());
		
		pro.sendMessage("test to 2", pro2.getNumber());
		
		assertEquals(outContent.toString(), "test to 2\r\n");
	}

}
