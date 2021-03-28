package com.example.ucdemo;

public class AddressesModel {

    private String fullName;
    private String mobileNo;
    private String address;
    private String pincode;
    private Boolean selected;

    public AddressesModel(String fullName, String address, String pincode, Boolean selected, String mobileNo) {
        this.fullName = fullName;
        this.address = address;
        this.pincode = pincode;
        this.selected = selected;
        this.mobileNo = mobileNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}


