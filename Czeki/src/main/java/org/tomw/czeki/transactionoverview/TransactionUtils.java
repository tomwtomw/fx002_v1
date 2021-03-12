/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.transactionoverview;

import java.util.Collection;
import org.tomw.czeki.entities.Transaction;

/**
 * Utility class for performing operations on transactions
 *
 * @author tomw
 */
public class TransactionUtils {

    public static double calculateSum(Collection<Transaction> listOfTransactions) {
        double sum = 0.;
        for (Transaction t : listOfTransactions) {
            sum = sum + t.getTransactionAmount();
        }
        return sum;
    }
}
