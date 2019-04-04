package com.es.model;

import java.io.Serializable;

public class DonGia implements Serializable {

    public int PriceId;
    public String OccupationsGroupCode;
    public String Description;
    public String Time;
    public double PriceRound;

    public DonGia() {

    }

    public DonGia(int priceId, String occupationsGroupCode, String description, String time, double priceRound) {
        PriceId = priceId;
        OccupationsGroupCode = occupationsGroupCode;
        Description = description;
        Time = time;
        PriceRound = priceRound;
    }

    @Override
    public String toString() {
        return "DonGia{" +
                "PriceId=" + PriceId +
                ", OccupationsGroupCode='" + OccupationsGroupCode + '\'' +
                ", Description='" + Description + '\'' +
                ", Time='" + Time + '\'' +
                ", PriceRound=" + PriceRound +
                '}';
    }

    public int getPriceId() {
        return PriceId;
    }

    public void setPriceId(int priceId) {
        PriceId = priceId;
    }

    public String getOccupationsGroupCode() {
        return OccupationsGroupCode;
    }

    public void setOccupationsGroupCode(String occupationsGroupCode) {
        OccupationsGroupCode = occupationsGroupCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public double getPriceRound() {
        return PriceRound;
    }

    public void setPriceRound(double priceRound) {
        PriceRound = priceRound;
    }
}
