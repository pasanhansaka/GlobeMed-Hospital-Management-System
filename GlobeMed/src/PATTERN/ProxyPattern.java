package PATTERN;

import CONTROLLER.PatientController;
import MODEL.Patient;
import java.util.List;

public class ProxyPattern {

    private static final String[] ALLOWED_ROLES = {"Doctor", "Nurse","Administrator"};

    // Proxy class - controls access to patient records
    public static class PatientRecordProxy {

        private PatientRecord realPatientRecord;
        private String currentUserRole;

        public PatientRecordProxy(String currentUserRole) {
            this.currentUserRole = currentUserRole;
            this.realPatientRecord = new PatientRecord();
        }

        public boolean isAuthorized() {
            for (String role : ALLOWED_ROLES) {
                if (currentUserRole.equalsIgnoreCase(role)) {
                    return true;
                }
            }
            return false;
        }

        // get patient records
        public List<Patient> getPatientRecords() {
            if (isAuthorized()) {
                return realPatientRecord.getPatientRecords();
            } else {
                throw new SecurityException("Access denied: " + currentUserRole + " does not have permission to view patient records.");
            }
        }

        // get specific patient record
        public Patient getPatientRecord(int patientId) {
            if (isAuthorized()) {
                return realPatientRecord.getPatientRecord(patientId);
            } else {
                throw new SecurityException("Access denied: " + currentUserRole + " does not have permission to view patient records.");
            }
        }

        // add patient record
        public boolean addPatientRecord(Patient patient) {
            if (isAuthorized()) {
                return realPatientRecord.addPatientRecord(patient);
            } else {
                throw new SecurityException("Access denied: " + currentUserRole + " does not have permission to add patient records.");
            }
        }

        // update patient record
        public boolean updatePatientRecord(Patient patient) {
            if (isAuthorized()) {
                return realPatientRecord.updatePatientRecord(patient);
            } else {
                throw new SecurityException("Access denied: " + currentUserRole + " does not have permission to update patient records.");
            }
        }

        // delete patient record
        public boolean deletePatientRecord(int patientId) {
            if (isAuthorized()) {
                return realPatientRecord.deletePatientRecord(patientId);
            } else {
                throw new SecurityException("Access denied: " + currentUserRole + " does not have permission to delete patient records.");
            }
        }
    }

    // Real class
    public static class PatientRecord {

        private PatientController patientController;

        public PatientRecord() {
            this.patientController = new PatientController();
        }

        public List<Patient> getPatientRecords() {
            return patientController.getAllPatients();
        }

        public Patient getPatientRecord(int patientId) {
            return patientController.getPatientById(patientId);
        }

        public boolean addPatientRecord(Patient patient) {
            return patientController.addPatient(patient);
        }

        public boolean updatePatientRecord(Patient patient) {
            return patientController.updatePatient(patient);
        }

        public boolean deletePatientRecord(int patientId) {
            return patientController.deletePatient(patientId);
        }
    }
}