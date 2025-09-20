package CONTROLLER;

import MODEL.MedicalReport;
import MODEL.MySQL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReportController {

    //find all reports
    public List<MedicalReport> getAllReports() {
        List<MedicalReport> reports = new ArrayList<>();
        try {
            ResultSet rs = MySQL.search("SELECT * FROM medical_report ORDER BY visit_date DESC");
            while (rs.next()) {
                reports.add(MedicalReport.fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reports;
    }

    //find a report by id
    public MedicalReport getReportById(int reportId) {
        MedicalReport report = null;
        try {
            ResultSet rs = MySQL.search("SELECT * FROM medical_report WHERE report_id = ?", reportId);
            if (rs.next()) {
                report = MedicalReport.fromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return report;
    }

    //find a report by patient id
    public List<MedicalReport> getReportsByPatientId(int patientId) {
        List<MedicalReport> reports = new ArrayList<>();
        try {
            ResultSet rs = MySQL.search("SELECT * FROM medical_report WHERE patient_id = ? ORDER BY visit_date DESC", patientId);
            while (rs.next()) {
                reports.add(MedicalReport.fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reports;
    }

    //add new report
    public boolean addReport(MedicalReport report) {
        try {
            if (report.getPatientId() <= 0 || report.getDoctorId() <= 0 || report.getDiagnosis() == null) {
                return false;
            }

            String query = "INSERT INTO medical_report (patient_id, doctor_id, visit_date, diagnosis, symptoms, treatment_plan, medications_prescribed, notes, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            MySQL.iud(query, report.getPatientId(), report.getDoctorId(), report.getVisitDate(),
                    report.getDiagnosis(), report.getSymptoms(), report.getTreatmentPlan(),
                    report.getMedicationsPrescribed(), report.getNotes(), report.getCreated_at());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //update a report
    public boolean updateReport(MedicalReport report) {
        try {
            String query = "UPDATE medical_report SET patient_id = ?, doctor_id = ?, visit_date = ?, diagnosis = ?, symptoms = ?, treatment_plan = ?, medications_prescribed = ?, notes = ?, created_at = ? WHERE report_id = ?";
            MySQL.iud(query, report.getPatientId(), report.getDoctorId(), report.getVisitDate(),
                    report.getDiagnosis(), report.getSymptoms(), report.getTreatmentPlan(),
                    report.getMedicationsPrescribed(), report.getNotes(), report.getCreated_at(), report.getReportId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //delete a report
    public boolean deleteReport(int reportId) {
        try {
            String query = "DELETE FROM medical_report WHERE report_id = ?";
            MySQL.iud(query, reportId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
