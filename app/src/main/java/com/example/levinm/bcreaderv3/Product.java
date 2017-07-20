package com.example.levinm.bcreaderv3;

/**
 * Created by levinm on 18/07/2017.
 */

public class Product {

    private String id;
    private String name;
    private String barcode;

    public Product(){}


    public Product(String id, String name, String barcode){

        this.id = id;
        this.name = name;
        this.barcode = barcode;
    }

public void setId(String id) {
    this.id = id;

}

public void setName(String name) {
    this.name = name;
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

}
