// IWebAidlCallBack.aidl.aidl
package com.cat.zhou.removtewebview;

// Declare any non-default types here with import statements

interface IWebAidlCallBack {
    void onResult(int responseCode, String actionName, String response);
}
