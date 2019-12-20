package com.sir.zhou.thewebview

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.alibaba.android.arouter.launcher.ARouter
import com.cat.zhou.removtewebview.command.Command
import com.cat.zhou.removtewebview.command.CommandsManager
import com.cat.zhou.removtewebview.command.ResultBack
import com.cat.zhou.removtewebview.remotewebview.BaseWebView
import com.cat.zhou.removtewebview.utils.WebConstants

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CommandsManager.instance.registerCommand(WebConstants.LEVEL_BASE, LoginCommand())
        findViewById<Button>(R.id.btn_local).setOnClickListener {
            WebActivity.startAppActivity(this, BaseWebView.CONTENT_SCHEME + "test.html")
        }
        findViewById<Button>(R.id.btn_baidu).setOnClickListener {
            WebActivity.startAppActivity(this, "https://www.baidu.com/")
        }
        findViewById<Button>(R.id.btn_arout).setOnClickListener {
            ARouter.getInstance().build("/app/test").navigation()
        }
    }

    /**
     * 页面路由
     */
    class LoginCommand : Command {
        override fun name(): String {
            return "appLogin"
        }

        override fun exec(context: Context, params: Map<*, *>, resultBack: ResultBack) {
            Log.d("exce",params.toString()+"------------------")
//            LoginActivity.startActivity(context,params.toString())
        }
    }
}
