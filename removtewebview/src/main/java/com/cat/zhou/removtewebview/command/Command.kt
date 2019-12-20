package com.cat.zhou.removtewebview.command

import android.content.Context

/**
 * @author: zjf
 * @data:2019/12/18
 */
interface Command {
    companion object {
        val COMMAND_UPDATE_TITLE = "xiangxue_webview_update_title"
        val COMMAND_UPDATE_TITLE_PARAMS_TITLE = "xiangxue_webview_update_title_params_title"
    }
    fun name(): String
    fun exec(context: Context, params: Map<*, *>, resultBack: ResultBack)
}