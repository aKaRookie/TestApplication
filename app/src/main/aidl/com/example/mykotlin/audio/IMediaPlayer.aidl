// IMediaPlayer.aidl
package com.example.mykotlin.audio;

// Declare any non-default types here with import statements
import com.example.mykotlin.audio.IMediaStatusListener;
interface IMediaPlayer {

    //初始化
    void init();
    //播放
    void play();
    //暂停
    void pause();
    //停止
    void stop();
    //销毁
    void release();
    //是否正在播放中
    boolean isPlaying();
    void  seekTo(long progress);
    int  getCurrentPosition();
    int  getDuration();
    void  setUrl(String url);
    void  setOnUpdateStatus(IMediaStatusListener url);

}
