package src;


/***
 * Interface for behaviours
 * @author eh222mk, js222xt
 *
 */
public interface Behaviour {
	
	/***
	 * Set affected ServiceProviders
	 */
	public void setAffects(ServiceProvider from, ServiceProvider to);
	/***
	 * Set if all providers should be affected
	 */
	public void setAffects(boolean all);
	/***
	 * Run behaviour
	 */
	public void enforceBehaviour(ServiceProvider from, ServiceProvider to);
	/***
	 * If current providers is relevant
	 */
	boolean isRelevant(ServiceProvider one, ServiceProvider two);
}
