package com.cat.zhou.removtewebview.remotewebview.webviewchromeclient

import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cat.zhou.removtewebview.command.Command
import com.cat.zhou.removtewebview.command.Command.Companion.COMMAND_UPDATE_TITLE_PARAMS_TITLE
import com.cat.zhou.removtewebview.remotewebview.ProgressWebView
import com.cat.zhou.removtewebview.utils.WebConstants
import com.google.gson.Gson
import java.util.HashMap

/**
 * @author: zjf
 * @data:2019/12/19
 */
class TheWebChromeClient(private var handler: Handler) : WebChromeClient() {



    /**
     * 获取webView的头部信息
     */
    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        if (view is ProgressWebView) {
            if (!TextUtils.isEmpty(title)) {
                val params = HashMap<String, String>()
                params[COMMAND_UPDATE_TITLE_PARAMS_TITLE] = title!!
                (view as ProgressWebView).getWebViewCallBack().exec(view.getContext(), WebConstants.LEVEL_LOCAL, Command.COMMAND_UPDATE_TITLE, Gson().toJson(params), view)
            }
        }
    }


    /**
     * 加载进度的监听
     */
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        val message = Message()
        var progress = newProgress
        if (progress == 100) {
            message.obj = progress
            handler.sendMessageDelayed(message, 200)
        } else {
            if (progress < 10) {
                progress = 10
            }
            message.obj = progress
            handler.sendMessage(message)
        }
        super.onProgressChanged(view, progress)
    }

    /**
     * js的alert监听
     */
    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        AlertDialog.Builder(view!!.context)
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { dialoginterface, i ->
                //按钮事件
                Toast.makeText(
                    view.context,
                    view.context.getString(android.R.string.ok) + " clicked.",
                    Toast.LENGTH_LONG
                ).show()
            }.show()
        result!!.confirm()// 不加这行代码，会造成Alert劫持：Alert只会弹出一次，并且WebView会卡死
        return true
    }
}