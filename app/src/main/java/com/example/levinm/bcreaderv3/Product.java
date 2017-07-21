package com.example.levinm.bcreaderv3;

/**
 * Created by levinm on 18/07/2017.
 */

public class Product {

    private String id;
    private String name;
    private String barcode;
    private String brand;

    public Product(){}


    public Product(String id, String name, String barcode, String brand){

        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.brand = brand;
    }

public void setId(String id) {
    this.id = id;

}

public void setName(String name) {
    this.name = name;
}

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setBarcode(String barcode) {this.barcode = barcode;}


public String getId() {
    return id;
}

public String getBarCode() {
    return barcode;
}

public String getName() {
    return name;
}

public String getBrand() {return brand;}

}
