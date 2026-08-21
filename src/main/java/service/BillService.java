package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.entity.Bill;
import com.sunrise.dentalclinic.entity.BillDetails;
import com.sunrise.dentalclinic.repository.BillRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class BillService {

    private final BillRepository repository =
            new BillRepository();

    public Bill addBill(Bill bill)
            throws SQLException {

        if (bill == null) {
            throw new IllegalArgumentException(
                    "Bill is required."
            );
        }

        if (bill.getAppointmentId() == null ||
                bill.getAppointmentId() <= 0) {

            throw new IllegalArgumentException(
                    "Valid appointment ID is required."
            );
        }

        if (bill.getConsultationFee() == null ||
                bill.getConsultationFee().signum() < 0) {

            throw new IllegalArgumentException(
                    "Consultation fee must be valid."
            );
        }

        if (bill.getTreatmentCost() == null ||
                bill.getTreatmentCost().signum() < 0) {

            throw new IllegalArgumentException(
                    "Treatment cost must be valid."
            );
        }

        BigDecimal totalAmount =
                bill.getConsultationFee()
                        .add(bill.getTreatmentCost());

        bill.setTotalAmount(totalAmount);

        return repository.save(bill);
    }

    public List<Bill> getAllBills()
            throws SQLException {

        return repository.findAll();
    }

    public BillDetails getBillDetails(
            Long billId)
            throws SQLException {

        if (billId == null || billId <= 0) {
            throw new IllegalArgumentException(
                    "Valid bill ID is required."
            );
        }

        return repository.findDetailsByBillId(
                billId
        );
    }
}