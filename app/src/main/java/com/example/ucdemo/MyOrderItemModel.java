package com.example.ucdemo;

import java.util.Date;

public class MyOrderItemModel {

    private String productId;
    private String productImage;
    private String productTitle;

    private String orderStatus;
    private String Address;
    private String couponId;
    private String cuttedPrice;
    private Date orderedDate;
    private Date confirmedDate;
    private Date cookedDate;
    private Date completedDate;
    private Date cancelledDate;
    private String discountedPrice;
    private Long freeCoupons;
    private String fullName;
    private String orderID;
    private String paymentMethod;
    private String pincode;
    private String productPrice;
    private Long productQuantity;
    private String userId;

    private int rating;


    public MyOrderItemModel(String productId, String orderStatus, String address, String couponId, String cuttedPrice, Date orderedDate, Date confirmedDate, Date cookedDate, Date completedDate, Date cancelledDate, String discountedPrice, Long freeCoupons, String fullName, String orderID, String paymentMethod, String pincode, String productPrice, Long productQuantity, String userId, String productImage, String productTitle) {
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productId = productId;
        this.orderStatus = orderStatus;
        Address = address;
        this.couponId = couponId;
        this.cuttedPrice = cuttedPrice;
        this.orderedDate = orderedDate;
        this.confirmedDate = confirmedDate;
        this.cookedDate = cookedDate;
        this.completedDate = completedDate;
        this.cancelledDate = cancelledDate;
        this.discountedPrice = discountedPrice;
        this.freeCoupons = freeCoupons;
        this.fullName = fullName;
        this.orderID = orderID;
        this.paymentMethod = paymentMethod;
        this.pincode = pincode;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    public Date getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(Date orderedDate) {
        this.orderedDate = orderedDate;
    }

    public Date getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(Date confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public Date getCookedDate() {
        return cookedDate;
    }

    public void setCookedDate(Date cookedDate) {
        this.cookedDate = cookedDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public Date getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(Date cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Long getFreeCoupons() {
        return freeCoupons;
    }

    public void setFreeCoupons(Long freeCoupons) {
        this.freeCoupons = freeCoupons;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
