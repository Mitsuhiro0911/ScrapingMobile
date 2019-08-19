package com.example.scrapingmobile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import org.jsoup.Jsoup
import android.content.Intent
import kotlinx.coroutines.*
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class MainActivity : AppCompatActivity() {
    companion object {
        // 抽出したHTMLデータを保持する
        val result: Elements = Elements()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val executeButton = findViewById<View>(R.id.executeButton)
        executeButton.setOnClickListener {
            runBlocking {
                // メインスレッドからHTML通信はできないため、コルーチンで別スレッドを作成
                GlobalScope.async {
                    val scraping = Scraping(Jsoup.connect("https://ja.wikipedia.org/wiki/メインページ").get())
//                val url = findViewById<View>(R.id.inputUrl) as TextInputEditText
//                val scraping = Scraping(Jsoup.connect(url.text.toString()).get())
                    val checkBoxElement = findViewById<View>(R.id.checkBoxElement) as CheckBox
                    if (checkBoxElement.isChecked) {
                        val elements = scraping.getInfoByElement("h2")
//                    val editTextElement = findViewById<View>(R.id.editTextElement) as EditText
//                    scraping.getInfoByElement(editTextElement.text.toString())
                        result.addAll(elements)
                    }
                    val checkBoxAttr = findViewById<View>(R.id.checkBoxAttr) as CheckBox
                    if (checkBoxAttr.isChecked) {
                        val attrs = scraping.getInfoByAttr("href")
//                    val editTextAttr = findViewById<View>(R.id.editTextAttr) as EditText
//                    scraping.getInfoByAttr(editTextAttr.text.toString())
                        result.addAll(attrs)
                    }
                    val checkBoxId = findViewById<View>(R.id.checkBoxId) as CheckBox
                    if (checkBoxId.isChecked) {
                        val id = scraping.getInfoById("top")
//                    val editTextId = findViewById<View>(R.id.editTextId) as EditText
//                    scraping.getInfoById(editTextId.text.toString())
                        result.add(id)
                    }
                    println(result)
                    // awaitで同期を取る。HTMLデータ取得前にページ遷移することを防ぐ
                }.await()
            }
            // Resultページへ遷移
            val intent = Intent(this, Result::class.java)
            startActivityForResult(intent, 0)
        }
    }
}
