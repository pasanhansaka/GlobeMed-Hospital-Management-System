package MODEL;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Appointment {

    private int appointmentId;
    private int patientId;
    private int doctorId;
    private String appointmentDate;
    private String appointmentTime;
    private String appointmentType;
    private String reason;
    private String status;

    public Appointment() {
    }

    public Appointment(int appointmentId, int patientId, int doctorId, String appointmentDate, String appointmentTime, String appointmentType, String reason, String status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentType = appointmentType;
        this.reason = reason;
        this.status = status;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
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

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment{"
                + "appointmentId=" + appointmentId
                + ", patientId=" + patientId
                + ", doctorId=" + doctorId
                + ", appointmentDate='" + appointmentDate + '\''
                + ", appointmentTime='" + appointmentTime + '\''
                + ", appointmentType='" + appointmentType + '\''
                + ", reason='" + reason + '\''
                + ", status='" + status + '\''
                + '}';
    }

    public static Appointment fromResultSet(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("appointment_id"));
        appointment.setPatientId(rs.getInt("patient_id"));
        appointment.setDoctorId(rs.getInt("doctor_id"));
        appointment.setAppointmentDate(rs.getString("appointment_date"));
        appointment.setAppointmentTime(rs.getString("appointment_time"));
        appointment.setAppointmentType(rs.getString("appointment_type"));
        appointment.setReason(rs.getString("reason"));
        appointment.setStatus(rs.getString("status"));
        return appointment;
    }
}
