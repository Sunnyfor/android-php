package com.cocosh.shmstore.zxing.control

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Vibrator
import android.preference.PreferenceManager
import android.util.Log
import com.cocosh.shmstore.R
import java.io.IOException

/**
 *
 * Created by zhangye on 2018/4/26.
 */
class BeepManager(private val activity: Activity) {
    private var mediaPlayer: MediaPlayer? = null
    private var playBeep: Boolean = false
    private var vibrate: Boolean = false

    init {
        this.mediaPlayer = null
        updatePrefs()
    }

    fun updatePrefs() {
        /* 配置修改 */
        val prefs = PreferenceManager
                .getDefaultSharedPreferences(activity)
        playBeep = shouldBeep(prefs, activity)
        vibrate = false// 不震动
        // vibrate = prefs.getBoolean(PreferencesActivity.KEY_VIBRATE, false);
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            activity.volumeControlStream = AudioManager.STREAM_MUSIC
            mediaPlayer = buildMediaPlayer(activity)
        }
    }

    fun playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer!!.start()
        }
        if (vibrate) {
            val vibrator = activity
                    .getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VIBRATE_DURATION)
        }
    }

    companion object {

        private val TAG = BeepManager::class.java.simpleName

        private const val BEEP_VOLUME = 0.50f
        private const val VIBRATE_DURATION = 200L

        private fun shouldBeep(prefs: SharedPreferences, activity: Context): Boolean {
            /* 配置修改 */
            var shouldPlayBeep = true// 声音
            // boolean shouldPlayBeep = prefs.getBoolean(
            // PreferencesActivity.KEY_PLAY_BEEP, true);

            if (shouldPlayBeep) {
                // See if sound settings overrides this
                val audioService = activity
                        .getSystemService(Context.AUDIO_SERVICE) as AudioManager
                if (audioService.ringerMode != AudioManager.RINGER_MODE_NORMAL) {
                    shouldPlayBeep = false
                }
            }
            return shouldPlayBeep
        }

        private fun buildMediaPlayer(activity: Context): MediaPlayer? {
            var mediaPlayer: MediaPlayer? = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            // When the beep has finished playing, rewind to queue up another one.
            mediaPlayer
                    .setOnCompletionListener { player -> player.seekTo(0) }

            val file = activity.resources.openRawResourceFd(
                    R.raw.baidu_beep)
            try {
                mediaPlayer.setDataSource(file.fileDescriptor,
                        file.startOffset, file.length)
                file.close()
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME)
                mediaPlayer.prepare()
            } catch (ioe: IOException) {
                Log.w(TAG, ioe)
                mediaPlayer = null
            }

            return mediaPlayer
        }
    }
}
