package org.vaadin.addon.oauthpopup;

import java.net.URI;
import java.util.LinkedList;

import org.scribe.builder.api.Api;

import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;

/**
 * A button that opens a OAuth authorization url in a separate browser window,
 * and lets the user to perform the authorization.
 * <p>
 * The success/failure of the authorization can be listened
 * with {@link #addOAuthListener(OAuthListener)}.
 * <p>
 * IMPORTANT: the callback comes from a different window, not from the usual
 * Vaadin server-visit thread. That's why the UI is not updated automatically,
 * UNLESS server push is enabled. So, it's good idea to enable @Push in the UI class.
 * <p>
 * This class may be subclassed for customization, or
 * used just by giving the Scribe Api class for the constructor.
 * The latter approach may not work for all Api's because
 * all the Apis don't work the same and some customization might
 * be necessary...
 * <p>
 * Available subclasses are at {@link org.vaadin.addon.oauthpop.buttons}.
 *
 */
@SuppressWarnings("serial")
// I guess we might have as well just inherit Button, not CustomComponent...
public class OAuthPopupButton extends CustomComponent {
	
	private LinkedList<OAuthListener> listeners = new LinkedList<OAuthListener>();

	private OAuthData data;
	
	private boolean useDefaultCallback = true;
		
	private Button button;
	
	private BrowserWindowOpener opener;
	private String popupFeatures;
	
	private OAuthListener dataListener;

	public OAuthPopupButton(Class<? extends Api> apiClass, String key, String secret) {
		this.data = new OAuthData(apiClass, key, secret);
		button = new Button();
		setCompositionRoot(button);
	}
	
	protected Button getButton() {
		return button;
	}
	
	/**
	 *  IMPORTANT: the listener call originates from a different window,
	 *  not from the usual Vaadin server-visit thread.
	 *  That's why the UI is not updated automatically, UNLESS server push is enabled.
	 *  So, it's good idea to enable @Push in the UI class.
	 */
	public void addOAuthListener(OAuthListener listener) {
		listeners.add(listener);
	}
	
	/**
	 *  IMPORTANT: the listener call originates from a different window,
	 *  not from the usual Vaadin server-visit thread.
	 *  That's why the UI is not updated automatically, UNLESS server push is enabled.
	 *  So, it's good idea to enable @Push in the UI class.
	 */
	public void removeListener(OAuthListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public void setCaption(String caption) {
		button.setCaption(caption);
	}
	
	@Override
	public void setDescription(String description) {
		button.setDescription(description);
	}
	
	@Override
	public void setIcon(Resource icon) {
		button.setIcon(icon);
	}
	
	@Override
	public void attach() {
		super.attach();
				
		if (useDefaultCallback) {
			setCallbackToDefault();
		}
		
		setOpener(createOpener());

		// Setting oauth data as a session attribute for the popup to use...
		String attr = data.getSessionAttributeName();
		getSession().setAttribute(attr, data);
		
		// ...and letting the popup know the name of the attribute.
		opener.setParameter("data", attr);
		
		dataListener = new OAuthListener() {
			@Override
			public void authSuccessful(String accessToken, String accessTokenSecret) {
				fireAuthSuccessful(accessToken, accessTokenSecret);
			}
			
			@Override
			public void authDenied(String reason) {
				fireAuthFailed(reason);
			}
		};
		data.addListener(dataListener);
	}
	
	@Override
	public void detach() {
		super.detach();
		
		data.removeListener(dataListener);
		
		// Deleting the session attribute.
		getSession().setAttribute(data.getSessionAttributeName(), null);
	}
	
	/**
	 * Comma-separated list of features given to the BrowserWindowOpener.
	 * <p>
	 * See here for feature names: https://vaadin.com/book/vaadin7/-/page/advanced.html
	 * 
	 */
	public void setPopupWindowFeatures(String features) {
		if (features==null ? popupFeatures==null : features.equals(popupFeatures)) {
			return;
		}
		popupFeatures = features;
		if (isAttached()) {
			// If we're already attached, have to create a new opener.
			setOpener(createOpener());
		}
	}
	
	/**
	 * Sets the callback URI for the OAuth.
	 * <p>
	 * Must be called before user opens the popup to have effect.
	 * <p>
	 * Default: see {@link #setCallbackToDefault()}
	 * 
	 */
	public void setCallback(String callback) {
		useDefaultCallback = false;
		data.setCallback(callback);
	}

	
	/**
	 * Sets the callback URI to default.
	 * <p>
	 * The default callback is constructed from current Page location:
	 *  SCHEME + "://" + AUTHORITY + PATH
	 * 	
	 */
	public void setCallbackToDefault() {
		useDefaultCallback = true;
		if (isAttached()) {
			setCallback(getDefaultCallback());
		}
		// if not attached, have to wait till attach() where we have the Page location.
	}
	
	public void setScope(String scope) {
		data.setScope(scope);
	}
	
	private BrowserWindowOpener createOpener() {
		BrowserWindowOpener opener = new BrowserWindowOpener(OAuthPopupUI.class);
		opener.setFeatures(popupFeatures);
		return opener;
	}
	
	private void setOpener(BrowserWindowOpener opener) {
		if (this.opener!=null) {
			this.opener.remove();
		}
		this.opener = opener;
		opener.extend(button);
	}
	
	private static String getDefaultCallback() {
		URI u = Page.getCurrent().getLocation();
		return u.getScheme()+"://"+u.getAuthority()+u.getPath();
	}
	
	private void fireAuthSuccessful(final String accessToken, final String accessTokenSecret) {
		// Coming from different thread than the usual Vaadin server visit.
		// That's why we have to call access (TODO: session or UI?)
		// Doing this here so our listeners don't need to.
		VaadinSession session = getSession();
		session.access(new Runnable() {
			@Override
			public void run() {
				for (final OAuthListener li : listeners) {
					li.authSuccessful(accessToken, accessTokenSecret);
				}
			}
		});
	}
	
	private void fireAuthFailed(final String reason) {
		getSession().access(new Runnable() {
			@Override
			public void run() {
				for (final OAuthListener li : listeners) {
					li.authDenied(reason);
				}

			}
		});
	}

	
}
