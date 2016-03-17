package org.vaadin.addon.oauthpopup;

import java.util.LinkedList;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.Token;
import com.vaadin.server.BrowserWindowOpener;

/**
 * Component extension that opens an OAuth authorization popup window when the extended
 * component is clicked.
 * 
 * @author Bryson Dunn
 *
 */
@SuppressWarnings("serial")
public class OAuthPopupOpener extends BrowserWindowOpener {
	
	private LinkedList<OAuthListener> listeners = new LinkedList<OAuthListener>();
	
	private final OAuthData data;
	private OAuthListener dataListener;
	
	/**
	 * Create a new OAuth popop opener for an OAuth 1.0a service.
	 * 
	 * @param api The ScribeJava OAuth 1.0a API singleton instance.
	 * @param config OAuth configuration for the particular service.
	 */
	public OAuthPopupOpener(DefaultApi10a api, OAuthPopupConfig config) {
		super(OAuthPopupUI.class);
		this.data = new OAuthData(api, config);
	}

	/**
	 * Create a new OAuth popop opener for an OAuth 2.0 service.
	 * 
	 * @param api The ScribeJava OAuth 2.0 API singleton instance.
	 * @param config OAuth configuration for the particular service.
	 */
	public OAuthPopupOpener(DefaultApi20 api, OAuthPopupConfig config) {
		super(OAuthPopupUI.class);
		this.data = new OAuthData(api, config);
	}

	/**
	 * Retrives the OAuth configuration in use by this widget.
	 * 
	 * @return OAuth configuration
	 */
	public OAuthPopupConfig getOAuthPopupConfig() {
		return data.getOAuthPopupConfig();
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
		listeners.add(listener);
	}

	/**
	 *  IMPORTANT: listener events originate from a different window,
	 *  not from the usual Vaadin server-visit thread.
	 *  That's why the UI is not updated automatically, UNLESS server push is enabled.
	 *  So, it's good idea to enable {@code @Push} in the UI class.
	 *  
	 * @param listener The OAuth authorization event listener.
	 */
	public void removeOAuthListener(OAuthListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public void attach() {
		super.attach();
		
		// Adding the session attribute.
		String attr = data.getSessionAttributeName();
		getSession().setAttribute(attr, data);
		setParameter(OAuthPopupUI.DATA_PARAM_NAME, attr);
		
		dataListener = new OAuthListener() {
			@Override
			public void authSuccessful(Token token, boolean isOAuth20) {
				fireAuthSuccessful(token, isOAuth20);
			}
			
			@Override
			public void authDenied(String reason) {
				fireAuthFailed(reason);
			}
		};
		data.addOAuthListener(dataListener);
	}
	
	@Override
	public void detach() {
		super.detach();
		
		data.removeOAuthListener(dataListener);
		
		// Deleting the session attribute.
		getSession().setAttribute(data.getSessionAttributeName(), null);
	}
	
	private void fireAuthSuccessful(final Token token, final boolean isOAuth20) {
		// Coming from different thread than the usual Vaadin server visit.
		// That's why we have to call access.
		// Doing this here so our listeners don't need to.
		getUI().access(new Runnable() {
			@Override
			public void run() {
				for (final OAuthListener li : listeners) {
					li.authSuccessful(token, isOAuth20);
				}
				getUI().push();
			}
		});
	}
	
	private void fireAuthFailed(final String reason) {
		getUI().access(new Runnable() {
			@Override
			public void run() {
				for (final OAuthListener li : listeners) {
					li.authDenied(reason);
				}
				getUI().push();
			}
		});
	}
}
