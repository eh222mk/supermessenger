package src;

/***
 * A view which Implements MessageReceiver
 * @author eh222mk, js222xt
 *
 */
public class MessageView implements MessageReceiver {

	/***
	 * Prints out a message
	 */
	@Override
	public void onMessage(String message) {
		System.out.println(message);
	}

}
