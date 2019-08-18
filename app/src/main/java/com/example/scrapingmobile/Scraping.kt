package com.example.scrapingmobile

import org.jsoup.nodes.Document

class Scraping(val document:Document) {
    init {
        println(document.html())
    }
    private fun getInfoByElement () {
        // 要素の指定
        val elements = document.select("h2")
        for (element in elements) {
            println(element.text())
        }
    }
    private fun getInfoByAttr () {
        // 属性の指定
        val attributes = document.select("a").attr("abs:href", "")
        for (attribute in attributes) {
            println(attribute)
        }
    }
    private fun getInfoById () {
        // IDの指定
        val id = document.getElementById("top")
        println(id)
    }
}