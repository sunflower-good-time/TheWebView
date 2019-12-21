<!DOCTYPE html> <html lang="zh"> <head> <meta charset="utf-8"/> 
  </head> <body><h1 id="h1-thewebview"><a name="TheWebView" class="reference-link"></a><span class="header-link octicon octicon-link"></span>TheWebView</h1><p>TheWebView</p> <p><strong>一、添加依赖</strong></p> <pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="pln">allprojects </span><span class="pun">{</span></code></li><li class="L1"><code><span class="pln"> repositories </span><span class="pun">{</span></code></li><li class="L2"><code><span class="pln"> </span><span class="pun">...</span></code></li><li class="L3"><code><span class="pln"> maven </span><span class="pun">{</span><span class="pln"> url </span><span class="str">'https://jitpack.io'</span><span class="pln"> </span><span class="pun">}</span></code></li><li class="L4"><code><span class="pln"> </span><span class="pun">}</span></code></li><li class="L5"><code><span class="pln"> </span><span class="pun">}</span></code></li><li class="L6"><code></code></li><li class="L7"><code><span class="pln">dependencies </span><span class="pun">{</span></code></li><li class="L8"><code><span class="pln"> implementation</span><span class="str">'com.github.ZhouJianFengFirst:TheWebView:1.0'</span></code></li><li class="L9"><code><span class="pln"> </span><span class="pun">}</span></code></li></ol></pre><p>二、介紹、</p> <p>前言：为什么要写出一个跨进程的webview，我们在开发过程中我们都知道，webview是一个非常耗费资源内存的控件，很容易造成内存溢出等问题，那么我们如何去解决这个问题呢，通过开起一个新的进程来加载WebView使用Bandler进程间通信，来完成js与native之间的通信。</p> <p>三、使用</p> <p>1)创建WebActivity，这里我把WebView抽取成了两个Fragment：<br>CommonWebFragment 用于单页面的WebView，<br>AccountWebFragment 同时增添了请求头部的添加Header和是否同步Cookie </p><pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code class="lang-java"><span class="com">/**</span></code></li><li class="L1"><code class="lang-java"><span class="com"> * @author: zjf</span></code></li><li class="L2"><code class="lang-java"><span class="com"> * @data:2019/12/20</span></code></li><li class="L3"><code class="lang-java"><span class="com"> */</span></code></li><li class="L4"><code class="lang-java"><span class="pln"> </span><span class="kwd">class</span><span class="pln"> </span><span class="typ">WebActivity</span><span class="pln"> </span><span class="pun">:</span><span class="pln"> </span><span class="typ">AppCompatActivity</span><span class="pun">()</span><span class="pln"> </span><span class="pun">{</span></code></li><li class="L5"><code class="lang-java"><span class="pln"> companion object</span><span class="pun">{</span></code></li><li class="L6"><code class="lang-java"><span class="pln"> fun startAppActivity</span><span class="pun">(</span><span class="pln">context</span><span class="pun">:</span><span class="typ">Context</span><span class="pun">,</span><span class="pln">url</span><span class="pun">:</span><span class="pln"> </span><span class="typ">String</span><span class="pun">)</span><span class="pln"> </span><span class="pun">{</span></code></li><li class="L7"><code class="lang-java"><span class="pln"> var intent </span><span class="pun">=</span><span class="pln"> </span><span class="typ">Intent</span><span class="pun">(</span><span class="pln">context</span><span class="pun">,</span><span class="typ">WebActivity</span><span class="pun">::</span><span class="kwd">class</span><span class="pun">.</span><span class="pln">java</span><span class="pun">)</span></code></li><li class="L8"><code class="lang-java"><span class="pln"> intent</span><span class="pun">.</span><span class="pln">putExtra</span><span class="pun">(</span><span class="str">"Url"</span><span class="pun">,</span><span class="pln"> url</span><span class="pun">)</span></code></li><li class="L9"><code class="lang-java"><span class="pln"> context</span><span class="pun">.</span><span class="pln">startActivity</span><span class="pun">(</span><span class="pln">intent</span><span class="pun">)</span></code></li><li class="L0"><code class="lang-java"><span class="pln"> </span><span class="pun">}</span></code></li><li class="L1"><code class="lang-java"><span class="pln"> </span><span class="pun">}</span></code></li><li class="L2"><code class="lang-java"><span class="pln"> override fun onCreate</span><span class="pun">(</span><span class="pln">savedInstanceState</span><span class="pun">:</span><span class="pln"> </span><span class="typ">Bundle</span><span class="pun">?)</span><span class="pln"> </span><span class="pun">{</span></code></li><li class="L3"><code class="lang-java"><span class="pln"> </span><span class="kwd">super</span><span class="pun">.</span><span class="pln">onCreate</span><span class="pun">(</span><span class="pln">savedInstanceState</span><span class="pun">)</span></code></li><li class="L4"><code class="lang-java"><span class="pln"> setContentView</span><span class="pun">(</span><span class="pln">R</span><span class="pun">.</span><span class="pln">layout</span><span class="pun">.</span><span class="pln">activity_web_layout</span><span class="pun">)</span></code></li><li class="L5"><code class="lang-java"><span class="pln"> val url </span><span class="pun">=</span><span class="pln"> intent</span><span class="pun">.</span><span class="pln">getStringExtra</span><span class="pun">(</span><span class="str">"Url"</span><span class="pun">)</span></code></li><li class="L6"><code class="lang-java"><span class="pln"> val fragmentTransaction </span><span class="pun">=</span><span class="pln"> supportFragmentManager</span><span class="pun">.</span><span class="pln">beginTransaction</span><span class="pun">()</span></code></li><li class="L7"><code class="lang-java"><span class="pln"> fragmentTransaction</span><span class="pun">.</span><span class="pln">replace</span><span class="pun">(</span><span class="pln">R</span><span class="pun">.</span><span class="pln">id</span><span class="pun">.</span><span class="pln">web_body</span><span class="pun">,</span><span class="pln"> </span><span class="typ">CommonWebFragment</span><span class="pun">.</span><span class="pln">newInstance</span><span class="pun">(</span><span class="pln">url</span><span class="pun">)).</span><span class="pln">commit</span><span class="pun">()</span></code></li><li class="L8"><code class="lang-java"><span class="pln"> </span><span class="pun">}</span></code></li><li class="L9"><code class="lang-java"><span class="pln"> </span><span class="pun">}</span></code></li></ol></pre> <p>2)清单文件注册,开启一个remoteweb的进程</p> <pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="pln"> </span><span class="tag">&lt;activity</span></code></li><li class="L1"><code><span class="pln"> </span><span class="atn">android:name</span><span class="pun">=</span><span class="atv">".WebActivity"</span></code></li><li class="L2"><code><span class="pln"> </span><span class="atn">android:hardwareAccelerated</span><span class="pun">=</span><span class="atv">"true"</span></code></li><li class="L3"><code><span class="pln"> </span><span class="atn">android:process</span><span class="pun">=</span><span class="atv">":remoteweb"</span><span class="pln"> </span><span class="tag">/&gt;</span></code></li></ol></pre><p>3)Webview与Js通信，了解Command机制(注意：注册Command一定要在主进程中进程注册Command)</p> <pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="lit">1.</span><span class="pln"> leve </span><span class="pun">级别：</span><span class="pln"> </span></code></li><li class="L1"><code><span class="pln"> val LEVEL_LOCAL </span><span class="pun">=</span><span class="pln"> </span><span class="lit">0</span><span class="pln"> </span><span class="com">// local command, that is to say, this command execution does not require app.</span></code></li><li class="L2"><code><span class="pln"> val LEVEL_BASE </span><span class="pun">=</span><span class="pln"> </span><span class="lit">1</span><span class="pln"> </span><span class="com">// 基础level</span></code></li><li class="L3"><code><span class="pln"> val LEVEL_ACCOUNT </span><span class="pun">=</span><span class="pln"> </span><span class="lit">2</span><span class="pln"> </span><span class="com">// 涉及到账号相关的level</span></code></li><li class="L4"><code><span class="pln"> </span><span class="pun">这里面</span><span class="typ">CommonWebFragment</span><span class="pln"> </span><span class="pun">用的是</span><span class="pln">LEVEL_BASE</span><span class="pun">，</span><span class="typ">AccountWebFragment</span><span class="pln"> </span><span class="pun">用于</span><span class="pln">LEVEL_ACCOUNT</span><span class="pun">,不同的</span><span class="typ">Fragment</span><span class="pun">使用不同的</span><span class="pln">LEVEL</span></code></li><li class="L5"><code></code></li><li class="L6"><code></code></li><li class="L7"><code><span class="lit">2.</span><span class="pun">范例，这里我使用</span><span class="typ">CommonWebFragment</span><span class="pun">所以使用的是</span><span class="pln">LEVEL_BASE</span><span class="pun">,（注意，如果</span><span class="pln">LEVEL</span><span class="pun">不对的话</span><span class="typ">Command</span><span class="pun">是接受不到数据的）</span></code></li></ol></pre><pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code class="lang-java"></code></li><li class="L1"><code class="lang-java"><span class="pln"> </span><span class="kwd">class</span><span class="pln"> </span><span class="typ">MainActivity</span><span class="pln"> </span><span class="pun">:</span><span class="pln"> </span><span class="typ">AppCompatActivity</span><span class="pun">()</span><span class="pln"> </span><span class="pun">{</span></code></li><li class="L2"><code class="lang-java"><span class="pln"> </span><span class="kwd">private</span><span class="pln"> lateinit var mContext</span><span class="pun">:</span><span class="pln"> </span><span class="typ">Context</span></code></li><li class="L3"><code class="lang-java"><span class="pln"> override fun onCreate</span><span class="pun">(</span><span class="pln">savedInstanceState</span><span class="pun">:</span><span class="pln"> </span><span class="typ">Bundle</span><span class="pun">?)</span><span class="pln"> </span><span class="pun">{</span></code></li><li class="L4"><code class="lang-java"><span class="pln"> </span><span class="kwd">super</span><span class="pun">.</span><span class="pln">onCreate</span><span class="pun">(</span><span class="pln">savedInstanceState</span><span class="pun">)</span></code></li><li class="L5"><code class="lang-java"><span class="pln"> mContext </span><span class="pun">=</span><span class="pln"> </span><span class="kwd">this</span></code></li><li class="L6"><code class="lang-java"><span class="pln"> setContentView</span><span class="pun">(</span><span class="pln">R</span><span class="pun">.</span><span class="pln">layout</span><span class="pun">.</span><span class="pln">activity_main</span><span class="pun">)</span></code></li><li class="L7"><code class="lang-java"><span class="pln"> </span><span class="typ">CommandsManager</span><span class="pun">.</span><span class="pln">instance</span><span class="pun">.</span><span class="pln">registerCommand</span><span class="pun">(</span><span class="typ">WebConstants</span><span class="pun">.</span><span class="pln">LEVEL_BASE</span><span class="pun">,</span><span class="typ">LoginCommand</span><span class="pun">())</span></code></li><li class="L8"><code class="lang-java"><span class="pln"> findViewById</span><span class="pun">&lt;</span><span class="typ">Button</span><span class="pun">&gt;(</span><span class="pln">R</span><span class="pun">.</span><span class="pln">id</span><span class="pun">.</span><span class="pln">btn_local</span><span class="pun">).</span><span class="pln">setOnClickListener </span><span class="pun">{</span></code></li><li class="L9"><code class="lang-java"><span class="pln"> </span><span class="typ">WebActivity</span><span class="pun">.</span><span class="pln">startAppActivity</span><span class="pun">(</span><span class="kwd">this</span><span class="pun">,</span><span class="pln"> </span><span class="typ">BaseWebView</span><span class="pun">.</span><span class="pln">CONTENT_SCHEME </span><span class="pun">+</span><span class="str">"test.html"</span><span class="pun">)</span></code></li><li class="L0"><code class="lang-java"><span class="pln"> </span><span class="pun">}</span></code></li><li class="L1"><code class="lang-java"><span class="pln"> findViewById</span><span class="pun">&lt;</span><span class="typ">Button</span><span class="pun">&gt;(</span><span class="pln">R</span><span class="pun">.</span><span class="pln">id</span><span class="pun">.</span><span class="pln">btn_baidu</span><span class="pun">).</span><span class="pln">setOnClickListener </span><span class="pun">{</span></code></li><li class="L2"><code class="lang-java"><span class="pln"> </span><span class="typ">WebActivity</span><span class="pun">.</span><span class="pln">startAppActivity</span><span class="pun">(</span><span class="kwd">this</span><span class="pun">,</span><span class="pln"> </span><span class="str">"https://www.baidu.com/"</span><span class="pun">)</span></code></li><li class="L3"><code class="lang-java"><span class="pln"> </span><span class="pun">}</span></code></li><li class="L4"><code class="lang-java"><span class="pln"> </span><span class="pun">}</span></code></li><li class="L5"><code class="lang-java"></code></li><li class="L6"><code class="lang-java"><span class="pln"> </span><span class="com">/**</span></code></li><li class="L7"><code class="lang-java"><span class="com"> * 页面路由</span></code></li><li class="L8"><code class="lang-java"><span class="com"> */</span></code></li><li class="L9"><code class="lang-java"><span class="pln"> inner </span><span class="kwd">class</span><span class="pln"> </span><span class="typ">LoginCommand</span><span class="pln"> </span><span class="pun">:</span><span class="pln"> </span><span class="typ">Command</span><span class="pln"> </span><span class="pun">{</span></code></li><li class="L0"><code class="lang-java"><span class="pln"> override fun name</span><span class="pun">():</span><span class="pln"> </span><span class="typ">String</span><span class="pln"> </span><span class="pun">{</span></code></li><li class="L1"><code class="lang-java"><span class="pln"> </span><span class="kwd">return</span><span class="pln"> </span><span class="str">"appLogin"</span></code></li><li class="L2"><code class="lang-java"><span class="pln"> </span><span class="pun">}</span></code></li><li class="L3"><code class="lang-java"></code></li><li class="L4"><code class="lang-java"><span class="pln"> override fun exec</span><span class="pun">(</span><span class="pln">context</span><span class="pun">:</span><span class="pln"> </span><span class="typ">Context</span><span class="pun">,</span><span class="pln"> params</span><span class="pun">:</span><span class="pln"> </span><span class="typ">Map</span><span class="pun">&lt;*,</span><span class="pln"> </span><span class="pun">*&gt;,</span><span class="pln"> resultBack</span><span class="pun">:</span><span class="pln"> </span><span class="typ">ResultBack</span><span class="pun">){</span></code></li><li class="L5"><code class="lang-java"><span class="pln"> </span><span class="typ">Log</span><span class="pun">.</span><span class="pln">d</span><span class="pun">(</span><span class="str">"exce"</span><span class="pun">,</span><span class="pln">params</span><span class="pun">.</span><span class="pln">toString</span><span class="pun">()+</span><span class="str">"------------------"</span><span class="pun">)</span></code></li><li class="L6"><code class="lang-java"><span class="pln"> </span><span class="typ">LoginActivity</span><span class="pun">.</span><span class="pln">startActivity</span><span class="pun">(</span><span class="pln">mContext</span><span class="pun">,</span><span class="str">""</span><span class="pun">)</span></code></li><li class="L7"><code class="lang-java"><span class="pln"> </span><span class="pun">}</span></code></li><li class="L8"><code class="lang-java"><span class="pln"> </span><span class="pun">}</span></code></li><li class="L9"><code class="lang-java"><span class="pun">}</span></code></li></ol></pre> </body> </html>
