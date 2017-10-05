package org.vaadin.addon.oauthpopup.base;

import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.oauth.OAuthService;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * UI that redirects the user to OAuth authorization url.
 * <p>
 * Should always be opened by {@link OAuthPopupButtonAbstract}.
 * <p>
 * Reads the {@link OAuthData} instance from session. The name of the session variable must be given
 * as URI parameter named "data".
 */
@SuppressWarnings("serial")
public abstract class OAuthPopupUIAbstract<S extends OAuthService, T extends BaseApi<S>>
    extends UI {

  public static final String DATA_PARAM_NAME = "data";

  @SuppressWarnings("unchecked")
  @Override
  protected void init(VaadinRequest request) {

    final String attr = request.getParameter(DATA_PARAM_NAME);
    if (attr == null) {
      throw new IllegalStateException(
          String.format("No URI parameter named \"%s\".\n", DATA_PARAM_NAME)
              + "Please use OAuthPopupButton or a subclass to open OAuthPopup.");
    }

    final OAuthDataAbstract<S, T> data = (OAuthDataAbstract<S, T>) getSession().getAttribute(attr);
    if (data == null) {
      throw new IllegalStateException(
          String.format("No session attribute named \"%s\" found.\n", attr)
              + "Please use OAuthPopupButton or a subclass to open OAuthPopup.");
    }

    redirectToAuthorization(data);
  }

  protected abstract void redirectToAuthorization(OAuthDataAbstract<S, T> data);

  @Override
  public void detach() {
    super.detach();
    cleanUpSession();
  }

  protected abstract void cleanUpSession();

}
