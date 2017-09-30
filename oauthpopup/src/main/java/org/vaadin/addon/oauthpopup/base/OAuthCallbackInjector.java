package org.vaadin.addon.oauthpopup.base;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.github.scribejava.core.exceptions.OAuthException;
import com.vaadin.server.VaadinRequest;

/**
 * "Injects" the OAuth callback URI with an id to make it possible to
 * determine which callback request relates to which OAuthData.
 * <p>
 * Currently there are two injecters:
 * - {@link PathInjector} modifies the URI path
 * - {@link QueryInjector} modifies the URI query
 * <p>
 * The reason there are different kind of injecters is that different
 * services deal with callbacks a bit differently.
 * If the callback is defined to be http://example.com/foo/ in the application settings,
 * some services may not accept http://example.com/foo/bar/baz
 * and some services may not send custom query parameters to the callback (i.e. http://example.com/foo?bar=baz).
 * Most services work with either {@link PathInjector} or {@link QueryInjector}, however.
 *
 */
public interface OAuthCallbackInjector {
	
	/**
	 * "Injects" a parameter into a callback URL.
	 */
	public String injectParameterToCallback(String callback, String parameterKey, String parameterValue);
	
	/**
	 * Extracts a parameter from a VaadinRequest.
	 */
	public String extractParameterFromCallbackRequest(VaadinRequest request, String parameterKey);
	
	/**
	 * Used to inject parameters into the URI path.
	 */
	public static OAuthCallbackInjector PATH_INJECTOR = new PathInjector();
	
	
	/**
	 * Used to inject query parameters into a URI.
	 */
	public static OAuthCallbackInjector QUERY_INJECTOR = new QueryInjector();
	
	
	public class PathInjector implements OAuthCallbackInjector {

		@Override
		public String injectParameterToCallback(String callback, String parameterKey, String parameterValue) {
			try {
				URI old = new URI(callback);
				String oldPath = old.getPath();
				String idPath = parameterKey+"/"+URLEncoder.encode(parameterValue, "UTF-8");
				String newPath = oldPath.endsWith("/") ? oldPath+idPath : oldPath+"/"+idPath;
				URI newUri = new URI(old.getScheme(), old.getAuthority(), newPath, old.getQuery(), old.getFragment());
				return newUri.toASCIIString();
			} catch (URISyntaxException e) {
				throw new OAuthException("Invalid callback URI syntax: " + callback, e);
			} catch (UnsupportedEncodingException e) {
				// Unreachable
				return null;
			}
			
		}

		@Override
		public String extractParameterFromCallbackRequest(VaadinRequest request, String parameterKey) {
			String path = request.getPathInfo();
			if (path==null) {
				return null;
			}
			String[] pathParts = path.split("/");
			int len = pathParts.length;
			if (len < 2) {
				return null;
			}
			if (!parameterKey.equals(pathParts[len-2])) {
				return null;
			}
			return pathParts[len-1];
		}
		
	}
	
	public class QueryInjector implements OAuthCallbackInjector {

		@Override
		public String injectParameterToCallback(String callback, String parameterKey, String parameterValue) {
			try {
				URI old = new URI(callback);
				String oldQuery = old.getQuery();
				String idQuery = parameterKey+"="+URLEncoder.encode(parameterValue, "UTF-8");
				String newQuery = oldQuery==null ? idQuery : oldQuery+"&"+idQuery;
				URI newUri = new URI(old.getScheme(), old.getAuthority(), old.getPath(), newQuery, old.getFragment());
				return newUri.toASCIIString();
			} catch (URISyntaxException e) {
				throw new OAuthException("Invalid callback URI syntax: " + callback, e);
			} catch (UnsupportedEncodingException e) {
				// Unreachable
				return null;
			}
		}

		@Override
		public String extractParameterFromCallbackRequest(VaadinRequest request, String parameterKey) {
			return request.getParameter(parameterKey);
		}
	}
	
	// This injector doesn't modify the callback URI, it relies on the Scribe library to set the "state" query parameter
	public class OAuth2StateInjector implements OAuthCallbackInjector {

		private OAuthPopupConfigAbstract config;
		
		public OAuth2StateInjector(OAuthPopupConfigAbstract config) {
			this.config = config;
		}
		
		@Override
		public String injectParameterToCallback(String callback, String parameterKey, String parameterValue) {
			try {
				// This inject
				String state = config.getState() == null ? "" : URLDecoder.decode(config.getState(), "UTF-8");
				state += state.isEmpty() ? "" : ",";
				state += parameterKey + ":" + parameterValue;
				config.setState(URLEncoder.encode(state, "UTF-8"));
				return callback;
			} catch (UnsupportedEncodingException e) {
				// Unreachable
				return null;
			}
		}

		@Override
		public String extractParameterFromCallbackRequest(VaadinRequest request, String parameterKey) {
			try {
				String state = config.getState() == null ? "" : URLDecoder.decode(config.getState(), "UTF-8");
				if (state == null || state.isEmpty()) return null;
				String[] kvPairs = state.split(",");
				for (String kv : kvPairs) {
					String[] kvSplit = kv.split(":");
					if (kvSplit[0].equals(parameterKey)) return kvSplit[1];
				}
			} catch (UnsupportedEncodingException e) {
				// Unreachable
			}
			return null;
		}
	}
}