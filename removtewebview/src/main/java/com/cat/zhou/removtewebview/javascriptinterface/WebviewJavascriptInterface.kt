package com.cat.zhou.removtewebview.javascriptinterface

import android.content.Context
import android.os.Handler
import android.webkit.JavascriptInterface

/**
 *
 * 1. 保留command的注册
 * 2. 不支持command通过远程aidl方式调用
 */
class WebviewJavascriptInterface(private val mContext: Context) {
    private val mHandler = Handler()
    private var javascriptCommand: JavascriptCommand? = null

    @JavascriptInterface
    fun post(cmd: String, param: String) {
        mHandler.post {
            try {
                if (javascriptCommand != null) {
                    javascriptCommand!!.exec(mContext, cmd, param)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setJavascriptCommand(javascriptCommand: JavascriptCommand) {
        this.javascriptCommand = javascriptCommand
    }

    interface JavascriptCommand {
        fun exec(context: Context, cmd: String, params: String)
    }
}
