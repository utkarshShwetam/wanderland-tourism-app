package com.wanderland.app.DashBoard.DataModals;

public class ViewAllPlacesModal {

    String image,place_country,place_name,place_price,place_discountPrice;

    public ViewAllPlacesModal(String  image, String place_country, String place_name, String place_price, String place_discountPrice) {
        this.image = image;
        this.place_country = place_country;
        this.place_name = place_name;
        this.place_price = place_price;
        this.place_discountPrice = place_discountPrice;
    }

    public String getImage() {
        return image;
    }

    public String getPlace_country() {
        return place_country;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getPlace_price() {
        return place_price;
    }

    public String getPlace_discountPrice() {
        return place_discountPrice;
    }
}
