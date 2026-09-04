package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.Dentist;
import com.sunrise.dentalclinic.entity.Patient;
import com.sunrise.dentalclinic.entity.Treatment;
import com.sunrise.dentalclinic.entity.User;

import com.sunrise.dentalclinic.service.AppointmentService;
import com.sunrise.dentalclinic.service.DentistService;
import com.sunrise.dentalclinic.service.PatientService;
import com.sunrise.dentalclinic.service.TreatmentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/appointments")
public class AppointmentServlet extends HttpServlet {

    private final AppointmentService appointmentService =
            new AppointmentService();

    private final PatientService patientService =
            new PatientService();

    private final DentistService dentistService =
            new DentistService();

    private final TreatmentService treatmentService =
            new TreatmentService();


// =========================================================
// GET
// =========================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        String action =
                request.getParameter("action");

        try {

            // =================================================
            // VIEW
            // =================================================

            if ("view".equalsIgnoreCase(action)) {

                String idParameter =
                        request.getParameter("id");

                if (idParameter == null ||
                        idParameter.isBlank()) {

                    throw new IllegalArgumentException(
                            "Appointment ID is required."
                    );
                }

                Long id =
                        Long.parseLong(idParameter);

                Appointment appointment =
                        appointmentService.getAppointmentById(id);

                request.setAttribute(
                        "appointment",
                        appointment
                );

                List<Patient> patients =
                        patientService.getAllPatients();

                List<Dentist> dentists =
                        dentistService.getAllDentists();

                List<Treatment> treatments =
                        treatmentService.getAllTreatments();

                Patient selectedPatient = null;

                if (appointment != null &&
                        appointment.getPatientId() != null &&
                        patients != null) {

                    for (Patient patient : patients) {

                        if (appointment.getPatientId()
                                .equals(patient.getId())) {

                            selectedPatient = patient;
                            break;
                        }
                    }
                }

                Dentist selectedDentist = null;

                if (appointment != null &&
                        appointment.getDentistId() != null &&
                        dentists != null) {

                    for (Dentist dentist : dentists) {

                        if (appointment.getDentistId()
                                .equals(dentist.getId())) {

                            selectedDentist = dentist;
                            break;
                        }
                    }
                }

                Treatment selectedTreatment = null;

                if (appointment != null &&
                        appointment.getTreatmentId() != null &&
                        treatments != null) {

                    for (Treatment treatment : treatments) {

                        if (appointment.getTreatmentId()
                                .equals(treatment.getId())) {

                            selectedTreatment = treatment;
                            break;
                        }
                    }
                }

                request.setAttribute(
                        "patient",
                        selectedPatient
                );

                request.setAttribute(
                        "dentist",
                        selectedDentist
                );

                request.setAttribute(
                        "treatment",
                        selectedTreatment
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/appointment-view.jsp"
                ).forward(
                        request,
                        response
                );

                return;
            }


            // =================================================
            // EDIT
            // =================================================

            if ("edit".equalsIgnoreCase(action)) {

                String idParameter =
                        request.getParameter("id");

                if (idParameter == null ||
                        idParameter.isBlank()) {

                    throw new IllegalArgumentException(
                            "Appointment ID is required."
                    );
                }

                Long id =
                        Long.parseLong(idParameter);

                Appointment appointment =
                        appointmentService.getAppointmentById(id);

                request.setAttribute(
                        "appointment",
                        appointment
                );

                request.setAttribute(
                        "patients",
                        patientService.getAllPatients()
                );

                request.setAttribute(
                        "dentists",
                        dentistService.getAllDentists()
                );

                request.setAttribute(
                        "treatments",
                        treatmentService.getAllTreatments()
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/appointment-edit.jsp"
                ).forward(
                        request,
                        response
                );

                return;
            }


            // =================================================
            // APPOINTMENTS PAGE
            // =================================================

            showAppointments(
                    request,
                    response
            );

        } catch (NumberFormatException e) {

            request.setAttribute(
                    "error",
                    "Invalid appointment ID."
            );

            showAppointments(
                    request,
                    response
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            showAppointments(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load appointment.",
                    e
            );
        }
    }


// =========================================================
// POST
// =========================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        request.setCharacterEncoding("UTF-8");

