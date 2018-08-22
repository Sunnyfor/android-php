package com.cocosh.shmstore.mine.contrat

import com.cocosh.shmstore.base.BaseBean
import com.cocosh.shmstore.base.BaseModel
import com.cocosh.shmstore.base.IBasePresenter
import com.cocosh.shmstore.base.IBaseView
import com.cocosh.shmstore.mine.model.*
import com.cocosh.shmstore.sms.model.SMS
import com.cocosh.shmstore.sms.type.SMSType

/**
 * Created by lmg on 2018/5/2.
 */
class MineContrat {
    /**
     * 我的钱包
     */
    interface IMyWalletView : IBaseView {
        fun myWalletData(result: BaseModel<MyWalletModel>)
        fun drawReslut(result: BaseModel<WithDrawResultModel>)
    }

    interface IMyWalletPresenter : IBasePresenter {
        fun requestMyWalletData(flag: Int)
        fun requestDrawReslut(flag: Int)
    }

    /**
     * 银行卡信息列表
     */
    interface IBankListView : IBaseView {
        fun deleteBank(result: BaseModel<String>)
        fun bankListData(result: BaseBean<ArrayList<BankModel>>)
        fun wouldDeleteBankData(result: BaseModel<Boolean>)
    }

    interface IBankListPresenter : IBasePresenter {
        fun requestBankListData(flag: Int)
        fun requestDeleteBankData(cardId: String, pwd: String)
        fun requestWouldDeleteBankData(flag: Int)
    }

    /**
     * 企业钱包信息
     */
    interface IEntWalletView : IBaseView {
        fun entWalletData(result: BaseModel<EntWalletModel>)
    }

    interface IEntWalletPresenter : IBasePresenter {
        fun requestEntWalletData(flag: Int)
    }

    /**
     * 钱包流水
     */
    interface IEntWalletWaterView : IBaseView {
        fun entWalletWaterData(result: BaseModel<ArrayList<WalletWaterModel>>)
        fun walletWaterData(result: BaseModel<ArrayList<WalletWaterModel>>)
    }

    interface IEntWalletWaterPresenter : IBasePresenter {
        fun requestEntWalletWaterData(flag: Int, idUserAccountRecord: String, begTime: String, showCount: String)
        fun requestWalletWaterData(flag: Int, idUserAccountRecord: String, begTime: String, showCount: String)
    }

    /**
     * 绑定银行卡发送验证码
     */
    interface ISendMessageView : IBaseView {
        fun sendMessageData(result: BaseBean<SMS>)
        fun authCode(result: BaseBean<String>)
    }

    interface ISendMessagePresenter : IBasePresenter {
        fun requestSendMessageData(phone: String, smsType: SMSType)
        fun requestAuthCodeData(type: String, code: String)
    }

    /**
     * 是否设置支付密码
     */
    interface IIsSetPwdView : IBaseView {
        fun isSetPwdData(result: BaseModel<Boolean>)
    }

    interface IIsSetPwdPresenter : IBasePresenter {
        fun requestIsSetPwdData(flag: Int)
    }

    /**
     * 验证密码是否正确
     */
    interface IIsPwdRightView : IBaseView {
        fun isPwdRight(result: BaseBean<ResetPass>)
        fun savePwdData(result: BaseBean<String>)
        fun modifyPwdData(result: BaseBean<String>)
    }

    interface IIsPwdRightPresenter : IBasePresenter {
        fun requestIsPwdRightData(pwd: String)
        fun requestModifyPwdData(pwd: String)
    }

    /**
     * 银行卡类型列表
     */
    interface IBankTypeView : IBaseView {
        fun bankType(result: BaseModel<ArrayList<BankTypeModel>>)
        fun bankInfoCheck(result: BaseModel<String>)
    }

    interface IBankTypePresenter : IBasePresenter {
        fun requestBankTypeData(flag: Int)
        fun requestBankInfoCheck(flag: Int, cardNumber: String, cardUserName: String, cardUserPhone: String)
    }

    /**
     * 添加银行卡
     */
    interface IAddBankView : IBaseView {
        fun addBank(result: BaseModel<AddBankModel>)
    }

