# For Retrofit 2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# For gradle-retrolambda
-dontwarn java.lang.invoke.*

-keep class android.databinding.** { *; }

# Paho library logger
-keep class org.eclipse.paho.client.mqttv3.logging.JSR47Logger {
    *;
}

# Avoid warnings for old code in Paho 1.0.2 on Android Studio 2
-keep class org.eclipse.paho.client.mqttv3.persist.** { *; }
-dontwarn org.eclipse.paho.client.mqttv3.persist.**
-keepattributes Exceptions, Signature, InnerClasses