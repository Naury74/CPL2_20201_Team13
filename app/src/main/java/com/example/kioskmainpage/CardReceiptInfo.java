package com.example.kioskmainpage;

public class CardReceiptInfo {

    private String tid;//단말기번호
    private String approvalNo;//승인번호
    private String approvalDate;//승인날짜
    private String cardNo;//카드번호
    private int monthVal;//할부개월
    private long sumVal;//최종결제금액
    private long priceVal;//물품가액
    private long vatVal;//부가세액
    private String issuerName;//발급사
    private String acquierName;//매입사
    private String orgNo;//사업자번호
    private String orgAddress;//주소
    private String orgCeoName;//대표자
    private String orgTel;//대표자 전화번호
    private String orgRegName;//상호
    private String message1;//에러메시지


    public CardReceiptInfo(String tid, String approvalNo, String approvalDate, String cardNo,
                           int monthVal, long sumVal, long priceVal, long vatVal,
                           String issuerName, String acquierName, String orgNo, String orgAddress, String orgCeoName, String orgTel, String orgRegName, String message1)
    {
            this.tid = tid;
            this.approvalNo = approvalNo;
            this.approvalDate = approvalDate;
            this.cardNo = cardNo;
            this.monthVal = monthVal;
            this.sumVal = sumVal;
            this.priceVal = priceVal;
            this.vatVal = vatVal;
            this.issuerName = issuerName;
            this.acquierName = acquierName;
            this.orgNo = orgNo;
            this.orgAddress = orgAddress;
            this.orgCeoName = orgCeoName;
            this.orgTel = orgTel;
            this.orgRegName = orgRegName;
            this.message1 = message1;
    }
    public CardReceiptInfo()
    {

    }

    public String getMessage1() {
        return message1;
    }

    public void setMessage1(String message1) {
        this.message1 = message1;
    }

    public String getOrgRegName() {
        return orgRegName;
    }

    public void setOrgRegName(String orgRegName) {
        this.orgRegName = orgRegName;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getApprovalNo() {
        return approvalNo;
    }

    public void setApprovalNo(String approvalNo) {
        this.approvalNo = approvalNo;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getMonthVal() {
        return monthVal;
    }

    public void setMonthVal(int monthVal) {
        this.monthVal = monthVal;
    }

    public long getSumVal() {
        return sumVal;
    }

    public void setSumVal(long sumVal) {
        this.sumVal = sumVal;
    }

    public long getPriceVal() {
        return priceVal;
    }

    public void setPriceVal(long priceVal) {
        this.priceVal = priceVal;
    }

    public long getVatVal() {
        return vatVal;
    }

    public void setVatVal(long vatVal) {
        this.vatVal = vatVal;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getAcquierName() {
        return acquierName;
    }

    public void setAcquierName(String acquierName) {
        this.acquierName = acquierName;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }

    public String getOrgCeoName() {
        return orgCeoName;
    }

    public void setOrgCeoName(String orgCeoName) {
        this.orgCeoName = orgCeoName;
    }

    public String getOrgTel() {
        return orgTel;
    }

    public void setOrgTel(String orgTel) {
        this.orgTel = orgTel;
    }
}
