# OAuth Popup Add-on for Vaadin 7.1+

Add [OAuth](http://en.wikipedia.org/wiki/OAuth) support to your Vaadin applications by embedding a button that does the work for you. 
This addon uses the terrific [Scribe](https://github.com/scribejava/scribejava/) library under the covers, which supports all 
major OAuth services out-of-the-box. 

The API for this add-on aims to make configuration as simple as possible without hiding any functionality to allow 
fine-tuning for unsupported OAuth services. The button provided by this add-on opens a popup window which handles the OAuth authentication.

Since the OAuth dialog is opened in a separate window,
**the application should enable [server push](https://vaadin.com/book/vaadin7/-/page/advanced.push.html)**.
Otherwise the actual application UI will not be updated when the OAuth window is done,
because without push the client of the application UI doesn't know that somethings's changed.

OAuth Popup add-on contains buttons that open an
 popup dialog where the user
can authorize the Vaadin application to do things on the users' behalf on
various services such as Facebook, Twitter, etc.

### NOTE
This project was forked from @ahn's initial implementation and is available as a separate add-on in the Vaadin Directory
([http://vaadin.com/addon/oauth-popup-add-on](http://vaadin.com/addon/oauth-popup-add-on)). Major differences between
the two projects include:

- Use of the latest Scribe library to support all major OAuth 1.0a and 2.0 services out-of-the-box.
- Add extensive Javadoc documentation.
- Add flexibility allowing fine-grained control of OAuth parameters. 
- Create an OAuth Popup button for any OAuth API supported by Scribe without subclassing.


## Usage example

To use OAuth, you must first create an application for the service in question.
Give the applications *key* and *secret* to the constructor of OAuthPopupButton (or of its subclass such as TwitterButton).
For example, Twitter applications can be created [here](https://dev.twitter.com/apps).

```java
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
...

OAuthPopupButton twitter = new OAuthPopupButton(
				com.github.scribejava.apis.TwitterApi.instance(), "my-key", "my-secret");
twitter.addOAuthListener(new OAuthListener() {

	@Override
	public void authSuccessful(Token token, boolean isOAuth20) {
		// Do something useful with the OAuth token, like persist it
		if (token instanceof OAuth2AccessToken) {
			((OAuth2AccessToken) token).getAccessToken();
			((OAuth2AccessToken) token).getRefreshToken();
			((OAuth2AccessToken) token).getExpiresIn();
		} else {
			((OAuth1AccessToken) token).getToken();
			((OAuth1AccessToken) token).getTokenSecret();
		}
	}

	@Override
	public void authDenied(String reason) {
		Notification.show("Failed to authenticate!", Notification.Type.ERROR_MESSAGE);
	}
});
layout.addComponent(twitter);
```

You can also control each aspect of the OAuth authorization flow by using an OAuthConfig object:

```java
OAuthPopupConfig config = OAuthPopupConfig.getStandardOAuth20Config("my-key", "my-secret");
config.setGrantType("authorization_code");
config.setScope("https://www.googleapis.com/auth/plus.login");
config.setCallbackUrl("urn:ietf:wg:oauth:2.0:oob");
OAuthPopupButton google = new OAuthPopupButton(GoogleApi20.instance(), "my-key", "my-secret");
...
```

By default, the OAuth window is opened in a new tab in most browsers.
You can control that by setting the [features](https://vaadin.com/book/vaadin7/-/page/advanced.html)
that are redirected to the BrowserWindowOpener of the button.

```java
button.setPopupWindowFeatures("resizable,width=400,height=300");
```

If you like to use some component other than button to open the popup window,
you can extend any component with an `OAuthPopupOpener`.


## Contribution

Contributions are appreciated as well. Process for contributing is the following:

- Fork this project
- Create an issue to this project about the contribution (bug or feature) if there is no such issue about it already. Try to keep the scope minimal.
- Develop and test the fix or functionality
- Refer to the fixed issue in commit
- Send a pull request for the original project
- Comment on the original issue that you have implemented a fix for it


## Building and running demo

To get, compile and run the project:

    git clone https://github.com/bdunn44/vaadin-oauthpopup.git
    cd vaadin-oauthpopup
    gradlew :oauthpopup-demo:vaadinRun

To see the demo, navigate to http://localhost:8080/


## License

Add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.




