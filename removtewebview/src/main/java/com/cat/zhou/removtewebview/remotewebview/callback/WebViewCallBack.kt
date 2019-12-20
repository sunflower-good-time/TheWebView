package com.cat.zhou.removtewebview.remotewebview.callback

import android.content.Context
import android.webkit.WebView

/**
 * @author: zjf
 * @data:2019/12/19
 */
interface WebViewCallBack {
    abstract fun getCommandLevel(): Int

    abstract fun pageStarted(url: String)

    abstract fun pageFinished(url: String)

    abstract fun overrideUrlLoading(view: WebView, url: String): Boolean

    abstract fun onError()

    abstract fun exec(
        context: Context,
        commandLevel: Int,
        cmd: String,
        params: String,
        webView: WebView
    )
}