package com.cat.zhou.removtewebview.mainproess

import android.content.Context
import android.os.Process.myPid
import android.util.Log
import com.cat.zhou.removtewebview.IWebAidlCallBack
import com.cat.zhou.removtewebview.IWebAidlHandleInterface
import com.cat.zhou.removtewebview.command.Command
import com.cat.zhou.removtewebview.command.CommandsManager
import com.cat.zhou.removtewebview.command.ResultBack
import com.google.gson.Gson
import java.util.*


/**
 * @author: zjf
 * @data:2019/12/18
 */
class MainProAidlInterface(private val context: Context) : IWebAidlHandleInterface.Stub() {


    override fun handleWebAction(level: Int, actionName: String?, jsonParams: String?, callback: IWebAidlCallBack?) {
        try {
            Log.d("tagger","${level}---------------")
            handleRemoteAction(level, actionName!!, Gson().fromJson(jsonParams, Map::class.java), callback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleRemoteAction(level: Int, actionName: String, paramMap: Map<*, *>, callBack: IWebAidlCallBack?) {
        CommandsManager.instance.findAndExecRemoteCommand(context, level, actionName, paramMap,
            object : ResultBack {
                override fun onResult(status: Int, action: String, result: Any) {
                    try {
                        if (callBack != null) {
                            callBack.onResult(status, actionName, Gson().toJson(result))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
    }
}