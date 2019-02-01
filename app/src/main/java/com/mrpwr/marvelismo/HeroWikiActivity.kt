package com.mrpwr.marvelismo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_hero_wiki.*

class HeroWikiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_wiki)

      var webView:WebView=findViewById(R.id.heroWikiWeb)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true

        val url:String= intent.getStringExtra("WIKI_URL")

        webView.loadUrl(url)
    }
}
