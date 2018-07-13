/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.cocosh.shmstore.baiduFace;


public class Config {
    //百度AI官网ai.baidu.com人脸模块创建应用，选择相应模块，然后查看ak,sk(公安权限需要在官网控制台提交工单开启)
    //apiKey,secretKey为您调用百度人脸在线接口时使用，如注册，比对等。
    //license 是为了使用sdk进行人脸检测。人脸识别 = 人脸检测 + 人脸比对
    //为了的安全，建议放在您的服务端，端把人脸传给服务器，在服务端端进行人脸注册、识别放在示例里面是为了您快速看到效果
    public static String apiKey = "YXgEb3Xew5LhxO5tAulVdVxz";
    public static String secretKey = "IEgqstzoGIl6nqRm1NbrMGuu2HxXqXq4";
    public static String licenseID = "sm1168-face-android";
    public static String licenseFileName = "idl-license.face-android";


}
