package CONTROLLER;

import MODEL.Appointment;
import MODEL.MySQL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AppointmentController {

    //find all appointments
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        try {
            ResultSet rs = MySQL.search("SELECT * FROM appointments ORDER BY appointment_date, appointment_time");
            while (rs.next()) {
                appointments.add(Appointment.fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointments;
    }

    //find appointment by id
    public Appointment getAppointmentById(int appointmentId) {
        Appointment appointment = null;
        try {
            ResultSet rs = MySQL.search("SELECT * FROM appointments WHERE appointment_id = ?", appointmentId);
            if (rs.next()) {
                appointment = Appointment.fromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointment;
    }

    //add new appointment
    public boolean addAppointment(Appointment appointment) {
        try {
            String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, appointment_type, reason, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            MySQL.iud(query, appointment.getPatientId(), appointment.getDoctorId(),
                    appointment.getAppointmentDate(), appointment.getAppointmentTime(),
                    appointment.getAppointmentType(), appointment.getReason(), appointment.getStatus());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //update existing appointment
    public boolean updateAppointment(Appointment appointment) {
        try {
            String query = "UPDATE appointments SET patient_id = ?, doctor_id = ?, appointment_date = ?, appointment_time = ?, appointment_type = ?, reason = ?, status = ? WHERE appointment_id = ?";
            MySQL.iud(query, appointment.getPatientId(), appointment.getDoctorId(),
                    appointment.getAppointmentDate(), appointment.getAppointmentTime(),
                    appointment.getAppointmentType(), appointment.getReason(), appointment.getStatus(), appointment.getAppointmentId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //delete a appointment
    public boolean deleteAppointment(int appointmentId) {
        try {
            String query = "DELETE FROM appointments WHERE appointment_id = ?";
            MySQL.iud(query, appointmentId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //find available time slots
    public List<String> getAvailableSlots(int doctorId, String date) {
        List<String> bookedSlots = new ArrayList<>();
        try {
            ResultSet rs = MySQL.search("SELECT appointment_time FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND status != 'Cancelled' ORDER BY appointment_time",
                    doctorId, date);
            while (rs.next()) {
                bookedSlots.add(rs.getString("appointment_time"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookedSlots;
    }
}
