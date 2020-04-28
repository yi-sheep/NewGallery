package com.gaoxianglong.newgallery

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton private constructor(context: Context){
    // 对象常量
    companion object{
        private var INSTANCE:VolleySingleton?=null
        fun getInstance(context: Context) = INSTANCE?: synchronized(this){ // 判断INSTANCE是否为空 是的话就创建一个
            VolleySingleton(context).also { INSTANCE = it } // 创建一个INSTANCE的同时再将这个赋值给常量
        }
    }
    // 成员
    val requestQueue:RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
}