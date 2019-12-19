package com.noon.a7pets.models;

import java.io.Serializable;

/**
 * Created by kshitij on 16/1/18.
 */

public class GenericProductModel implements Serializable {

    public String cardid;
    public String cardname;
    public String cardimage;
    public String carddiscription;
    public String cardprice;


    public GenericProductModel(String cardid, String cardname, String cardimage, String carddiscription, String cardprice) {
        this.cardid = cardid;
        this.cardname = cardname;
        this.cardimage = cardimage;
        this.carddiscription = carddiscription;
        this.cardprice = cardprice;
    }

    public String getCardid() {
        return cardid;
    }

    public String getCardname() {
        return cardname;
    }

    public String getCardimage() {
        return cardimage;
    }

    public String getCarddiscription() {
        return carddiscription;
    }

    public String getCardprice() {
        return cardprice;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public void setCardimage(String cardimage) {
        this.cardimage = cardimage;
    }

    public void setCarddiscription(String carddiscription) {
        this.carddiscription = carddiscription;
    }

    public void setCardprice(String cardprice) {
        this.cardprice = cardprice;
    }
}