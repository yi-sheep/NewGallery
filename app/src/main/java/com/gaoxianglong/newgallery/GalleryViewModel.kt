package com.gaoxianglong.newgallery

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val _photoListLive=MutableLiveData<List<PhotoItem>>()
    val photoListLive : LiveData<List<PhotoItem>>
    get() = _photoListLive

    // 获取数据
    fun fetchData() {
        val stringRequest = StringRequest(
            Request.Method.GET,
            getUrl(),
            // 成功监听
            Response.Listener {
                _photoListLive.value = Gson().fromJson(it,Pixabay::class.java).wallpapers.toList() // 使用gson将请求到的json数据保存到数据类中然后取出要用到的hits字段并转换为list
            },
            // 错误监听
            Response.ErrorListener {
                Log.d("Error", it.toString())
            }
        )
        // 添加到队列中
        VolleySingleton.getInstance(getApplication()).requestQueue.add(stringRequest) // 在队列中进行网络请求，如果失败就回调上面的错误监听，成功就回调成功监听
    }

    // 获取地址
    private fun getUrl():String {
        return "https://wall.alphacoders.com/api2.0/get.php?auth=c12587414008cd0a6cf5ef60841f66d3&method=popular&page=10&info_level=2"
    }
    // 图片的关键字，每一次刷新随机选一个
    private val keyWords = arrayOf("背景","风景","成都","贵州","北京","上海")
}