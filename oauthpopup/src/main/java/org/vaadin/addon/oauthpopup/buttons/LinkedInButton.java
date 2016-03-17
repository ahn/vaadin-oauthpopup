package org.vaadin.addon.oauthpopup.buttons;

import org.vaadin.addon.oauthpopup.OAuthPopupButton;
import org.vaadin.addon.oauthpopup.OAuthPopupConfig;

import com.github.scribejava.apis.LinkedInApi;
import com.vaadin.server.ClassResource;

@SuppressWarnings("serial")
public class LinkedInButton extends OAuthPopupButton {
	
	public LinkedInButton(String key, String secret) {
		super(LinkedInApi.instance(), OAuthPopupConfig.getStandardOAuth10aConfig(key, secret));
		
		setIcon(new ClassResource("/org/vaadin/addon/oauthpopup/icons/linkedin16.png"));
		setCaption("LinkedIn");
	}
}