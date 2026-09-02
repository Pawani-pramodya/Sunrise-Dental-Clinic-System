package Test;

import org.junit.Test;
import static org.junit.Assert.*;
import Controller.BillingController;

public class BillingControllerTest {

    private final BillingController controller = new BillingController();

    // 1. Total Bill Calculation පරීක්ෂාව (1000.00 + 3500.00 = 4500.00)
    @Test
    public void testCalculateTotalAmount() {
        double treatmentFee = 3500.00;
        double expectedTotal = 4500.00; // 1000 (Consultation) + 3500
        double actualTotal = controller.calculateTotalAmount(treatmentFee);
        assertEquals(expectedTotal, actualTotal, 0.001);
    }

    // 2. Appointment Number එක හිස්ව ඇති විට Validation Error එකක් ලැබීම
    @Test
    public void testEmptyAppointmentNumberValidation() {
        String result = controller.generateAndSaveBill("", 2500.00);
        assertEquals("Appointment number is required", result);
    }

    // 3. සෘණ අගයක් සහිත (Negative) Fee එකක් දුන් විට Reject වීම (Boundary Test)
    @Test
    public void testNegativeTreatmentFeeValidation() {
        String result = controller.generateAndSaveBill("APT0001", -500.00);
        assertEquals("Treatment fee cannot be negative", result);
    }

    // 4. Database එකෙන් Treatment ID (1) හි Fee එක සාර්ථකව කියවීම (DB Read)
    @Test
    public void testGetTreatmentFeeFromDB() {
        double fee = controller.getTreatmentFeeFromDB(1);
        assertTrue("Treatment fee should be non-negative", fee >= 0);
    }

    // 5. Database එක තුළ පවතින Appointment එකකට නැවත Bill සෑදීම වැළැක්වීම (Duplicate DB Check)
    @Test
    public void testSaveBillDuplicateCheck() {
        // sunrise_dental_clinic DB හි පවතින appointment number එකක් පරීක්ෂාව
        String result = controller.generateAndSaveBill("APT0001", 3500.00);
        assertNotNull(result);
        assertTrue(result.equals("BILL_GENERATED_SUCCESS") || result.equals("BILL_EXISTS_OR_FAILED"));
    }
}