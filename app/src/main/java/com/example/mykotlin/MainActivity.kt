package com.example.mykotlin

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import com.example.mykotlin.audio.IMediaPlayer
import com.example.mykotlin.audio.IMediaStatusListener
import com.example.mykotlin.audio.MediaPlayerService
import com.zhidaoauto.lib.soundtouch.SoundTouch
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_dialog.*
import kotlinx.coroutines.*
import java.lang.Runnable
import kotlin.math.absoluteValue

const val TAG = "MainActivity"

class MainActivity : DialogActivity(), ServiceConnection, Runnable,
    HorzTextProgressView1.OnProgressChangeListener {
    override fun onStartTracking() {
        mService?.let {
            mProgressHandler.removeCallbacks(this@MainActivity)
        }
    }

    override fun onStopTracking(progress: Double) {
        mService?.let {
            Log.i(TAG, "更新进度至:position= $progress")
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
                5 -> {
                    //播放完成
                    tv.text = "播放"
                    home_iv_media_spectrogram.pause()
                    mProgressHandler.removeCallbacksAndMessages(null)
                }
                else -> mProgressHandler.removeCallbacksAndMessages(null)
            }
            mCurStatus = status
        }

    }

    override fun getDialogRes(): Int = R.layout.item_dialog
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

            /*setDialogContent("上传成功", R.drawable.icon_dialog_upload_ok)
            GlobalScope.launch(Dispatchers.Default) {
                delay(1000)
                withContext(Dispatchers.Main) {
                    setDialogContent("上传失败", R.drawable.icon_dialog_upload_err)

                }
            }
            showDialog()*/
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

    private fun setDialogContent(content: String, id: Int) {
        val view = getDialogLayout()
        view?.run {
            val tv = findViewById<TextView>(R.id.dialog_tv_content)
            tv.text = content
            val progressBar = findViewById<ProgressBar>(R.id.progressbar)
            val d: BitmapDrawable = resources.getDrawable(id) as BitmapDrawable
            val dimen = resources.getDimension(R.dimen.dp_56)
            val left = (dimen - d.bitmap.width).toInt() / 2
            val top = (dimen - d.bitmap.height).toInt() / 2
            val right = left + d.bitmap.width
            val bottom = top + d.bitmap.height
            d.setBounds(
                left.absoluteValue,
                top.absoluteValue,
                right.absoluteValue,
                bottom.absoluteValue
            )
            progressBar.indeterminateDrawable = d
            progressBar.isIndeterminate = true

        }
    }

    override fun onStop() {
        dismissDialog()
        super.onStop()
    }

    override fun onDestroy() {
        destoryDialog()
        super.onDestroy()
    }

    private var mService: IMediaPlayer.Stub? = null
    private fun initData() {
        testSoundTouch()
        live_progress.setMaxProgress(10000.0)
        //startService()

    }

    private fun testSoundTouch() {
        val soundTouch = SoundTouch()
        val inFileName = url
        val outFileName = "/mnt/sdcard/myaudio/guofeng1.wav"
        soundTouch.run {
            //取值0.01-1
            setTempo(1.0f)
            setPitchSemiTones(10f)
            setDialogContent("正在转换中", R.drawable.icon_dialog_upload_ok)
            GlobalScope.launch(Dispatchers.Default) {
                Log.i("SoundTouch", "输入文件路径 file: $inFileName")
                val startTime = System.currentTimeMillis()
                val result = processFile(inFileName, outFileName)
                val endTime = System.currentTimeMillis()
                val duration = (endTime - startTime) * 0.001f
                Log.i("SoundTouch", "转换完成, result=$result, 耗时:$duration, 输出:$outFileName")
                withContext(Dispatchers.Main) {
                    if (result == 0) {
                        url = outFileName
                        setDialogContent("转换成功", R.drawable.icon_dialog_upload_ok)
                    } else {
                        setDialogContent("转换失败", R.drawable.icon_dialog_upload_err)
                    }
                    startService()
                    dismissDialog()
                }
            }
            showDialog()

        }

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

    //val url = "http://broad-video.zhidaohulian.com/audio/LVDRyGnINa/1574734440239_16000.wav"
//val url = "https://front-dev.zhidaohulian.com/music/kc.mp3"
//var url = "http://broad-video.zhidaohulian.com/audio/栏目2.m4a"
    var url = "/mnt/sdcard/myaudio/guofeng.wav"
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
