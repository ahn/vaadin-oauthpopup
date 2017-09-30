package org.vaadin.addon.oauthpopup.buttons;

import org.vaadin.addon.oauthpopup.oauth20.OAuth20PopupButtonImpl;

import com.github.scribejava.apis.FacebookApi;
import com.vaadin.server.ClassResource;

@SuppressWarnings("serial")
public class FacebookButton extends OAuth20PopupButtonImpl {

  public FacebookButton(String key, String secret) {
    super(FacebookApi.instance(), key, secret);

    setIcon(new ClassResource("/org/vaadin/addon/oauthpopup/icons/facebook16.png"));
    setCaption("Facebook");
  }
}
