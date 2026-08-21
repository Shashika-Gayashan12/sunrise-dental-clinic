package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.config.DatabaseConnection;
import com.sunrise.dentalclinic.entity.Bill;
import com.sunrise.dentalclinic.entity.BillDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillRepository {

    public Bill save(Bill bill)
            throws SQLException {

        String sql = """
                INSERT INTO bills
                (
                    bill_date,
                    consultation_fee,
                    total_amount,
                    treatment_cost,
                    appointment_id
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setTimestamp(
                    1,
                    Timestamp.valueOf(
                            bill.getBillDate()
                    )
            );

            statement.setBigDecimal(
                    2,
                    bill.getConsultationFee()
            );

            statement.setBigDecimal(
                    3,
                    bill.getTotalAmount()
            );

            statement.setBigDecimal(
                    4,
                    bill.getTreatmentCost()
            );

            statement.setLong(
                    5,
                    bill.getAppointmentId()
            );

            statement.executeUpdate();

            try (ResultSet keys =
                         statement.getGeneratedKeys()) {

                if (keys.next()) {
                    bill.setId(
                            keys.getLong(1)
                    );
                }
            }
        }

        return bill;
    }

    public List<Bill> findAll()
            throws SQLException {

        List<Bill> bills =
                new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    bill_date,
                    consultation_fee,
                    total_amount,
                    treatment_cost,
                    appointment_id
                FROM bills
                ORDER BY bill_date DESC
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Bill bill =
                        new Bill(
                                resultSet.getLong("id"),

                                resultSet.getTimestamp(
                                        "bill_date"
                                ).toLocalDateTime(),

                                resultSet.getBigDecimal(
                                        "consultation_fee"
                                ),

                                resultSet.getBigDecimal(
                                        "total_amount"
                                ),

                                resultSet.getBigDecimal(
                                        "treatment_cost"
                                ),

                                resultSet.getLong(
                                        "appointment_id"
                                )
                        );

                bills.add(bill);
            }
        }

        return bills;
    }

    public BillDetails findDetailsByBillId(
            Long billId)
            throws SQLException {

        String sql = """
                SELECT
                    b.id AS bill_id,
                    b.bill_date,
                    b.appointment_id,

                    a.appointment_number,
                    a.appointment_date,
                    a.appointment_time,

                    p.patient_name,
                    p.contact_number AS patient_contact,

                    d.dentist_name,
                    d.specialization,

                    t.treatment_name,

                    b.consultation_fee,
                    b.treatment_cost,
                    b.total_amount

                FROM bills b

                INNER JOIN appointments a
                    ON b.appointment_id = a.id

                INNER JOIN patients p
                    ON a.patient_id = p.id

                INNER JOIN dentists d
                    ON a.dentist_id = d.id

                INNER JOIN treatments t
                    ON a.treatment_id = t.id

                WHERE b.id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, billId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return new BillDetails(
                            resultSet.getLong(
                                    "bill_id"
                            ),

                            resultSet.getTimestamp(
                                    "bill_date"
                            ).toLocalDateTime(),

                            resultSet.getLong(
                                    "appointment_id"
                            ),

                            resultSet.getString(
                                    "appointment_number"
                            ),

                            resultSet.getDate(
                                    "appointment_date"
                            ).toLocalDate(),

                            resultSet.getTime(
                                    "appointment_time"
                            ).toLocalTime(),

                            resultSet.getString(
                                    "patient_name"
                            ),

                            resultSet.getString(
                                    "patient_contact"
                            ),

                            resultSet.getString(
                                    "dentist_name"
                            ),

                            resultSet.getString(
                                    "specialization"
                            ),

                            resultSet.getString(
                                    "treatment_name"
                            ),

                            resultSet.getBigDecimal(
                                    "consultation_fee"
                            ),

                            resultSet.getBigDecimal(
                                    "treatment_cost"
                            ),

                            resultSet.getBigDecimal(
                                    "total_amount"
                            )
                    );
                }
            }
        }

        return null;
    }
}