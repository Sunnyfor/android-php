/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.cocosh.shmstore.baiduFace.utils;


import com.cocosh.shmstore.baiduFace.exception.FaceException;

/**
 * JSON解析
 * @param <T>
 */
public interface Parser<T> {
    T parse(String json) throws FaceException;
}
