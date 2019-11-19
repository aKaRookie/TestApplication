// IMediaStatusListener.aidl
package com.example.mykotlin.audio;

// Declare any non-default types here with import statements

interface IMediaStatusListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onUpdateStatus(int status);

}
