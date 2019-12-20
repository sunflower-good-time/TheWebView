// IWebAidlHandleInterface.aidl
package com.cat.zhou.removtewebview;
import com.cat.zhou.removtewebview.IWebAidlCallBack;
// Declare any non-default types here with import statements

interface IWebAidlHandleInterface {
      void handleWebAction(int level, String actionName, String jsonParams, in IWebAidlCallBack callback);
}
