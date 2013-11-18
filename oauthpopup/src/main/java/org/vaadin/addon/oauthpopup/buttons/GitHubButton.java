package org.vaadin.addon.oauthpopup.buttons;

import org.vaadin.addon.oauthpopup.OAuthPopupButton;

import com.vaadin.server.ClassResource;

@SuppressWarnings("serial")
public class GitHubButton extends OAuthPopupButton {
	
	public GitHubButton(String key, String secret) {
		super(GitHubApi.class, key, secret);
		
		setIcon(new ClassResource("/org/vaadin/addon/oauthpopup/icons/github16.png"));
		setCaption("GitHub");
	}

}
