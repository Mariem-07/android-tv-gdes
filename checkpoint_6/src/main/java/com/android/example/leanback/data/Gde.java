package com.android.example.leanback.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maui on 18.11.14.
 */
public class Gde {
    private final String email, location, name;
    private final String[] productCodes, products;

    @SerializedName("_id")
    private final String id;

    private Country country;

    private Geo geo;

    public Gde(String email, String location, String name, String[] productCodes, String[] products, String id, Country country) {
        this.email = email;
        this.location = location;
        this.name = name;
        this.productCodes = productCodes;
        this.products = products;
        this.id = id;
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String[] getProductCodes() {
        return productCodes;
    }

    public String[] getProducts() {
        return products;
    }

    private class Country {
        private final String name;

        @SerializedName("_id")
        private final String id;

        private Country(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }
    }

    private class Geo {
        private final double lat;
        private final double lon;

        private Geo(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }
    }
}
