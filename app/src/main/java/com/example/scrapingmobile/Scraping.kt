package com.example.scrapingmobile

import org.jsoup.Jsoup

class Scraping {
    fun test () {
        // URLの指定
        val document = Jsoup.connect("https://ja.wikipedia.org/wiki/メインページ").get()
        println(document.html())
        // 要素の指定
        val elements = document.select("h2")
        for (element in elements) {
            println(element.text())
        }
        // 属性の指定
        val attributes = document.select("a").attr("abs:href", "")
        for (attribute in attributes) {
            println(attribute)
        }
        // IDの指定
        val id = document.getElementById("top")
        println(id)
    }

}