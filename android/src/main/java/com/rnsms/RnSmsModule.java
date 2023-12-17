package com.rnsms;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Telephony;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ReactModule(name = RnSmsModule.NAME)
public class RnSmsModule extends ReactContextBaseJavaModule {
  public static final String NAME = "RnSms";

  public RnSmsModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  public String getAttachment(ReadableMap attachment, String key) {
    return attachment.getString(key);
  }

  public List<String> convertReadableArrayToList(ReadableArray readableArray) {
    List<String> resultList = new ArrayList<>();

    if (readableArray != null && readableArray.size() > 0) {
      for (int i = 0; i < readableArray.size(); i++) {
        String stringValue = readableArray.getString(i);
        resultList.add(stringValue);
      }
    }

    return resultList;
  }

  @ReactMethod
  public void sendSMS(ReadableArray addresses, String message, ReadableMap options, Promise promise) {
    ReadableArray attachments = null;
    if (options != null) {
      attachments = options.getArray("attachments");
    }

    Intent smsIntent;
    if (attachments != null && attachments.size() > 0) {
      smsIntent = new Intent(Intent.ACTION_SEND);
      smsIntent.setType("text/plain");
      smsIntent.putExtra("address", String.join(";", convertReadableArrayToList(addresses)));
      ReadableMap attachment = attachments.getMap(0);
      smsIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(getAttachment(attachment, "uri")));
      smsIntent.setType(getAttachment(attachment, "mimeType"));
      smsIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    } else {
      smsIntent = new Intent(Intent.ACTION_SENDTO);
      smsIntent.setData(Uri.parse("smsto:" + String.join(";", convertReadableArrayToList(addresses))));
    }

    String defaultSMSPackage = Telephony.Sms.getDefaultSmsPackage(this.getReactApplicationContext());

    if (defaultSMSPackage != null) {
      smsIntent.setPackage(defaultSMSPackage);
    } else {
      promise.reject("NO_SMS_APP", "No messaging application available");
      return;
    }

    smsIntent.putExtra("exit_on_sent", true);
    smsIntent.putExtra("compose_mode", true);
    smsIntent.putExtra("sms_body", message);

    Activity currentActivity = this.getCurrentActivity();
    if (currentActivity != null) {
      this.getCurrentActivity().startActivity(smsIntent);
      promise.resolve(true);
      return;
    }

    promise.resolve(false);
  }
}
