package org.vaadin.addon.oauthpopup;

import com.github.scribejava.core.model.OAuth1RequestToken;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * UI that redirects the user to OAuth authorization url.
 * <p>
 * Should always be opened by {@link OAuthPopupButton}.
 * <p>
 * Reads the {@link OAuthData} instance from session.
 * The name of the session variable must be given as
 * URI parameter named "data".
 */
@SuppressWarnings("serial")
public class OAuthPopupUI extends UI {

	public static final String DATA_PARAM_NAME = "data";
	
	private OAuthCallbackRequestHandler callbackHandler;
	
	@Override
	protected void init(VaadinRequest request) {
		
		String attr;
		OAuthData data;
		OAuth1RequestToken requestToken = null;
		if ((attr=request.getParameter(DATA_PARAM_NAME)) == null) {
			throw new IllegalStateException(
					String.format("No URI parameter named \"%s\".\n", DATA_PARAM_NAME) +
					"Please use OAuthPopupButton or a subclass to open OAuthPopup.");
		} else if ((data = (OAuthData) getSession().getAttribute(attr)) == null) {
			throw new IllegalStateException(
					String.format("No session attribute named \"%s\" found.\n", attr) +
					"Please use OAuthPopupButton or a subclass to open OAuthPopup.");
		} else if (!data.isOAuth20()) {
			requestToken = data.createNewRequestToken();
		}
		
		addCallbackHandler(requestToken, data);
		goToAuthorizationUrl(requestToken, data);
	}
	
	@Override
	public void detach() {
		super.detach();
				
		// The session may have been already cleaned up by requestHandler,
		// not always though.
		// Doing it again doesn't do harm (?).
		callbackHandler.cleanUpSession(getSession());
	}
	
	private void addCallbackHandler(OAuth1RequestToken requestToken, OAuthData data) {
		callbackHandler = new OAuthCallbackRequestHandler(requestToken, data);
		getSession().addRequestHandler(callbackHandler);
	}
	
	public void removeCallbackHandler() {
		if (callbackHandler!=null) {
			getSession().removeRequestHandler(callbackHandler);
			callbackHandler = null;
		}
	}
	
	private void goToAuthorizationUrl(OAuth1RequestToken requestToken, OAuthData data) {
		String authUrl = data.getAuthorizationUrl(requestToken);
		//System.out.println("Navigating to authorization URL: " + authUrl);
		Page.getCurrent().setLocation(authUrl);
	}
}
