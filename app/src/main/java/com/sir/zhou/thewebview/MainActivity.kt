package com.sir.zhou.thewebview

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.cat.zhou.removtewebview.command.Command
import com.cat.zhou.removtewebview.command.CommandsManager
import com.cat.zhou.removtewebview.command.ResultBack
import com.cat.zhou.removtewebview.remotewebview.BaseWebView
import com.cat.zhou.removtewebview.utils.WebConstants

class MainActivity : AppCompatActivity() {
    private lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(R.layout.activity_main)
        CommandsManager.instance.registerCommand(WebConstants.LEVEL_BASE, LoginCommand())
        findViewById<Button>(R.id.btn_local).setOnClickListener {
            WebActivity.startAppActivity(this, BaseWebView.CONTENT_SCHEME + "test.html")
        }
        findViewById<Button>(R.id.btn_baidu).setOnClickListener {
            WebActivity.startAppActivity(this, "https://www.baidu.com/")
        }
    }

    /**
     * 页面路由
     */
   inner class LoginCommand : Command {
        override fun name(): String {
            return "appLogin"
        }

        override fun exec(context: Context, params: Map<*, *>, resultBack: ResultBack) {
            Log.d("exce",params.toString()+"------------------")
            LoginActivity.startActivity(mContext,"")
        }
    }
}
