package com.example.mykotlin

import android.app.Application
import android.content.Context
import android.net.Uri
import com.danikula.videocache.HttpProxyCacheServer
import com.danikula.videocache.file.FileNameGenerator
import java.io.File

/**
 *
 * @ProjectName:    MyKotlinApplication
 * @Package:        com.example.mykotlin
 * @ClassName:      MyApplication
 * @Description:    java类作用描述
 * @Author:         作者名
 * @CreateDate:     2019/11/18 15:46
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/11/18 15:46
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class MyApplication : Application() {
    companion object {
        lateinit var sContext: Context

    }
    override fun onCreate() {
        super.onCreate()
        sContext = applicationContext
    }

    private var proxy: HttpProxyCacheServer? = null
}