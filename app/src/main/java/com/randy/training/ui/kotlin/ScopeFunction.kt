package com.randy.training.ui.kotlin

/**
 *    author : yinzhiyu
 *    e-mail : yinzhiyu@zongheng.com
 *    date   : 2021/9/1610:27
 *    desc   : 作用域函数--在对象的上下文中执行代码块
 *             共有以下五种：let、run、with、apply 以及 also。
 */
class ScopeFunction {
    /**
     * 典型用法
     * 结果：Person(name=Alice, age=20, city=Amsterdam)
     *     Person(name=Alice, age=21, city=London)
     * 为什么：如果不使用 let 来写这段代码，就必须引入一个新变量，并在每次使用它时重复其名称,如下：
     *   val alice = Person("Alice", 20, "Amsterdam")
     *   println(alice)
     *   alice.moveTo("London")
     *   alice.incrementAge()
     *   println(alice)
     */
    data class Person(var name: String, var age: Int, var city: String) {
        fun moveTo(newCity: String) { city = newCity }
        fun incrementAge() { age++ }
    }

    fun main() {
        Person("Alice", 20, "Amsterdam").let {
            println(it)
            it.moveTo("London")
            it.incrementAge()
            println(it)
        }
    }

    /**
     * 由于作用域函数本质上都非常相似，因此了解它们之间的区别很重要。每个作用域函数之间有两个主要区别：
     * =============引用上下文对象的方式=======================
     * ===================返回值============================
     */
}