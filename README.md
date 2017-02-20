SMS Receiver plugin for Cordova
===============================
By Ahmed Wahba

Android Cordova plugin allows you to receive incoming SMS. You have the possibility to start and stop the message broadcasting.

This plugin was successfully tested on Android 5.1 , also on Marshmallow (Android 6.0).

## Install  ##

	cordova plugin add cordova-plugin-sms-receiver


## Using the plugin ##

### isSupported ###
Check if the SMS permission is granted and  SMS technology is supported by the device. In case of Marshmallow devices, it requests permission from user here

Example:
```javascript
  window.sms.isSupported ((function(supported) {
        if(supported){
             alert("you have permission to receive SMS ");             
         } else {
             alert("SMS not supported");
         }

  }), function() {
          alert("Error while checking the SMS permission ");
  });     
```

### startReception ###
Start the SMS receiver waiting for incoming message
The success callback function will be called everytime a new message is received.
The error callback is called if an error occurs.

Example:
```javascript
  window.sms.startRecieving (function(msg) {
      alert(msg); /* message received successfully */
  }, function() {
      alert("Error while receiving messages");
  });
```

### stopReception ###
Stop the SMS receiver

Example:
```javascript
  window.sms.stopRecieving (function() {
    alert("has stopped receiver Correctly");
  }, function() {
    alert("Error while stopping the SMS receiver");
  });
```



## Licence ##


Copyright (c) 2017	Ahmed Wahba

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
