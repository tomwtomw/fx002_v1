package org.tomw.rachunki.gui;

import org.tomw.rachunki.entities.Konto;

public class AccountsWindowResult {
    private boolean okClicked = false;
    private Konto selectedAccount = null;

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }

    public Konto getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(Konto selectedAccount) {
        this.selectedAccount = selectedAccount;
    }
}
