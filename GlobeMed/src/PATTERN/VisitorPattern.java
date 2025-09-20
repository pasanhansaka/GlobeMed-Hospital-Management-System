package PATTERN;

import MODEL.Appointment;
import MODEL.Bill;
import MODEL.MedicalReport;
import MODEL.Patient;
import java.util.ArrayList;
import java.util.List;

public class VisitorPattern {

    // Element interface
    public interface ReportElement {

        void accept(ReportVisitor visitor);
    }

    // Concrete elements
    public static class PatientElement implements ReportElement {

        private Patient patient;

        public PatientElement(Patient patient) {
            this.patient = patient;
        }

        @Override
        public void accept(ReportVisitor visitor) {
            visitor.visit(this);
        }

        public Patient getPatient() {
            return patient;
        }

        public void setPatient(Patient patient) {
            this.patient = patient;
        }
    }

    //element classes
    public static class AppointmentElement implements ReportElement {

        private Appointment appointment;

        public AppointmentElement(Appointment appointment) {
            this.appointment = appointment;
        }

        @Override
        public void accept(ReportVisitor visitor) {
            visitor.visit(this);
        }

        // Getters and setters
        public Appointment getAppointment() {
            return appointment;
        }

        public void setAppointment(Appointment appointment) {
            this.appointment = appointment;
        }
    }

    public static class BillElement implements ReportElement {

        private Bill bill;

        public BillElement(Bill bill) {
            this.bill = bill;
        }

        @Override
        public void accept(ReportVisitor visitor) {
            visitor.visit(this);
        }

        // Getters and setters
        public Bill getBill() {
            return bill;
        }

        public void setBill(Bill bill) {
            this.bill = bill;
        }
    }

    public static class MedicalReportElement implements ReportElement {

        private MedicalReport report;

        public MedicalReportElement(MedicalReport report) {
            this.report = report;
        }

        @Override
        public void accept(ReportVisitor visitor) {
            visitor.visit(this);
        }

        public MedicalReport getReport() {
            return report;
        }

        public void setReport(MedicalReport report) {
            this.report = report;
        }
    }

    // Visitor interface
    public interface ReportVisitor {

        void visit(PatientElement element);

        void visit(AppointmentElement element);

        void visit(BillElement element);

        void visit(MedicalReportElement element);
    }

    // Concrete visitors
    public static class TreatmentSummaryVisitor implements ReportVisitor {

        private StringBuilder reportContent;

        public TreatmentSummaryVisitor() {
            this.reportContent = new StringBuilder();
        }

        @Override
        public void visit(PatientElement element) {
            Patient patient = element.getPatient();
            reportContent.append("Patient: ").append(patient.getFirstName()).append(" ").append(patient.getLastName()).append("\n");
            reportContent.append("Date of Birth: ").append(patient.getDateOfBirth()).append("\n");
            reportContent.append("Gender: ").append(patient.getGender()).append("\n");
            reportContent.append("Phone: ").append(patient.getMobileNumber()).append("\n");
            reportContent.append("Email: ").append(patient.getEmail()).append("\n\n");
        }

        @Override
        public void visit(AppointmentElement element) {
            Appointment appointment = element.getAppointment();
            reportContent.append("Appointment Date: ").append(appointment.getAppointmentDate()).append("\n");
            reportContent.append("Appointment Time: ").append(appointment.getAppointmentTime()).append("\n");
            reportContent.append("Appointment Type: ").append(appointment.getAppointmentType()).append("\n");
            reportContent.append("Reason: ").append(appointment.getReason()).append("\n\n");
        }

        @Override
        public void visit(BillElement element) {
            Bill bill = element.getBill();
            reportContent.append("Total Amount: $").append(bill.getTotalAmount()).append("\n");
            reportContent.append("Insurance Coverage: $").append(bill.getInsuranceCoverage()).append("\n");
            reportContent.append("Patient Payment: $").append(bill.getPatientPayment()).append("\n");
            reportContent.append("Status: ").append(bill.getStatus()).append("\n\n");
        }

        @Override
        public void visit(MedicalReportElement element) {
            MedicalReport report = element.getReport();
            reportContent.append("Diagnosis: ").append(report.getDiagnosis()).append("\n");
            reportContent.append("Symptoms: ").append(report.getSymptoms()).append("\n");
            reportContent.append("Treatment Plan: ").append(report.getTreatmentPlan()).append("\n");
            reportContent.append("Medications Prescribed: ").append(report.getMedicationsPrescribed()).append("\n");
            reportContent.append("Notes: ").append(report.getNotes()).append("\n");
        }

        public String getReport() {
            return reportContent.toString();
        }
    }

    public static class FinancialReportVisitor implements ReportVisitor {

        private StringBuilder reportContent;

        public FinancialReportVisitor() {
            this.reportContent = new StringBuilder();
        }

        @Override
        public void visit(PatientElement element) {
            
        }

        @Override
        public void visit(AppointmentElement element) {
            
        }

        @Override
        public void visit(BillElement element) {
            Bill bill = element.getBill();
            reportContent.append("Bill ID: ").append(bill.getBillId()).append("\n");
            reportContent.append("Patient ID: ").append(bill.getPatientId()).append("\n");
            reportContent.append("Total Amount: $").append(bill.getTotalAmount()).append("\n");
            reportContent.append("Insurance Coverage: $").append(bill.getInsuranceCoverage()).append("\n");
            reportContent.append("Patient Payment: $").append(bill.getPatientPayment()).append("\n");
            reportContent.append("Status: ").append(bill.getStatus()).append("\n");
            reportContent.append("Billing Date: ").append(bill.getBillingDate()).append("\n");
            reportContent.append("Due Date: ").append(bill.getDueDate()).append("\n\n");
        }

        @Override
        public void visit(MedicalReportElement element) {
            
        }

        public String getReport() {
            return reportContent.toString();
        }
    }

    // Object structure that holds elements
    public static class ReportStructure {

        private List<ReportElement> elements;

        public ReportStructure() {
            this.elements = new ArrayList<>();
        }

        public void addElement(ReportElement element) {
            elements.add(element);
        }

        public void removeElement(ReportElement element) {
            elements.remove(element);
        }

        public void accept(ReportVisitor visitor) {
            for (ReportElement element : elements) {
                element.accept(visitor);
            }
        }
    }
}
