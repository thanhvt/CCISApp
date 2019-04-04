package com.es.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "DonGia_DB")
public class DonGia_DB extends Model implements Serializable {

    @Column(name = "PriceId")
    public int PriceId;

    @Column(name = "OccupationsGroupCode")
    public String OccupationsGroupCode;

    @Column(name = "Description")
    public String Description;

    @Column(name = "Time")
    public String Time;

    @Column(name = "PriceRound")
    public double PriceRound;

    public DonGia_DB() {

    }

    public DonGia_DB(int priceId, String occupationsGroupCode, String description, String time, double priceRound) {
        PriceId = priceId;
        OccupationsGroupCode = occupationsGroupCode;
        Description = description;
        Time = time;
        PriceRound = priceRound;
    }

    @Override
    public String toString() {
        return "DonGia_DB{" +
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
