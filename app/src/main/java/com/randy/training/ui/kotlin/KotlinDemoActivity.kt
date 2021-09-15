package com.randy.training.ui.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.randy.training.R

class KotlinDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_demo)
    }


    fun main() {
        println("Hello world!")
    }
}