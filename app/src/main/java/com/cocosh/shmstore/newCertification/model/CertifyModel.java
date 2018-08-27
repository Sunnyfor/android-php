package com.cocosh.shmstore.newCertification.model;

/**
 * Created by wt on 2018/2/12.
 */

public class CertifyModel {

    /**
     * code : 0
     * entity : {"mAddress":"string","applyTime":"string","cardAddress":"string","faceRecognition":"string","idBack":"string","idFront":"string","idNo":"string","issuingAgency":"string","money":0,"partnerStatus":"string","nickname":"string","sex":"string","smCode":"string","userName":"string","validityPeriodEndTime":"string","validityPeriodStartTime":"string"}
     * message : string
     * success : true
     */

    private int code;
    private EntityBean entity;
    private String message;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public EntityBean getEntity() {
        return entity;
    }

    public void setEntity(EntityBean entity) {
        this.entity = entity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class EntityBean {
        /**
         * mAddress : string
         * applyTime : string
         * cardAddress : string
         * faceRecognition : string
         * idBack : string
         * idFront : string
         * idNo : string
         * issuingAgency : string
         * money : 0
         * partnerStatus : string
         * nickname : string
         * sex : string
         * smCode : string
         * userName : string
         * validityPeriodEndTime : string
         * validityPeriodStartTime : string
         */

        private String address;
        private String applyTime;
        private String cardAddress;
        private String faceRecognition;
        private String idBack;
        private String idFront;
        private String idNo;
        private String issuingAgency;
        private String money;
        private String partnerStatus;
        private String realName;
        private String sex;
        private String smCode;
        private String userName;
        private String validityPeriodEndTime;
        private String validityPeriodStartTime;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getApplyTime() {
            return applyTime;
        }

        public void setApplyTime(String applyTime) {
            this.applyTime = applyTime;
        }

        public String getCardAddress() {
            return cardAddress;
        }

        public void setCardAddress(String cardAddress) {
            this.cardAddress = cardAddress;
        }

        public String getFaceRecognition() {
            return faceRecognition;
        }

        public void setFaceRecognition(String faceRecognition) {
            this.faceRecognition = faceRecognition;
        }

        public String getIdBack() {
            return idBack;
        }

        public void setIdBack(String idBack) {
            this.idBack = idBack;
        }

        public String getIdFront() {
            return idFront;
        }

        public void setIdFront(String idFront) {
            this.idFront = idFront;
        }

        public String getIdNo() {
            return idNo;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }

        public String getIssuingAgency() {
            return issuingAgency;
        }

        public void setIssuingAgency(String issuingAgency) {
            this.issuingAgency = issuingAgency;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getPartnerStatus() {
            return partnerStatus;
        }

        public void setPartnerStatus(String partnerStatus) {
            this.partnerStatus = partnerStatus;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSmCode() {
            return smCode;
        }

        public void setSmCode(String smCode) {
            this.smCode = smCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getValidityPeriodEndTime() {
            return validityPeriodEndTime;
        }

        public void setValidityPeriodEndTime(String validityPeriodEndTime) {
            this.validityPeriodEndTime = validityPeriodEndTime;
        }

        public String getValidityPeriodStartTime() {
            return validityPeriodStartTime;
        }

        public void setValidityPeriodStartTime(String validityPeriodStartTime) {
            this.validityPeriodStartTime = validityPeriodStartTime;
        }
    }
}
