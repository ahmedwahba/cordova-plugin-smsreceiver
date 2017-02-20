/*
Copyright (C) 2017 by Ahmed Wahba

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package org.apache.cordova.smsreceiver;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.app.Activity;
import android.content.IntentFilter;
import android.content.pm.PackageManager;



public class SmsReceivingPlugin extends CordovaPlugin {
	public final String ACTION_HAS_RECEIVE_PERMISSION = "HasReceivePermission";
	public final String ACTION_START_RECEIVE_SMS = "StartReceiving";
	public final String ACTION_STOP_RECEIVE_SMS = "StopReceiving";
	
	private CallbackContext callback_receive;
	private SmsReceiver smsReceiver = null;
	private boolean isReceiving = false;
	
	public SmsReceivingPlugin() {
		super();
	}
	
	@Override
	public boolean execute(String action, JSONArray arg1,
			final CallbackContext callbackContext) throws JSONException {
		
		if (action.equals(ACTION_HAS_RECEIVE_PERMISSION)) {
			
                    Activity ctx = this.cordova.getActivity();
                    if (Build.VERSION.SDK_INT >= 23) {
                       
                        if(PermissionHelper.hasPermission(this,Manifest.permission.RECEIVE_SMS)){
                            if(ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)){
                                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, true));
                            } else {
                                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, false));
                            }
                            return true;
                        } else {
                            PermissionHelper.requestPermission(this, 0, Manifest.permission.RECEIVE_SMS);
                            JSONObject returnObj = new JSONObject();
                            addProperty(returnObj, "permissionGranted", true);
                            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, returnObj));
                            return true;
                        }
                    } else {
                       if(ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)){
                            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, true));
                        } else {
                            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, false));
                        }
                        return true;
                    }
                    
		}
		else if (action.equals(ACTION_START_RECEIVE_SMS)) {
			
			// if already receiving (this case can happen if the startReception is called
			// several times
			if(this.isReceiving) {
				// close the already opened callback ...
				PluginResult pluginResult = new PluginResult(
						PluginResult.Status.NO_RESULT);
				pluginResult.setKeepCallback(false);
				this.callback_receive.sendPluginResult(pluginResult);
				
				// ... before registering a new one to the sms receiver
			}
			this.isReceiving = true;
				
			if(this.smsReceiver == null) {
				this.smsReceiver = new SmsReceiver();
				IntentFilter fp = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
			    fp.setPriority(1000);
			    // fp.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
			    this.cordova.getActivity().registerReceiver(this.smsReceiver, fp);
			}
			
			this.smsReceiver.startReceiving(callbackContext);
	
			PluginResult pluginResult = new PluginResult(
					PluginResult.Status.NO_RESULT);
			pluginResult.setKeepCallback(true);
			callbackContext.sendPluginResult(pluginResult);
			this.callback_receive = callbackContext;
			
			return true;
		}
		else if(action.equals(ACTION_STOP_RECEIVE_SMS)) {
			
			if(this.smsReceiver != null) {
				smsReceiver.stopReceiving();
			}

			this.isReceiving = false;
			
			// 1. Stop the receiving context
			PluginResult pluginResult = new PluginResult(
					PluginResult.Status.NO_RESULT);
			pluginResult.setKeepCallback(false);
			this.callback_receive.sendPluginResult(pluginResult);
			
			// 2. Send result for the current context
			pluginResult = new PluginResult(
					PluginResult.Status.OK);
			callbackContext.sendPluginResult(pluginResult);
			
			return true;
		}

		return false;
	}
        
       private void addProperty(JSONObject obj, String key, Object value) {
            try {
                if (value == null) {
                    obj.put(key, JSONObject.NULL);
                } else {
                    obj.put(key, value);
                }
            } catch (JSONException ignored) {
                //Believe exception only occurs when adding duplicate keys, so just ignore it
            }
        } 
    
}
