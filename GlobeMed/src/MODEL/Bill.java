package MODEL;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Bill {

    private int billId;
    private int patientId;
    private int appointmentId;
    private double totalAmount;
    private double insuranceCoverage;
    private double patientPayment;
    private String status;
    private String billingDate;
    private String dueDate;

    public Bill() {
    }

    public Bill(int billId, int patientId, int appointmentId, double totalAmount, double insuranceCoverage, double patientPayment, String status, String billingDate, String dueDate) {
        this.billId = billId;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.totalAmount = totalAmount;
        this.insuranceCoverage = insuranceCoverage;
        this.patientPayment = patientPayment;
        this.status = status;
        this.billingDate = billingDate;
        this.dueDate = dueDate;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getInsuranceCoverage() {
        return insuranceCoverage;
    }

    public void setInsuranceCoverage(double insuranceCoverage) {
        this.insuranceCoverage = insuranceCoverage;
    }

    public double getPatientPayment() {
        return patientPayment;
    }

    public void setPatientPayment(double patientPayment) {
        this.patientPayment = patientPayment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Bill{"
                + "billId=" + billId
                + ", patientId=" + patientId
                + ", appointmentId=" + appointmentId
                + ", totalAmount=" + totalAmount
                + ", insuranceCoverage=" + insuranceCoverage
                + ", patientPayment=" + patientPayment
                + ", status='" + status + '\''
                + ", billingDate='" + billingDate + '\''
                + ", dueDate='" + dueDate + '\''
                + '}';
    }

    public static Bill fromResultSet(ResultSet rs) throws SQLException {
        Bill bill = new Bill();
        bill.setBillId(rs.getInt("bill_id"));
        bill.setPatientId(rs.getInt("patient_id"));
        bill.setAppointmentId(rs.getInt("appointment_id"));
        bill.setTotalAmount(rs.getDouble("total_amount"));
        bill.setInsuranceCoverage(rs.getDouble("insurance_coverage"));
        bill.setPatientPayment(rs.getDouble("patient_payment"));
        bill.setStatus(rs.getString("status"));
        bill.setBillingDate(rs.getString("billing_date"));
        bill.setDueDate(rs.getString("due_date"));
        return bill;
    }
}
