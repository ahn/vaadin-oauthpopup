package org.vaadin.addon.oauthpopup.buttons;

import org.vaadin.addon.oauthpopup.oauth10a.OAuth10aPopupButtonImpl;
import org.vaadin.addon.oauthpopup.oauth10a.OAuth10aPopupConfigImpl;

import com.github.scribejava.apis.LinkedInApi;
import com.vaadin.server.ClassResource;

@SuppressWarnings("serial")
public class LinkedInButton extends OAuth10aPopupButtonImpl {

  public LinkedInButton(String key, String secret) {
    super(LinkedInApi.instance(), OAuth10aPopupConfigImpl.getStandardOAuthConfig(key, secret));

    setIcon(new ClassResource("/org/vaadin/addon/oauthpopup/icons/linkedin16.png"));
    setCaption("LinkedIn");
  }
}
