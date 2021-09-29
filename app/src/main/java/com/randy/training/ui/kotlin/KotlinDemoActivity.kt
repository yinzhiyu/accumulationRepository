package com.randy.training.ui.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.randy.training.R
import com.randy.training.ui.kotlin.bean.BeanDemo1Dto

class KotlinDemoActivity : AppCompatActivity() {
    var beanDemo1Dto: BeanDemo1Dto? = null
    val listDemo = listOf("apple", "banana", "kiwifruit")
    val items1 = setOf("apple", "banana", "kiwifruit")
    var items2 = mutableListOf("apple", "banana", "kiwifruit")
    val map = mapOf("a" to 1, "b" to 2, "c" to 3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_demo)
        initData()
    }

    private fun initData() {
        val xxx: String = beanDemo1Dto!!.message

        /**
         * 过滤 list
         */
        var listDemo2 = listDemo.filter {
            it.startsWith("b")
        }

        /**
         * 映射
         */
        val fruits = listOf("banana", "avocado", "apple", "kiwifruit")
        fruits
            .filter { it.startsWith("a") }
            .sortedBy { it }
            .map { it.toUpperCase() }
            .forEach { println(it) }

        /**
         * 检测元素是否存在于集合中
         */
        if ("apple" in items1) {
            items1.drop(1)
            println(items1)
        }
        /**
         * 遍历 map/pair型list
         */
        for ((k, v) in map) {
            println("$k -> $v")
            println(map["key"])
        }
    }


}
