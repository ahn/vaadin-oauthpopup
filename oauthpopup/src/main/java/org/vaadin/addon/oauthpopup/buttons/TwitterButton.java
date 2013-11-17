package org.vaadin.addon.oauthpopup.buttons;

import org.scribe.builder.api.TwitterApi;
import org.vaadin.addon.oauthpopup.OAuthPopupButton;

import com.vaadin.server.ClassResource;

@SuppressWarnings("serial")
public class TwitterButton extends OAuthPopupButton {
	
	public TwitterButton(String key, String secret) {
		super(TwitterApi.class, key, secret);
		
		setIcon(new ClassResource("/org/vaadin/addon/oauthpopup/icons/twitter16.png"));
		setCaption("Twitter");
	}

}
