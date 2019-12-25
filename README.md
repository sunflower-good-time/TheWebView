TheWebView
TheWebView

一、添加依赖
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

dependencies {
	        implementation 'com.github.sonflower:TheWebView:Tag'
	}
二、介紹、

前言：为什么要写出一个跨进程的webview，我们在开发过程中我们都知道，webview是一个非常耗费资源内存的控件，很容易造成内存溢出等问题，那么我们如何去解决这个问题呢，通过开起一个新的进程来加载WebView使用Bandler进程间通信，来完成js与native之间的通信。

三、使用

1)：创建WebActivity，这里我把WebView抽取成了两个Fragment：
CommonWebFragment 用于单页面的WebView，
AccountWebFragment 同时增添了请求头部的添加Header和是否同步Cookie 

/**
*@author: zjf
*@data:2019/12/20
*/
class WebActivity : AppCompatActivity() {
companion object{
​
    fun startAppActivity(context:Context,url: String) {
        var intent = Intent(context,WebActivity::class.java)
        intent.putExtra("Url", url)
        context.startActivity(intent)
    }
}
​
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_web_layout)
​
    val url = intent.getStringExtra("Url")
    val fragmentTransaction = supportFragmentManager.beginTransaction()
   fragmentTransaction.replace(R.id.web_body,CommonWebFragment.newInstance(url)).commit()
  }
}
​
​
​
2)：清单文件注册,开启一个remoteweb的进程
        

3)：Webview与Js通信，了解Command机制(注意：注册Command一定要在主进程中进程注册Command)

leve 级别：    
val LEVEL_LOCAL = 0 // local command, that is to say, this command execution does not require app.
val LEVEL_BASE = 1 // 基础level
val LEVEL_ACCOUNT = 2 // 涉及到账号相关的level
这里面CommonWebFragment  用的是LEVEL_BASE，AccountWebFragment 用于LEVEL_ACCOUNT,不同的Fragment使用不同的LEVEL
2.范例，这里我使用CommonWebFragment所以使用的是LEVEL_BASE,（注意，如果LEVEL不对的话Command是接受不到数据的）
 

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
 
