package org.vaadin.addon.oauthpopup.oauth20;

import org.vaadin.addon.oauthpopup.base.OAuthPopupConfig;
import org.vaadin.addon.oauthpopup.oauth10a.OAuth10aPopupUIImpl;

import com.github.scribejava.core.builder.api.DefaultApi20;

/**
 * Component extension that opens an OAuth authorization popup window when the extended component is
 * clicked. This popup opener works in a Spring Boot Vaadin environment by configuring the
 * BrowserWindowOpener with a url instead of a Vaadin UI class. For this to work a UI extending the
 * {@link OAuth10aPopupUIImpl} needs to be registered using the SpringUI annotation and the path
 * attribute pointing to {@link #UI_URL}. </br>
 * Only supports OAuth 2.0 for now.
 *
 *
 * @see com.vaadin.server.BrowserWindowOpener
 * @see URLBasedButton
 *
 * @author Svante Kumlien
 *
 */
@SuppressWarnings("serial")
public class URLBasedOAuth20PopupOpener extends OAuth20PopupOpenerImpl {

  // Mimic the url created in BrowserWindowOpener#generateUIClassUrl
  public static final String UI_URL = "popup/OAuthPopupUI";

  public URLBasedOAuth20PopupOpener(DefaultApi20 api, OAuthPopupConfig config) {
    super(api, config, UI_URL);
  }
}
