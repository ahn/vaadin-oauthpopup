package org.vaadin.addon.oauthpopup;

import java.net.URI;
import java.net.URISyntaxException;

import org.scribe.exceptions.OAuthException;

import com.vaadin.server.VaadinRequest;

/**
 * "Injects" the OAuth callback URI with an id to make it possible to
 * determine which callback request relates to which OAuthData.
 * <p>
 * Currently there are two injecters:
 * - {@link PathInjecter} modifies the URI path
 * - {@link QueryInjecter} modifies the URI query
 * <p>
 * The reason there are different kind of injecters is that different
 * services deal with callbacks a bit differently.
 * If the callback is defined to be http://example.com/foo/ in the application settings,
 * some services may not accept http://example.com/foo/bar/
 * and some services may not send custom query parameters to the callback.
 * Most services work with both {@link PathInjecter} and {@link QueryInjecter}, however.
 *
 */
public interface OAuthCallbackInjecter {
	
	/**
	 * "Injects" the id into the callback.
	 */
	public String injectIdToCallback(String callback, String id);
	
	/**
	 * Extracts the id that was injected into the callback.
	 */
	public String extractIdFromCallback(VaadinRequest request);
	
	public static final String CALLBACK_ID_NAME = "oauthpopupcallback";
	
	public static OAuthCallbackInjecter PATH_INJECTER = new PathInjecter();
	public static OAuthCallbackInjecter QUERY_INJECTER = new QueryInjecter();
	
	/**
	 * 
	 * CALLBACK_URI/oauthcallback/ID
	 *
	 */
	public class PathInjecter implements OAuthCallbackInjecter {

		@Override
		public String injectIdToCallback(String callback, String id) {
			try {
				URI old = new URI(callback);
				String oldPath = old.getPath();
				String idPath = CALLBACK_ID_NAME+"/"+id;
				String newPath = oldPath.endsWith("/") ? oldPath+idPath : oldPath+"/"+idPath;
				URI newUri = new URI(old.getScheme(), old.getAuthority(), newPath, old.getQuery(), old.getFragment());
				return newUri.toASCIIString();
			} catch (URISyntaxException e) {
				throw new OAuthException("Invalid callback URI syntax: " + callback, e);
			}
			
		}

		@Override
		public String extractIdFromCallback(VaadinRequest request) {
			String path = request.getPathInfo();
			if (path==null) {
				return null;
			}
			String[] pathParts = path.split("/");
			int len = pathParts.length;
			if (len < 2) {
				return null;
			}
			if (!CALLBACK_ID_NAME.equals(pathParts[len-2])) {
				return null;
			}
			return pathParts[len-1];
		}
		
	}
	
	/**
	 * 
	 * CALLBACK_URI?oauthcallback=ID
	 *
	 */
	public class QueryInjecter implements OAuthCallbackInjecter {

		@Override
		public String injectIdToCallback(String callback, String id) {
			try {
				URI old = new URI(callback);
				String oldQuery = old.getQuery();
				String idQuery = CALLBACK_ID_NAME+"="+id;
				String newQuery = oldQuery==null ? idQuery : oldQuery+"&"+idQuery;
				URI newUri = new URI(old.getScheme(), old.getAuthority(), old.getPath(), newQuery, old.getFragment());
				return newUri.toASCIIString();
			} catch (URISyntaxException e) {
				throw new OAuthException("Invalid callback URI syntax: " + callback, e);
			}
		}

		@Override
		public String extractIdFromCallback(VaadinRequest request) {
			return request.getParameter(CALLBACK_ID_NAME);
		}
		
	}

}
