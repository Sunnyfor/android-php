/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.cocosh.shmstore.baiduFace;

import com.cocosh.shmstore.baiduFace.exception.FaceException;

public interface OnResultListener<T> {
    void onResult(T result);

    void onError(FaceException error);
}
