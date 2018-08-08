package com.cocosh.shmstore.http

import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.SharedUtil

/**
 * 接口配置清单
 * Created by zhangye on 2017/10/12.
 */
object Constant {

    fun isDebug(): Boolean = SharedUtil.getBoolean(DataCode.ISDEBUG)

    var SHOWLOG = true //是否打印LOG

    const val APPSECRET = "SM_ANDROID" //接口SECRET
    const val VERSION = "v1"  //接口版本
    const val CLIENT = "android"

    /**
     * 图片，照相
     */
    const val IMAGE_REQUEST_CODE = 100
    const val SELECT_PIC_NOUGAT = 101
    const val RESULT_REQUEST_CODE = 102
    const val CAMERA_REQUEST_CODE = 104


    /*新版PHP接口*/

    const val REGISTER_SEND_CODE = "$VERSION/common/sms" //注册发送验证码

    const val REGISTER = "$VERSION/portal/register" //注册

    const val LOGIN = "$VERSION/portal/login"//登录

    const val LOGOUT = "$VERSION/portal/logout" //注销

    const val SMSCHECK = "$VERSION/pass/smscheck" //修改登录密码-短信校验

    const val OLDCHECK = "$VERSION/pass/oldcheck" //修改登录密码-旧密码校验

    const val PAYPASS_OLDCHECK = "$VERSION/paypass/oldcheck" //修改支付密码-旧密码校验

    const val PAYPASS_SMSCHECK = "$VERSION/paypass/smscheck" //支付密码-短信检测

    const val PAYPASS_SET = "$VERSION/paypass/set" //支付密码-设置

    const val PASS_SET = "$VERSION/pass/set" //登录密码-更新

    const val FORGOTPWD = "$VERSION/portal/forgotpwd" //忘记密码

    const val PROFILE = "$VERSION/profile/" //获取个人档案

    const val PROFILE_UPDATE = "$VERSION/profile/update" //更新个人档案

    const val INDUSTRY = "$VERSION/common/industry" //行业列表

    const val COMMON_UPLOADS= "$VERSION/common/uploads" //上传文件

    const val ADDRESS_LIST = "$VERSION/address/list" //收货地址列表
    const val ADDRESS_SAVE = "$VERSION/address/save" //保存收货地址
    const val ADDRESS_DELETE = "$VERSION/address/delete" //删除收货地址
    const val ADDRESS_SETDEF = "$VERSION/address/setdef" //设置默认


    const val CERT_DO = "$VERSION/cert/do" //个人认证

    const val NEW_CERT_INVITEE = "$VERSION/new_cert/invitee" //新媒人-被邀约信息


    /**
     * 首页
     * */
    const val HOME_BANNER = "red-packet/red/packet/advertisement/getHomePageBanners"
    const val HOME_BOTTOM = "red-packet/red/packet/advertisement/getIndexOfBannersInfo"
    /**
     * 提现页面类型区分
     *
     */
    const val TYPE_MY = "type_my"//我的钱包提现
    const val TYPE_ENTERPRISE = "type_enterprise"//企业体现

    /**
     * qiniu token key 前缀
     */
    const val QINIU_KEY_HEAD = "http://res.shoumeiapp.com/"

    const val WEATHER_KEY = "fa73e6cf930a1c4ec46e0f0aebce3f1e"

    /**
     * 字典（目前用于查询分享链接）
     */
    const val GET_SHARE_URL = "ucenter/dictionary/getValueByKey"

    const val CHECK_WX = "ucenter/wb/validate/wx "
    const val WEB_VALIDATE = "ucenter/wb/validate"  //第三方验证 openId验证
    const val WEB_SEND_MESSGE = "ucenter/wb/sendMessage " //第三方绑定手机发送验证码
    const val WEB_AUTH_CODE = "ucenter/wb/authCode" //第三方验证短信验证码
    const val WEB_REGISTER = "ucenter/wb/registeredUser " //第三方注册

    const val USER_LOCATION = "ucenter/user/location/userLocation" //用户定位

    const val REGISTER_AUTH_CODE = "ucenter/member/checkVerificationCode"//校验验证码
    const val REGISTER_PHONE = "ucenter/member/registeredUser" //手机号码注册


    const val GET_MYFILEINFO = "ucenter/member/getMyFileInfo" //档案

    const val UPDATE_MYFILESINFO = "ucenter/member/updateMyFilesInfo" //更新档案

