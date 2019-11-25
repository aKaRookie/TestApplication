package com.example.mykotlin

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.ProgressBar
import android.widget.SeekBar
import com.example.mykotlin.audio.IMediaPlayer
import com.example.mykotlin.audio.IMediaStatusListener
import com.example.mykotlin.audio.MediaPlayerService
import kotlinx.android.synthetic.main.activity_main.*

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), ServiceConnection, Runnable, HorzTextProgressView1.OnProgressChangeListener {
    override fun onStartTracking() {
        mService?.let {
            mProgressHandler.removeCallbacks(this@MainActivity)
        }
    }

    override fun onStopTracking(progress: Double) {
        mService?.let {
            Log.i(TAG,"更新进度至:position= $progress")
            it.seekTo(progress.toLong())
            mProgressHandler.removeCallbacks(this@MainActivity)
            mProgressHandler.post(this@MainActivity)
        }

    }

    //当前MediaPlayer播放状态
    private var mCurStatus = -1

    private inner class MyCallBack : IMediaStatusListener.Stub(), IMediaStatusListener {
        override fun onUpdateStatus(status: Int) {
            Log.i(TAG, "更新状态! status: $status")
            when (status) {
                3 -> {
                    //播放
                    mProgressHandler.removeCallbacks(this@MainActivity)
                    mProgressHandler.post(this@MainActivity)
                }
                5->{
                    //播放完成
                    tv.text ="播放"
                    home_iv_media_spectrogram.pause()
                    mProgressHandler.removeCallbacksAndMessages(null)
                }
                else -> mProgressHandler.removeCallbacksAndMessages(null)
            }
            mCurStatus = status
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("123")
        // pgView.setMaxProgress(1111111.0)
        //pgView.setCurrentProgress(0.0)
        val visView = findViewById<VisualizerView>(R.id.home_iv_media_spectrogram)
        /* val array = ByteArray(129)

         for (i in 0 until  array.size) {
             array[i] = 30
         }
         visView.updateVisualizer(array)*/


        tv.setOnClickListener {
            Log.i("MainActivity", "onclick")
            mService?.let { stub ->
                if (stub.isPlaying) {
                    tv.text = "播放"
                    stub.pause()
                } else {
                    tv.text = "暂停"
                    stub.play()
                }
                if (visView.isRunning) {
                    visView.stop()
                } else {
                    visView.play()
                }
            }
        }
        tv_stop.setOnClickListener {
            mService?.let { stub ->
                stub.stop()
            }
        }
        live_progress.setOnProgressChangedListener(this)
        initData()
    }

    private var mService: IMediaPlayer.Stub? = null
    private fun initData() {
        live_progress.setMaxProgress(10000.0)
        startService()

    }


    private fun startService() {
        val intent = Intent(this, MediaPlayerService::class.java)
        bindService(intent, this, Context.BIND_AUTO_CREATE)
        startService(intent)

    }

    /* override fun onBindingDied(name: ComponentName?) {
         super.onBindingDied(name)
         Log.i("MainActivity", "onBindingDied")
     }*/

    override fun onServiceDisconnected(name: ComponentName?) {
        Log.i("MainActivity", "onServiceDisconnected")
    }

    // val url = "http://isure.stream.qqmusic.qq.com/C200002HpUq83Ps3jN.m4a?guid=2000001271&vkey=9C70C43E26B801DDC3A0C8B870D46D551C11EE306651F5F1E7C0D3A57817EDCCFBFC3D38FC459A18AF6534BB18621FB996C654E4B9668E0D&uin=&fromtag=50"
    val url = "http://front-dev.zhidaohulian.com/music/kc.mp3"
    var mDuration = 0
    var mPostion = 0
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.i("MainActivity", "onServiceConnected")
        mService = service as IMediaPlayer.Stub
        mService!!.init()
        mService!!.setUrl(url)
        mService!!.setOnUpdateStatus(MyCallBack())
        mDuration = mService!!.duration
        mPostion = mService!!.currentPosition
        updateUI()
    }

    override fun onPause() {
        super.onPause()
        mProgressHandler.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
        if (mCurStatus == 2 || mCurStatus == 3) {
            mProgressHandler.post(this)
        }
    }

    private fun updateUI() {
        live_progress.setCurrentProgress(mPostion.toDouble())
        live_progress.setMaxProgress(mDuration.toDouble())
    }

    private val mProgressHandler: Handler = Handler()
    override fun run() {
        mService?.let {
            mPostion = it.currentPosition
            mDuration = it.duration
            //it.seekTo(3L*mPostion)
        }
        Log.i(TAG, "更新进度! Position: $mPostion")
        updateUI()
        mProgressHandler.postDelayed(this, 1000)
    }
}
