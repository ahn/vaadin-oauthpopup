package org.vaadin.addon.oauthpopup.buttons;

import org.vaadin.addon.oauthpopup.OAuthPopupButton;
import org.vaadin.addon.oauthpopup.OAuthPopupConfig;

import com.github.scribejava.apis.GitHubApi;
import com.vaadin.server.ClassResource;

@SuppressWarnings("serial")
public class GitHubButton extends OAuthPopupButton {
	
	public GitHubButton(String key, String secret) {
		super(GitHubApi.instance(), OAuthPopupConfig.getStandardOAuth20Config(key, secret));
		
		setIcon(new ClassResource("/org/vaadin/addon/oauthpopup/icons/github16.png"));
		setCaption("GitHub");
	}
}