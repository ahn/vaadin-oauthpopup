package org.vaadin.addon.oauthpopup.buttons;

import org.vaadin.addon.oauthpopup.OAuthPopupButton;
import org.vaadin.addon.oauthpopup.OAuthPopupConfig;

import com.github.scribejava.apis.FacebookApi;
import com.vaadin.server.ClassResource;

@SuppressWarnings("serial")
public class FacebookButton extends OAuthPopupButton {
	
	public FacebookButton(String key, String secret) {
		super(FacebookApi.instance(), OAuthPopupConfig.getStandardOAuth20Config(key, secret));
		
		setIcon(new ClassResource("/org/vaadin/addon/oauthpopup/icons/facebook16.png"));
		setCaption("Facebook");
	}
}