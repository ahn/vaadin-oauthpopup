package org.vaadin.addon.oauthpopup;

import java.io.IOException;
import java.net.URLDecoder;

import com.github.scribejava.core.model.OAuth1RequestToken;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;

/**
 * Handles the callback from the OAuth authorization url.
 * <p>
 * When done, closes the window and removes this handler.
 *
 */
@SuppressWarnings("serial")
public class OAuthCallbackRequestHandler implements RequestHandler {

	private final OAuth1RequestToken requestToken;
	private final OAuthData data;

	private static final String CLOSE_WINDOW_HTML =
			"<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"utf-8\"/>" +
			"<script>window.close();</script>" +
			"</head><body>" +
			"</body></html>";

	/**
	 * Only handles request that match the data id.
	 * 
	 * @param requestToken may be null (in case of OAuth2)
	 * @param data
	 */
	public OAuthCallbackRequestHandler(OAuth1RequestToken requestToken, OAuthData data) {
		this.requestToken = requestToken;
		this.data = data;
	}

	@Override
	public boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response) throws IOException {
		
		if (!data.isCallbackForMe(request)) {
			// Debugger
			/*System.out.println("Request isn't for me");
			for (Entry<String, String[]> e : request.getParameterMap().entrySet()) {
				System.out.println("\t"+e.getKey()+"="+URLDecoder.decode(e.getValue()[0], "UTF-8"));
			}*/
			return false;
		}
		
		String verifier = request.getParameter(data.getOAuthPopupConfig().getVerifierParameterName());
		//System.out.println("Found verifier request with verifier " + verifier);
		if (verifier != null) {
			// Got verifier!
			data.setVerifier(requestToken, verifier);
			finish(session, response);
			return true;
		}
		
		// No verifier in the parameters. That's most likely because the user denied the OAuth.
		String error = URLDecoder.decode(request.getParameter(data.getOAuthPopupConfig().getErrorParameterName()), "UTF-8");
		error = error == null ? "OAuth failed due to an unspecified reason" : error;
		
		data.setDenied(error);
		finish(session, response);
		return true;
	}

	private void finish(VaadinSession session, VaadinResponse response) throws IOException {
		response.setContentType("text/html");
		response.getWriter().append(CLOSE_WINDOW_HTML);
		cleanUpSession(session);
	}
	
	// NOTE: the finish() method above is not called if the user
	// doesn't browse back from the OAuth authentication page.
	// That's why we make cleanUpSession public so that others can clean up also.
	public void cleanUpSession(final VaadinSession session) {
		session.access(new Runnable() {
			@Override
			public void run() {
				session.removeRequestHandler(OAuthCallbackRequestHandler.this);
			}
		});
	}
}