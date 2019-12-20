package com.cat.zhou.removtewebview.command

import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cat.zhou.removtewebview.utils.WebConstants
import com.cat.zhou.removtewebview.utils.WebUtils

/**
 * @author: zjf
 * @data:2019/12/19
 */
class LocalCommands : Commands {

    constructor() : super(){
        registCommands()
    }

    override fun getCommandLevel(): Int {
        return WebConstants.LEVEL_LOCAL
    }

    private fun registCommands() {
        registerCommand(showToastCommand)
        registerCommand(showDialogCommand)
    }

    /**
     * 显示Toast信息
     */
    private val showToastCommand = object : Command {
        override fun name(): String {
            return "showToast"
        }

        override fun exec(context: Context, params: Map<*, *>, resultBack: ResultBack) {
            Toast.makeText(context, params["message"].toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private val showDialogCommand = object : Command {
        override fun name(): String {
            return "showDialog"
        }

        override fun exec(context: Context, params: Map<*, *>, resultBack: ResultBack) {
            Log.i("tagger",params.toString())
            if (WebUtils.isNotNull(params)) {
                val title = params["title"] as String
                val content = params["content"] as String
                var canceledOutside = 1
                if (params["canceledOutside"] != null) {
                    canceledOutside = (params["canceledOutside"] as Double).toInt()
                }
                val buttons = params["buttons"] as List<Map<String, String>>?
                val callbackName = params[WebConstants.WEB2NATIVE_CALLBACk] as String?
                if (!TextUtils.isEmpty(content)) {
                    val dialog = AlertDialog.Builder(context)
                        .setTitle(title)
                        .setMessage(content)
                        .create()
                    dialog.setCanceledOnTouchOutside(if (canceledOutside == 1) true else false)
                    if (WebUtils.isNotNull(buttons)) {
                        if (buttons!=null){
                            for (data in 0..buttons!!.size-1) {
                                val button = buttons[data].toMutableMap()
                                val buttonWhich = getDialogButtonWhich(data)
                                if (buttonWhich == 0) return
                                dialog.setButton(buttonWhich, button["title"]) { dialog, which ->
                                    callbackName?.let {
                                        button[WebConstants.NATIVE2WEB_CALLBACK] = callbackName
                                    }
                                    resultBack.onResult(WebConstants.SUCCESS, name(), button)
                                }
                            }
                        }
                    }
                    dialog.show()
                }
            }
        }
    }

    private fun getDialogButtonWhich(index: Int): Int {
        when (index) {
            0 -> return DialogInterface.BUTTON_POSITIVE
            1 -> return DialogInterface.BUTTON_NEGATIVE
            2 -> return DialogInterface.BUTTON_NEUTRAL
        }
        return 0
    }
}