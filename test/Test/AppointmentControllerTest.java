package Test;

import org.junit.Test;
import static org.junit.Assert.*;
import Controller.AppointmentController;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

public class AppointmentControllerTest {

    private final AppointmentController controller = new AppointmentController();

    // 1. Patient Name හිස් වූ විට Database එකට නොගොස් Reject වීම (Validation Test)
    @Test
    public void testEmptyPatientName() {
        Date futureDate = Date.valueOf(LocalDate.now().plusDays(10));
        Time futureTime = Time.valueOf("10:00:00");

        String result = controller.bookAppointment("", "Colombo", "0771234567", 1, 1, futureDate, futureTime);
        assertEquals("Patient name is required", result);
    }

    // 2. අකුරු සහිත දුරකථන අංක ප්‍රතික්ෂේප වීම (Validation Test)
    @Test
    public void testInvalidContactNumber() {
        Date futureDate = Date.valueOf(LocalDate.now().plusDays(10));
        Time futureTime = Time.valueOf("10:00:00");

        String result = controller.bookAppointment("Kasun Perera", "Colombo", "077abc1234", 1, 1, futureDate, futureTime);
        assertEquals("Invalid contact number format", result);
    }

    // 3. Controller එක හරහා Database එකට අලුත් Record එකක් සාර්ථකව Save වීම (Integration Test)
    @Test
    public void testSuccessfulBookingPersistence() {
        // Repeated test runs වලදී slot conflict නොවීමට dynamic time slot එකක් හැදීම
        long uniqueOffset = (System.currentTimeMillis() / 1000) % 200 + 40;
        Date futureDate = Date.valueOf(LocalDate.now().plusDays(uniqueOffset));
        Time futureTime = Time.valueOf("10:15:00");

        String result = controller.bookAppointment(
                "Kasun Perera Controller",
                "Temple Road, Galle",
                "0771234567",
                1, // Database එකේ ඇති Dentist ID
                1, // Database එකේ ඇති Treatment ID
                futureDate,
                futureTime
        );

        assertEquals("BOOKING_SUCCESS", result);
    }

    // 4. එකම දොස්තරට එකම වෙලාවට Double-booking වැළැක්වීම (Database Conflict Test)
    @Test
    public void testDoubleBookingSlotConflict() {
        Date conflictDate = Date.valueOf(LocalDate.now().plusDays(360));
        Time conflictTime = Time.valueOf("15:30:00");

        // First attempt saves to DB
        controller.bookAppointment("First Patient", "Colombo", "0712223344", 1, 1, conflictDate, conflictTime);

        // Second attempt for exact same dentist & slot must be rejected by the DB
        String duplicateResult = controller.bookAppointment("Second Patient", "Kandy", "0719998877", 1, 1, conflictDate, conflictTime);

        assertEquals("SLOT_CONFLICT_OR_FAILED", duplicateResult);
    }

    // 5. පද්ධතියේ නොමැති Appointment අංකයක් Update කිරීමට ගිය විට Reject වීම (Database Validation)
    @Test
    public void testUpdateNonExistingAppointment() {
        Date futureDate = Date.valueOf(LocalDate.now().plusDays(15));
        Time futureTime = Time.valueOf("09:00:00");

        String result = controller.updateAppointment(
                "NON_EXISTING_APP_999",
                99999,
                "Unknown Patient",
                "No Address",
                "0770000000",
                1,
                1,
                futureDate,
                futureTime
        );

        assertEquals("UPDATE_FAILED", result);
    }
}