        String action =
                request.getParameter("action");

        try {

            // =================================================
            // CANCEL
            // =================================================

            if ("cancel".equalsIgnoreCase(action)) {

                String idParameter =
                        request.getParameter("id");

                if (idParameter == null ||
                        idParameter.isBlank()) {

                    throw new IllegalArgumentException(
                            "Appointment ID is required."
                    );
                }

                Long id =
                        Long.parseLong(idParameter);

                appointmentService.cancelAppointment(id);

                response.sendRedirect(
                        request.getContextPath()
                                + "/appointments"
                );

                return;
            }


            // =================================================
            // UPDATE
            // =================================================

            if ("update".equalsIgnoreCase(action)) {

                String idParameter =
                        request.getParameter("id");

                if (idParameter == null ||
                        idParameter.isBlank()) {

                    throw new IllegalArgumentException(
                            "Appointment ID is required."
                    );
                }

                Long id =
                        Long.parseLong(idParameter);

                Long patientId =
                        Long.parseLong(
                                request.getParameter("patientId")
                        );

                Long dentistId =
                        Long.parseLong(
                                request.getParameter("dentistId")
                        );

                Long treatmentId =
                        Long.parseLong(
                                request.getParameter("treatmentId")
                        );

                LocalDate appointmentDate =
                        LocalDate.parse(
                                request.getParameter(
                                        "appointmentDate"
                                )
                        );

                LocalTime appointmentTime =
                        LocalTime.parse(
                                request.getParameter(
                                        "appointmentTime"
                                )
                        );

                String status =
                        request.getParameter("status");

                if (status == null ||
                        status.isBlank()) {

                    status = "PENDING";
                }

                Appointment appointment =
                        new Appointment();

                appointment.setId(id);

                appointment.setPatientId(
                        patientId
                );

                appointment.setDentistId(
                        dentistId
                );

                appointment.setTreatmentId(
                        treatmentId
                );

                appointment.setAppointmentDate(
                        appointmentDate
                );

                appointment.setAppointmentTime(
                        appointmentTime
                );

                appointment.setStatus(
                        status
                );

                appointmentService.updateAppointment(
                        appointment
                );

                response.sendRedirect(
                        request.getContextPath()
                                + "/appointments"
                );

                return;
            }


            // =================================================
            // CREATE
            // =================================================

            Long patientId =
                    Long.parseLong(
                            request.getParameter("patientId")
                    );

            Long dentistId =
                    Long.parseLong(
                            request.getParameter("dentistId")
                    );

            Long treatmentId =
                    Long.parseLong(
                            request.getParameter("treatmentId")
                    );

            LocalDate appointmentDate =
                    LocalDate.parse(
                            request.getParameter(
                                    "appointmentDate"
                            )
                    );

            LocalTime appointmentTime =
                    LocalTime.parse(
                            request.getParameter(
                                    "appointmentTime"
                            )
                    );

            Appointment appointment =
                    new Appointment();

            appointment.setPatientId(
                    patientId
            );

            appointment.setDentistId(
                    dentistId
            );

            appointment.setTreatmentId(
                    treatmentId
            );

            appointment.setAppointmentDate(
                    appointmentDate
            );

            appointment.setAppointmentTime(
                    appointmentTime
            );

            // Create appointment
            Appointment savedAppointment =
                    appointmentService.createAppointment(
                            appointment
                    );

            // Get generated appointment number
            String appointmentNumber =
                    savedAppointment.getAppointmentNumber();

            // Create success message
            String successMessage =
                    "Appointment " +
                            appointmentNumber +
                            " added successfully!";

            // Redirect with success message
            response.sendRedirect(
                    request.getContextPath()
                            + "/appointments?success="
                            + URLEncoder.encode(
                            successMessage,
                            StandardCharsets.UTF_8
                    )
            );

        } catch (NumberFormatException e) {

            request.setAttribute(
                    "error",
                    "Invalid appointment data."
            );

            showAppointments(
                    request,
                    response
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            showAppointments(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to process appointment.",
                    e
            );
        }
    }


// =========================================================
// LOGIN CHECK
// =========================================================

