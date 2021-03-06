package com.example.mykotlin

import android.location.Location
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.net.URLEncoder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.example.mykotlin", appContext.packageName)
    }
    @Test
    fun testTime() {
       var t1 = 1573974000000
       var t2 = System.currentTimeMillis()
        println("time long: t1=$t1,t2=$t2")
       var time =  SimpleDateFormat("HH:mm:ss").format(Date(t2))
        println("time: $time")
    }
    @Test
    fun testUrl(){
        val url = "https://broad-video.zhidaohulian.com/audio/栏目2.m4a"
        println("before: $url")
        println("after: ${URLEncoder.encode(url,"utf-8")}")
    }
    @Test
    fun testDirection() {
        //昌平
        val lat1 = 40.22
        val lng1 = 116.2
        //目标大兴
        val dstLat = 39.73
        val dstLng = 116.33
        println("输入经纬度1={$lat1,$lng1)  经纬度2={$dstLat,$dstLng) ")
        var floatArray = floatArrayOf(0.0f,0.0f,0.0f)
        Location.distanceBetween(lat1,lng1,lat1,lng1,floatArray)
        for (fl in floatArray) {
            println("距离: $fl")
        }

    }
}
