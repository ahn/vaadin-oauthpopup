package org.vaadin.addon.oauthpopup.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.GitHubApi;
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.apis.LinkedInApi;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.model.Token;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addon.oauthpopup.OAuthListener;
import org.vaadin.addon.oauthpopup.OAuthPopupButton;
import org.vaadin.addon.oauthpopup.OAuthPopupOpener;
import org.vaadin.addon.oauthpopup.buttons.GitHubButton;
import org.vaadin.addon.oauthpopup.buttons.GoogleButton;
import org.vaadin.addon.oauthpopup.buttons.LinkedInButton;
import org.vaadin.addon.oauthpopup.buttons.TwitterButton;

import java.io.IOException;

public class DemoLayout extends VerticalLayout {
	
	private static final long serialVersionUID = -5419208604938947038L;
	
	public DemoLayout() {
		setSpacing(true);

		addLinkedInButton();
		addGitHubButton();
		addGoogleButton();
		addTwitterButtons();
	}
	
	private void addTwitterButtons() {
		ApiInfo api = readClientSecrets("/client_secret.twitter.json",
				"Twitter",
				TwitterApi.instance(),
				"https://api.twitter.com/1.1/account/settings.json");
		if (api == null) return;
		OAuthPopupButton button = new TwitterButton(api.apiKey, api.apiSecret);
		addButton(api, button);
		final NativeButton b = new NativeButton("Another Twitter Auth Button");
		
		OAuthPopupOpener opener = new OAuthPopupOpener(TwitterApi.instance(), api.apiKey, api.apiSecret);
		opener.extend(b);
		opener.addOAuthListener(new OAuthListener() {
			@Override
			public void authSuccessful(Token token, boolean isOAuth20) {
				Notification.show("authSuccessful");
			}
			
			@Override
			public void authDenied(String reason) {
				Notification.show("authDenied");
			}
		});
		
		addComponent(b);
	}
	
	private void addLinkedInButton() {
		ApiInfo api = readClientSecrets("/client_secret.linkedin.json",
				"LinkedIn",
				LinkedInApi.instance(),
				"https://api.linkedin.com/v1/people/~?format=json");
		if (api == null) return;
		OAuthPopupButton button = new LinkedInButton(api.apiKey, api.apiSecret);
		addButton(api, button);
	}
	
	private void addGitHubButton() {
		ApiInfo api = readClientSecrets("/client_secret.github.json", 
				"GitHub", 
				GitHubApi.instance(), 
				"https://api.github.com/user");
		if (api == null) return;
		OAuthPopupButton button = new GitHubButton(api.apiKey, api.apiSecret);
		addButton(api, button);
	}
	
	private void addGoogleButton() {
		ApiInfo api = readClientSecrets("/client_secret.google.json", 
				"Google", 
				GoogleApi20.instance(), 
				"https://www.googleapis.com/plus/v1/people/me");
		/*api = new ApiInfo("Google", GoogleApi20.instance(), 
				"127486145149-q8or6g21t7hok8ngj83re7b1l06u22ff.apps.googleusercontent.com",
				"Oth69gnVeJOevAoGbYRIxygA",
				"https://www.googleapis.com/plus/v1/people/me");*/
		if (api == null) return;
		OAuthPopupButton button = new GoogleButton(api.apiKey, api.apiSecret, "https://www.googleapis.com/auth/plus.login");
		addButton(api, button);
	}
	
	// Client secrets are stored in a JSON file on the classpath in the following format:
	// { "client_id": "my client id", "client_secret": "my client secret" }
	private ApiInfo readClientSecrets(String resourcePath, String name, Object scribeApi, String getEndpoint) {
		ApiInfo api = null;
		if (getClass().getResource(resourcePath) != null) {
			try {
				JsonNode web = new ObjectMapper().readTree(getClass().getResourceAsStream(resourcePath));
				if (web != null) {
					api = new ApiInfo(name, scribeApi,
						web.get("client_id").asText(),
						web.get("client_secret").asText(),
						getEndpoint);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return api;
	}

	private void addButton(final ApiInfo service, OAuthPopupButton button) {

		// In most browsers "resizable" makes the popup
		// open in a new window, not in a tab.
		// You can also set size with eg. "resizable,width=400,height=300"
		button.setPopupWindowFeatures("resizable,width=600,height=500");
		button.setWidth("150px");
		
		HorizontalLayout hola = new HorizontalLayout();
		hola.setSpacing(true);
		hola.addComponent(button);

		addComponent(hola);

		button.addOAuthListener(new Listener(service, hola));
	}

	private class Listener implements OAuthListener {

		private final ApiInfo service;
		private final HorizontalLayout hola;
		
		private Listener(ApiInfo service, HorizontalLayout hola) {
			this.service = service;
			this.hola = hola;
		}

		@Override
		public void authSuccessful(final Token token, final boolean isOAuth20) {
			Label l = new Label("Authorized.");
			hola.addComponent(l);
			hola.setComponentAlignment(l, Alignment.MIDDLE_CENTER);
			
			Button testButton = new Button("Test " + service.name + " API");
			testButton.addStyleName(ValoTheme.BUTTON_LINK);
			hola.addComponent(testButton);
			hola.setComponentAlignment(testButton, Alignment.MIDDLE_CENTER);
			
			testButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7561258877089832115L;

				@Override
				public void buttonClick(ClickEvent event) {
					GetTestComponent get = new GetTestComponent(service, token);
					Window w = new Window(service.name, get);
					w.center();
					w.setWidth("75%");
					w.setHeight("75%");
					UI.getCurrent().addWindow(w);
				}
			});
		}
		
		@Override
		public void authDenied(String reason) {
			Label l = new Label("Auth failed.");
			hola.addComponent(l);
			hola.setComponentAlignment(l, Alignment.MIDDLE_CENTER);
		}
	}
}
