package org.vaadin.addon.oauthpopup;

import com.github.scribejava.core.builder.api.DefaultApi20;

/**
 * {@link OAuthPopupButton} which can be used in Spring Boot Vaadin
 * environments where creating a BrowserWindowOpener based on a UI class
 * can be problematic. For this to work a UI needs to be registered with
 * a path set to {@link URLBasedOAuthPopupOpener#UI_URL}.
 * </br>
 * Only supports OAuth 2.0 for now
 *
 * @author Svante Kumlien
 */
public class URLBasedButton extends OAuthPopupButton {

    public URLBasedButton(DefaultApi20 api, OAuthPopupConfig config) {
        super(new URLBasedOAuthPopupOpener(api, config));
    }
}
