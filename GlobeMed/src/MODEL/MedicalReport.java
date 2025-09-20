package MODEL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class MedicalReport {

    private int reportId;
    private int patientId;
    private int doctorId;
    private LocalDate visitDate;
    private String diagnosis;
    private String symptoms;
    private String treatmentPlan;
    private String medicationsPrescribed;
    private String notes;
    private String created_at;

    public MedicalReport() {
    }

    public MedicalReport(int reportId, int patientId, int doctorId, LocalDate visitDate, String diagnosis, String symptoms, String treatmentPlan, String medicationsPrescribed, String notes, String created_at) {
        this.reportId = reportId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.visitDate = visitDate;
        this.diagnosis = diagnosis;
        this.symptoms = symptoms;
        this.treatmentPlan = treatmentPlan;
        this.medicationsPrescribed = medicationsPrescribed;
        this.notes = notes;
        this.created_at = created_at;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }

    public String getMedicationsPrescribed() {
        return medicationsPrescribed;
    }

    public void setMedicationsPrescribed(String medicationsPrescribed) {
        this.medicationsPrescribed = medicationsPrescribed;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "MedicalReport{"
                + "reportId=" + reportId
                + ", patientId=" + patientId
                + ", doctorId=" + doctorId
                + ", visitDate='" + visitDate + '\''
                + ", diagnosis='" + diagnosis + '\''
                + ", symptoms='" + symptoms + '\''
                + ", treatmentPlan='" + treatmentPlan + '\''
                + ", medicationsPrescribed='" + medicationsPrescribed + '\''
                + ", notes='" + notes + '\''
                + ", created_at='" + created_at + '\''
                + '}';
    }

    public static MedicalReport fromResultSet(ResultSet rs) throws SQLException {
        MedicalReport report = new MedicalReport();
        report.setReportId(rs.getInt("report_id"));
        report.setPatientId(rs.getInt("patient_id"));
        report.setDoctorId(rs.getInt("doctor_id"));

        java.sql.Date sqlDate = rs.getDate("visit_date");
        if (sqlDate != null) {
            report.setVisitDate(sqlDate.toLocalDate());
        }

        report.setDiagnosis(rs.getString("diagnosis"));
        report.setSymptoms(rs.getString("symptoms"));
        report.setTreatmentPlan(rs.getString("treatment_plan"));
        report.setMedicationsPrescribed(rs.getString("medications_prescribed"));
        report.setNotes(rs.getString("notes"));
        report.setCreated_at(rs.getString("created_at"));
        return report;
    }
}
