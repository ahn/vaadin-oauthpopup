package org.vaadin.addon.oauthpopup.buttons;

import org.scribe.builder.api.FacebookApi;
import org.vaadin.addon.oauthpopup.OAuthPopupButton;

import com.vaadin.server.ClassResource;

@SuppressWarnings("serial")
public class FacebookButton extends OAuthPopupButton {
	
	public FacebookButton(String key, String secret) {
		super(FacebookApi.class, key, secret);
		
		setIcon(new ClassResource("/org/vaadin/addon/oauthpopup/icons/facebook16.png"));
		setCaption("Facebook");
	}

}
