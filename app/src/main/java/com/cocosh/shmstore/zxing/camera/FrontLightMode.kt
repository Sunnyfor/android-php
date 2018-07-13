package com.cocosh.shmstore.zxing.camera

import android.content.SharedPreferences

/**
 *
 * Created by zhangye on 2018/4/26.
 */
enum class FrontLightMode {

    /** Always on.  */
    ON,
    /** On only when ambient light is low.  */
    AUTO,
    /** Always off.  */
    OFF;


    companion object {

        private fun parse(modeString: String?): FrontLightMode =
                if (modeString == null) OFF else valueOf(modeString)

        fun readPref(sharedPrefs: SharedPreferences): FrontLightMode = parse(null)
        /* 配置修改 */
        // return
        // parse(sharedPrefs.getString(PreferencesActivity.KEY_FRONT_LIGHT_MODE,
        // null));
    }

}