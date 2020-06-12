package com.cottonwind.webviewcw

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.*
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var button_back: Button
    private lateinit var button_forward: Button
    private lateinit var button_home: Button
    private lateinit var button_reload: Button
    private lateinit var button_setting: Button

    //エコー写真のファイル送信を可能にする
    private var INTENT_CODE = 101
    private var mUploadMessage: ValueCallback<Uri?>? = null
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.webView)
        button_back = findViewById(R.id.button_back)
        button_forward = findViewById(R.id.button_forward)
        button_home = findViewById(R.id.button_home)
        button_reload = findViewById(R.id.button_reload)
        button_setting = findViewById(R.id.button_setting)
        webView.getSettings().setJavaScriptEnabled(true)
        webView.setWebViewClient(object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if (request?.url?.scheme == "http" || request?.url?.scheme == "https" || request == null) {
                    return false
                }
                val shareIntent: Intent = Uri.parse(
                    request.url.toString()
                ).let { location ->
                    Intent(Intent.ACTION_VIEW, location)
                }
                val apps = packageManager.queryIntentActivities(shareIntent, 0)
                if (apps.size > 0) {
                    startActivity(shareIntent)
                } else {
                    Toast.makeText(applicationContext, "開くことができませんでした", Toast.LENGTH_LONG).show()
                }
                return true
            }
        })
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
        webView.apply {
            WebSettings.FORCE_DARK_ON
        }
        button_back.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            }
        }
        button_forward.setOnClickListener {
            if (webView.canGoForward()) {
                webView.goForward()
            }
        }
        button_home.setOnClickListener {
            webView.loadUrl(getString(R.string.default_path_home))
        }
        button_reload.setOnClickListener {
            webView.reload()
        }
        if (intent.dataString === null) {
            webView.loadUrl(getString(R.string.default_path_home))
        } else {
            webView.loadUrl(intent.dataString)
        }
    }
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            webView.clearCache(true)
            super.onBackPressed()
        }
    }
}