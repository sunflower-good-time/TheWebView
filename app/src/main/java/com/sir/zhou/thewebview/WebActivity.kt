package com.sir.zhou.thewebview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cat.zhou.removtewebview.CommonWebFragment
import com.cat.zhou.removtewebview.command.Command
import com.cat.zhou.removtewebview.command.CommandsManager
import com.cat.zhou.removtewebview.command.ResultBack
import com.cat.zhou.removtewebview.utils.WebConstants

/**
 * @author: zjf
 * @data:2019/12/20
 */
class WebActivity : AppCompatActivity() {


    companion object{

        fun startAppActivity(context:Context,url: String) {
            var intent = Intent(context,WebActivity::class.java)
            intent.putExtra("Url", url)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_layout)

        val url = intent.getStringExtra("Url")
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.web_body, CommonWebFragment.newInstance(url)).commit()
    }
}