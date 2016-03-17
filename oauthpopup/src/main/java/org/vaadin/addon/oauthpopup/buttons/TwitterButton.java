package org.vaadin.addon.oauthpopup.buttons;

import org.vaadin.addon.oauthpopup.OAuthPopupButton;
import org.vaadin.addon.oauthpopup.OAuthPopupConfig;

import com.github.scribejava.apis.TwitterApi;
import com.vaadin.server.ClassResource;

@SuppressWarnings("serial")
public class TwitterButton extends OAuthPopupButton {
	
	public TwitterButton(String key, String secret) {
		super(TwitterApi.instance(), OAuthPopupConfig.getStandardOAuth10aConfig(key, secret));
		
		setIcon(new ClassResource("/org/vaadin/addon/oauthpopup/icons/twitter16.png"));
		setCaption("Twitter");
	}

}
