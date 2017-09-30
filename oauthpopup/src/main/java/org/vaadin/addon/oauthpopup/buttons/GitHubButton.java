package org.vaadin.addon.oauthpopup.buttons;

import org.vaadin.addon.oauthpopup.oauth20.OAuth20PopupButtonImpl;

import com.github.scribejava.apis.GitHubApi;
import com.vaadin.server.ClassResource;

@SuppressWarnings("serial")
public class GitHubButton extends OAuth20PopupButtonImpl {

  public GitHubButton(String key, String secret) {
    super(GitHubApi.instance(), key, secret);

    setIcon(new ClassResource("/org/vaadin/addon/oauthpopup/icons/github16.png"));
    setCaption("GitHub");
  }
}
