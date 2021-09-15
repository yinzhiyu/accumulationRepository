package com.randy.training.ui.kotlin

/**
 *    author : yinzhiyu
 *    e-mail : yinzhiyu@zongheng.com
 *    date   : 2021/9/1515:13
 *    desc   :
 */
fun main() {
    println("Hello world!")
}

/**
 * 字符串模板
 */
fun strDemo() {
    var a = 1
    val s1 = "a is $a"

    a = 2
    val s2 = "${s1.replace("is", "was")},but now is $a"
    println(s2)
}