    interface IAddBankPresenter : IBasePresenter {
        fun requestAddBankData(idUserBankInfo: String, messageCode: String)
    }


    /**
     * 个人钱包提现
     */
    interface IMyWalletDrawView : IBaseView {
        fun myWalletDraw(result: BaseModel<WithDrawResultModel>)
        fun bankListDraw(result: BaseModel<BankDrawListModel>)
        fun entWalletDrawData(result: BaseModel<WithDrawResultModel>)
        fun corporateAccountData(result: BaseModel<CorporateAccountModel>)
        fun runningNum(result: BaseModel<String>)
    }

    interface IMyWalletDrawPresenter : IBasePresenter {
        fun requestMyWalletDrawData(userBankInfoId: String, amount: String, runningNum: String, paymentPassword: String)
        fun requestBankListData()
        fun requestEntWalletDrawData(money: String, token: String)
        fun requestCorporateAccountData(flag: Int)
        fun requestRunningNum(flag: Int)
    }

    /**
     * 收藏列表+
     */
    interface ICollectionView : IBaseView {
        fun collection(result: BaseModel<CollectionListModel>)
    }

    interface ICollectionPresenter : IBasePresenter {
        fun requestCollectionData(flag: Int, currentPage: String, showCount: String, timeStamp: String)
    }

    /**
     * 关注列表
     */
    interface IFollowView : IBaseView {
        fun follow(result: BaseModel<FollowListModel>)
        fun cancelFollow(result: BaseModel<Boolean>)//取消关注
    }

    interface IFollowPresenter : IBasePresenter {
        fun requestFollowData(flag: Int, currentPage: String, showCount: String, timeStamp: String)
        fun requestCancelFollowData(idCompanyInfoBase: String)
    }

    /**
     * 校验身份信息
     */
    interface ICheckPersonInfoView : IBaseView {
        fun checkPersonInfo(result: BaseBean<ResetPass>)
    }

    interface ICheckPersonInfoPresenter : IBasePresenter {
        fun requestCheckPersonInfoData(name: String, idCardNum: String)
    }

    /**
     * 地址管理
     */
    interface IAddressView : IBaseView {
        fun deleteAddress(result: BaseBean<String>)
        fun getAddress(result: BaseBean<ArrayList<Address>>)
        fun defaultAddress(result: BaseBean<String>)
    }

    interface IAddAddressView : IBaseView {
        fun addAddress(result: BaseBean<String>)
    }

    interface IAddressPresenter : IBasePresenter {
        fun requestDeleteAddress(flag: Int, idUserAddressInfo: String)
        fun requestGetAddress(flag: Int)
        fun requestDefaultAddress(idUserAddressInfo: String)
        fun requestAddAddress(id: String, receiver: String, phone: String, province: String, city: String,town:String,addr:String,default:String)
    }

    /**
     * 红包账户
     */
    interface IRedWalletView : IBaseView {
        fun redWalletData(result: BaseModel<MyWalletModel>)
        fun redWalletWaterData(result: BaseModel<ArrayList<RedWaterModel>>)
    }

    interface IRedWalletPresenter : IBasePresenter {
        fun requestRedWalletData(flag: Int)
        fun requestRedWalletWaterData(flag: Int, idUserAccountRecord: String, begTime: String, showCount: String, sort: String, sortType: String)
    }

    /**
     * 账户状态
     */
    interface IAccountStatusView : IBaseView {
        fun accountStatusData(result: BaseModel<Boolean>)
    }

    interface IAccountStatusPresenter : IBasePresenter {
        fun requestAccountStatusData()
    }

    /**
     * 红包账户转入个人账户
     */
    interface IRedToWalletView : IBaseView {
        fun redToWalletData(result: BaseModel<RedToWalletModel>)
        fun runningNumData(result: BaseModel<String>)
        fun outToData(result: BaseModel<RedToWalletModel>)
    }

    interface IRedToWalletPresenter : IBasePresenter {
        fun requestRedToWalletData(money: String, paymentPassword: String)
        fun requestRunningNumTo(flag: Int)
        fun requestDrawTo(personType: String, amount: String, runningNum: String, paymentPassword: String)
    }

}