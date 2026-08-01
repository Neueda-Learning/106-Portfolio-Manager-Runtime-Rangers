package com.neueda.portfolio_manager.entity;

import java.time.LocalDate;

public class Holding {
    private int id;
    private int marketId;
    private int quantity;
    private double purchasePrice;
    private LocalDate purchaseDate;

    public Holding() {
    }

    public Holding(int id, int marketId, int quantity, double purchasePrice, LocalDate purchaseDate) {
        this.id = id;
        this.marketId = marketId;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.purchaseDate = purchaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

}
