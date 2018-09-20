package com.cocosh.shmstore.mine.model

/**
 * Created by lmg on 2018/3/22.
 *
 *   "邀请二维码图片地址"
 *   private String inviteImage;
 *   "姓名"
 *  private String nickname;
 *  "企业全名称"
 *  private String corpFname;
 *   "用户身份 新媒人 或者 服务商"
 *  private String userIdentity;
 *  "用户头像"
 *  private String avatar;
 *   "邀请码"
 *   private String invite_code;
 *
 */
data class InviteCodeModel(var nickname: String?,
                           var avatar: String?,
                           var invite_code: String?,
                           var url:String)