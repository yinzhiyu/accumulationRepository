package com.randy.training.widget

import android.graphics.Rect
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View

/**
 *    @author : yinzhiyu
 *    e-mail : yinzhiyu@zongheng.com
 *    date   : 2021/11/311:08
 *    desc   :
 */
// 多重触摸代理
class MultiTouchDelegate(bound: Rect? = null, delegateView: View)
    : TouchDelegate(bound, delegateView) {
    // 保存多个代理控件及其触摸区域的容器
    val delegateViewMap = mutableMapOf<View, Rect>()
    // 当前的代理控件
    private var delegateView: View? = null

    // 新增代理控件
    fun addDelegateView(delegateView: View, rect: Rect) {
        delegateViewMap[delegateView] = rect
    }

    // 完全重写, 以屏蔽父类逻辑
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        var handled = false
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // DOWN 发生时找到对应坐标下的代理控件
                delegateView = findDelegateViewUnder(x, y)
            }
            MotionEvent.ACTION_CANCEL -> {
                delegateView = null
            }
        }
        // 若找到代理控件，则将所有事件都传递给它消费
        delegateView?.let {
            event.setLocation(it.width / 2f, it.height / 2f)
            handled = it.dispatchTouchEvent(event)
        }
        return handled
    }

    // 遍历代理控件，返回其触摸区域包含指定坐标的那一个代理控件
    private fun findDelegateViewUnder(x: Int, y: Int): View? {
        delegateViewMap.forEach { entry -> if (entry.value.contains(x, y)) return entry.key }
        return null
    }

}