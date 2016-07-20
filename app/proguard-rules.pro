# For Retrofit 2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# For gradle-retrolambda
-dontwarn java.lang.invoke.*

-keep class android.databinding.** { *; }