package com.randy.training.ui.diyui.list

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import com.randy.training.base.BaseActivity
import com.randy.training.databinding.ActivityPasswordInputBoxActivityBinding

/**
 * ViewBinding 示例
 */
class LetterListActivity : BaseActivity(), LetterView.OnLetterChangeListener,
    AbsListView.OnScrollListener {
    private var binding: ActivityPasswordInputBoxActivityBinding? = null
    private var mAdapter: LetterAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordInputBoxActivityBinding.inflate(layoutInflater)
        val rootView: View = binding!!.root
        setContentView(rootView)
        initData()
    }

    private fun initData() {
        binding?.letterView?.setOnLetterChangeListener(this)
        mAdapter = LetterAdapter(this)
        binding!!.lvData.setAdapter(mAdapter)
        binding!!.lvData.setOnScrollListener(this)
    }

    override fun onLetterListener(touchIndex: String?) {
        binding!!.tvLetterHeader.setVisibility(View.VISIBLE)
        binding!!.tvLetterHeader.setText(touchIndex)
        touchIndex?.let { updateListView(it) }
    }

    private fun updateListView(header: String) {
        val list = mAdapter!!.dataList
        for (i in list.indices) {
            val head = list[i].nameHeader
            if (head == header) {
                binding!!.lvData.setSelection(i)
                return
            }
        }
    }

    override fun onLetterDismissListener() {
        binding!!.tvLetterHeader.setVisibility(View.GONE)
    }

    override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {
    }

    override fun onScroll(p0: AbsListView?, p1: Int, p2: Int, p3: Int) {
        binding?.letterView?.setTouchIndex(mAdapter!!.dataList[p1].nameHeader)
    }

    override fun getResources(): Resources {
        val resources = super.getResources()
        val configuration = Configuration()
        configuration.setToDefaults()
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return resources
    }
}