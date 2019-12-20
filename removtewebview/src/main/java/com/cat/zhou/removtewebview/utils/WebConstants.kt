package com.cat.zhou.removtewebview.utils

/**
 * @author: zjf
 * @data:2019/12/19
 */
class WebConstants {

    companion object {
        val LEVEL_LOCAL = 0 // local command, that is to say, this command execution does not require app.
        val LEVEL_BASE = 1 // 基础level
        val LEVEL_ACCOUNT = 2 // 涉及到账号相关的level

        val CONTINUE = 2 // 继续分发command
        val SUCCESS = 1 // 成功
        val FAILED = 0 // 失败
        val EMPTY = "" // 无返回结果

        val WEB2NATIVE_CALLBACk = "callback"
        val NATIVE2WEB_CALLBACK = "callbackname"

        val ACTION_EVENT_BUS = "eventBus"

        val INTENT_TAG_TITLE = "title"
        val INTENT_TAG_URL = "url"
        val INTENT_TAG_HEADERS = "headers"
    }

    class ERRORCODE {
        companion object {
            val NO_METHOD = -1000
            val NO_AUTH = -1001
            val NO_LOGIN = -1002
            val ERROR_PARAM = -1003
            val ERROR_EXCEPTION = -1004
        }
    }

    class ERRORMESSAGE {
        companion object {
            val NO_METHOD = "方法找不到"
            val NO_AUTH = "方法权限不够"
            val NO_LOGIN = "尚未登录"
            val ERROR_PARAM = "参数错误"
            val ERROR_EXCEPTION = "未知异常"
        }

    }
}