package org.vaadin.addon.oauthpopup;

/**
 * Called on OAuth success/failure.
 * <p>
 * In the case that the user doesn't browse back to our application from
 * the authorization url, neither of the methods is called.
 *
 */
public interface OAuthListener {
	
	/**
	 * Called on successful OAuth. 
	 * 
	 * @param accessToken the OAuth access token
	 * @param accessTokenSecret the OAuth access token secret
	 * @param oauthRawResponse the raw response from OAuth2 server. It can contain some useful extended information such as user-id or e-mail.
	 */
	public void authSuccessful(String accessToken, String accessTokenSecret, String oauthRawResponse);
	
	/**
	 * Called when the OAuth was denied.
	 * 
	 * @param reason TODO: the reasons currently returned are not helpful.
	 */
	public void authDenied(String reason);
}