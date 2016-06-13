package org.vaadin.addon.oauthpopup.demo;

import javax.servlet.annotation.WebServlet;

import org.vaadin.addon.oauthpopup.OAuthListener;
import org.vaadin.addon.oauthpopup.OAuthPopupButton;
import org.vaadin.addon.oauthpopup.OAuthPopupOpener;
import org.vaadin.addon.oauthpopup.buttons.FacebookButton;
import org.vaadin.addon.oauthpopup.buttons.GitHubButton;
import org.vaadin.addon.oauthpopup.buttons.GoogleButton;
import org.vaadin.addon.oauthpopup.buttons.LinkedInButton;
import org.vaadin.addon.oauthpopup.buttons.TwitterButton;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.apis.GitHubApi;
import com.github.scribejava.apis.LinkedInApi;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.model.Token;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

@Push
@PreserveOnRefresh
@Theme("valo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "org.vaadin.addon.oauthpopup.demo.DemoWidgetset")
	public static class Servlet extends VaadinServlet {  }
	
	private static final ApiInfo TWITTER_API = new ApiInfo("Twitter",
			TwitterApi.instance(),
			"31ssXGMU4WW6KPxWwT6IMQ",
			"FR3wJmGyGAdpQMxB3vMreED2UnsHVb6nPF16f1RrtU",
			"https://api.twitter.com/1.1/account/settings.json");
	
	private static final ApiInfo FACEBOOK_API = new ApiInfo("Facebook",
			FacebookApi.instance(),
			"170732353126405",
			"dd59293cda395bf38a88044c22937e7e",
			"https://graph.facebook.com/me");
	
	private static final ApiInfo LINKEDIN_API = new ApiInfo("LinkedIn",
			LinkedInApi.instance(),
			"bp0aa1rxk2re",
			"Q2Na42cZmVs3OWnI",
			"https://api.linkedin.com/v1/people/~");
	
	private static final ApiInfo GITHUB_API = new ApiInfo("GitHub",
			GitHubApi.instance(),
			"97a7e251c538106e7922",
			"6a36b0992e5e2b00a38c44c21a6e0dc8ae01d83b",
			"https://api.github.com/user");
	
	private static final ApiInfo GOOGLE_API = new ApiInfo("Google",
			GitHubApi.instance(),
			"127486145149-2e3cdqvuhq9b0iheesevhcp8vona5hug.apps.googleusercontent.com",
			"PKJiIhj68V-uo-l9DOooRNL7",
			"https://www.googleapis.com/plus/v1/people/me");

	private final VerticalLayout layout = new VerticalLayout();

	@Override
	protected void init(VaadinRequest request) {

		layout.setMargin(true);
		layout.setSpacing(true);
		setContent(layout);
		
		addTwitterButton();
		addFacebookButton();
		addLinkedInButton();
		addGitHubButton();
		addGoogleButton();
		
		addTwitterNativeButton();
		
		layout.addComponent(new Link("Add-on at Vaadin Directory", new ExternalResource("http://vaadin.com/addon/oauth-popup-add-on")));
		layout.addComponent(new Link("Source code at GitHub", new ExternalResource("https://github.com/ahn/vaadin-oauthpopup")));
	}
	
	private void addTwitterButton() {
		ApiInfo api = TWITTER_API;
		OAuthPopupButton button = new TwitterButton(api.apiKey, api.apiSecret);
		addButton(api, button);
	}
	
	private void addFacebookButton() {
		ApiInfo api = FACEBOOK_API;
		OAuthPopupButton button = new FacebookButton(api.apiKey, api.apiSecret);
		addButton(api, button);
	}
	
	private void addLinkedInButton() {
		ApiInfo api = LINKEDIN_API;
		OAuthPopupButton button = new LinkedInButton(api.apiKey, api.apiSecret);
		addButton(api, button);
	}
	
	private void addGitHubButton() {
		ApiInfo api = GITHUB_API;
		OAuthPopupButton button = new GitHubButton(api.apiKey, api.apiSecret);
		addButton(api, button);
	}
	
	private void addGoogleButton() {
		ApiInfo api = GOOGLE_API;
		OAuthPopupButton button = new GoogleButton(api.apiKey, api.apiSecret, "https://www.googleapis.com/auth/plus.login");
		button.getOAuthPopupConfig().setCallbackUrl("urn:ietf:wg:oauth:2.0:oob");
		addButton(api, button);
	}
	
	private void addTwitterNativeButton() {
		final NativeButton b = new NativeButton("Another Twitter Auth Button");
		
		OAuthPopupOpener opener = new OAuthPopupOpener(TwitterApi.instance(), TWITTER_API.apiKey, TWITTER_API.apiSecret);
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
		
		layout.addComponent(b);
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

		layout.addComponent(hola);

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
			testButton.addStyleName(BaseTheme.BUTTON_LINK);
			hola.addComponent(testButton);
			hola.setComponentAlignment(testButton, Alignment.MIDDLE_CENTER);
			
			testButton.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					GetTestComponent get = new GetTestComponent(service, token);
					Window w = new Window(service.name, get);
					w.center();
					w.setWidth("75%");
					w.setHeight("75%");
					addWindow(w);
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
