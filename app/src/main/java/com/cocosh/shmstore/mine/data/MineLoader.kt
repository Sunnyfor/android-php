package com.cocosh.shmstore.mine.data

import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.*
import com.cocosh.shmstore.utils.DataCode
import com.cocosh.shmstore.utils.DigestUtils
import com.cocosh.shmstore.utils.LogUtil
import com.cocosh.shmstore.utils.StringUtils

/**
 * Created by lmg on 2018/5/2.
 */
class MineLoader(val activity: BaseActivity, val view: IBaseView) {
    /**
     * 获取我的钱包主页信息
     */
    fun requestMyWalletData(flag: Int) {
        var map = HashMap<String, String>()
        ApiManager.get(flag, activity, map, Constant.MY_WALLET_DATA, object : ApiManager.OnResult<BaseModel<MyWalletModel>>() {
            override fun onSuccess(data: BaseModel<MyWalletModel>) {
                (view as MineContrat.IMyWalletView).myWalletData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<MyWalletModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取我的钱包银行卡列表
     */
    fun requestBankListData(flag: Int) {
        var map = HashMap<String, String>()
        ApiManager.get(flag, activity, map, Constant.MY_WALLET_BANKLIST, object : ApiManager.OnResult<BaseModel<ArrayList<BankModel>>>() {
            override fun onSuccess(data: BaseModel<ArrayList<BankModel>>) {
                (view as MineContrat.IBankListView).bankListData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<ArrayList<BankModel>>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取企业钱包信息
     */
    fun requestEntWalletData(flag: Int) {
        var map = HashMap<String, String>()
        ApiManager.get(flag, activity, map, Constant.ENT_WALLET_DATA, object : ApiManager.OnResult<BaseModel<EntWalletModel>>() {
            override fun onSuccess(data: BaseModel<EntWalletModel>) {
                (view as MineContrat.IEntWalletView).entWalletData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<EntWalletModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取企业钱包流水
     */
    fun requestEntWalletWaterData(flag: Int, idUserAccountRecord: String, begTime: String, showCount: String) {
        var map = HashMap<String, String>()
        map["idUserAccountRecord"] = idUserAccountRecord
        map["begTime"] = begTime
        map["showCount"] = showCount
        ApiManager.get(flag, activity, map, Constant.ENT_WALLET_WATER, object : ApiManager.OnResult<BaseModel<ArrayList<WalletWaterModel>>>() {
            override fun onSuccess(data: BaseModel<ArrayList<WalletWaterModel>>) {
                (view as MineContrat.IEntWalletWaterView).entWalletWaterData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<ArrayList<WalletWaterModel>>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取企业钱包流水
     */
    fun requestWalletWaterData(flag: Int, idUserAccountRecord: String, begTime: String, showCount: String) {
        var map = HashMap<String, String>()
        map["idUserAccountRecord"] = idUserAccountRecord
        map["begTime"] = begTime
        map["showCount"] = showCount
        ApiManager.get(flag, activity, map, Constant.WALLET_BANKLIST, object : ApiManager.OnResult<BaseModel<ArrayList<WalletWaterModel>>>() {
            override fun onSuccess(data: BaseModel<ArrayList<WalletWaterModel>>) {
                (view as MineContrat.IEntWalletWaterView).entWalletWaterData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<ArrayList<WalletWaterModel>>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 对公账号信息
     */
    fun requestCorporateAccountData(flag: Int) {
        var map = HashMap<String, String>()
        ApiManager.get(flag, activity, map, Constant.ENT_WALLET_DRAWINFO, object : ApiManager.OnResult<BaseModel<CorporateAccountModel>>() {
            override fun onSuccess(data: BaseModel<CorporateAccountModel>) {
                (view as MineContrat.IMyWalletDrawView).corporateAccountData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<CorporateAccountModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 企业钱包提现
     */
    fun requestEntWalletDrawData(amt: String, paymentPassword: String) {
        var map = HashMap<String, String>()
        map["amt"] = amt
        map["paymentPassword"] = paymentPassword
        ApiManager.post(activity, map, Constant.ENT_WALLET_DRAW, object : ApiManager.OnResult<BaseModel<WithDrawResultModel>>() {
            override fun onSuccess(data: BaseModel<WithDrawResultModel>) {
                (view as MineContrat.IMyWalletDrawView).entWalletDrawData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<WithDrawResultModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 发送验证码
     */
//    fun requestSendMessageData(phone: String) {
//        var map = HashMap<String, String>()
//        map["mobile"] = phone
//        ApiManager.post(activity, map, Constant.CHANGE_PAY_PWD_SEND_CODE, object : ApiManager.OnResult<BaseModel<String>>() {
//            override fun onSuccess(data: BaseModel<String>) {
//                (view as MineContrat.ISendMessageView).sendMessageData(data)
//            }
//
//            override fun onFailed(e: Throwable) {
//                LogUtil.d(e.message.toString())
//            }
//
//            override fun onCatch(data: BaseModel<String>) {
//                LogUtil.d(data.toString())
//            }
//        })
//    }

//    /**
//     * 发送验证码
//     */
//    fun requestSendMessageData(phone: String, hasVolidate: Boolean) {
//        var map = HashMap<String, String>()
//        map["mobile"] = phone
//        map["hasVolidate"] = hasVolidate.toString()
//        ApiManager.post(activity, map, Constant.CHANGE_PAY_PWD_SEND_CODE, object : ApiManager.OnResult<BaseModel<String>>() {
//            override fun onSuccess(data: BaseModel<String>) {
//                (view as MineContrat.ISendMessageView).sendMessageData(data)
//            }
//
//            override fun onFailed(e: Throwable) {
//                LogUtil.d(e.message.toString())
//            }
//
//            override fun onCatch(data: BaseModel<String>) {
//                LogUtil.d(data.toString())
//            }
//        })
//    }


    /**
     * 校验 验证码
     */
    fun requestAuthCodeData(target: String, smskey: String, code: String) {
        val map = HashMap<String, String>()
        map["target"] = target
        map["smscode"] = code
        map["smskey"] = smskey
        ApiManager2.post(activity, map, Constant.PAYPASS_SMSCHECK, object : ApiManager2.OnResult<BaseBean<String>>() {

            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<String>) {
                (view as MineContrat.ISendMessageView).authCode(data)
            }


            override fun onCatch(data: BaseBean<String>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 是否设置支付密码
     */
    fun requestIsSetPwdData(flag: Int) {
        var map = HashMap<String, String>()
        ApiManager.get(flag, activity, map, Constant.IS_SET_PWD, object : ApiManager.OnResult<BaseModel<Boolean>>() {
            override fun onSuccess(data: BaseModel<Boolean>) {
                (view as MineContrat.IIsSetPwdView).isSetPwdData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<Boolean>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 校验支付密码
     */
    fun requestIsPwdRightData(pwd: String) {
        val map = HashMap<String, String>()
        map["ts"] = StringUtils.getTimeStamp()
        map["pwd"] = DigestUtils.md5(pwd)
        ApiManager2.post(activity, map, Constant.PAYPASS_OLDCHECK, object : ApiManager2.OnResult<BaseBean<ResetPass>>() {
            override fun onSuccess(data: BaseBean<ResetPass>) {
                (view as MineContrat.IIsPwdRightView).isPwdRight(data)
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<ResetPass>) {
            }
        })
    }

    /**
     * 银行卡类型列表
     */
    fun requestBankTypeData(flag: Int) {
        val map = HashMap<String, String>()
        ApiManager.get(flag, activity, map, Constant.GET_BANK_TYPE, object : ApiManager.OnResult<BaseModel<ArrayList<BankTypeModel>>>() {
            override fun onSuccess(data: BaseModel<ArrayList<BankTypeModel>>) {
                (view as MineContrat.IBankTypeView).bankType(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<ArrayList<BankTypeModel>>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 添加银行卡
     */
    fun requestAddBankData(idUserBankInfo: String, messageCode: String) {
        val map = HashMap<String, String>()
        map["idUserBankInfo"] = idUserBankInfo
        map["messageCode"] = messageCode
        ApiManager.post(activity, map, Constant.ADD_BANK, object : ApiManager.OnResult<BaseModel<AddBankModel>>() {
            override fun onSuccess(data: BaseModel<AddBankModel>) {
                (view as MineContrat.IAddBankView).addBank(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<AddBankModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 删除银行卡
     */
    fun requestDeleteBankData(cardId: String, pwd: String) {
        var map = HashMap<String, String>()
        map["cardId"] = cardId
        map["pwd"] = pwd
        ApiManager.post(activity, map, Constant.DELETE_BANK, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                (view as MineContrat.IBankListView).deleteBank(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<String>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 校验是否可解绑银行卡
     */
    fun requestWouldDeleteBankData(flag: Int) {
        var map = HashMap<String, String>()
        ApiManager.get(flag, activity, map, Constant.CHECK_BANK_DELETE, object : ApiManager.OnResult<BaseModel<Boolean>>() {
            override fun onSuccess(data: BaseModel<Boolean>) {
                (view as MineContrat.IBankListView).wouldDeleteBankData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<Boolean>) {
                LogUtil.d(data.toString())
            }
        })
    }


    /**
     * 修改支付密码
     */
    fun requestModifyPwdData(pwd: String) {
        val map = HashMap<String, String>()
        val resetPass = SmApplication.getApp().getData<ResetPass>(DataCode.RESET_PAY_PASS, false)
        map["ticket"] = resetPass?.ticket ?: ""
        map["ts"] = resetPass?.ts ?: ""
        map["paypwd"] = DigestUtils.md5(pwd)
        ApiManager2.post(activity, map, Constant.PAYPASS_SET, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onSuccess(data: BaseBean<String>) {
                (view as MineContrat.IIsPwdRightView).modifyPwdData(data)
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<String>) {

            }

        })
    }


    /**
     * 提现 银行卡弹窗列表
     */
    fun requestBankListData() {
        var map = HashMap<String, String>()
        ApiManager.get(1, activity, map, Constant.MY_WALLET_DRAWINFO, object : ApiManager.OnResult<BaseModel<BankDrawListModel>>() {
            override fun onSuccess(data: BaseModel<BankDrawListModel>) {
                (view as MineContrat.IMyWalletDrawView).bankListDraw(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<BankDrawListModel>) {
                LogUtil.d(data.toString())
            }
        })
    }


    /**
     * 提现
     */
    fun requestMyWalletDrawData(userBankInfoId: String, amount: String, runningNum: String, paymentPassword: String) {
        var map = HashMap<String, String>()
        map["userBankInfoId"] = userBankInfoId
        map["amount"] = amount
        map["runningNum"] = runningNum
        map["paymentPassword"] = paymentPassword
        ApiManager.post(activity, map, Constant.MY_WALLET_DRAW, object : ApiManager.OnResult<BaseModel<WithDrawResultModel>>() {
            override fun onSuccess(data: BaseModel<WithDrawResultModel>) {
                (view as MineContrat.IMyWalletDrawView).myWalletDraw(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<WithDrawResultModel>) {
                LogUtil.d(data.toString())
            }
        })
    }


    /**
     * 取消关注
     */
    fun requestCancelFollowData(idCompanyInfoBase: String) {
        var map = HashMap<String, String>()
        map["idCompanyInfoBase"] = idCompanyInfoBase
        ApiManager.post(activity, map, Constant.FOLLOW_CANAEL, object : ApiManager.OnResult<BaseModel<Boolean>>() {
            override fun onSuccess(data: BaseModel<Boolean>) {
                (view as MineContrat.IFollowView).cancelFollow(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<Boolean>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 收藏列表
     */
    fun requestCollectionData(flag: Int, currentPage: String, showCount: String, timeStamp: String) {
        var map = HashMap<String, String>()
        map["currentPage"] = currentPage
        map["showCount"] = showCount
        map["timeStamp"] = timeStamp
        ApiManager.get(flag, activity, map, Constant.COLLECTION_LIST, object : ApiManager.OnResult<BaseModel<CollectionListModel>>() {
            override fun onSuccess(data: BaseModel<CollectionListModel>) {
                (view as MineContrat.ICollectionView).collection(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<CollectionListModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 关注列表
     */
    fun requestFollowData(flag: Int, currentPage: String, showCount: String, timeStamp: String) {
        var map = HashMap<String, String>()
        map["currentPage"] = currentPage
        map["showCount"] = showCount
        map["timeStamp"] = timeStamp
        ApiManager.get(flag, activity, map, Constant.FOLLOW_LIST, object : ApiManager.OnResult<BaseModel<FollowListModel>>() {
            override fun onSuccess(data: BaseModel<FollowListModel>) {
                (view as MineContrat.IFollowView).follow(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<FollowListModel>) {
                LogUtil.d(data.toString())
            }
        })
    }


    /**
     * 校验身份信息
     */
    fun requestCheckPersonInfoData(name: String, idCardNum: String) {
        val map = HashMap<String, String>()
        map["name"] = name
        map["idno"] = idCardNum
        ApiManager2.post(activity, map, Constant.PAYPASS_CERTCHECK, object : ApiManager2.OnResult<BaseBean<ResetPass>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<ResetPass>) {
                (view as MineContrat.ICheckPersonInfoView).checkPersonInfo(data)
            }

            override fun onCatch(data: BaseBean<ResetPass>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 删除地址
     */
    fun requestDeleteAddress(idUserAddressInfo: String) {
        val map = HashMap<String, String>()
        map["comment_id"] = idUserAddressInfo
        ApiManager2.post(activity, map, Constant.ADDRESS_DELETE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<String>) {
                (view as MineContrat.IAddressView).deleteAddress(data)
            }


            override fun onCatch(data: BaseBean<String>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取地址列表
     */
    fun requestGetAddress(flag: Int) {
        ApiManager2.get(flag, activity, null, Constant.ADDRESS_LIST, object : ApiManager2.OnResult<BaseBean<ArrayList<Address>>>() {
            override fun onSuccess(data: BaseBean<ArrayList<Address>>) {
                (view as MineContrat.IAddressView).getAddress(data)
            }

            override fun onFailed(code: String, message: String) {
            }

            override fun onCatch(data: BaseBean<ArrayList<Address>>) {
            }
        })
    }


    /**
     * 更改 新增地址
     */
    fun requestAddAddress(id: String, receiver: String, phone: String, province: String, city: String, town: String, addr: String, default: String) {
        val map = HashMap<String, String>()
        map["comment_id"] = id
        map["receiver"] = receiver
        map["phone"] = phone
        map["province"] = province
        map["city"] = city
        map["town"] = town
        map["addr"] = addr
        map["default"] = default
        ApiManager2.post(activity, map, Constant.ADDRESS_SAVE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<String>) {
                (view as MineContrat.IAddAddressView).addAddress(data)
            }


            override fun onCatch(data: BaseBean<String>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 更改 默认地址
     */
    fun requestDefaultAddress(idUserAddressInfo: String) {
        val map = HashMap<String, String>()
        map["comment_id"] = idUserAddressInfo
        ApiManager2.post(activity, map, Constant.ADDRESS_SETDEF, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<String>) {
                (view as MineContrat.IAddressView).defaultAddress(data)
            }


            override fun onCatch(data: BaseBean<String>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取红包列表信息
     */
    fun requestRedWalletWater(flag: Int, idUserAccountRecord: String, begTime: String, showCount: String, sort: String, sortType: String) {
        val map = HashMap<String, String>()
        map["idUserAccountRecord"] = idUserAccountRecord
        map["begTime"] = begTime
        map["showCount"] = showCount
        map["sort"] = sort
        map["sortType"] = sortType
        ApiManager.get(flag, activity, map, Constant.RED_WALLET_WATER, object : ApiManager.OnResult<BaseModel<ArrayList<RedWaterModel>>>() {
            override fun onSuccess(data: BaseModel<ArrayList<RedWaterModel>>) {
                (view as MineContrat.IRedWalletView).redWalletWaterData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<ArrayList<RedWaterModel>>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取我的钱包主页信息
     */
    fun requestRedWalletData(flag: Int) {
        var map = HashMap<String, String>()
        ApiManager.get(flag, activity, map, Constant.MY_WALLET_DATA, object : ApiManager.OnResult<BaseModel<MyWalletModel>>() {
            override fun onSuccess(data: BaseModel<MyWalletModel>) {
                (view as MineContrat.IRedWalletView).redWalletData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<MyWalletModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 转至红包账户
     */
    fun requestRedToWallet(money: String, paymentPassword: String) {
        var map = HashMap<String, String>()
        map["money"] = money
        map["paymentPassword"] = paymentPassword
        ApiManager.post(activity, map, Constant.RED_TO_MYWALLET, object : ApiManager.OnResult<BaseModel<RedToWalletModel>>() {
            override fun onSuccess(data: BaseModel<RedToWalletModel>) {
                (view as MineContrat.IRedToWalletView).redToWalletData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<RedToWalletModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取账户状态
     */
    fun requestAccountStatus() {
        var map = HashMap<String, String>()
        ApiManager.post(activity, map, Constant.ACCOUNT_STATUS, object : ApiManager.OnResult<BaseModel<Boolean>>() {
            override fun onSuccess(data: BaseModel<Boolean>) {
                (view as MineContrat.IAccountStatusView).accountStatusData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<Boolean>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取提现流水号
     */
    fun requestRunningNum(flag: Int) {
        var map = HashMap<String, String>()
        ApiManager.get(flag, activity, map, Constant.RUNNING_NUM, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                (view as MineContrat.IMyWalletDrawView).runningNum(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<String>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取提现流水号
     */
    fun requestCheckBankInfo(flag: Int, cardNumber: String, cardUserName: String, cardUserPhone: String) {
        var map = HashMap<String, String>()
        map["cardNumber"] = cardNumber
        map["cardUserName"] = cardUserName
        map["cardUserPhone"] = cardUserPhone
        ApiManager.get(flag, activity, map, Constant.BANK_INFO_CHECK, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                (view as MineContrat.IBankTypeView).bankInfoCheck(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<String>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取提现结果
     */
    fun requestDrawReslut(flag: Int) {
        var map = HashMap<String, String>()
        ApiManager.get(flag, activity, map, Constant.WITH_DRAW_RESULT, object : ApiManager.OnResult<BaseModel<WithDrawResultModel>>() {
            override fun onSuccess(data: BaseModel<WithDrawResultModel>) {
                (view as MineContrat.IMyWalletView).drawReslut(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<WithDrawResultModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取转出流水号
     */
    fun requestRunningNumTo(flag: Int) {
        var map = HashMap<String, String>()
        ApiManager.get(flag, activity, map, Constant.OUT_TO_RUNNINGNUM, object : ApiManager.OnResult<BaseModel<String>>() {
            override fun onSuccess(data: BaseModel<String>) {
                (view as MineContrat.IRedToWalletView).runningNumData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<String>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取提现结果
     */
    fun requestDrawTo(personType: String, amount: String, runningNum: String, paymentPassword: String) {
        var map = HashMap<String, String>()
        map["personType"] = personType
        map["amount"] = amount
        map["runningNum"] = runningNum
        map["paymentPassword"] = paymentPassword
        ApiManager.post(activity, map, Constant.OUT_TO_WALLET, object : ApiManager.OnResult<BaseModel<RedToWalletModel>>() {
            override fun onSuccess(data: BaseModel<RedToWalletModel>) {
                (view as MineContrat.IRedToWalletView).outToData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseModel<RedToWalletModel>) {
                LogUtil.d(data.toString())
            }
        })
    }
}