package com.randy.training.ui.main

import android.Manifest
import android.os.Bundle
import android.view.View
import com.randy.training.utils.IntentUtil
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.graphics.Color

import androidx.core.content.ContextCompat
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.randy.training.R
import com.randy.training.Rotate3DActivity
import com.randy.training.base.BaseActivity
import com.randy.training.base.adapter.recyclerview.BaseAdapter
import com.randy.training.databinding.ActivityMainBinding
import com.randy.training.ui.diyui.list.LetterListActivity
import com.randy.training.ui.kotlin.KotlinDemoActivity
import com.randy.training.webview.WebViewActivity
import com.randy.training.webview.WebViewGoodActivity


class MainActivity : BaseActivity() {
    private val GET_STORAGE_PERMISSION = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSlidingEnable(false);
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView(binding)
    }

    private fun initView(binding: ActivityMainBinding) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvTrainingItemList.layoutManager = layoutManager
        val adapter = Main2Adapter(this, R.layout.item_main_layout)
        binding.rvTrainingItemList.adapter = adapter
        getLocalList().let {
            adapter.refreshData(it)
        }
        adapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                clickItem(view, position)
            }
        })
        binding.alMaintain.setBackgroundColor(Color.CYAN)
    }

    private fun clickItem(view: View, position: Int) {
        when (position) {
            0 -> IntentUtil.go(this, WebViewActivity::class.java)
            1 -> IntentUtil.go(this, WebViewGoodActivity::class.java)
            2 -> IntentUtil.go(this, Rotate3DActivity::class.java)
            3 -> IntentUtil.go(this, LetterListActivity::class.java)
            4 -> IntentUtil.go(this, KotlinDemoActivity::class.java)

        }
    }

    private fun getLocalList(): MutableList<String> {
        val functionName = mutableListOf("Webview1", "Webview2", "3D翻转", "字母列表")
        functionName.add("Kotlin demo")
        functionName.add("other")
        return functionName
    }

    fun ccc(view: View) {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //没有授权进行权限申请
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                GET_STORAGE_PERMISSION
            )
        } else {
            IntentUtil.go(this, WebViewGoodActivity::class.java)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GET_STORAGE_PERMISSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                IntentUtil.go(this, WebViewGoodActivity::class.java)
            } else {
                Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show()
            }
        }
    }
}