    const val GET_ALLINTERESTS = "ucenter/member/getAllInterests" //获取所有兴趣爱好
    const val GET_MYINTEREST = "ucenter/member/getMyInterest" //获取我的兴趣爱好
    const val SET_MYINTEREST = "ucenter/member/setInterestLabel" //获取我的兴趣爱好

    const val INSERT_PRIVATE_LABEL = "ucenter/member/insertPrivateLabel" //自定义兴趣爱好

    const val GET_INDUSTRYINFO = "ucenter/member/getIndustryInfo" //获取所有行业

    const val IDENTIFY_MOBILE_GET_CODE = "ucenter/member/pwd/sendMessage"//验证手机发送验证码
    const val IDENTIFY_MOBILE_CHECKED_CODE = "ucenter/member/pwd/authCode"//验证手机校验验证码

    const val SET_NEW_PSD = "ucenter/member/pwd/setNewPassword"//设置新密码
    const val SERVICE_ADDRESS = "ucenter/member/partner/get/region"//新媒人获取地址

    const val CHECK_OLD_PASSWORD = "ucenter/member/pwd/setNewPasswordByOldPassword" //检测旧密码
    const val MOTIFY_PASSWORD = "ucenter/member/pwd/checkNewPassword" //修改密码接口

    const val IDCARD_IDENTITY = "ucenter/auth/idCardAuth"//身份认证
    const val FACE_TOKEN = "ucenter/sevenCows/getUpToken" //获取七牛的token

    const val APPLY_PARTNER = "ucenter/member/partner/applyPartner"//申请成为新媒人
    const val GET_CHARGE = "ucenter/pingpp/get-pingxx-charge"//获得预订单 titleManager.defaultTitle("确认服务区域")
    const val CHECK_AUTH = "ucenter/member/memberEntrance"//获得认证状态
    const val GETINFOR = "ucenter/member/partner/toBePaidDetail"//待支付状态获取认证信息
    const val CERTIFY_FINAL = "ucenter/member/partner/partnerDetail"//新媒人认证最终页

    const val INVITE_CODE_INFO = "/ucenter/member/getInvitedCodeInfo"//二维码邀请页信息接口

    const val PARTNER_INVITE_CODE = "ucenter/member/partner/invite/code" //新媒人的邀请码获取
    const val PARTNER_CHECKED_CODE = "ucenter/member/partner/invite/ent" //检查新媒人的邀请码
    const val PARTNER_SERVICE_LIST = "ucenter/member/partner/area/operators" //获取新媒人服务生列表
    /**
     *企业主
     */
    const val ENTERPRISE_INFO_REGISTER = "ucenter/ent/record"//企业主信息登记
    const val ENTERPRISE_INFO_BANKCARD = "ucenter/ent/save/bankCard"//保存企业主对公账户信息
    const val ENTERPRISE_INFO_IDENTITY = "ucenter/ent/save/identity"//保存企业主身份认证信息
    const val ENTERPRISE_INFO_LICENSE = "ucenter/ent/save/license"//保存企业主营业执照信息
    const val ENTERPRISE_INVITE_CODE = "ucenter/ent/invite/code"//保存企业主邀请码
    const val ENTERPRISE_INFO = "ucenter/ent/info"//企业主概要信息
    const val SAVE_IDENTITY = "ucenter/ent/save/identity" //提交企业主身份认证资料
    const val GET_IDENTITY = "ucenter/ent/authentication/identity"//企业主身份认证资料信息
    const val ENTERPRISE_INFO_LICENSE_SHOW = "ucenter/ent/authentication/license"//营业执照回显
    const val ENTERPRISE_INFO_BANKCARD_SHOW = "ucenter/ent/authentication/bankCard"//对公帐号回显
    const val ENTERPRISE_INFO_ACTIVE = "ucenter/ent/activate"//激活
    const val ENTERPRISE_ACTIVE_INFO = "ucenter/ent/authentication/info"//认证信息展示
    /**
     * 服务商
     */
    const val FACILITOTAAR_INFO_ADDRESS = "ucenter/region/operator/region"//服务商地址选择
    const val FACILITOTAAR_INFO_SHOW = "ucenter/member/operator/info"//获取服务商认证信息
    const val FACILITOTAAR_INFO_SAVE = "ucenter/member/operator/save"//服务商保存
    const val FACILITOTAAR_AUTH_NUM = "ucenter/member/operator/volidate"//验证服务商营业执照信息

    /**
     * 个人
     */
    const val IDCARDDETAIL = "ucenter/auth/idcardDetail" //个人认证结果

