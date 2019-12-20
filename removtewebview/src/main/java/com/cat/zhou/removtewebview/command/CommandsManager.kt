package com.cat.zhou.removtewebview.command

import android.content.Context
import android.util.Log
import com.cat.zhou.removtewebview.utils.AidlError
import com.cat.zhou.removtewebview.utils.WebConstants

class CommandsManager {

    companion object {
        var instance =CommandsManager()
    }

    private lateinit var baseLeveCommands: BaseLeveCommand
    private lateinit var accoutLeveCommands: AccoutLeveCommands
    private lateinit var localCommands: LocalCommands

    init {
        baseLeveCommands = BaseLeveCommand()
        accoutLeveCommands = AccoutLeveCommands()
        localCommands = LocalCommands()
    }

    /**
     * 动态注册Command
     */
    fun registerCommand(commandLeve: Int, command: Command?) {
        when (commandLeve) {
            WebConstants.LEVEL_LOCAL -> localCommands.registerCommand(command!!)
            WebConstants.LEVEL_BASE -> baseLeveCommands.registerCommand(command!!)
            WebConstants.LEVEL_ACCOUNT -> accoutLeveCommands.registerCommand(command!!)
        }
    }

    /**
     * 非UICommand执行
     */

    fun findAndExecRemoteCommand(context: Context, level: Int, action: String, params: Map<*, *>, requestBack: ResultBack) {
        Log.d("tagger","${level}---------------"+accoutLeveCommands.getCommands().toString())
        var methodFlag = false
        when (level) {
            WebConstants.LEVEL_ACCOUNT -> {
                baseLeveCommands.getCommands()[action]?.let {
                    methodFlag = true;
                    baseLeveCommands.getCommands()[action]!!.exec(context, params, requestBack)
                }
                accoutLeveCommands.getCommands()[action]?.let {
                    methodFlag = true;
                    baseLeveCommands.getCommands()[action]!!.exec(context, params, requestBack)
                }
            }
            WebConstants.LEVEL_BASE -> {
                baseLeveCommands.getCommands()[action]?.let {
                    methodFlag = true
                    baseLeveCommands.getCommands()[action]!!.exec(context, params, requestBack)
                }
                accoutLeveCommands.getCommands()[action]?.let {
                    val aidlError =
                        AidlError(WebConstants.ERRORCODE.NO_AUTH, WebConstants.ERRORMESSAGE.NO_AUTH)
                    requestBack.onResult(WebConstants.FAILED, action, aidlError)
                }
            }
        }
    }


    /**
     * Commands handled by Webview itself, these command does not require aidl.
     */
    fun findAndExceLocalCommand(context: Context, level: Int, action: String, params: Map<*, *>, requestBack: ResultBack) {
        localCommands.getCommands()[action]?.let {
            localCommands.getCommands()[action]!!.exec(context, params, requestBack)
        }
    }

    /**
     * 获取本地的Command
     */
    fun checkHitLocalCommand(action: String): Boolean {
        return localCommands.getCommands()[action]!=null
    }

}
