package org.vaadin.addon.oauthpopup;

import java.net.URI;
import java.net.URISyntaxException;

import org.scribe.exceptions.OAuthException;

import com.vaadin.server.VaadinRequest;

public interface OAuthCallbackInjecter {
	
	public String injectIdToCallback(String callback, String id);
	
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
