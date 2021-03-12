package org.tomw.rachunki.entities;

import org.apache.commons.lang3.StringUtils;
import org.tomw.utils.TomwStringUtils;

public class TransakcjaUtils {

    /**
     * Convert transaction into string of tab separated values, as they appear in transaction table
     *
     * @param t transaction
     * @return string
     */
    public static String transactionToTabSeparatedString(Transakcja t, Konto currentAccount) {
        String result = "";

        int longestChecknumber = "0003523454".length();

        result = result + t.transactionDateProperty().get().toString() + TomwStringUtils.TAB;
        result = result + StringUtils.leftPad(String.format("%.2f", t.getTransactionDirectionAmount(currentAccount).get()), 10) + TomwStringUtils.TAB;
        result = result + StringUtils.leftPad(TomwStringUtils.null2blank(t.checkNumberProperty().get()), longestChecknumber + 1) + TomwStringUtils.TAB;
        result = result + StringUtils.leftPad(TomwStringUtils.boolean2BlankY(t.isClearedLocal(currentAccount)), 2) + TomwStringUtils.TAB;
        result = result + StringUtils.leftPad(TomwStringUtils.boolean2BlankY(t.isClearedRemote(currentAccount)), 2) + TomwStringUtils.TAB;

        result = result + StringUtils.rightPad(getFirstNCharacters(t.getOther(currentAccount).get().shortNameProperty().get(), 20), 21) + TomwStringUtils.TAB;
        result = result + StringUtils.rightPad(getFirstNCharacters(t.shortCommentProperty().get(), 20), 21) + TomwStringUtils.TAB;

        result = result + StringUtils.leftPad(
                TomwStringUtils.formatMoney(Double.parseDouble(t.runningSumClearedTransactionsProperty().get())
                ), 10)
                + TomwStringUtils.TAB;
        result = result + StringUtils.leftPad(
                TomwStringUtils.formatMoney(Double.parseDouble(t.runningSumAllTransactionsProperty().get())
                ), 10) + TomwStringUtils.TAB;

        return result;
    }

    public static String getFirstNCharacters(String s, int n) {
        if (s == null) {
            return TomwStringUtils.BLANK;
        } else {
            if (s.length() < n + 1) {
                return s;
            } else {
                return s.substring(0, n - 1);
            }
        }
    }
}

