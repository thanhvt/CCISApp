package com.es.model;


import com.es.printer.StaticValue;

/**
 * Created by shohrab.uddin on 29.12.2015.
 */
public class SalesModel {

    String productShortName;
    int salesAmount;
    double unitSalesCost;

    public SalesModel(String productSName, int amount, double unitSCost) {
        this.productShortName = productSName;
        this.salesAmount = amount;
        this.unitSalesCost = unitSCost;
    }

    public SalesModel() {

    }

    public static void generatedMoneyReceipt() {
//        SalesModel salesModel = new SalesModel("Vegetable Noodle", 10, 3);
//        StaticValue.arrayListSalesModel.add(salesModel);
        SalesModel salesModel1 = new SalesModel("Số khối", 15, 20000);
        StaticValue.arrayListSalesModel.add(salesModel1);
        SalesModel salesModel2 = new SalesModel("Số người", 3, 50000);
        StaticValue.arrayListSalesModel.add(salesModel2);

    }

    public String getProductShortName() {
        return productShortName;
    }

    public int getSalesAmount() {
        return salesAmount;
    }

    public double getUnitSalesCost() {
        return unitSalesCost;
    }


}
