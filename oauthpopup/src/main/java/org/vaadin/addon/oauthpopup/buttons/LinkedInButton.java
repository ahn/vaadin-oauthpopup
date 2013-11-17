package org.vaadin.addon.oauthpopup.buttons;

import org.scribe.builder.api.LinkedInApi;
import org.vaadin.addon.oauthpopup.OAuthPopupButton;

import com.vaadin.server.ClassResource;

@SuppressWarnings("serial")
public class LinkedInButton extends OAuthPopupButton {
	
	public LinkedInButton(String key, String secret) {
		super(LinkedInApi.class, key, secret);
		
		setIcon(new ClassResource("/org/vaadin/addon/oauthpopup/icons/linkedin16.png"));
		setCaption("LinkedIn");
	}
}
