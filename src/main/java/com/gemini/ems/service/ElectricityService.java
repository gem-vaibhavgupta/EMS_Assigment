package com.gemini.ems.service;

import com.gemini.ems.dao.EMSDao;
import com.gemini.ems.model.BillDetails;
import com.gemini.ems.model.Grievance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ElectricityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElectricityService.class);

    private final EMSDao emsDao;

    public ElectricityService(final EMSDao emsDao) {
        this.emsDao = emsDao;
    }

    /**
     * method to calculate total bill
     * @param noOfUnits
     * @return obj
     */
    public CompletableFuture<BillDetails> calculateBill(Integer noOfUnits) {
        LOGGER.info("Calculated bill for units {}", noOfUnits);
        return CompletableFuture.supplyAsync(() -> calculateBillAmount(noOfUnits))
                .thenApply(this::billWithGreenTax)
                .thenApply(this::totalBillAmountWithGST)
                .thenApply(totalBillAmount ->
                {
                    LOGGER.info("Calculated bill details {}", totalBillAmount);
                    return new BillDetails.Builder()
                            .setTotalBillAmount(totalBillAmount).build();
                });
    }

    /**
     * method to calculate total bill amount with GST
     * @param billAmountWithGreenTax
     * @return double
     */
    private double totalBillAmountWithGST(Double billAmountWithGreenTax) {
        double totalBillAmountWithGST = billAmountWithGreenTax + billAmountWithGreenTax * 18 / 100;
        LOGGER.info("Calculated total bill amount with GST {}", totalBillAmountWithGST);
        return totalBillAmountWithGST;
    }

    /**
     * method to calculate bill amount with green tax
     * @param billAmount
     * @return double
     */
    private double billWithGreenTax(double billAmount) {
        double billAmountWithGreenTax = billAmount + billAmount * 3 / 100;
        LOGGER.info("Calculated bill amount with green tax {}", billAmountWithGreenTax);
        return billAmountWithGreenTax;
    }

    /**
     * method to calculate the bill amount
     * @param noOfUnits spent for bill
     * @return double
     */
    private double calculateBillAmount(Integer noOfUnits) {
        double billAmountWithoutTax = 0;
        if (noOfUnits >= 0 && noOfUnits <= 100)
            billAmountWithoutTax = noOfUnits * 5;
        else if (noOfUnits > 100 && noOfUnits <= 400)
            billAmountWithoutTax = noOfUnits * 6;
        else if (noOfUnits > 400) {
            billAmountWithoutTax = noOfUnits * 8;
        }
        LOGGER.info("Bill amount without tax {}", billAmountWithoutTax);
        return billAmountWithoutTax;
    }

    /**
     * method to register customer grievance
     *
     * @param grievance details
     * @return
     * @return grievance registration ID
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public UUID registerGrievance(Grievance grievance) throws ExecutionException, InterruptedException {
        LOGGER.info("Registering grievance with details {}", grievance);
        UUID grievanceId = UUID.randomUUID();
        Boolean flag = CompletableFuture.supplyAsync(() ->
                emsDao.registerGrievance(grievance, grievanceId)).get();
        LOGGER.info("Grievance registration flag {}", flag);
        if (flag) {
            return grievanceId;
        } else
            return null;
    }

}
