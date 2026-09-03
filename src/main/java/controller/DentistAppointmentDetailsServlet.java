package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.Dentist;
import com.sunrise.dentalclinic.entity.Patient;
import com.sunrise.dentalclinic.entity.Treatment;
import com.sunrise.dentalclinic.entity.User;
import com.sunrise.dentalclinic.repository.DentistRepository;
import com.sunrise.dentalclinic.repository.PatientRepository;
import com.sunrise.dentalclinic.repository.TreatmentRepository;
import com.sunrise.dentalclinic.service.AppointmentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/dentist-appointment-details")
public class DentistAppointmentDetailsServlet
        extends HttpServlet {

    private final AppointmentService appointmentService =
            new AppointmentService();

    private final PatientRepository patientRepository =
            new PatientRepository();

    private final TreatmentRepository treatmentRepository =
            new TreatmentRepository();

    private final DentistRepository dentistRepository =
            new DentistRepository();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login?type=dentist"
            );

            return;
        }

        Object userObject =
                session.getAttribute("loggedInUser");

        if (!(userObject instanceof User)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login?type=dentist"
            );

            return;
        }

        User user =
                (User) userObject;

        if (!"DENTIST".equalsIgnoreCase(
                user.getRole())) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login?type=dentist"
            );

            return;
        }

        if (!"ACTIVE".equalsIgnoreCase(
                user.getStatus())) {

            session.invalidate();

            response.sendRedirect(
                    request.getContextPath()
                            + "/login?type=dentist"
            );

            return;
        }

        Long dentistId =
                user.getDentistId();

        if (dentistId == null ||
                dentistId <= 0) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/dentist-bookings"
            );

            return;
        }

        String idParameter =
                request.getParameter("id");

        if (idParameter == null ||
                idParameter.trim().isEmpty()) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/dentist-bookings"
            );

            return;
        }

        Long appointmentId;

        try {

            appointmentId =
                    Long.parseLong(
                            idParameter
                    );

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/dentist-bookings"
            );

            return;
        }

        try {

            Appointment appointment =
                    appointmentService.getAppointmentById(
                            appointmentId
                    );

            if (appointment == null) {

                request.setAttribute(
                        "error",
                        "Appointment not found."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/dentist-appointment-details.jsp"
                ).forward(request, response);

                return;
            }

            /*
             * SECURITY CHECK
             *
             * Make sure this appointment belongs
             * to the currently logged-in dentist.
             */

            if (appointment.getDentistId() == null ||
                    !dentistId.equals(
                            appointment.getDentistId())) {

                request.setAttribute(
                        "error",
                        "You are not authorized to view this appointment."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/dentist-appointment-details.jsp"
                ).forward(request, response);

                return;
            }

            Patient patient = null;

            if (appointment.getPatientId() != null) {

                patient =
                        patientRepository.findById(
                                appointment.getPatientId()
                        );
            }

            Treatment treatment = null;

            if (appointment.getTreatmentId() != null) {

                treatment =
                        treatmentRepository.findById(
                                appointment.getTreatmentId()
                        );
            }

            Dentist dentist =
                    dentistRepository.findById(
                            dentistId
                    );

            request.setAttribute(
                    "appointment",
                    appointment
            );

            request.setAttribute(
                    "patient",
                    patient
            );

            request.setAttribute(
                    "treatment",
                    treatment
            );

            request.setAttribute(
                    "dentist",
                    dentist
            );

            request.setAttribute(
                    "loggedInUser",
                    user
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/dentist-appointment-details.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Unable to load appointment details."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/dentist-appointment-details.jsp"
            ).forward(request, response);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doGet(
                request,
                response
        );
    }
}