    /**
     * 服务商认证 协议
     */
    const val FACILITOTAAR_SERVICE_RULE = "usercenter/agreement/operatorAgreement.html"// 服务商
    /**
     * 新媒人认证 协议
     */
    const val PARTNER_SERVICE_RULE = "usercenter/agreement/partnerAgreement.html"//新媒人
    /**
     * 充值 协议
     */
    const val CHARGE_SERVICE_RULE = "account/account/personal/draw/protcol"//充值

    /**
     * 添加银行卡协议
     */
    const val ADD_CARD_RULE = "usercenter/agreement/partnerAgreement.html"//新媒人

    /**
     * 红包排行榜
     */
    const val BONUS_RANKING = "red-packet/red/packet/advertisement/rankingList"

    const val BONUS_LIST = "red-packet/red/packet/advertisement/advertisementList" //红包排行榜
    const val BONUS_GIVE = "red-packet/red/packet/give" //赠送红包
    const val BONUS_GIVE_COLLECT = "red-packet/red/packet/collect/give" //赠送收藏的红包
    const val BONUS_AMOUNT = "red-packet/red/packet/advertisement/getRedPacketAmount" //红包资金池
    const val BONUS_HIT = "red-packet/red/packet/hit" //抢红包
    const val BONUS_COLLECT = "red-packet/red/packet/collect" //收藏红包
    const val BONUS_OPEN = "red-packet/red/packet/open" //开启红包
    const val BONUS_OPEN_COLLECT = "red-packet/red/packet/collect/open" //开启收藏的红包
    const val BONUS_RULE = "ucenter/user/location/getRedPacketRule" //红包规则
    const val BONUS_PARAMS = "red-packet/specialuser/business/getRedPacketParam" //获取红包参数
    const val BONUS_SAVE = "red-packet/specialuser/business/saveRedPacketData" //保存红包
    const val BONUS_AD_SAVE = "red-packet/specialuser/business/saveADData" //保存广告
    const val BONUS_SEND_LISTT = "red-packet/red/packet/advertisement/userSendPacketList"//用户发出的红包列表
    const val BONUS_CANCLE = "red-packet/red/packet/advertisement/cancelRelease" //取消投放
    const val BONUS_PUSH_DATA = "red-packet/red/packet/advertisement/redPacketDetails"//投放数据源

    /**
     * 举报接口
     */
    const val REPORT = "ucenter/user/report/addUserReport" //举报接口

    /**
     * 我的钱包
     */
    const val MY_WALLET_DATA = "account/account/personal/balance"//我的钱包
    const val MY_WALLET_DRAWINFO = "account/account/personal/draw/info"//个人账户提现到银行卡 信息
    const val MY_WALLET_DRAW = "account/cash/withdrawals"//个人账户提现到银行卡
    const val MY_WALLET_BANKLIST = "account/bank/card/personal/list"//个人账户银行卡list
    const val WALLET_BANKLIST = "account/account/balance/flow"//个人流水

    const val ENT_WALLET_WATER = "account/account/ent/balance/flow"//企业钱包流水
    const val ENT_WALLET_DATA = "account/account/ent/balance"//企业钱包信息
    const val ENT_WALLET_DRAWINFO = "account/account/ent/bankcard"//获取用户企业对公账户信息
    const val ENT_WALLET_DRAW = "account/cash/ent/draw"//提款到对公账户

    const val ADD_BANK = "account/bank/card/save"//添加银行卡
    const val DELETE_BANK = "account/bank/card/del"//删除银行卡
    const val GET_BANK_TYPE = "account/bank/card/type"//获取银行卡类型
    const val CHECK_BANK_DELETE = "account/bank/card/check"//校验是否可解绑银行卡

    const val CHECK_MESSAGECODE = "account/pwd/authCode"//验证 验证码
    const val CHANGE_PWD = "account/pwd/modify"//通过账户密码 - 修改账户密码
    const val CHANGE_PAY_PWD_SEND_CODE = "account/pwd/sendMessage"//修改支付密码 - 发送验证码
    const val SET_NEW_PWD_BY_PHONENUM = "account/pwd/setNewPassword"//通过手机号 - 设置新密码
    const val CHECK_PWD = "account/pwd/volidate"//校验账户密码 测试账号 userId=1,pwd=123456

    const val IS_SET_PWD = "account/user/authentication"//校验账户密码是否存在

    const val COLLECTION_LIST = "ucenter/collection/collectionList"//收藏列表
    const val FOLLOW_LIST = "ucenter/follow/followList"//关注列表
    const val FOLLOW_CANAEL = "ucenter/follow/cancelFollow"//取消关注 post
    const val CHECK_PERSON_INFO = "account/pwd/auth/identity"//校验身份信息

