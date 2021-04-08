package com.wanderland.app.BookingHistory.Modal;

import java.util.Arrays;

public class BookingHistoryModal {
    String id,package_id,days,nights,people,amount,transaction_id,user_id,create_time,locations;
    String [] locationsArray;

    public BookingHistoryModal() {

    }

    public BookingHistoryModal(String id, String package_id, String days, String nights,
                               String people, String amount, String transaction_id, String user_id, String create_time, String locations, String[] locationsArray) {
        this.id = id;
        this.package_id = package_id;
        this.days = days;
        this.nights = nights;
        this.people = people;
        this.amount = amount;
        this.transaction_id = transaction_id;
        this.user_id = user_id;
        this.create_time = create_time;
        this.locations = locations;
        this.locationsArray = locationsArray;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
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

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String[] getLocationsArray() {
        return locationsArray;
    }

    public void setLocationsArray(String[] locationsArray) {
        this.locationsArray = locationsArray;
    }

    @Override
    public String toString() {
        return "BookingHistoryModal{" +
                "id='" + id + '\'' +
                ", package_id='" + package_id + '\'' +
                ", days='" + days + '\'' +
                ", nights='" + nights + '\'' +
                ", people='" + people + '\'' +
                ", amount='" + amount + '\'' +
                ", transaction_id='" + transaction_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", create_time='" + create_time + '\'' +
                ", locations=" + Arrays.toString(locationsArray) +
                '}';
    }
}
