package CONTROLLER;

import MODEL.MySQL;
import MODEL.Patient;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PatientController {

    //find all patients
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        try {
            ResultSet rs = MySQL.search("SELECT p.*, a.line1, a.line2, a.postal_code, c.city_name "
                    + "FROM patients p "
                    + "JOIN address a ON p.address_id = a.address_id "
                    + "JOIN city c ON a.city_city_id = c.city_id");
            while (rs.next()) {
                patients.add(Patient.fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patients;
    }

    //find patinet by id
    public Patient getPatientById(int patientId) {
        Patient patient = null;
        try {
            ResultSet rs = MySQL.search("SELECT p.*, a.line1, a.line2, a.postal_code, c.city_name "
                    + "FROM patients p "
                    + "JOIN address a ON p.address_id = a.address_id "
                    + "JOIN city c ON a.city_city_id = c.city_id "
                    + "WHERE p.patient_id = ?", patientId);
            if (rs.next()) {
                patient = Patient.fromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patient;
    }

    //add new patient
    public boolean addPatient(Patient patient) {
        try {
            String addressQuery = "INSERT INTO address (line1, line2, postal_code, city_city_id) VALUES (?, ?, ?, ?)";
            MySQL.iud(addressQuery, patient.getAddressLine1(), patient.getAddressLine2(),
                    patient.getPostalCode(), getCityId(patient.getCity()));

            ResultSet rs = MySQL.search("SELECT LAST_INSERT_ID() AS address_id");
            int addressId = 0;
            if (rs.next()) {
                addressId = rs.getInt("address_id");
            }

            String patientQuery = "INSERT INTO patients (first_name, last_name, date_of_birth, gender, phone, email, address_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            MySQL.iud(patientQuery, patient.getFirstName(), patient.getLastName(), patient.getDateOfBirth(),
                    patient.getGender(), patient.getMobileNumber(), patient.getEmail(), addressId);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //update a patient
    public boolean updatePatient(Patient patient) {
        try {
            ResultSet rs = MySQL.search("SELECT address_id FROM patients WHERE patient_id = ?", patient.getPatientId());
            int addressId = -1;
            if (rs.next()) {
                addressId = rs.getInt("address_id");
            }

            if (addressId == -1) {
                return false;
            }

            String addressQuery = "UPDATE address SET line1 = ?, line2 = ?, postal_code = ?, city_city_id = ? WHERE address_id = ?";
            MySQL.iud(addressQuery, patient.getAddressLine1(), patient.getAddressLine2(),
                    patient.getPostalCode(), getCityId(patient.getCity()), addressId);

            String patientQuery = "UPDATE patients SET first_name = ?, last_name = ?, date_of_birth = ?, gender = ?, phone = ?, email = ? WHERE patient_id = ?";
            MySQL.iud(patientQuery, patient.getFirstName(), patient.getLastName(), patient.getDateOfBirth(),
                    patient.getGender(), patient.getMobileNumber(), patient.getEmail(), patient.getPatientId());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //delete a patient
    public boolean deletePatient(int patientId) {
        try {
            ResultSet rs = MySQL.search("SELECT address_id FROM patients WHERE patient_id = ?", patientId);
            int addressId = -1;
            if (rs.next()) {
                addressId = rs.getInt("address_id");
            }

            if (addressId == -1) {
                return false;
            }

            String patientQuery = "DELETE FROM patients WHERE patient_id = ?";
            MySQL.iud(patientQuery, patientId);

            String addressQuery = "DELETE FROM address WHERE address_id = ?";
            MySQL.iud(addressQuery, addressId);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //find city by id
    private int getCityId(String cityName) {
        try {
            ResultSet rs = MySQL.search("SELECT city_id FROM city WHERE city_name = ?", cityName);
            if (rs.next()) {
                return rs.getInt("city_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
