# OAuth Popup Add-on for Vaadin 7.1+

OAuth Popup add-on contains buttons that open an
[OAuth](http://en.wikipedia.org/wiki/OAuth) popup dialog where the user
can authorize the Vaadin application to do things on the users' behalf on
various services such as Facebook, Twitter, etc.

### NOTE
**Unfortunately I (@ahn) currently don't have time to maintain this project.
I can accept pull requests and put a new version to Vaadin Directory once in a while but not do much beyond that.
If you'd like to be a more active maintainer, feel free to contact me.**

<!--
## Online demo

Try the [OAuth Popup add-on demo](http://130.230.142.91:8080/oauthpopup/).
-->

## Add-on

Available as an [add-on in Vaadin
Directory](http://vaadin.com/addon/oauth-popup-add-on).


## Description

This add-on is similar to the [OAuth Buttons add-on](http://vaadin.com/addon/oauth-buttons), except that this one:

- requires Vaadin 7.1+
- opens the OAuth dialog in a separate browser window, keeping the application window open
- doesn't have helper user classes for login; this one simply returns an OAuth access token and it's up to the application to do something with it

Since the OAuth dialog is opened in a separate window,
**the application should enable [server push](https://vaadin.com/book/vaadin7/-/page/advanced.push.html)**.
Otherwise the actual application UI will not be updated when the OAuth window is done,
because without push the client of the application UI doesn't know that somethings's changed.

This add-on uses [Scribe](https://github.com/fernandezpablo85/scribe-java/) library for OAuth.

The `OAuthPopupButton` can be used by simply giving a Scribe API and API key+secret to its constructor, or by extending it.
A couble of subclasses are already at package `org.vaadin.addon.oauthpopup.buttons`.

NOTE: I'm not sure if the add-on currently works with all the Scribe APIs, probably not...


## Usage example

To use OAuth, you must first create an application for the service in question.
Give the applications *key* and *secret* to the constructor of OAuthPopupButton (or of its subclass such as TwitterButton).
For example, Twitter applications can be created [here](https://dev.twitter.com/apps).

```java
OAuthPopupButton ob = new TwitterButton(TW_KEY, TW_SECRET);

ob.addOAuthListener(new OAuthListener() {
  @Override
  public void authSuccessful(Token token, boolean isOAuth20) {
    Notification.show("Authorized");
    // TODO: do something with the access token
  }

  @Override
  public void authDenied(String reason) {
    Notification.show("Authorization denied");
  }
});

layout.addComponent(ob);

```

For some services it's possible to set the *scope* of OAuth authorization.
The format of scope is service-dependent, often a comma or space separated list of names.

```java
ob.getOAuthPopupConfig().setScope("email");
```

By default, the OAuth window is opened in a new tab in most browsers.
You can control that by setting the [features](https://vaadin.com/book/vaadin7/-/page/advanced.html)
that are redirected to the BrowserWindowOpener of the button.

```java
ob.setPopupWindowFeatures("resizable,width=400,height=300");
```

If you like to use some component other than button to open the popup window,
you can extend any component with an `OAuthPopupOpener`.

## Roadmap

This component has no public roadmap or any guarantees of upcoming releases.

Feedback is welcome. [Comment on Directory](http://vaadin.com/addon/oauth-popup), [add an issue on GitHub](https://github.com/ahn/vaadin-oauthpopup/issues/), or [mail me](mailto:anttihn@gmail.com).


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

    git clone https://github.com/ahn/vaadin-oauthpopup.git
    cd vaadin-oauthpopup
    mvn clean install
    cd oauthpopup-demo
    mvn jetty:run

To see the demo, navigate to http://localhost:8080/

To create an addon package that can be uploaded to Vaadin Directory

    cd oauthpopup
    mvn clean package assembly:single

## About implementation

The basic flow goes as follows:

1. `OAuthPopupButton` extends itself with `OAuthPopupOpener`
1. When `OAuthPopupOpener` is attached, it
    * stores a `OAuthData` instance as a session attribute, for other windows to read
2. When the button is clicked, the opener opens a `OAuthPopupUI` in a new window
3. The `OAuthPopupUI`
    * reads the `OAuthData` from the session attribute
    * adds a new `OAuthCallbackRequestHandler` to the current session
    * redirects the user to the OAuth authorization URL
4. When the user returns from the authorization URL to our callback URL:
    * the `OAuthCallbackRequestHandler` is no longer needed, and is removed from session
    * the `OAuthListener`s of are called, either `authSuccessful` or `authDenied` 
5. When the `OAuthPopupOpener` is detached, it clears the session attribute where the `OAuthData` was


## License

Add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.




