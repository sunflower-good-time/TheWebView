package com.cat.zhou.removtewebview.remotewebview.webviewclient

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient

import androidx.annotation.RequiresApi

import com.cat.zhou.removtewebview.R
import com.cat.zhou.removtewebview.remotewebview.BaseWebView
import com.cat.zhou.removtewebview.remotewebview.callback.WebViewCallBack

class XiangxueWebviewClient(private val webView: WebView, private val webViewCallBack: WebViewCallBack?, private val mHeaders: Map<String, String>, private val mWebviewTouch: WebviewTouch) : WebViewClient() {
     var isReady: Boolean = false

    interface WebviewTouch {
        val isTouchByUser: Boolean
    }

    /**
     * url重定向会执行此方法以及点击页面某些链接也会执行此方法
     *
     * @return true:表示当前url已经加载完成，即使url还会重定向都不会再进行加载 false 表示此url默认由系统处理，该重定向还是重定向，直到加载完成
     */
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        Log.e(TAG, "shouldOverrideUrlLoading url: $url")
        // 当前链接的重定向, 通过是否发生过点击行为来判断
        if (!mWebviewTouch.isTouchByUser) {
            return super.shouldOverrideUrlLoading(view, url)
        }
        // 如果链接跟当前链接一样，表示刷新
        if (webView.url == url) {
            return super.shouldOverrideUrlLoading(view, url)
        }
        if (handleLinked(url)) {
            return true
        }
        if (webViewCallBack != null && webViewCallBack.overrideUrlLoading(view, url)) {
            return true
        }
        // 控制页面中点开新的链接在当前webView中打开
        view.loadUrl(url, mHeaders)
        return true
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        Log.e(TAG, "shouldOverrideUrlLoading url: " + request.url)
        // 当前链接的重定向
        if (!mWebviewTouch.isTouchByUser) {
            return super.shouldOverrideUrlLoading(view, request)
        }
        // 如果链接跟当前链接一样，表示刷新
        if (webView.url == request.url.toString()) {
            return super.shouldOverrideUrlLoading(view, request)
        }
        if (handleLinked(request.url.toString())) {
            return true
        }
        if (webViewCallBack != null && webViewCallBack.overrideUrlLoading(view, request.url.toString())) {
            return true
        }
        // 控制页面中点开新的链接在当前webView中打开
        view.loadUrl(request.url.toString(), mHeaders)
        return true
    }

    /**
     * 支持电话、短信、邮件、地图跳转，跳转的都是手机系统自带的应用
     */
    private fun handleLinked(url: String): Boolean {
        if (url.startsWith(WebView.SCHEME_TEL)
                || url.startsWith(SCHEME_SMS)
                || url.startsWith(WebView.SCHEME_MAILTO)
                || url.startsWith(WebView.SCHEME_GEO)) {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                webView.context.startActivity(intent)
            } catch (ignored: ActivityNotFoundException) {
                ignored.printStackTrace()
            }

            return true
        }
        return false
    }

    override fun onPageFinished(view: WebView, url: String) {
        Log.e(TAG, "onPageFinished url:$url")
        if (!TextUtils.isEmpty(url) && url.startsWith(BaseWebView.CONTENT_SCHEME)) {
            isReady = true
        }
        webViewCallBack?.pageFinished(url)
    }

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
        Log.e(TAG, "onPageStarted url: $url")
        webViewCallBack?.pageStarted(url)
    }

    override fun onScaleChanged(view: WebView, oldScale: Float, newScale: Float) {
        super.onScaleChanged(view, oldScale, newScale)
    }

    @TargetApi(21)
    override//url拦截
    fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
        return shouldInterceptRequest(view, request.url.toString())
    }

    override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
        return null
    }

    override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
        super.onReceivedError(view, errorCode, description, failingUrl)
        Log.e(TAG, "webview error$errorCode + $description")
        webViewCallBack?.onError()
    }

    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        val channel = ""
        if (!TextUtils.isEmpty(channel) && channel == "play.google.com") {
            val builder = AlertDialog.Builder(webView.context)
            var message = webView.context.getString(R.string.ssl_error)
            when (error.primaryError) {
                SslError.SSL_UNTRUSTED -> message = webView.context.getString(R.string.ssl_error_not_trust)
                SslError.SSL_EXPIRED -> message = webView.context.getString(R.string.ssl_error_expired)
                SslError.SSL_IDMISMATCH -> message = webView.context.getString(R.string.ssl_error_mismatch)
                SslError.SSL_NOTYETVALID -> message = webView.context.getString(R.string.ssl_error_not_valid)
            }
            message += webView.context.getString(R.string.ssl_error_continue_open)

            builder.setTitle(R.string.ssl_error)
            builder.setMessage(message)
            builder.setPositiveButton(R.string.continue_open) { dialog, which -> handler.proceed() }
            builder.setNegativeButton(R.string.cancel) { dialog, which -> handler.cancel() }
            val dialog = builder.create()
            dialog.show()
        } else {
            handler.proceed()
        }
    }

    companion object {

        private val TAG = "XXWebviewCallBack"
        val SCHEME_SMS = "sms:"
    }
}