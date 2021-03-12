/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.cpsummary;

import org.tomw.czeki.entities.CounterParty;
import org.tomw.czeki.entities.Transaction;

/**
 * Summary for a counterParty
 * @author tomw
 */
public class CounterPartySummary {
    
    
    
    private CounterParty counterParty;
    private double sumCleared = 0.;
    private double sumAll = 0.0;

    public CounterParty getCounterParty() {
        return counterParty;
    }

    public void setCounterParty(CounterParty counterParty) {
        this.counterParty = counterParty;
    }

    public double getSumCleared() {
        return sumCleared;
    }

    public void setSumCleared(double sumCleared) {
        this.sumCleared = sumCleared;
    }

    public double getSumAll() {
        return sumAll;
    }

    public void setSumAll(double sumAll) {
        this.sumAll = sumAll;
    }
    
    public CounterPartySummary(CounterParty cp){
        counterParty=cp;
    }
    
    
    public void addTransaction(Transaction transaction){
        sumAll=sumAll + transaction.getTransactionAmount();
        if(transaction.isCleared()){
            sumCleared = sumCleared +transaction.getTransactionAmount();
        }
    }   

    @Override
    public String toString() {
        return "CounterPartySummary{" + "counterParty=" + counterParty.getShortName() + ", sumCleared=" + sumCleared + ", sumAll=" + sumAll + '}';
    }
    
    
}
