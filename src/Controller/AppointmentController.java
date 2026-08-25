package Controller;

import service.AppointmentService;
import java.sql.Date;
import java.sql.Time;

public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController() {
        this.appointmentService = new AppointmentService();
    }

    // Contact number format verification (10 digits)
    public boolean isValidContactNumber(String contactNumber) {
        return contactNumber != null && contactNumber.matches("\\d{10}");
    }

    // Input validation prior to database booking
    public String validateAppointmentInputs(
            String patientName,
            String address,
            String contactNumber,
            int dentistId,
            int treatmentId,
            Date date,
            Time time) {

        if (patientName == null || patientName.trim().isEmpty()) {
            return "Patient name is required";
        }
        if (address == null || address.trim().isEmpty()) {
            return "Address is required";
        }
        if (!isValidContactNumber(contactNumber)) {
            return "Invalid contact number format";
        }
        if (dentistId <= 0) {
            return "Invalid dentist selection";
        }
        if (treatmentId <= 0) {
            return "Invalid treatment selection";
        }
        if (date == null) {
            return "Appointment date is required";
        }
        if (time == null) {
            return "Appointment time is required";
        }

        return "VALID";
    }

    // Validates inputs and persists patient and appointment records into MySQL database
    public String bookAppointment(
            String patientName,
            String address,
            String contactNumber,
            int dentistId,
            int treatmentId,
            Date date,
            Time time) {

        String validationResult = validateAppointmentInputs(patientName, address, contactNumber, dentistId, treatmentId, date, time);
        if (!validationResult.equals("VALID")) {
            return validationResult;
        }

        boolean saved = appointmentService.saveAppointment(patientName, address, contactNumber, dentistId, treatmentId, date, time);
        if (saved) {
            return "BOOKING_SUCCESS";
        }

        return "SLOT_CONFLICT_OR_FAILED";
    }

    // Validates and updates existing appointment in the database
    public String updateAppointment(
            String appointmentNumber,
            int patientId,
            String patientName,
            String address,
            String contactNumber,
            int dentistId,
            int treatmentId,
            Date date,
            Time time) {

        if (appointmentNumber == null || appointmentNumber.trim().isEmpty()) {
            return "Appointment number is required";
        }

        boolean updated = appointmentService.updateAppointment(
                appointmentNumber,
                patientId,
                patientName,
                address,
                contactNumber,
                dentistId,
                treatmentId,
                date,
                time
        );

        if (updated) {
            return "UPDATE_SUCCESS";
        }

        return "UPDATE_FAILED";
    }
}