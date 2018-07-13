/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.cocosh.shmstore.baiduFace.utils;

import android.util.Log;


import com.cocosh.shmstore.baiduFace.exception.FaceException;
import com.cocosh.shmstore.baiduFace.model.LivenessVsIdcardResult;

import org.json.JSONException;
import org.json.JSONObject;

public class PoliceCheckResultParser implements Parser<LivenessVsIdcardResult> {

    @Override
    public LivenessVsIdcardResult parse(String json) throws FaceException {

        Log.i("PoliceCheckResultParser", "LivenessVsIdcardResult->" + json);
        try {
            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject.has("error_code")) {
                FaceException error = new FaceException(jsonObject.optInt("error_code"),
                        jsonObject.optString("error_msg"));
                throw error;
            }

            LivenessVsIdcardResult result = new LivenessVsIdcardResult();
            result.setLogId(jsonObject.optLong("log_id"));
            result.setJsonRes(json);

            result.setScore(jsonObject.optDouble("result"));
            result.setIdcardImage(jsonObject.optString("matting_image"));

            JSONObject extInfoObject = jsonObject.optJSONObject("ext_info");
            if (extInfoObject != null) {
                double faceliveness =  extInfoObject.optDouble("faceliveness");
                result.setFaceliveness(faceliveness);
            }

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            FaceException error = new FaceException(FaceException.ErrorCode.JSON_PARSE_ERROR,
                    "Json parse error:" + json, e);
            throw error;
        }
    }
}
