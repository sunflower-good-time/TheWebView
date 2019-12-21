package com.sir.zhou.thewebview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @author: zjf
 * @data:2019/12/20
 */
class LoginActivity :AppCompatActivity() {

    companion object {
        fun startActivity(context:Context,cmd: String) {
            var intent = Intent(context,LoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("cmd", cmd)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_layout)
    }
}