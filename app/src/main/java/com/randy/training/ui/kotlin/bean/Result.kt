package com.randy.training.ui.kotlin.bean

data class Result(
    val canGrabNormal: Any,
    val canGrabNum: Int,
    val currentTime: Long,
    val lastestNormalNum: Int,
    val latestBig: Any,
    val latestBigNum: Int,
    val latestNormal: Any,
    val newBig: Any,
    val newBigNum: Int,
    val newGlobalBigList: Any,
    val newNormal: Any,
    val newNormalNum: Int
)