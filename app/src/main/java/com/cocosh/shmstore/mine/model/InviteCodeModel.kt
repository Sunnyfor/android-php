package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/3/22.
 *
 *   "邀请二维码图片地址"
 *   private String inviteImage;
 *   "姓名"
 *  private String realName;
 *  "企业全名称"
 *  private String corpFname;
 *   "用户身份 新媒人 或者 服务商"
 *  private String userIdentity;
 *  "用户头像"
 *  private String headPic;
 *   "邀请码"
 *   private String inviteCode;
 *
 */
data class InviteCodeModel(var codeInfo: String?,
                      var inviteImage: String?,
                      var realName: String? ,
                      var corpFname: String? ,
                      var userIdentity: String? ,
                      var headPic: String? ,
                      var inviteCode: String? )