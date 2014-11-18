package com.android.example.leanback.data;

/**
 * Created by maui on 18.11.14.
 */
public class GdeProduct {
    private final String name, productCode;

    public GdeProduct(String name, String productCode) {
        this.name = name;
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public String getProductCode() {
        return productCode;
    }
}
