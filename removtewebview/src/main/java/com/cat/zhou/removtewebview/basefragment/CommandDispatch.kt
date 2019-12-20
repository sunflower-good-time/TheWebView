package com.cat.zhou.removtewebview.basefragment

import android.content.Context
import android.os.Process.myPid
import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import com.cat.zhou.removtewebview.IWebAidlCallBack
import com.cat.zhou.removtewebview.IWebAidlHandleInterface
import com.cat.zhou.removtewebview.command.CommandsManager
import com.cat.zhou.removtewebview.command.ResultBack
import com.cat.zhou.removtewebview.mainproess.RemoteWebBinderPool
import com.cat.zhou.removtewebview.remotewebview.BaseWebView
import com.cat.zhou.removtewebview.utils.MainLooper
import com.cat.zhou.removtewebview.utils.SystemInfoUtil
import com.cat.zhou.removtewebview.utils.WebConstants
import com.google.gson.Gson

/**
 * @author: zjf
 * @data:2019/12/19
 */
class CommandDispatch {

    private var webAidlInterface : IWebAidlHandleInterface ? = null
    private val gson = Gson()
    /**
     * 单例
     */

    companion object {
        private var instance:CommandDispatch? = null
        fun getInstance(): CommandDispatch {
            if (instance == null) {
                synchronized(CommandDispatch::class.java) {
                    if (instance == null) {
                        instance = CommandDispatch()
                    }
                }
            }
            return instance!!
        }
    }


    /**
     * 注册
     */
    fun initAidlConnect(context: Context){
        if (webAidlInterface != null){
            return
        }
        Thread(Runnable {
            Log.i("AIDL", "Begin to connect with main process")
            val binderPool = RemoteWebBinderPool.getInstance(context)
            val iBinder = binderPool.queryBinder(RemoteWebBinderPool.BINDER_WEB_AIDL)
            webAidlInterface = IWebAidlHandleInterface.Stub.asInterface(iBinder)
            Log.i("AIDL", "Connect success with main process")
        }).start()
    }


    /**
     * 执行
     */
    fun exec(context:Context,commandLevel:Int,cmd:String,params:String,webView:WebView,dispatherCallBack:DispatcherCallBack?){
        if ( CommandsManager.instance.checkHitLocalCommand(cmd)){
            execLocalCommand(context, commandLevel, cmd, params, webView, dispatherCallBack)
        }else{
            execRemoteCommand(context, commandLevel, cmd, params, webView, dispatherCallBack)
        }
    }

    /**
     * 执行本地
     */
    private fun execLocalCommand(context:Context,commandLevel:Int,cmd:String,params:String,webView:WebView,dispatherCallBack:DispatcherCallBack?){
        val mapParams = gson.fromJson(params, Map::class.java)
        CommandsManager.instance.findAndExceLocalCommand(context,commandLevel,cmd,mapParams,
            object : ResultBack {
                override fun onResult(status: Int, action: String, result: Any) {
                    if (status == WebConstants.CONTINUE) {
                        execRemoteCommand(context, commandLevel, action, gson.toJson(result), webView, dispatherCallBack)
                    } else {
                        handleCallback(status, action, gson.toJson(result), webView, dispatherCallBack)
                    }
                }
            })
    }

    /**
     * 执行远程
     */
    private fun execRemoteCommand(context: Context, commandLevel: Int, cmd: String, params: String, webView: WebView, dispatcherCallBack: DispatcherCallBack?) {
        val mapParams = Gson().fromJson(params, Map::class.java)
        if (SystemInfoUtil.inMainProcess(context,myPid())){
            CommandsManager.instance.findAndExecRemoteCommand(context,commandLevel,cmd,mapParams, object : ResultBack {
                    override fun onResult(responseCode: Int, actionName: String, result: Any) {
                        handleCallback(responseCode, actionName, Gson().toJson(result), webView, dispatcherCallBack)
                    }
                })
        }else{
            if (webAidlInterface!=null) {
                webAidlInterface!!.handleWebAction(commandLevel,cmd,params,IWebAidlCallBackImpl(webView,dispatcherCallBack))
            }
        }
    }



    private fun handleCallback(responseCode: Int, actionName: String, response: String, webView: WebView, dispatcherCallBack: DispatcherCallBack?) {
        Log.d("CommandDispatcher", String.format("Callback result: action= %s, result= %s", actionName, response))
        MainLooper.runOnUiThread(Runnable {
            dispatcherCallBack?.preHandleBeforeCallback(responseCode, actionName, response)
            val params = Gson().fromJson(response, Map::class.java)
            if (params[WebConstants.NATIVE2WEB_CALLBACK] != null && !TextUtils.isEmpty(params[WebConstants.NATIVE2WEB_CALLBACK]!!.toString())) {
                if (webView is BaseWebView) {
                    (webView as BaseWebView).handleCallback(response)
                }
            }
        })
    }

    /**
     * Dispatcher 过程中的回调介入
     */
    interface DispatcherCallBack {
        fun preHandleBeforeCallback(responseCode: Int, actionName: String, response: String): Boolean
    }

   inner class IWebAidlCallBackImpl(private val webView: WebView, private val dispatherCallBack: DispatcherCallBack?) : IWebAidlCallBack.Stub(){
        override fun onResult(responseCode: Int, actionName: String?, response: String?) {
            handleCallback(responseCode, actionName!!, response!!, webView, dispatherCallBack)
        }
    }
}