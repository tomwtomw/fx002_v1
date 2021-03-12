package org.tomw.rachunki.core;

import org.tomw.rachunki.entities.Konto;
import org.tomw.rachunki.entities.Transakcja;
import org.tomw.utils.TomwStringUtils;

import java.util.Collections;
import java.util.List;

public class TransactionUtils {

    /**
     * Calculate sum of all transactions and
     *
     * @param listOfTransactions - list of transactions to analyze
     * @param sums               - list of results [sum of cleared transactions,non cleared
     *                           transactions, all transactions]
     * @param nTransactions      - list of results [number of cleared transactions,
     *                           non cleared transactions, all transactions]
     */
    public static void calculateSums(List<Transakcja> listOfTransactions,
                                      Konto account,
                                      List<Double> sums,
                                      List<Integer> nTransactions
    ) {
        double clearedTransactionsAmount = 0.;
        int clearedTransactionsNumberOf = 0;

        double nonClearedTransactionsAmount = 0.;
        int nonClearedTransactionsNumberOf = 0;

        double totalTransactionsAmount = 0.;
        int totalTransactionsNumberOf = 0;

        Collections.sort(listOfTransactions, Transakcja.transactionByDateComparator);

        for (Transakcja transaction : listOfTransactions) {
            totalTransactionsAmount = totalTransactionsAmount +
                    transaction.getTransactionDirectionAmount(account).get();
            transaction.setSumAllTransactions(totalTransactionsAmount);
            transaction.setRunningSumAllTransactions(
                    TomwStringUtils.money2String(totalTransactionsAmount)
            );

            totalTransactionsNumberOf = totalTransactionsNumberOf + 1;

            if (transaction.isCleared(account)) {
                clearedTransactionsNumberOf = clearedTransactionsNumberOf + 1;
                clearedTransactionsAmount = clearedTransactionsAmount +
                        transaction.getTransactionDirectionAmount(account).get();
            } else {
                nonClearedTransactionsNumberOf = nonClearedTransactionsNumberOf + 1;
                nonClearedTransactionsAmount = nonClearedTransactionsAmount +
                        transaction.getTransactionDirectionAmount(account).get();
            }
            transaction.setSumClearedTransactions(clearedTransactionsAmount);
            transaction.setRunningSumClearedTransactions(
                    TomwStringUtils.money2String(transaction.getSumClearedTransactions())
            );
        }

        // pack the results
        sums.clear();
        sums.add(clearedTransactionsAmount);
        sums.add(nonClearedTransactionsAmount);
        sums.add(totalTransactionsAmount);

        nTransactions.clear();
        nTransactions.add(clearedTransactionsNumberOf);
        nTransactions.add(nonClearedTransactionsNumberOf);
        nTransactions.add(totalTransactionsNumberOf);
    }


}
