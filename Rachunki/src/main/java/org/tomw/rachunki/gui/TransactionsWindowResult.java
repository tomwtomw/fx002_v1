package org.tomw.rachunki.gui;

import org.tomw.rachunki.entities.Konto;
import org.tomw.rachunki.entities.Transakcja;

public class TransactionsWindowResult {
    private boolean okClicked = false;
    private Transakcja selectedEntity=null;
    private Konto account;

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }

    public Transakcja getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(Transakcja selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public Konto getAccount() {
        return account;
    }

    public void setAccount(Konto account) {
        this.account = account;
    }
}
