package com.wanderland.app.DashBoard.DataModals;

import android.os.Parcel;
import android.os.Parcelable;

public class GetPackagesModal implements Parcelable {
    String id,title,days,nights,price,description,tag,createTime;
    String [] imagesArray, locationsArray;
    String images,locations;


    protected GetPackagesModal(Parcel in) {
        id = in.readString();
        title = in.readString();
        days = in.readString();
        nights = in.readString();
        price = in.readString();
        description = in.readString();
        tag = in.readString();
        createTime = in.readString();
        imagesArray = in.createStringArray();
        locationsArray = in.createStringArray();
        images = in.readString();
        locations = in.readString();
    }

    public static final Creator<GetPackagesModal> CREATOR = new Creator<GetPackagesModal>() {
        @Override
        public GetPackagesModal createFromParcel(Parcel in) {
            return new GetPackagesModal(in);
        }

        @Override
        public GetPackagesModal[] newArray(int size) {
            return new GetPackagesModal[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getNights() {
        return nights;
    }

    public void setNights(String nights) {
        this.nights = nights;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String[] getImagesArray() {
        return imagesArray;
    }

    public void setImagesArray(String[] imagesArray) {
        this.imagesArray = imagesArray;
    }

    public String[] getLocationsArray() {
        return locationsArray;
    }

    public void setLocationsArray(String[] locationsArray) {
        this.locationsArray = locationsArray;
    }

    public String getImages() {
        return images;
    }

    public String getLocations() {
        return locations;
    }
    public void setImages(String images) {
        this.images = images;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }
    public GetPackagesModal() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(days);
        dest.writeString(nights);
        dest.writeString(price);
        dest.writeString(description);
        dest.writeString(tag);
        dest.writeString(createTime);
        dest.writeStringArray(imagesArray);
        dest.writeStringArray(locationsArray);
        dest.writeString(images);
        dest.writeString(locations);
    }
}
