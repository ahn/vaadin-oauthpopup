package org.vaadin.addon.oauthpopup.demo;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Push
@PreserveOnRefresh
@Theme("valo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
	public static class Servlet extends VaadinServlet {  }

	private VerticalLayout layout;
	
	@Override
	protected void init(VaadinRequest request) {
		layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		setContent(layout);
		
		layout.addComponent(new DemoLayout());
		layout.addComponent(new Link("Add-on at Vaadin Directory", new ExternalResource("https://vaadin.com/directory#!addon/oauth2-popup-add-on")));
		layout.addComponent(new Link("Source code at GitHub", new ExternalResource("https://github.com/bdunn44/vaadin-oauthpopup")));
	}
}
