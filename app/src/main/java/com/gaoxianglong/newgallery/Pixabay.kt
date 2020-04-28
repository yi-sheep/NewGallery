package com.gaoxianglong.newgallery

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Pixabay( // 使用data关键字表示这是一个数据类，自动拥有get/set方法
    val wallpapers: Array<PhotoItem>,
    val success: Boolean // 构造方法的参数
) {
    // 这个类中的方法
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Pixabay

        if (!wallpapers.contentEquals(other.wallpapers)) return false
        if (success != other.success) return false

        return true
    }

    override fun hashCode(): Int {
        var result = wallpapers.contentHashCode()
        result = 31 * result + success.hashCode()
        return result
    }
}

@Parcelize data class PhotoItem(
    @SerializedName("id") val photoId: Int, // 序列化
    @SerializedName("url_image") val fullUrl: String,
    @SerializedName("url_thumb") val previewUrl: String,
    @SerializedName("height")val height:Int,
    @SerializedName("user_name")val userName:String,
    @SerializedName("category")val category:String
):Parcelable