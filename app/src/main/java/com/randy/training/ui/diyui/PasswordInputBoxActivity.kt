package com.randy.training.ui.diyui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.randy.training.databinding.ActivityPasswordInputBoxActivityBinding

/**
 * ViewBinding 示例
 */
class PasswordInputBoxActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPasswordInputBoxActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvTitle.text = "Hello"
    }
}