package com.cat.zhou.removtewebview.basefragment

import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView

import com.cat.zhou.removtewebview.R
import com.cat.zhou.removtewebview.remotewebview.BaseWebView
import com.cat.zhou.removtewebview.remotewebview.callback.WebViewCallBack
import com.cat.zhou.removtewebview.utils.WebConstants

import java.util.HashMap

open abstract class BaseWebviewFragment : BaseFragment(), WebViewCallBack {
    protected var webView: BaseWebView? = null
    protected var accountInfoHeaders: HashMap<String, String>? = null

    var webUrl: String? = null

    abstract fun layoutRes(): Int

    protected val dispatcherCallBack: CommandDispatch.DispatcherCallBack? get() = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            webUrl = bundle.getString(WebConstants.INTENT_TAG_URL)
            if (bundle.containsKey(ACCOUNT_INFO_HEADERS)) {
                accountInfoHeaders =
                    bundle.getSerializable(ACCOUNT_INFO_HEADERS) as HashMap<String, String>
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(layoutRes(),null)
        webView = view.findViewById(R.id.web_view)
        if (accountInfoHeaders != null) {
            webView!!.setHeaders(accountInfoHeaders!!)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView!!.registerdWebViewCallBack(this)
        CommandDispatch.getInstance().initAidlConnect(context!!)
        loadUrl()
    }

    protected fun loadUrl() {
        webView!!.loadUrl(webUrl)
    }

    override fun onResume() {
        super.onResume()
        webView!!.dispatchEvent("pageResume")
        webView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView!!.dispatchEvent("pagePause")
        webView!!.onPause()
    }

    override fun onStop() {
        super.onStop()
        webView!!.dispatchEvent("pageStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webView!!.dispatchEvent("pageDestroy")
        clearWebView(webView)
    }


    override fun getCommandLevel(): Int {
        return WebConstants.LEVEL_BASE
    }

    override fun pageStarted(url: String) {

    }

    override fun pageFinished(url: String) {

    }

    override fun overrideUrlLoading(view: WebView, url: String): Boolean {
        return false
    }

    override fun onError() {

    }

    override fun exec(context: Context, commandLevel: Int, cmd: String, params: String, webView: WebView) {
        Log.d("leve","${commandLevel}---------------")
        CommandDispatch.getInstance()
            .exec(context, commandLevel, cmd, params, webView, dispatcherCallBack)
    }

    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            onBackHandle()
        } else false
    }

    protected fun onBackHandle(): Boolean {
        if (webView != null) {
            if (webView!!.canGoBack()) {
                webView!!.goBack()
                return true
            } else {
                return false
            }
        }
        return false
    }

    private fun clearWebView(m: WebView?) {
        var m: WebView? = m ?: return
        if (Looper.myLooper() != Looper.getMainLooper())
            return
        m!!.stopLoading()
        if (m.handler != null) {
            m.handler.removeCallbacksAndMessages(null)
        }
        m.removeAllViews()
        val mViewGroup = m.parent as ViewGroup
        if (mViewGroup!= null) {
            mViewGroup.removeView(m)
        }
        m.webChromeClient = null
        m.webViewClient = null
        m.tag = null
        m.clearHistory()
        m.destroy()
        m = null
    }

    companion object {
        val ACCOUNT_INFO_HEADERS = "account_header"
    }
}
