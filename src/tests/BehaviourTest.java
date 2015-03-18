package tests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Before;
import org.junit.Test;
import src.ActiveMQProvider;
import src.Channel;
import src.DelayBehaviour;
import src.MessageLossBehaviour;
import src.Server;
import src.ServiceProviderFactory;

/***
* @author eh222mk, js222xt
*/
public class BehaviourTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	}
	
	@Test
	public void affectedTest(){
		
		ActiveMQProvider one = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		ActiveMQProvider two = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		
		Server.registerNewProvider(one);
		Server.registerNewProvider(two);
		
		DelayBehaviour b = new DelayBehaviour();
		MessageLossBehaviour mb = new MessageLossBehaviour();
		
		assertEquals(false, b.isRelevant(one, two));
		assertEquals(false, mb.isRelevant(one, two));
		
		b.setAffects(true);
		mb.setAffects(true);
		
		assertEquals(true, b.isRelevant(one, two));
		assertEquals(true, mb.isRelevant(one, two));
		
		b.setAffects(false);
		mb.setAffects(false);
		
		assertEquals(false, b.isRelevant(one, two));
		assertEquals(false, mb.isRelevant(one, two));
		
		b.setAffects(one, two);
		mb.setAffects(one, two);
		
		assertEquals(true, b.isRelevant(one, two));
		assertEquals(true, mb.isRelevant(one, two));
		
	}
	
	@Test
	public void messageLossTest(){
		ActiveMQProvider one = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		ActiveMQProvider two = (ActiveMQProvider) ServiceProviderFactory.createServiceProvider();
		
		Server.registerNewProvider(one);
		Server.registerNewProvider(two);
	
		MessageLossBehaviour mb = new MessageLossBehaviour();
		Channel.addBehaviour(mb);
		
		mb.setAffects(true);
		
		mb.setMessageLossChances(1,1,1);
		
		one.sendMessage("test1", "2");		
		
		assertEquals(outContent.toString(), "Lost message\r\n");
		outContent.reset();
		
		mb.setMessageLossChances(3,3,3);
		
		one.sendMessage("test1", "1");
		one.sendMessage("test2", "1");
		one.sendMessage("test3", "1");
		
		assertEquals(outContent.toString(), "test1\r\nLost message\r\ntest3\r\n");
		outContent.reset();
		
		mb.setAffects(false);
		
		one.sendMessage("test1", "1");
		one.sendMessage("test2", "1");
		one.sendMessage("test3", "1");
		
		assertEquals(outContent.toString(), "test1\r\ntest2\r\ntest3\r\n");
		outContent.reset();
		
		mb.setAffects(one, two);
		mb.setMessageLossChances(3,3,3);
		
		one.sendMessage("test1", "1");
		one.sendMessage("test2", "1");
		one.sendMessage("test3", "1");
		
		assertEquals(outContent.toString(), "test1\r\nLost message\r\ntest3\r\n");
		outContent.reset();
		
		mb.setMessageLossChances(1,3,2);
		
		one.sendMessage("test1", "1");
		one.sendMessage("test2", "1");
		one.sendMessage("test3", "1");
		one.sendMessage("test4", "1");
		one.sendMessage("test5", "1");
		one.sendMessage("test6", "1");
		one.sendMessage("test7", "1");
		one.sendMessage("test8", "1");
		
		assertEquals(outContent.toString(), "Lost message\r\ntest2\r\nLost message\r\nLost message\r\ntest5\r\nLost message\r\nLost message\r\ntest8\r\n");
		outContent.reset();
		
	}

}
