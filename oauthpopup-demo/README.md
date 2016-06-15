### Running the Demo

The demo app checks the classpath for the following JSON files:

- client_secret.github.json
- client_secret.google.json
- client_secret.linkedin.json
- client_secret.twitter.json

If the file exists a corresponding button will be added to the layout. These files contain the Client ID and Client Secret for the services, which should be kept secret.

These files should have the following structure:

```JSON
{
	"client_id": "my fake client id",
	"client_secret": "my fake client secret"
}
```

To run the demo simply run the following command ***from the project root***:

```sh
gradlew :oauthpopup-demo:vaadinRun
```