    //地址管理
    const val DELETE_ADDRESS = "ucenter/mAddress/deleteAddressInfo"//删除
    const val GET_ADDRESS = "ucenter/mAddress/getAddressInfo"//获取地址列表
    const val UPDATE_ADDRESS = "ucenter/mAddress/saveOrUpdateAddressInfo"//新增或比编辑地址
    const val DEFAULT_ADDRESS = "ucenter/mAddress/setDefaultAddressInfo"//默认地址

    const val RED_WALLET_WATER = "account/account/redPacketBalance/flow"//红包流水

    const val USER_INCOME = "ucenter/member/getProfitInfo"//收益
    const val ACCOUNT_STATUS = "ucenter/member/getProfitInfo"//判断账户状态 正常状态 响应码200 entity=true
    const val RED_TO_MYWALLET = "account/account/personal/transferAccounts"//红包账户转入个人账户

    const val RUNNING_NUM = "account/cash/runningnum"//提现流水号

    const val BANK_INFO_CHECK = "account/bank/card/identifyIdCard"//银行卡校验
    const val WITH_DRAW_RESULT = "account/cash/queryWithdrawals"//查询提现结果

    const val CASH_PAY = "account/cash/pinpp/pay"//支付接口 支付类型 RECHARGE (1,"充值"),PAYMENT(3,"消费"), SEND_RED_PACKET(7,"发红包") 8-押金
    const val LOCAL_PAY = "account/cash/local/pay"//本地支付  支付类型 RECHARGE (1,"充值"),PAYMENT(3,"消费"), SEND_RED_PACKET(7,"发红包") 8-押金
    const val PAY_RESULT = "account/cash/get/pinpp/pay"//支付结果确认
    const val PAY_SECURITY = "ucenter/member/partner/paySecurity" //平台支付押金

    /**
     * 消息
     */
    const val MSG_SYSTEM = "ucenter/message/getSystemMessage"// 系统消息
    const val MSG_REPLY = "ucenter/message/getReplyMessage"// 回复消息
    const val MSG_RED = "ucenter/message/getRedPacketMessage"//用户红包消息
    const val MSG_UNREAD = "ucenter/message/getUnreadMessageInfo"// 未读消息数量

    /**
     * 收益
     */
    const val PROFITINFO_INFO = "ucenter/user/special/account/info"//收益
    const val PROFITINFO_LIST = "ucenter/user/special/account/detail"//收益列表

    /**
     * 转出至企业钱包 钱包
     */
    const val OUT_TO_WALLET = "account/account/special/draw"//转出至企业钱包和我的钱包
    const val OUT_TO_RUNNINGNUM = "account/account/special/runningnum"//获取收益账户转至现金账户的转出流水号

    /**
     * 首媒之家
     */
    const val SM_COMPANY_LIST = "sm-home/sm/getCompanyList"//公司列表
    const val SM_DETAIL_COMMENT = "sm-home/sm/getCommentList"//评论列表+文章详情
    const val SM_DELETE_COMMENT = "sm-home/sm/deleteComment"//删除评论
    const val SM_ADD_COMMENT = "sm-home/sm/releaseComment"//添加评论
    const val SM_FOLLOW_OR_CANCEL = "sm-home/company/home/theme/followAndCancel"//关注/取消关注
    const val BONUS_CHECKFOLLOW = "red-packet/red/packet/checkFollow" //粉丝红包检查是否关注企业
    const val SM_THEME_LIST = "sm-home/company/home/theme/getBrandExclusive"//品牌专属论坛
    const val SM_COMMON_LIST = "sm-home/company/home/theme/getThemeList"//论坛列表
    const val SM_COMMONT_DETAIL = "sm-home/sm/getCommentByCommentId"//评论详情
    const val SM_READ_ACCOUNT = "sm-home/sm/refreshReadCount"//浏览计数

    const val OPERATOR_MAIN = "ucenter/member/operator/ownership/info"//我的-服务商信息页
    const val OPERATOR_MAIN_LIST = "ucenter/member/operator/ownership/list"//获得该服务商的单独类型下线用户列表
    const val PARTNER_MAIN = "ucenter/member/partner/ownership/info"//我的-新媒人信息页
    const val PARTNER_MAIN_LIST = "ucenter/member/partner/ownership/list"//获得该新媒人的单独类型下线用户列表
}

