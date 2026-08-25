package service;

import dao.AppointmentDAO;
import dao.PatientDAO;
import model.Appointment;
import model.Patient;

import java.sql.Date;
import java.sql.Time;

public class AppointmentService {

    private PatientDAO patientDAO;
    private AppointmentDAO appointmentDAO;

    /**
     * Creates DAO objects required by the service layer.
     */
    public AppointmentService() {

        patientDAO = new PatientDAO();
        appointmentDAO = new AppointmentDAO();
    }

    /**
     * Returns the next appointment number.
     *
     * @return generated appointment number
     */
    public String getNextAppointmentNumber() {

        return appointmentDAO.generateAppointmentNumber();
    }

    /**
     * Saves a new patient and appointment.
     *
     * @return true if the appointment is saved successfully
     */
    public boolean saveAppointment(
            String patientName,
            String address,
            String contactNumber,
            int dentistId,
            int treatmentId,
            Date date,
            Time time) {

        // Create patient object
        Patient patient =
                new Patient(
                        patientName,
                        address,
                        contactNumber
                );

        // Save patient first
        int patientId =
                patientDAO.savePatient(patient);

        if (patientId == -1) {
            return false;
        }

        // Check dentist availability
        if (!appointmentDAO.isDentistAvailable(
                dentistId,
                date,
                time)) {

            return false;
        }

        // Generate appointment number
        String appointmentNumber =
                appointmentDAO.generateAppointmentNumber();

        // Create appointment object
        Appointment appointment =
                new Appointment(
                        appointmentNumber,
                        patientId,
                        dentistId,
                        treatmentId,
                        date,
                        time
                );

        // Save appointment
        return appointmentDAO.saveAppointment(
                appointment
        );
    }

    /**
     * Updates an existing appointment and its patient details.
     *
     * @param appointmentNumber appointment to update
     * @param patientId existing patient ID
     * @param patientName updated patient name
     * @param address updated patient address
     * @param contactNumber updated contact number
     * @param dentistId selected dentist
     * @param treatmentId selected treatment
     * @param date selected appointment date
     * @param time selected appointment time
     * @return true if the update is successful
     */
    public boolean updateAppointment(
            String appointmentNumber,
            int patientId,
            String patientName,
            String address,
            String contactNumber,
            int dentistId,
            int treatmentId,
            Date date,
            Time time) {

        // Create updated patient object
        Patient patient =
                new Patient(
                        patientName,
                        address,
                        contactNumber
                );

        // Check dentist availability
        if (!appointmentDAO.isDentistAvailableForUpdate(
                appointmentNumber,
                dentistId,
                date,
                time)) {

            return false;
        }

        // Update patient information
        boolean patientUpdated =
                patientDAO.updatePatient(
                        patientId,
                        patient
                );

        if (!patientUpdated) {
            return false;
        }

        // Update appointment information
        return appointmentDAO.updateAppointment(
                appointmentNumber,
                dentistId,
                treatmentId,
                date,
                time
        );
    }
}