    private boolean isLoggedIn(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return false;
        }

        Object userObject =
                session.getAttribute("loggedInUser");

        if (!(userObject instanceof User)) {
            return false;
        }

        User user =
                (User) userObject;

        return "ACTIVE".equalsIgnoreCase(
                user.getStatus()
        );
    }


// =========================================================
// SHOW APPOINTMENTS + FILTER
// =========================================================

    private void showAppointments(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<Appointment> appointments =
                    appointmentService.getAllAppointments();

            List<Patient> patients =
                    patientService.getAllPatients();

            List<Dentist> dentists =
                    dentistService.getAllDentists();

            List<Treatment> treatments =
                    treatmentService.getAllTreatments();


            // =================================================
            // GET FILTER
            // =================================================

            String filter =
                    request.getParameter("filter");

            if (filter == null ||
                    filter.isBlank()) {

                filter = "active";
            }


            // =================================================
            // FILTERED LIST
            // =================================================

            List<Appointment> filteredAppointments =
                    new ArrayList<>();

            if (appointments != null) {

                for (Appointment appointment :
                        appointments) {

                    String status =
                            appointment.getStatus();


                    // ALL

                    if ("all".equalsIgnoreCase(filter)) {

                        filteredAppointments.add(
                                appointment
                        );
                    }


                    // ACTIVE = PENDING + CONFIRMED

                    else if ("active".equalsIgnoreCase(filter)) {

                        if ("PENDING".equalsIgnoreCase(status)
                                ||
                                "CONFIRMED".equalsIgnoreCase(status)) {

                            filteredAppointments.add(
                                    appointment
                            );
                        }
                    }


                    // PENDING

                    else if ("pending".equalsIgnoreCase(filter)) {

                        if ("PENDING".equalsIgnoreCase(status)) {

                            filteredAppointments.add(
                                    appointment
                            );
                        }
                    }


                    // CONFIRMED

                    else if ("confirmed".equalsIgnoreCase(filter)) {

                        if ("CONFIRMED".equalsIgnoreCase(status)) {

                            filteredAppointments.add(
                                    appointment
                            );
                        }
                    }


                    // COMPLETED

                    else if ("completed".equalsIgnoreCase(filter)) {

                        if ("COMPLETED".equalsIgnoreCase(status)) {

                            filteredAppointments.add(
                                    appointment
                            );
                        }
                    }


                    // CANCELLED

                    else if ("cancelled".equalsIgnoreCase(filter)) {

                        if ("CANCELLED".equalsIgnoreCase(status)) {

                            filteredAppointments.add(
                                    appointment
                            );
                        }
                    }
                }
            }


            // =================================================
            // SEND DATA TO JSP
            // =================================================

            request.setAttribute(
                    "appointments",
                    filteredAppointments
            );

            request.setAttribute(
                    "patients",
                    patients
            );

            request.setAttribute(
                    "dentists",
                    dentists
            );

            request.setAttribute(
                    "treatments",
                    treatments
            );

            request.setAttribute(
                    "filter",
                    filter
            );

            request.setAttribute(
                    "appointmentCount",
                    filteredAppointments.size()
            );


            // =================================================
            // FORWARD TO JSP
            // =================================================

            request.getRequestDispatcher(
                    "/WEB-INF/views/appointments.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load appointments.",
                    e
            );
        }
    }
}