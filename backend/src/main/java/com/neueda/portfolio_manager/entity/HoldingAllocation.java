package com.neueda.portfolio_manager.entity;

public class HoldingAllocation {
    private int holdingId;
    private int marketId;
    private String symbol;
    private String companyName;
    private String sector;
    private int quantity;
    private double purchasePrice;
    private double currentPrice;
    private double investedValue;
    private double currentValue;
    private double gainLoss;
    private double allocationPercentage;

    public HoldingAllocation() {
    }

    public int getHoldingId() {
        return holdingId;
    }

    public void setHoldingId(int holdingId) {
        this.holdingId = holdingId;
    }

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
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

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getInvestedValue() {
        return investedValue;
    }

    public void setInvestedValue(double investedValue) {
        this.investedValue = investedValue;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public double getGainLoss() {
        return gainLoss;
    }

    public void setGainLoss(double gainLoss) {
        this.gainLoss = gainLoss;
    }

    public double getAllocationPercentage() {
        return allocationPercentage;
    }

    public void setAllocationPercentage(double allocationPercentage) {
        this.allocationPercentage = allocationPercentage;
    }
}

