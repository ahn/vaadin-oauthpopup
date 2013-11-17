package org.vaadin.addon.oauthpopup.demo;

import javax.servlet.annotation.WebServlet;

import org.scribe.builder.api.FacebookApi;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.builder.api.TwitterApi;
import org.vaadin.addon.oauthpopup.OAuthListener;
import org.vaadin.addon.oauthpopup.OAuthPopupButton;
import org.vaadin.addon.oauthpopup.buttons.FacebookButton;
import org.vaadin.addon.oauthpopup.buttons.LinkedInButton;
import org.vaadin.addon.oauthpopup.buttons.TwitterButton;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

@Push
@PreserveOnRefresh
@Theme("demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "org.vaadin.addon.oauthpopup.demo.DemoWidgetSet")
	public static class Servlet extends VaadinServlet {
	}
	
	// Twitter test application at http://localhost:8080
	private static final ApiInfo TWITTER_API = new ApiInfo("Twitter",
			TwitterApi.class,
			"31ssXGMU4WW6KPxWwT6IMQ",
			"FR3wJmGyGAdpQMxB3vMreED2UnsHVb6nPF16f1RrtU",
			"https://api.twitter.com/1.1/account/settings.json");
	
	// Facebook test application at http://localhost:8080
	private static final ApiInfo FACEBOOK_API = new ApiInfo("Facebook",
			FacebookApi.class,
			"170732353126405",
			"dd59293cda395bf38a88044c22937e7e",
			"https://graph.facebook.com/me");
	
	// LinkedIn test application at http://localhost:8080
	private static final ApiInfo LINKEDIN_API = new ApiInfo("LinkedIn",
			LinkedInApi.class,
			"bp0aa1rxk2re",
			"Q2Na42cZmVs3OWnI",
			"https://api.linkedin.com/v1/people/~");

	private final VerticalLayout layout = new VerticalLayout();

	@Override
	protected void init(VaadinRequest request) {

		layout.setMargin(true);
		layout.setSpacing(true);
		setContent(layout);

		addTwitterButton();
		addFacebookButton();
		addLinkedInButton();
		
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

	private void addButton(final ApiInfo service, OAuthPopupButton button) {

		// In most browsers "resizable" makes the popup
		// open in a new window, not in a tab.
		// You can also set size with eg. "resizable,width=400,height=300"
		button.setPopupWindowFeatures("resizable,width=400,height=300");

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
		public void authSuccessful(final String accessToken,
				final String accessTokenSecret) {
			hola.addComponent(new Label("Authorized."));
			Button testButton = new Button("Test " + service.name + " API");
			testButton.addStyleName(BaseTheme.BUTTON_LINK);
			hola.addComponent(testButton);
			testButton.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					GetTestComponent get = new GetTestComponent(service,
							accessToken, accessTokenSecret);
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
			hola.addComponent(new Label("Auth failed."));
		}
	}

}
