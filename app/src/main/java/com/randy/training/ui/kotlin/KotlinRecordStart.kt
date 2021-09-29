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
/**
 * if not null 执行代码
 */
//val value = ……
//value?.let {
//    …… // 代码会执行到此处, 假如data不为null
//}
/**
 * 映射可空值（如果非空的话）
 */
//    val value = ……
//    val mapped = value?.let { transformValue(it) } ?: defaultValue
// 如果该值或其转换结果为空，那么返回 defaultValue。
/**
 * 对一个对象实例调用多个方法 （----------------------------with-----------------------------------）
 */
//class Turtle {
//    fun penDown()
//    fun penUp()
//    fun turn(degrees: Double)
//    fun forward(pixels: Double)
//}
//
//val myTurtle = Turtle()
//with(myTurtle) { // 画一个 100 像素的正方形
//    penDown()
//    for (i in 1..4) {
//        forward(100.0)
//        turn(90.0)
//    }
//    penUp()
//}
/**
 *配置对象的属性（---------------------------------------apply--------------------------------------）
 */
//val myRectangle = Rectangle().apply {
//    length = 4
//    breadth = 5
//    color = 0xFAFAFA
//}