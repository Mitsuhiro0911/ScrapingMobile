package com.example.scrapingmobile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import org.jsoup.Jsoup
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.coroutines.*
import org.jsoup.select.Elements
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter


class MainActivity : AppCompatActivity() {
    // TODO:ファイル出力出来るようにする
    // TODO:タグ設定のラジオボタンが両方選択されてしまうバグが発生
    companion object {
        // 全HTMLデータ
        var allHtml:String? = null
        // 抽出したHTMLデータを保持する
        val result: Elements = Elements()
        // 出力モード設定
        var outModeSetting = 1
        // タグ設定
        var tagSetting = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // ラジオボタンのIDが動的生成されないよう、固定値をセット
        val screenOutFlag = findViewById<View>(R.id.screenOutFlag) as RadioButton
        screenOutFlag.id = 0
        screenOutFlag.isChecked = true
        val fileOutFlag = findViewById<View>(R.id.fileOutFlag) as RadioButton
        fileOutFlag.id = -1

        // ラジオボタンのIDが動的生成されないよう、固定値をセット
        val taggedFlag = findViewById<View>(R.id.taggedFlag) as RadioButton
        taggedFlag.id = 0
        taggedFlag.isChecked = true
        val noTaggedFlag = findViewById<View>(R.id.noTaggedFlag) as RadioButton
        noTaggedFlag.id = -1

        val executeButton = findViewById<View>(R.id.executeButton)
        executeButton.setOnClickListener {
            runBlocking {
                // メインスレッドからHTTP通信はできないため、コルーチンで別スレッドを作成
                GlobalScope.async {
                    val scraping = Scraping(Jsoup.connect("https://ja.wikipedia.org/wiki/メインページ").get())
                    // 選択されたラジオボタンのidを取得
                    val tagRadioGroup = findViewById<View>(R.id.tagRadioGroup) as RadioGroup
                    tagSetting = tagRadioGroup.checkedRadioButtonId
//                val url = findViewById<View>(R.id.inputUrl) as TextInputEditText
//                val scraping = Scraping(Jsoup.connect(url.text.toString()).get())
                    val checkBoxAll = findViewById<View>(R.id.checkBoxAll) as CheckBox
                    // 「全HTML」のチェックボックスがONのとき
                    if (checkBoxAll.isChecked) {
                        allHtml = scraping.getAllHtml()
                    }
                    val checkBoxElement = findViewById<View>(R.id.checkBoxElement) as CheckBox
                    // 「要素」のチェックボックスがONのとき
                    if (checkBoxElement.isChecked) {
                        val elements = scraping.getInfoByElement("h2")
//                    val editTextElement = findViewById<View>(R.id.editTextElement) as EditText
//                    scraping.getInfoByElement(editTextElement.text.toString())
                        result.addAll(elements)
                    }
                    val checkBoxAttr = findViewById<View>(R.id.checkBoxAttr) as CheckBox
                    // 「属性」のチェックボックスがONのとき
                    if (checkBoxAttr.isChecked) {
                        val attrs = scraping.getInfoByAttr("href")
//                    val editTextAttr = findViewById<View>(R.id.editTextAttr) as EditText
//                    scraping.getInfoByAttr(editTextAttr.text.toString())
                        result.addAll(attrs)
                    }
                    val checkBoxId = findViewById<View>(R.id.checkBoxId) as CheckBox
                    // 「ID」のチェックボックスがONのとき
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
            // 選択されたラジオボタンのidを取得
            val outRadioGroup = findViewById<View>(R.id.outRadioGroup) as RadioGroup
            outModeSetting = outRadioGroup.checkedRadioButtonId
            // 画面出力モードの場合
            if (outModeSetting == KbnData.SCREEN_OUT) {
                // Resultページへ遷移
                val intent = Intent(this, Result::class.java)
                startActivityForResult(intent, 0)
            }
            // ファイル出力モードの場合
            else if (outModeSetting == KbnData.FILE_OUT) {
                writeFile()
            }
        }
    }

    private fun writeFile () {
        // APIレベルが19以下だとEnvironment.DIRECTORY_DOCUMENTSを使用できない
        if (Build.VERSION.SDK_INT >= 23) {
            val path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val fileName = "HTML抽出結果.txt"
            val file = File(path, fileName)
            val fileOutputStream = FileOutputStream(file)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            val bw = BufferedWriter(outputStreamWriter)
            // 全HTML情報を取得した場合
            if (allHtml != null) {
                bw.write(allHtml)
            }
            // 出力後、全HTMLデータをクリア
            allHtml = null
            // 抽出したHTMLデータを出力
            for (element in result) {
                // タグ付きの場合
                if (tagSetting == KbnData.TAGGED) {
                    bw.write("${element.html()}\n")
                    // タグなしの場合
                } else if (tagSetting == KbnData.NO_TAGGED) {
                    bw.write("${element.text()}\n")
                }
            }
            // 出力後、抽出したHTMLデータをクリア
            MainActivity.result.clear()
            bw.flush()
        }
    }
}
