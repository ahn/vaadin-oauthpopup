package org.vaadin.addon.oauthpopup.buttons;

import org.vaadin.addon.oauthpopup.oauth10a.OAuth10aPopupButtonImpl;
import org.vaadin.addon.oauthpopup.oauth10a.OAuth10aPopupConfigImpl;

import com.github.scribejava.apis.TwitterApi;
import com.vaadin.server.ClassResource;

@SuppressWarnings("serial")
public class TwitterButton extends OAuth10aPopupButtonImpl {

  public TwitterButton(String key, String secret) {
    super(TwitterApi.instance(), OAuth10aPopupConfigImpl.getStandardOAuthConfig(key, secret));

    setIcon(new ClassResource("/org/vaadin/addon/oauthpopup/icons/twitter16.png"));
    setCaption("Twitter");
  }

}
