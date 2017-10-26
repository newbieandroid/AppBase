package com.fuyoul.sanwenseller.im.session.activity.avchat;

/**
 * 音视频界面操作
 */
public interface AVChatUIListener {
    void onHangUp();
    void onRefuse();
    void onReceive();
    void toggleMute();
    void toggleSpeaker();
    void toggleRecord();
    void videoSwitchAudio();
    void audioSwitchVideo();
    void switchCamera();
    void closeCamera();
}
