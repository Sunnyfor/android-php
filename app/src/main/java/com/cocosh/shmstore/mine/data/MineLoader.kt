package com.cocosh.shmstore.mine.data

import com.cocosh.shmstore.application.SmApplication
import com.cocosh.shmstore.base.BaseActivity
import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.home.model.SMCompanyThemeData
import com.cocosh.shmstore.http.ApiManager
import com.cocosh.shmstore.http.ApiManager2
import com.cocosh.shmstore.http.Constant
import com.cocosh.shmstore.mine.contrat.MineContrat
import com.cocosh.shmstore.mine.model.*
import com.cocosh.shmstore.utils.*
import com.umeng.socialize.media.Base

/**
 *
 * Created by lmg on 2018/5/2.
 */
class MineLoader(val activity: BaseActivity, val view: IBaseView) {
    /**
     * 获取我的钱包主页信息
     */
    fun requestMyWalletData(flag: Int) {
        val map = HashMap<String, String>()
        map["user_type"] = "p"
        ApiManager2.post(flag, activity, map, Constant.EWT, object : ApiManager2.OnResult<BaseBean<WalletModel>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<WalletModel>) {
                (view as MineContrat.IMyWalletView).myWalletData(data)
            }

            override fun onCatch(data: BaseBean<WalletModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取我的钱包银行卡列表
     */
    fun requestBankListData(flag: Int) {
        ApiManager2.get(flag, activity, null, Constant.BANKCARD, object : ApiManager2.OnResult<BaseBean<ArrayList<BankModel>>>() {
            override fun onFailed(code: String, message: String) {
                (view as MineContrat.IBankListView).bankListData(BaseBean())
            }

            override fun onSuccess(data: BaseBean<ArrayList<BankModel>>) {
                (view as MineContrat.IBankListView).bankListData(data)
            }

            override fun onCatch(data: BaseBean<ArrayList<BankModel>>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取企业钱包信息
     */
    fun requestEntWalletData(flag: Int) {
        val map = HashMap<String, String>()
        map["user_type"] = "f"
        ApiManager2.post(flag, activity, map, Constant.EWT, object : ApiManager2.OnResult<BaseBean<WalletModel>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<WalletModel>) {
                (view as MineContrat.IEntWalletView).entWalletData(data)
            }


            override fun onCatch(data: BaseBean<WalletModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     *
     */
    fun requestEntWalletWaterData(flag: Int, idUserAccountRecord: String, begTime: String, showCount: String) {
        val map = HashMap<String, String>()
        map["flowno"] = idUserAccountRecord
        map["date"] = begTime
        map["num"] = showCount
        ApiManager2.post(flag, activity, map, Constant.BALANCE_TRANS_SVC, object : ApiManager2.OnResult<BaseBean<ArrayList<WalletWaterModel>>>() {
            override fun onFailed(code: String, message: String) {
                val data = BaseBean<ArrayList<WalletWaterModel>>()
                data.status = code
                (view as MineContrat.IEntWalletWaterView).walletWaterData(data)
            }

            override fun onSuccess(data: BaseBean<ArrayList<WalletWaterModel>>) {
                (view as MineContrat.IEntWalletWaterView).entWalletWaterData(data)
            }


            override fun onCatch(data: BaseBean<ArrayList<WalletWaterModel>>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 个人钱包流水
     */
    fun requestWalletWaterData(flag: Int, idUserAccountRecord: String, begTime: String, showCount: String) {
        val map = HashMap<String, String>()
        if (idUserAccountRecord != "") {
            map["flowno"] = idUserAccountRecord
        }
        map["date"] = begTime
        map["num"] = showCount
        ApiManager2.post(flag, activity, map, Constant.BALANCE_TRANS_PERSON, object : ApiManager2.OnResult<BaseBean<ArrayList<WalletWaterModel>>>() {
            override fun onFailed(code: String, message: String) {
                val data = BaseBean<ArrayList<WalletWaterModel>>()
                data.status = code
                (view as MineContrat.IEntWalletWaterView).walletWaterData(data)
            }

            override fun onSuccess(data: BaseBean<ArrayList<WalletWaterModel>>) {
                (view as MineContrat.IEntWalletWaterView).walletWaterData(data)
            }


            override fun onCatch(data: BaseBean<ArrayList<WalletWaterModel>>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 企业提现要素
     */
    fun requestCorporateAccountData(flag: Int) {
        ApiManager2.post(flag, activity, hashMapOf(), Constant.BALANCE_CASH_SVC, object : ApiManager2.OnResult<BaseBean<CorporateAccountModel>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<CorporateAccountModel>) {
                (view as MineContrat.IMyWalletDrawView).corporateAccountData(data)
            }

            override fun onCatch(data: BaseBean<CorporateAccountModel>) {

            }
        })
    }

    /**
     * 企业钱包提现
     */
    fun requestEntWalletDrawData(amt: String, paymentPassword: String) {
        val map = HashMap<String, String>()
        map["ts"] = StringUtils.getTimeStamp()
        map["amount"] = amt
        map["paypass"] = DigestUtils.sha1(DigestUtils.md5(paymentPassword)+ map["ts"])
        ApiManager2.post(activity, map, Constant.BALANCE_DOCASH_SVC, object : ApiManager2.OnResult<BaseBean<WithDrawResultModel>>() {
            override fun onFailed(code: String, message: String) {
                val data = BaseBean<WithDrawResultModel>()
                data.status = code
                (view as MineContrat.IMyWalletDrawView).entWalletDrawData(data)
            }

            override fun onSuccess(data: BaseBean<WithDrawResultModel>) {
                (view as MineContrat.IMyWalletDrawView).entWalletDrawData(data)
            }


            override fun onCatch(data: BaseBean<WithDrawResultModel>) {
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
        map["pwd"] = DigestUtils.sha256(DigestUtils.md5(pwd), map["ts"] ?: "")
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
        ApiManager2.get(flag, activity, map, Constant.BANKLIST, object : ApiManager2.OnResult<BaseBean<ArrayList<BankTypeModel>>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<ArrayList<BankTypeModel>>) {
                (view as MineContrat.IBankTypeView).bankType(data)
            }


            override fun onCatch(data: BaseBean<ArrayList<BankTypeModel>>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 添加银行卡
     */
    fun requestAddBankData(smsKey: String, smscode: String, jobid: String) {
        val map = HashMap<String, String>()
        map["smskey"] = smsKey
        map["smscode"] = smscode
        map["jobid"] = jobid
        map["smno"] = UserManager2.getLogin()?.code ?: ""

        ApiManager2.post(activity, map, Constant.BANKCARD_BIND, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onSuccess(data: BaseBean<String>) {
                (view as MineContrat.IAddBankView).addBank(data)
            }

            override fun onCatch(data: BaseBean<String>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 删除银行卡
     */
    fun requestDeleteBankData(cardId: String, pwd: String) {
        val map = HashMap<String, String>()
        val ts = StringUtils.getTimeStamp()
        map["id"] = cardId
        map["ts"] = ts
        map["paypass"] = DigestUtils.sha1(DigestUtils.md5(pwd) + ts)
        ApiManager2.post(activity, map, Constant.BANKCARD_REMOVE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<String>) {
                (view as MineContrat.IBankListView).deleteBank(data)
            }


            override fun onCatch(data: BaseBean<String>) {
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
        ApiManager2.post(1, activity, hashMapOf(), Constant.BALANCE_CASH_PERSON, object : ApiManager2.OnResult<BaseBean<BankDrawListModel>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<BankDrawListModel>) {
                (view as MineContrat.IMyWalletDrawView).bankListDraw(data)
            }

            override fun onCatch(data: BaseBean<BankDrawListModel>) {
                LogUtil.d(data.toString())
            }
        })
    }


    /**
     * 提现
     */
    fun requestMyWalletDrawData(userBankInfoId: String, amount: String, runningNum: String, paymentPassword: String) {
        val map = HashMap<String, String>()
        map["ts"] = StringUtils.getTimeStamp()
        map["bankcard_id"] = userBankInfoId
        map["amount"] = amount
        map["paypass"] = DigestUtils.sha1(DigestUtils.md5(paymentPassword) + map["ts"])
        ApiManager2.post(activity, map, Constant.BALANCE_DOCASH_PERSON, object : ApiManager2.OnResult<BaseBean<WithDrawResultModel>>() {
            override fun onFailed(code: String, message: String) {
                val data = BaseBean<WithDrawResultModel>()
                data.status = code
                (view as MineContrat.IMyWalletDrawView).myWalletDraw(data)
            }

            override fun onSuccess(data: BaseBean<WithDrawResultModel>) {
                (view as MineContrat.IMyWalletDrawView).myWalletDraw(data)
            }

            override fun onCatch(data: BaseBean<WithDrawResultModel>) {
                LogUtil.d(data.toString())
            }
        })
    }


    /**
     * 取消关注
     */
    fun requestCancelFollowData(idCompanyInfoBase: String) {
        val map = HashMap<String, String>()
        map["idCompanyInfoBase"] = idCompanyInfoBase
        ApiManager.post(activity, map, Constant.FOLLOW_CANAEL, object : ApiManager.OnResult<BaseBean<Boolean>>() {
            override fun onSuccess(data: BaseBean<Boolean>) {
                (view as MineContrat.IFollowView).cancelFollow(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseBean<Boolean>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 收藏列表
     */
    fun requestCollectionData(flag: Int, currentPage: String, showCount: String) {
        val map = HashMap<String, String>()
        if (currentPage != "1"){
            map["no"] = currentPage
        }
        map["num"] = showCount
        ApiManager2.post(flag, activity, map, Constant.RP_FAVLIST, object : ApiManager2.OnResult<BaseBean<ArrayList<NewCollection>>>() {
            override fun onFailed(code: String, message: String) {
                val baseBean = BaseBean<ArrayList<NewCollection>>()
                baseBean.status = code
                (view as MineContrat.ICollectionView).collection(baseBean)
            }

            override fun onSuccess(data: BaseBean<ArrayList<NewCollection>>) {
                (view as MineContrat.ICollectionView).collection(data)
            }


            override fun onCatch(data: BaseBean<ArrayList<NewCollection>>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 关注列表
     */
    fun requestFollowData(flag: Int, currentPage: String, showCount: String) {
        val map = HashMap<String, String>()
        if (currentPage != "1") {
            map["eid"] = currentPage
        }
        map["num"] = showCount
        ApiManager2.post(flag, activity, map, Constant.EHOME_FOLLOW_MINE, object : ApiManager2.OnResult<BaseBean<ArrayList<SMCompanyThemeData.BBS>>>() {

            override fun onFailed(code: String, message: String) {
                val baseBean = BaseBean<ArrayList<SMCompanyThemeData.BBS>>()
                baseBean.status = code
                (view as MineContrat.IFollowView).follow(baseBean)
            }

            override fun onSuccess(data: BaseBean<ArrayList<SMCompanyThemeData.BBS>>) {
                (view as MineContrat.IFollowView).follow(data)
            }

            override fun onCatch(data: BaseBean<ArrayList<SMCompanyThemeData.BBS>>) {
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
        map["id"] = idUserAddressInfo
        ApiManager2.post(activity, map, Constant.ADDRESS_DELETE, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<String>) {
                (view as MineContrat.IAddressView).deleteAddress(idUserAddressInfo)
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
                val  data = BaseBean<ArrayList<Address>>()
                (view as MineContrat.IAddressView).getAddress(data)
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
        map["id"] = id
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
        map["id"] = idUserAddressInfo
        ApiManager2.post(activity, map, Constant.ADDRESS_SETDEF, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<String>) {
                (view as MineContrat.IAddressView).defaultAddress(idUserAddressInfo)
            }


            override fun onCatch(data: BaseBean<String>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取红包列表信息
     */
    fun requestRedWalletWater(flag: Int, idUserAccountRecord: String, begTime: String) {
        val map = HashMap<String, String>()
        map["date"] = begTime
        map["flowno"] = idUserAccountRecord
        map["num"] = "20"
        ApiManager2.post(flag, activity, map, Constant.RP_TRANS, object : ApiManager2.OnResult<BaseBean<ArrayList<RedWaterModel>>>() {
            override fun onFailed(code: String, message: String) {
                val data = BaseBean<ArrayList<RedWaterModel>>()
                (view as MineContrat.IRedWalletView).redWalletWaterData(data)
            }

            override fun onSuccess(data: BaseBean<ArrayList<RedWaterModel>>) {
                (view as MineContrat.IRedWalletView).redWalletWaterData(data)
            }


            override fun onCatch(data: BaseBean<ArrayList<RedWaterModel>>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取我的钱包主页信息
     */
    fun requestRedWalletData(flag: Int) {
        val map = HashMap<String, String>()
        map["user_type"] = "p"
        ApiManager2.post(flag, activity, map, Constant.EWT, object : ApiManager2.OnResult<BaseBean<WalletModel>>() {
            override fun onFailed(code: String, message: String) {
            }

            override fun onSuccess(data: BaseBean<WalletModel>) {
                (view as MineContrat.IRedWalletView).redWalletData(data)
            }


            override fun onCatch(data: BaseBean<WalletModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 转至红包账户
     */
    fun requestRedToWallet(money: String, paymentPassword: String) {
        val map = HashMap<String, String>()
        map["ts"] = StringUtils.getTimeStamp()
        map["amount"] = money
        map["paypass"] = DigestUtils.sha1(DigestUtils.md5(paymentPassword) + map["ts"])
        ApiManager2.post(activity, map, Constant.RP_TRANSFER, object : ApiManager2.OnResult<BaseBean<RedToWalletModel>>() {
            override fun onFailed(code: String, message: String) {
                val baseBean = BaseBean<RedToWalletModel>()
                baseBean.status = code
                (view as MineContrat.IRedToWalletView).redToWalletData(baseBean)
            }

            override fun onSuccess(data: BaseBean<RedToWalletModel>) {
                (view as MineContrat.IRedToWalletView).redToWalletData(data)
            }

            override fun onCatch(data: BaseBean<RedToWalletModel>) {
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
//        var map = HashMap<String, String>()
//        ApiManager.get(flag, activity, map, Constant.RUNNING_NUM, object : ApiManager.OnResult<BaseBean<String>>() {
//            override fun onSuccess(data: BaseBean<String>) {
//                (view as MineContrat.IMyWalletDrawView).runningNum(data)
//            }
//
//            override fun onFailed(e: Throwable) {
//                LogUtil.d(e.message.toString())
//            }
//
//            override fun onCatch(data: BaseBean<String>) {
//                LogUtil.d(data.toString())
//            }
//        })
    }


    /**
     * 银行卡校验
     */
    fun requestCheckBankInfo(flag: Int, bank_kind: String, cardNumber: String, cardUserPhone: String) {
        val map = HashMap<String, String>()
        map["bank_kind"] = bank_kind
        map["card_kind"] = "0"
        map["card_no"] = cardNumber
        map["phone"] = cardUserPhone

        ApiManager2.post(flag, activity, map, Constant.BANKCARD_CARD_VIERIFY, object : ApiManager2.OnResult<BaseBean<String>>() {
            override fun onFailed(code: String, message: String) {

            }

            override fun onSuccess(data: BaseBean<String>) {
                (view as MineContrat.IBankTypeView).bankInfoCheck(data)
            }

            override fun onCatch(data: BaseBean<String>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取提现结果
     */
    fun requestDrawReslut(flag: Int) {
        var map = HashMap<String, String>()
        ApiManager.get(flag, activity, map, Constant.WITH_DRAW_RESULT, object : ApiManager.OnResult<BaseBean<WithDrawResultModel>>() {
            override fun onSuccess(data: BaseBean<WithDrawResultModel>) {
                (view as MineContrat.IMyWalletView).drawReslut(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseBean<WithDrawResultModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取转出流水号
     */
    fun requestRunningNumTo(flag: Int,type:String) {
        val url = if(type == "x") Constant.MYSELF_MATCHMAKER_PROFIT_DATA else Constant.MYSELF_PROVIDER_PROFIT_DATA
        ApiManager.post(flag, activity, hashMapOf(), url, object : ApiManager.OnResult<BaseBean<ProfitInfoModel>>() {
            override fun onSuccess(data: BaseBean<ProfitInfoModel>) {
                (view as MineContrat.IRedToWalletView).runningNumData(data)
            }

            override fun onFailed(e: Throwable) {
                LogUtil.d(e.message.toString())
            }

            override fun onCatch(data: BaseBean<ProfitInfoModel>) {
                LogUtil.d(data.toString())
            }
        })
    }

    /**
     * 获取提现结果
     */
    fun requestDrawTo(personType: String, amount: String,paymentPassword: String) {
        val url = if (personType == "x") Constant.INCOME_TRANSFER_OUT_NEW else Constant.INCOME_TRANSFER_OUT_SVC
        val map = HashMap<String, String>()
        map["ts"] = StringUtils.getTimeStamp()
        map["amount"] = amount
        map["paypass"] = DigestUtils.sha1(DigestUtils.md5(paymentPassword) + map["ts"])
        ApiManager2.post(activity, map, url, object : ApiManager2.OnResult<BaseBean<RedToWalletModel>>() {
            override fun onFailed(code: String, message: String) {
                val baseBean =BaseBean<RedToWalletModel>()
                baseBean.status = code
                (view as MineContrat.IRedToWalletView).outToData(baseBean)
            }

            override fun onSuccess(data: BaseBean<RedToWalletModel>) {
                (view as MineContrat.IRedToWalletView).outToData(data)
            }


            override fun onCatch(data: BaseBean<RedToWalletModel>) {
                LogUtil.d(data.toString())
            }
        })
    }
}