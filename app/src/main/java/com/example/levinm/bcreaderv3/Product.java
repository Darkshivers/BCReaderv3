package com.example.levinm.bcreaderv3;

/**
 * Created by levinm on 18/07/2017.
 */

public class Product {

    private String id;
    private String name;
    private String barcode;
    private String brand;

//    private String prodid;
//    private String price;

    public Product(){}


    public Product(String id, String name, String barcode, String brand){

        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.brand = brand;

//        this.prodid = prodid;
//        this.price = price;


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

//    public void setProdid(String prodid) {this.prodid = prodid;}
//
//    public void setPrice(String price) {this.price = price;}


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

//public String getProdid() {return prodid;}
//
//public String getPrice() {return price;}
}
