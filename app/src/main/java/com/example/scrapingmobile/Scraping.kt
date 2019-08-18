package com.example.scrapingmobile

import org.jsoup.nodes.Document

class Scraping(val document:Document) {
//    init {
//        println(document.html())
//    }
    fun getInfoByElement (element:String) {
        // 要素の指定
        val elements = document.select(element)
        for (element in elements) {
            println(element.text())
        }
    }
    fun getInfoByAttr (attr:String) {
        // 属性の指定
        val attributes = document.select("a").attr("abs:${attr}", "")
        for (attribute in attributes) {
            println(attribute)
        }
    }
    fun getInfoById (id:String) {
        // IDの指定
        val id = document.getElementById("${id}")
        println(id)
    }
}