package org.tomw.rachunki.core;

import org.tomw.rachunki.entities.Konto;

/**
 * wrapper class containing result of account merge
 */
public class MergeAccountsResult {
    private Konto account1;
    private Konto account2;
    private Konto newAccount;

    private boolean ok=false;

    // number of transactions affected by merge
    private int nTransactions=0;

    public Konto getAccount1() {
        return account1;
    }

    public void setAccount1(Konto account1) {
        this.account1 = account1;
    }

    public Konto getAccount2() {
        return account2;
    }

    public void setAccount2(Konto account2) {
        this.account2 = account2;
    }

    public Konto getNewAccount() {
        return newAccount;
    }

    public void setNewAccount(Konto newAccount) {
        this.newAccount = newAccount;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getnTransactions() {
        return nTransactions;
    }

    public void setnTransactions(int nTransactions) {
        this.nTransactions = nTransactions;
    }
}
