package com.gaoxianglong.newgallery

import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.android.synthetic.main.photo_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL
import java.util.jar.Manifest

/**
 * A simple [Fragment] subclass.
 */
const val REQUEST_WRITE_EXTERNAL_STORAGE = 1

class PhotoFragment : Fragment() {
    var mPosition:Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val photoList = arguments?.getParcelableArrayList<PhotoItem>("PHOTO_LIST")
        PagerPhotoListAdapter().apply {
            viewPager2.adapter = this
            submitList(photoList)
        }
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                textView.text = "${position + 1}/${photoList?.size}"
                mPosition=position
            }
        })

        viewPager2.setCurrentItem(arguments?.getInt("PHOTO_POSITION") ?: 0, false)

        saveButton.setOnClickListener {
            // 动态获取权限
            if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                requestPermissions(
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_EXTERNAL_STORAGE
                )
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    savePhoto()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        savePhoto()
                    }
                } else {
                    Toast.makeText(requireContext(), "需要存储权限才能下载", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun savePhoto() {
        withContext(Dispatchers.IO){
            MainScope().launch { Toast.makeText(requireContext(),"下载高清图需要时间，看到这个提示说明已经开始下载了，请稍等",Toast.LENGTH_LONG).show() }
            val photoList = arguments?.getParcelableArrayList<PhotoItem>("PHOTO_LIST")
            val fullUrl = photoList?.get(mPosition)?.fullUrl

//            val position = arguments?.getInt("PHOTO_POSITION")?:0
//            val fullUrl = photoList?.get(position)?.fullUrl
            val url = URL(fullUrl)
            var bitmap:Bitmap? = null
            url.openStream().use {
                bitmap = BitmapFactory.decodeStream(it)
            }
            val saveUri = requireContext().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            )?: kotlin.run {
                Toast.makeText(requireContext(), "失败", Toast.LENGTH_SHORT).show()
                return@withContext
            }
            requireContext().contentResolver.openOutputStream(saveUri).use {
                if (bitmap!!.compress(Bitmap.CompressFormat.PNG, 90, it)) {
                    MainScope().launch { Toast.makeText(requireContext(), "下载完成，请到手机图库中查看", Toast.LENGTH_SHORT).show() }
                } else {
                    MainScope().launch { Toast.makeText(requireContext(), "失败", Toast.LENGTH_SHORT).show() }
                }
            }
        }
//        val holder =
//            (viewPager2[0] as RecyclerView).findViewHolderForAdapterPosition(viewPager2.currentItem)
//                    as PagerPhotoViewHolder
//        Toast.makeText(requireContext(), "成功", Toast.LENGTH_SHORT).show()
    }
}
