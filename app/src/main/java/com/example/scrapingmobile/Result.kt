package com.example.scrapingmobile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.TextView

class Result : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val textView = findViewById<View>(R.id.textView) as TextView
        textView.setMovementMethod(ScrollingMovementMethod.getInstance())
        for (element in MainActivity.result) {
            textView.append("${element.text()}\n")
        }
    }
}
