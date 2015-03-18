package src;


/***
 * An Active serviceprovider, works as an user
 * @author eh222mk, js222xt
 *
 */
public class ActiveMQProvider implements ServiceProvider{

	//Fields
	/***
	 * Provider number/id
	 */
	private String _number;
	private MessageReceiver _messageReceiver;
	
	/***
	 * Starts listening to the number using the messageReceiver.
	 */
	@Override
	public void startListening(String endPoint, MessageReceiver messageReceiver) {
		_number = endPoint;
		_messageReceiver = messageReceiver;
	}

	/***
	 * Stops listening to messages
	 */
	@Override
	public void stopListening() {
		Channel.removeProvider(_number);		
	}

	/***
	 * Send a message to the server through the channel with a destination endpoint
	 */
	@Override
	public void sendMessage(String message, String destinationEndPoint) {
		Channel.sendMessageToServer(message, destinationEndPoint, _number);
		
	}
	
	/***
	 * Receives a message and presents it through the messagereceiver
	 * @param message
	 */
	public void receiveMessage(String message){
		_messageReceiver.onMessage(message);
	}
	
	/***
	 * Returns the number this device is set listening to
	 * @return
	 */
	public String getNumber(){
		return _number;
	}
}
