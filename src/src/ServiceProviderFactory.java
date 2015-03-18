package src;


/**
 * This class helps to choose a service provider.
 * 
 * @author M. Usman Iftikhar & Yifan Ruan
 */
public class ServiceProviderFactory {
	
    /**
     * This method helps to choose a service provider from list of available service providers.
     * @return ServiceProvider
     */
    public static ServiceProvider createServiceProvider() {
    	return new ActiveMQProvider();
    }
}