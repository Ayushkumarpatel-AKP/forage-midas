package com.vagabond.midas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Incentive {
    
    @JsonProperty("amount")
    private float amount;
    
    public Incentive() {
    }
    
    public float getAmount() {
        return amount;
    }
    
    public void setAmount(float amount) {
        this.amount = amount;
    }
    
    @Override
    public String toString() {
        return String.format("Incentive[amount=%f]", amount);
    }
}
