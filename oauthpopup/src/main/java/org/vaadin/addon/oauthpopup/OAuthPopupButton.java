package org.vaadin.addon.oauthpopup;

import java.net.URI;
import java.util.LinkedList;

import org.scribe.builder.api.Api;

import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;

/**
 * A button that opens a OAuth authorization url in a separate browser window,
 * and lets the user to perform the authorization.
 * <p>
 * The success/failure of the authorization can be listened
 * with {@link #addOAuthListener(OAuthListener)}.
 * <p>
 * IMPORTANT: the callback comes from a different window, not from the usual
 * Vaadin server-visit thread. That's why the UI is not updated automatically,
 * UNLESS server push is enabled. So, it's good idea to enable @Push in the UI class.
 * <p>
 * This class may be subclassed for customization, or
 * used just by giving the Scribe Api class for the constructor.
 * The latter approach may not work for all Api's because
 * all the Apis don't work the same and some customization might
 * be necessary...
 * <p>
 * Available subclasses are at {@link org.vaadin.addon.oauthpop.buttons}.
 *
 */
@SuppressWarnings("serial")
// I guess we might have as well just inherit Button, not CustomComponent...
public class OAuthPopupButton extends CustomComponent {
	
	
	private final OAuthPopupOpener opener;
	
	private Button button;

	public OAuthPopupButton(Class<? extends Api> apiClass, String key, String secret) {
		this.opener = new OAuthPopupOpener(apiClass, key, secret);
		button = new Button();
		opener.extend(button);
		setCompositionRoot(button);
	}
	
	protected Button getButton() {
		return button;
	}
	
	/**
	 *  IMPORTANT: the listener call originates from a different window,
	 *  not from the usual Vaadin server-visit thread.
	 *  That's why the UI is not updated automatically, UNLESS server push is enabled.
	 *  So, it's good idea to enable @Push in the UI class.
	 */
	public void addOAuthListener(OAuthListener listener) {
		opener.addOAuthListener(listener);
	}
	
	/**
	 *  IMPORTANT: the listener call originates from a different window,
	 *  not from the usual Vaadin server-visit thread.
	 *  That's why the UI is not updated automatically, UNLESS server push is enabled.
	 *  So, it's good idea to enable @Push in the UI class.
	 */
	public void removeListener(OAuthListener listener) {
		opener.removeOAuthListener(listener);
	}
	
	@Override
	public void setCaption(String caption) {
		button.setCaption(caption);
	}
	
	@Override
	public void setDescription(String description) {
		button.setDescription(description);
	}
	
	@Override
	public void setIcon(Resource icon) {
		button.setIcon(icon);
	}
	
	/**
	 * Comma-separated list of features given to the BrowserWindowOpener.
	 * <p>
	 * See here for feature names: https://vaadin.com/book/vaadin7/-/page/advanced.html
	 * 
	 */
	public void setPopupWindowFeatures(String features) {
		opener.setFeatures(features);
	}
	
	/**
	 * Sets the callback URI for the OAuth.
	 * <p>
	 * Must be called before user opens the popup to have effect.
	 * <p>
	 * Default: see {@link #setCallbackToDefault()}
	 * 
	 */
	public void setCallback(String callback) {
		opener.setCallback(callback);
	}
	
	/**
	 * Sets the callback URI to default.
	 * <p>
	 * The default callback is constructed from current Page location:
	 *  SCHEME + "://" + AUTHORITY + PATH
	 * 	
	 */
	public void setCallbackToDefault() {
		opener.setCallbackToDefault();
	}
	
	public void setScope(String scope) {
		opener.setScope(scope);
	}
	
}
