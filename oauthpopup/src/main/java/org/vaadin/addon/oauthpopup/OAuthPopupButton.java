package org.vaadin.addon.oauthpopup;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.vaadin.ui.Button;

/**
 * <p>A button that opens a OAuth authorization url in a separate browser window,
 * and lets the user to perform the authorization.</p>
 * 
 * <p>The success/failure of the authorization can be listened
 * with {@link #addOAuthListener(OAuthListener)}.</p>
 * 
 * <p>IMPORTANT: the callback comes from a different window, not from the usual
 * Vaadin server-visit thread. That's why the UI is not updated automatically,
 * UNLESS server push is enabled. So, it's good idea to enable {@code @Push} in the UI class.</p>
 * 
 * <p>This class may be subclassed for customization, or used directly
 * by giving the ScribeJava API class and OAuth configuration for the constructor.</p>
 * 
 * <p>Pre-configured subclasses for a number of OAuth services are  available at 
 * {@link org.vaadin.addon.oauthpop.buttons}.</p>
 *
 */
public class OAuthPopupButton extends Button {
	
	private static final long serialVersionUID = -3227617699696740673L;

	private final OAuthPopupOpener opener;

	/**
	 * Create a new OAuth popup button for an OAuth 1.0a service.
	 * 
	 * @param api The ScribeJava OAuth 1.0a API singleton instance.
	 * @param apiKey The client API key for the OAuth service.
	 * @param apiSecret The client API secret for the OAuth service.
	 */
	public OAuthPopupButton(DefaultApi10a api, String apiKey, String apiSecret) {
		this(api, OAuthPopupConfig.getStandardOAuth10aConfig(apiKey, apiSecret));
	}
	
	/**
	 * Create a new OAuth popup button for an OAuth 1.0a service.
	 * 
	 * @param api The ScribeJava OAuth 1.0a API singleton instance.
	 * @param config OAuth configuration for the particular service.
	 */
	public OAuthPopupButton(DefaultApi10a api, OAuthPopupConfig config) {
		this.opener = new OAuthPopupOpener(api, config);
		opener.extend(this);
	}
	
	/**
	 * Create a new OAuth popup button for an OAuth 2.0 service.
	 * 
	 * @param api The ScribeJava OAuth 2.0 API singleton instance.
	 * @param apiKey The client API key for the OAuth service.
	 * @param apiSecret The client API secret for the OAuth service.
	 */
	public OAuthPopupButton(DefaultApi20 api, String apiKey, String apiSecret) {
		this(api, OAuthPopupConfig.getStandardOAuth20Config(apiKey, apiSecret));
	}
	
	/**
	 * Create a new OAuth popup button for an OAuth 2.0 service.
	 * 
	 * @param api The ScribeJava OAuth 2.0 API singleton instance.
	 * @param config OAuth configuration for the particular service.
	 */
	public OAuthPopupButton(DefaultApi20 api, OAuthPopupConfig config) {
		this.opener = new OAuthPopupOpener(api, config);
		opener.extend(this);
	}
	
	/**
	 * Retrives the OAuth configuration in use by this widget.
	 * 
	 * @return OAuth configuration
	 */
	public OAuthPopupConfig getOAuthPopupConfig() {
		return opener.getOAuthPopupConfig();
	}

	/**
	 *  IMPORTANT: listener events originate from a different window,
	 *  not from the usual Vaadin server-visit thread.
	 *  That's why the UI is not updated automatically, UNLESS server push is enabled.
	 *  So, it's good idea to enable {@code @Push} in the UI class.
	 *  
	 * @param listener The OAuth authorization event listener.
	 */
	public void addOAuthListener(OAuthListener listener) {
		opener.addOAuthListener(listener);
	}

	/**
	 *  IMPORTANT: listener events originate from a different window,
	 *  not from the usual Vaadin server-visit thread.
	 *  That's why the UI is not updated automatically, UNLESS server push is enabled.
	 *  So, it's good idea to enable {@code @Push} in the UI class.
	 *  
	 * @param listener The OAuth authorization event listener.
	 */
	public void removeListener(OAuthListener listener) {
		opener.removeOAuthListener(listener);
	}
	
	/**
	 * <p>Set the features given to the {@link BrowserWindowOpener}.</p>
	 * <p>See here for feature names: https://vaadin.com/book/vaadin7/-/page/advanced.html</p>
	 * 
	 * @param features Comma separated list of features.
	 */
	public void setPopupWindowFeatures(String features) {
		opener.setFeatures(features);
	}
}
