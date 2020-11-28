package com.es.phoneshop.web.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class OrderDataForm {

    @Size(min = 1, message = "wrong value",max = 20)
    private String firstName;

    @Size(min = 1, message = "wrong value",max = 20)
    private String lastName;

    @Size(min = 1, message = "wrong value", max = 50)
    private String deliveryAddress;

    @Pattern(message = "wrong format", regexp="^(\\+\\d{1,3} ?)?(\\d{2} ?)(\\d{3}[- ]?)(\\d{2}[- ]?)\\d{2}$")
    private String contactPhoneNo;

    @Size(min = 0, message = "wrong value", max = 200)
    private String additionalInformation;

    public OrderDataForm() { }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getContactPhoneNo() {
        return contactPhoneNo;
    }

    public void setContactPhoneNo(String contactPhoneNo) {
        this.contactPhoneNo = contactPhoneNo;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
}
