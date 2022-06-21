# PlantFam

Android application to keep track of plants.

## Setup Errors

#### INSTALL_FAILED_INSUFFICIENT_STORAGE /.../apk/debug/app-debug.apk

Allocate more memory to your virtual device in the AVD manager. I gave my Pixel 5 API 33 5GB and
that seemed to be sufficient.

#### RealmTransformer doesn't seem to be applied. Please update the project configuration to use the Realm Gradle plugin.

To bypass for now, use `com.google.dagger:hilt-android-gradle-plugin:2.38` with AGP `7.2.1`.
Check [here](https://github.com/realm/realm-java/issues/7685) for updates.

#### Tried to get a plugin before it was configured. make sure you call amplify.configure() first.

1. [Set up Amplify CLI](https://docs.amplify.aws/start/getting-started/installation/q/integration/android/)
2. Run `amplify configure`, create a new user. My CloudFormation stack is based in the `us-west-1`
   AWS region.
3. Run `amplify init`, and select `Y` when it asks if you're using an existing environment.