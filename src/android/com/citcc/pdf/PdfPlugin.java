package com.citcc.pdf;

import android.content.Intent;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/6/8 0008.
 */

public final class PdfPlugin extends CordovaPlugin {

  private CallbackContext callbackContext;

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  public static final class Result {
    public static final String SUPPORTED = "supported";

    public static final String STATUS = "status";

    public static final String MESSAGE = "message";

    public static final String MISSING_APP_ID = "missingAppId";
  }

  public static final class Args {
    public static final String URL = "url";

    public static final String CONTENT_TYPE = "contentType";

    public static final String OPTIONS = "options";
  }

  public static final class Actions {

    public static final String GET_SUPPORT_INFO = "getSupportInfo";

    public static final String CAN_VIEW = "canViewDocument";

    public static final String VIEW_DOCUMENT = "viewDocument";

    public static final String CLOSE = "close";

    public static final String APP_PAUSED = "appPaused";

    public static final String APP_RESUMED = "appResumed";

    public static final String INSTALL_VIEWER_APP = "install";

  }

  public static final String PDF = "application/pdf";
  private static final String TAG = "PdfPluginIn";

  @Override
  public boolean execute(String action, JSONArray argsArray, CallbackContext callbackContext) throws JSONException {
    this.callbackContext = callbackContext;
    JSONObject args;
    JSONObject options;
    if (argsArray.length() > 0) {
      args = argsArray.getJSONObject(0);
      options = args.getJSONObject(Args.OPTIONS);
    } else {
      //no arguments passed, initialize with empty JSON Objects
      args = new JSONObject();
      options = new JSONObject();
    }
    if (action.equals(Actions.VIEW_DOCUMENT)) {
      String url = args.getString(Args.URL);
      String contentType = args.getString(Args.CONTENT_TYPE);

      final JSONObject successObj = new JSONObject();
      if (PDF.equals(contentType.toLowerCase())) {
        Intent intent = new Intent(this.cordova.getActivity(), PdfActivity.class);
        intent.putExtra("FileUrl", url);
        this.cordova.getActivity().startActivityForResult(intent, 101);
        callbackContext.success("");
      } else {
        //不支持类型
        String message =
          "Content type '" + contentType + "' is not supported";
        Log.d(TAG, message);
        successObj.put(Result.STATUS,
          PluginResult.Status.NO_RESULT.ordinal()
        );
        successObj.put(Result.MESSAGE, message);
        callbackContext.success(successObj);
      }
    }
    return true;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if (this.callbackContext == null)
      return;

    if (requestCode == 101 && resultCode == 100) {
      String error = intent.getStringExtra("Error");
      JSONObject successObj = new JSONObject();
      try {
        successObj.put(Result.STATUS, error);
        this.callbackContext.success(successObj);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    this.callbackContext = null;
  }
}
