package org.vaadin.addon.oauthpopup.buttons;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;

/**
 * 
 * TODO: this should be part of the Scribe package
 *
 */
public class GitHubApi extends DefaultApi20 {

	private static final String ACCESS_TOKEN_URL =
			"https://github.com/login/oauth/access_token";
	private static final String AUTHORIZE_URL_FORMAT =
			"https://github.com/login/oauth/authorize?client_id=%s";
	
	@Override
	public String getAccessTokenEndpoint() {
		return ACCESS_TOKEN_URL;
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		String url = String.format(AUTHORIZE_URL_FORMAT, config.getApiKey());
		String callback = config.getCallback();
		if (callback!=null) {
			url += "&redirect_uri="+callback;
		}
		String scope = config.getScope();
		if (scope!=null) {
			url += "&scope="+scope;
		}
		return url;
	}

}
