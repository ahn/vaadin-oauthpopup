package org.vaadin.addon.oauthpopup.demo;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
class GetTestComponent extends Panel {
	
	private final VerticalLayout layout = new VerticalLayout();
	private final Token accessToken;
	private final ApiInfo service;
	private TextArea responseArea;
	
	GetTestComponent(ApiInfo service, String accessToken, String accessTokenSecret) {
		setSizeFull();
		
		setContent(layout);
		layout.setSizeFull();
		
		this.service = service;
		this.accessToken = new Token(accessToken, accessTokenSecret);
		
		layout.setMargin(true);
		final TextField field = new TextField("Request:", service.exampleGetRequest);
		field.setWidth("100%");
		layout.addComponent(field);
		
		Button bu = new Button("GET");
		layout.addComponent(bu);
		bu.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				sendGet(field.getValue());
			}
		});
		
		responseArea = new TextArea("Response:");
		responseArea.setSizeFull();
		layout.addComponent(responseArea);
		layout.setExpandRatio(responseArea, 1);
	}
	
	private void sendGet(String get) {
		OAuthRequest request = new OAuthRequest(Verb.GET, get);
		createOAuthService().signRequest(accessToken, request);
		Response resp = request.send();
		responseArea.setValue(resp.getBody());
		
	}
	
	private OAuthService createOAuthService() {
		ServiceBuilder sb = new ServiceBuilder();
		sb.provider(service.scribeApi);
		sb.apiKey(service.apiKey);
		sb.apiSecret(service.apiSecret);
		sb.callback("http://www.google.fi");
		return sb.build();
	}
}