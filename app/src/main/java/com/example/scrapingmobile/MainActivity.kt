package com.example.scrapingmobile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.util.Log
import android.view.View
import android.widget.CheckBox
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val executeButton = findViewById<View>(R.id.executeButton)
        executeButton.setOnClickListener {
            // メインスレッドからHTML通信はできないため、コルーチンで別スレッドを作成
            GlobalScope.launch {
//                val scraping = Scraping(Jsoup.connect("https://ja.wikipedia.org/wiki/メインページ").get())
                val url = findViewById<View>(R.id.inputUrl) as TextInputEditText
                val scraping = Scraping(Jsoup.connect(url.text.toString()).get())
                val checkBoxElement = findViewById<View>(R.id.checkBoxElement) as CheckBox
                if (checkBoxElement.isChecked) {
                    scraping.getInfoByElement()
                }
                val checkBoxAttr = findViewById<View>(R.id.checkBoxAttr) as CheckBox
                if (checkBoxAttr.isChecked) {
                    scraping.getInfoByAttr()
                }
                val checkBoxId = findViewById<View>(R.id.checkBoxId) as CheckBox
                if (checkBoxId.isChecked) {
                    scraping.getInfoById()
                }
            }
        }
    }
}
