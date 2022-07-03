// IWebbiewProcessToMainProcessAidlInterface.aidl
package com.vero.libwebview;

// Declare any non-default types here with import statements

interface IWebviewProcessToMainProcessAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void handleWebCommand(String commandName,String params);
}