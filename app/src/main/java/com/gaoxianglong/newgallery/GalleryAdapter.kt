package com.gaoxianglong.newgallery

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.gallery_item.view.*

class GalleryAdapter : ListAdapter<PhotoItem, MyViewHolder>(DIFFCALLBACK) {
    // 比较提交前后的列表进行对应的显示
    object DIFFCALLBACK : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem === newItem // 判断提交前后的列表是否是同一个
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.photoId == newItem.photoId // 判断提交前后的列表内容是不是相同，因为id是唯一的所以比较id就行了
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val holder = MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)
        )
        holder.itemView.setOnClickListener {
            Bundle().run {
                putParcelableArrayList("PHOTO_LIST", ArrayList(currentList)) // 将当前列表的数据打包
                putInt("PHOTO_POSITION", holder.absoluteAdapterPosition)
                holder.itemView.findNavController()
                    .navigate(R.id.action_galleryFragment_to_photoFragment, this)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val photoItem = getItem(position)
        with(holder.itemView) {
            // 设置闪动
            shimmer.run {
                setShimmerColor(0x55ffffff) // 闪动的颜色
                setShimmerAngle(0) // 闪动的角度
                startShimmerAnimation() // 开启闪动
            }
            imageView.layoutParams.height = photoItem.height / 5 // 根据图片的高度来设置站位图的高度
            userName.text = photoItem.userName
            category.text = photoItem.category
        }
        Glide.with(holder.itemView)
            .load(getItem(position).previewUrl)
            .placeholder(R.drawable.ic_photo_black_24dp)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // 失败时
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // 成功时
                    return false.also { holder.itemView.shimmer?.stopShimmerAnimation() } // 停止闪动
                }

            })
            .into(holder.itemView.imageView)
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)