/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.cocosh.shmstore.baiduFace.model;

import java.io.File;
import java.util.Map;

public interface RequestParams {

    Map<String, File> getFileParams();
    Map<String, String> getStringParams();
}
