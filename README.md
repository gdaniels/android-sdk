# Nest Android SDK

> Interact with the Nest API quickly and easily on Android.

## Documentation

Sample Android app: https://github.com/nestlabs/android-sdk-sample

Javadocs: https://nestlabs.github.io/android-sdk/

Examples are also available in this README below.

## Install

Add the following line to your `build.gradle` in your Android project:

```gradle
compile 'com.nestlabs:android-sdk:1.0.0'
```

## Quickstart (required)

Setup your Nest instance, preparing it for Authorization / Authentication.

```java
// Set your Context (required - only once)
NestAPI.setAndroidContext(context);

// Get your Nest API instance.
NestAPI nest = NestAPI.getInstance();
```

## Authorization / Authentication

Before we can get started making requests to the Nest API, we must first get authorization from the
user. This authorization is in the form of an **access token**.

### Acquiring an access token

We must launch an OAuth 2.0 flow in order to get an access token initially. Then later, we can
re-use that token.

```java
// A request code you can verify later.
int AUTH_TOKEN_REQUEST_CODE = 123;

// Set the configuration values.
nest.setConfig("client-id", "client-secret", "https://redirect-url");

// Launch the auth flow if you don't already have a token.
nest.launchAuthFlow(getActivity(), AUTH_TOKEN_REQUEST_CODE);

// On your Activity, override the following method to receive the token:
@Override
public void onActivityResult(int requestCode, int resultCode, Intent intent) {
  if (resultCode != RESULT_OK || requestCode != AUTH_TOKEN_REQUEST_CODE) {
    return; // No token will be available.
  }

  NestToken token = NestAPI.getTokenFromIntent(intent);
  // Save the token to a safe place here so it can be re-used later.
}
```

### Re-using an existing access token

If you already have an access token, you can authenticate using that immediately.

```java
// Get existing instance.
NestAPI nest = NestAPI.getInstance();

// Get the token string from your safe place.
String token = "abc123";

// or...

// Get the NestToken object (it's Parcelable!)
NestToken token = getMyToken();

// Authenticate with an existing token.
nest.authWithToken(token, new NestAuthListener() {
  @Override
  public void onAuthSuccess() {
    // Handle success here. Start pulling from Nest API.
  }

  @Override
  public void onAuthFailure(NestException e) {
    // Handle exceptions here.
  }

  @Override
  public void onAuthRevoked() {
    // Your previously authenticated connection has become unauthenticated.
    // Recommendation: Relaunch an auth flow with nest.launchAuthFlow().
  }
});
```

## Get values and listen for changes

### Listen for changes to everything

This includes all devices, structures and metadata.

``` java
nest.addGlobalListener(new GlobalListener() {
  @Override
  public void onUpdate(@NonNull GlobalUpdate update) {
    Metadata metadata = update.getMetadata();
    ArrayList<Camera> cameras = update.getCameras();
    ArrayList<SmokeCOAlarm> thermostats = update.getSmokeCOAlarms();
    ArrayList<Thermostat> thermostats = update.getThermostats();
    ArrayList<Structure> structures = update.getStructures();

    // Handle updates here.
  }
});
```

### Listen for changes to all devices

This includes all thermostats, smoke alarms and cameras.

```java
nest.addDeviceListener(new DeviceListener() {
  @Override
  public void onUpdate(@NonNull NestDeviceUpdate update) {
    ArrayList<Camera> cameras = update.getCameras();
    ArrayList<SmokeCOAlarm> thermostats = update.getSmokeCOAlarms();
    ArrayList<Thermostat> thermostats = update.getThermostats();

    // Handle updates here.
  }
});
```

### Listen for changes to specific device types

#### All thermostats:

```java
nest.addThermostatListener(new ThermostatListener() {
  @Override
  public void onUpdate(@NonNull ArrayList<Thermostat> thermostats) {
    // Handle thermostat update...
  }
});
```

#### All smoke alarms:

```java
nest.addSmokeCOAlarmListener(new SmokeCOAlarmListener() {
  @Override
  public void onUpdate(@NonNull ArrayList<SmokeCOAlarm> alarms) {
    // Handle smoke+co alarm update...
  }
});
```

#### All cameras:

```java
nest.addCameraListener(new CameraListener() {
  @Override
  public void onUpdate(@NonNull ArrayList<Camera> cameras) {
    // Handle camera update...
  }
});
```

### Listen to changes to all structures

```java
nest.addStructureListener(new StructureListener() {
  @Override
  public void onUpdate(@NonNull ArrayList<Structure> structures) {
    // Handle structure update...
  }
});
```

### Listen to metadata changes

This includes the access token and client version.

```java
nest.addMetadataListener(new MetadataListener() {
  @Override
  public void onUpdate(Metadata data) {
    // Handle metadata update... do action.
  }
});
```

## Stop listening to changes

Remove a specific listener.

```java
nest.removeListener(listener); // Removes a specific listener.
```

Remove all listeners.

```java
nest.removeAllListeners(); // Removes all listeners.
```

## Set values and update devices / structures

Updating values on devices and structures is easy. Here are a few examples.

### Thermostat example

[See the full list of possible Thermostat methods here.](https://nestlabs.github.io/android-sdk/index.html?com/nestlabs/sdk/ThermostatSetter.html)

```java
// Get id from Thermostat#getDeviceId
String thermostatId = myThermostat.getDeviceId();

// The temperature in Farhenheit to set. (Note: type long)
long newTemp = 75;

// Set thermostat target temp (in degrees F).
nest.thermostats.setTargetTemperatureF(thermostatId, newTemp);
```

### Thermostat example with callback

[See the full list of possible Thermostat methods here.](https://nestlabs.github.io/android-sdk/index.html?com/nestlabs/sdk/ThermostatSetter.html)

```java
// Get id from Thermostat#getDeviceId
String thermostatId = myThermostat.getDeviceId();

// The temperature in Celsius to set. (Note: type double)
double newTemp = 22.5;

// Set thermostat target temp (in degrees C) with an optional success callback.
nest.thermostats.setTargetTemperatureC(thermostatId, newTemp, new Callback() {
  @Override
  public void onSuccess() {
    // The update to the thermostat succeeded.
  }

  @Override
  public void onFailure(NestException e) {
    // The update to the thermostat failed.
  }
});
```

### Camera example

[See the full list of possible Camera methods here.](https://nestlabs.github.io/android-sdk/index.html?com/nestlabs/sdk/CameraSetter.html)

```java
// Get id from Camera#getDeviceId.
String camId = myCamera.getDeviceId();

// Set camera to start streaming.
nest.cameras.setIsStreaming(camId, true);
```

### Camera example with callback

[See the full list of possible Camera methods here.](https://nestlabs.github.io/android-sdk/index.html?com/nestlabs/sdk/CameraSetter.html)

```java
// Get id from Camera#getDeviceId.
String camId = myCamera.getDeviceId();

// Set camera to start streaming with an optional success callback.
nest.cameras.setIsStreaming(camId, true, new Callback() {
  @Override
  public void onSuccess() {
    // The update to the camera succeeded.
  }

  @Override
  public void onFailure(NestException e) {
    // The update to the camera failed.
  }
});
```

## Contributing

Contributions are always welcome and highly encouraged.

See [CONTRIBUTING](CONTRIBUTING.md) for more information on how to get started.

## License

Apache 2.0 - See [LICENSE](LICENSE) for more information.
