package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import src.ActiveMQProvider;
import src.Behaviour;
import src.Channel;
import src.DelayBehaviour;
import src.MessageLossBehaviour;
import src.MessageReceiver;
import src.MessageView;
import src.Server;
import src.ServiceProviderFactory;

/***
 * Testing the channel
 * @author eh222mk, js222xt
 * NOTE: Testing the channel required Server and serviceproviders to work
 */
public class ChannelTest {
	
	/***
	 * Tests adding behaviours to the channel
	 */
	@Test
	public void testAddBehaviour(){
		Behaviour dbe = new DelayBehaviour();
		Behaviour mbe = new MessageLossBehaviour();
		
		//add first behaviour
		Channel.addBehaviour(dbe);
		assertEquals(Channel._currentBehaviours.size(), 1);
		assertEquals(Channel._currentBehaviours.get(0), dbe);
		
		//add second behaviour
		Channel.addBehaviour(mbe);
		assertEquals(Channel._currentBehaviours.size(), 2);
		assertEquals(Channel._currentBehaviours.get(1), mbe);
	}	
	
	/***
	 * Tests sending a message to a provider through channel
	 */
	@Test
	public void testSendMessageToProvider(){
		ActiveMQProvider p1 = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		ActiveMQProvider p2 = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		
		p1 = Server.registerNewProvider(p1);
		p2 = Server.registerNewProvider(p2);
		
		Channel.sendMessageToProvider("Testing", p1, p2);
	}
	
	/***
	 * Tests if a message is lost when it is supposed to
	 */
	@Test
	public void testSendMessageToProviderWithMessageloss(){
		Channel.clearBehaviours();
		ActiveMQProvider p1 = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		ActiveMQProvider p2 = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		
		p1 = Server.registerNewProvider(p1);
		p2 = Server.registerNewProvider(p2);
		
		MessageLossBehaviour be = new MessageLossBehaviour();
		be.setAffects(p1, p2);
		be.setMessageLossChances(1,1,1);
		Channel.addBehaviour(be);
		assertTrue(be.isRelevant(p1, p2));
		
		Channel.sendMessageToProvider("Testing", p1, p2);
		assertFalse(Channel.lostMessage);
		Channel.sendMessageToProvider("Testing again", p2, p1);
		assertTrue(Channel.lostMessage);
	}
	
	/***
	 * Tests making a provider starts listening
	 */
	@Test
	public void testStartListening(){
		ActiveMQProvider p1 = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		
		//Makes p1 listen with manual information going through the channel
		
		String testString = "21";
		Channel.startListening(p1, testString, new MessageView());
		
		assertEquals(p1.getNumber(), testString);
		p1.stopListening();
		
		testString = "test";
		Channel.startListening(p1, testString, new MessageView());
		assertEquals(p1.getNumber(), testString);
	}
	
	/***
	 * Tests removing a provider
	 */
	@Test
	public void testRemoveProvider(){
		//Creates a new provider and registers
		Server.clearAllProviders();
		ActiveMQProvider p1 = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		p1 = Server.registerNewProvider(p1);
		
		//tests so provider exists in server
		ArrayList<ActiveMQProvider> ap = Server.getActiveProviders();
		assertNotEquals(ap.size(), 0);
		assertEquals(p1.getNumber(), ap.get(0).getNumber());
		
		//Removing p1 from channel
		Channel.removeProvider(p1.getNumber());
		
		ArrayList<ActiveMQProvider> apRemoved = Server.getActiveProviders();
		assertEquals(apRemoved.size(), 0);		
		
	}
	
}
