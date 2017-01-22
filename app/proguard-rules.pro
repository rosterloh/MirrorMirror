# For Retrofit 2
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-keep class android.databinding.** { *; }

# Paho library logger
-keep class org.eclipse.paho.client.mqttv3.logging.JSR47Logger {
    *;
}

# Avoid warnings for old code in Paho 1.0.2 on Android Studio 2
-keep class org.eclipse.paho.client.mqttv3.persist.** { *; }
-dontwarn org.eclipse.paho.client.mqttv3.persist.**
-keepattributes Exceptions, Signature, InnerClasses

# Some methods are only called from tests, so make sure the shrinker keeps them.
-keep class com.rosterloh.mirror.** { *; }

-keep class com.google.common.base.Preconditions { *; }
-keep class android.databinding.** { *; }

# Proguard rules that are applied to your test apk/code.
-ignorewarnings

-keepattributes *Annotation*

-dontnote junit.framework.**
-dontnote junit.runner.**

-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**
# Uncomment this if you use Mockito
-dontwarn org.mockito.**