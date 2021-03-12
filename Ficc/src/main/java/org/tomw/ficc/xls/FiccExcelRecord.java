package org.tomw.ficc.xls;

import java.time.LocalDate;

public class FiccExcelRecord {
    private LocalDate date;
    private String direction;
    private String name;
    private String memo;
    private double amount;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "FiccExcelRecord{" +
                "date=" + date +
                ", direction='" + direction + '\'' +
                ", name='" + name + '\'' +
                ", memo='" + memo + '\'' +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FiccExcelRecord that = (FiccExcelRecord) o;

        if (Double.compare(that.amount, amount) != 0) return false;
        if (!date.equals(that.date)) return false;
        if (!direction.equals(that.direction)) return false;
        if (!name.equals(that.name)) return false;
        return memo.equals(that.memo);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = date.hashCode();
        result = 31 * result + direction.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + memo.hashCode();
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
