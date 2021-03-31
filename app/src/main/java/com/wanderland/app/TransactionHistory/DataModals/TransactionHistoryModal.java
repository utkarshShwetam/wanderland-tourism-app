package com.wanderland.app.TransactionHistory.DataModals;

public class TransactionHistoryModal {

    String transaction_id, package_id, user_id, payment_method, amount, description, status, create_time;


    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public TransactionHistoryModal(String transaction_id, String package_id, String user_id, String payment_method,
                                   String amount, String description, String status, String create_time) {
        this.transaction_id = transaction_id;
        this.package_id = package_id;
        this.user_id = user_id;
        this.payment_method = payment_method;
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.create_time = create_time;
    }

    public TransactionHistoryModal() {
    }


    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
