package com.example.scrapingmobile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // メインスレッドからHTML通信はできないため、コルーチンで別スレッドを作成
        GlobalScope.launch {
            val scraping = Scraping(Jsoup.connect("https://ja.wikipedia.org/wiki/メインページ").get())
        }
    }
}
