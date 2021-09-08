package com.randy.training.ui.main

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.randy.training.utils.IntentUtil
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager

import androidx.core.content.ContextCompat
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.randy.training.R
import com.randy.training.Rotate3DActivity
import com.randy.training.Rotate3DAnimation
import com.randy.training.base.adapter.recyclerview.BaseAdapter
import com.randy.training.webview.WebViewActivity
import com.randy.training.webview.WebViewGoodActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val GET_STORAGE_PERMISSION = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val layoutManager = LinearLayoutManager(this)
        rv_training_item_list.layoutManager = layoutManager
        val adapter = Main2Adapter(this, R.layout.item_main_layout)
        rv_training_item_list.adapter = adapter
        getLocalList().let {
            adapter.refreshData(it) }
        adapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                clickItem(view,position)
            }
        })
    }
    private fun clickItem(view: View, position: Int) {
when(position){
    0-> IntentUtil.go(this, WebViewActivity::class.java)
    1-> IntentUtil.go(this, WebViewGoodActivity::class.java)
    2-> IntentUtil.go(this, Rotate3DActivity::class.java)

}
    }
    private fun getLocalList(): MutableList<String> {
        val functionName = mutableListOf("Webview1", "Webview2", "3D翻转", "four")
        functionName.add("five")
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