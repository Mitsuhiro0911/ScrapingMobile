package com.example.scrapingmobile

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class Scraping(val document:Document) {
    fun getAllHtml ():String? {
        return document.html()
    }
    fun getInfoByElement (element:String):Elements {
        // 要素の指定
        val elements = document.select(element)
//        for (element in elements) {
//            println(element.text())
//        }
        return elements
    }
    fun getInfoByAttr (attr:String):Elements {
        // 属性の指定
        val attributes = document.select("a").attr("abs:${attr}", "")
//        for (attribute in attributes) {
//            println(attribute)
//        }
        return attributes
    }
    fun getInfoById (id:String): Element {
        // IDの指定
        val id = document.getElementById("${id}")
//        println(id)
        return id
    }
}