# 网络图库
使用了
Navigation+Fragment : 采用单Activity模式
RecyclerView+ListAdapter : 实现了数据前后对比
StaggeredGridLayoutManager : 不规则显示
SwipeRefreshLayout : 下拉刷新
ShimmerLayout : 实现图片还未加载时为闪烁状态
ViewPager2 : 图片分页查看

添加的依赖库
```gradle
    implementation 'com.android.volley:volley:1.1.1'
    implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.0.0'
    implementation 'com.github.bumptech.glide:glide:4.11.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0-alpha01'
    implementation 'androidx.navigation:navigation-fragment-ktx:2.2.2'
    implementation 'androidx.navigation:navigation-ui-ktx:2.2.2'
    implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'io.supercharge:shimmerlayout:2.1.0'
    implementation 'com.github.chrisbanes.photoview:library:1.2.4'
    implementation 'androidx.viewpager2:viewpager2:1.1.0-alpha01'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.3.0-alpha01'
```