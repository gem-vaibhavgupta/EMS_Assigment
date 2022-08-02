package com.gemini.ems.model;

public class BillDetails {
    private final double totalBillAmount;

    public BillDetails(Builder builder) {
        this.totalBillAmount = builder.totalBillAmount;
    }

    public double getTotalBillAmount() {
        return totalBillAmount;
    }

    public static final class Builder {
        private double totalBillAmount;

        public Builder() {
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder setBillAmountWithoutTax(double billAmountWithoutTax) {
            return this;
        }

        public Builder setTotalBillAmount(double totalBillAmount) {
            this.totalBillAmount = totalBillAmount;
            return this;
        }

        public BillDetails build() {
            return new BillDetails(this);
        }

    }
}
