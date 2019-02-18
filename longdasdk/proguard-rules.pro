# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class com.yuan.shi.lonng.BuildConfig{
public *;
}
#保持调用接口不被混淆
-keep class com.yuan.shi.lonng.LongDaGame{
public *;
}
-keep class com.yuan.shi.lonng.activity.LongDaPayAcitivity{
public *;
}
-keep class com.yuan.shi.lonng.activity.LongDaPayWebActivity{
public *;
}
-keep class com.yuan.shi.lonng.ui.widget.LongDaMarqueeTextView{
public *;
}

#保持调用接口不被混淆
-keep class com.yuan.shi.lonng.bean.LongDaLoginMethod{
public *;
}

#保持调用接口不被混淆
-keep class com.yuan.shi.lonng.bean.LongDaPayScreen{
public *;
}

#保持调用接口不被混淆
-keep class com.yuan.shi.lonng.LongDaLoginActivity{
public *;
}

#保持调用接口不被混淆
-keep class com.yuan.shi.lonng.LongDaLoginListener{
public *;
}
-keep class com.yuan.shi.lonng.LongDaPayListener{
public *;
}
-keep class com.yuan.shi.lonng.login.LongDaLogin$BaseUiListener {
    <methods>;
    <fields>;
}
-keep class * implements com.tencent.tauth.IUiListener {
<methods>;
<fields>;
}


-libraryjars 'D:\Androidsdk\platforms\android-16\android.jar'

-target 1.6
-optimizationpasses 5
-useuniqueclassmembernames
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-adaptresourcefilenames **.properties
-adaptresourcefilecontents **.properties,META-INF/MANIFEST.MF
-verbose
-dontwarn android.support.**,android.widget.RelativeLayout.**
-ignorewarnings


-keep public class com.google.vending.licensing.ILicensingService

-keep public class com.android.vending.licensing.ILicensingService

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# Keep - Library. Keep all public and protected classes, fields, and methods.
# -keep public class * {
#     public protected <fields>;
#     public protected <methods>;
# }

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

# 支付宝混淆过滤 start
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}
-dontwarn android.net.**
-keep class android.net.SSLCertificateSocketFactory{*;}
# 支付宝混淆过滤 end

# 微信混淆过滤 start
-dontwarn  com.tencent.**
-keep class com.tencent.** {*;}
# 微信混淆过滤 end

#QQ登录混淆 start
-keep public interface com.tencent.**
-keep public class com.tencent.** {*;}
-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
#QQ登录混淆 end