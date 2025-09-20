package PATTERN;

import CONTROLLER.AppointmentController;
import MODEL.Appointment;
import java.util.ArrayList;
import java.util.List;

public class CommandPattern {

    // Abstract command class
    public interface AppointmentCommand {

        void execute();

        void undo();
    }

    // Concrete command for scheduling an appointment
    public static class ScheduleAppointmentCommand implements AppointmentCommand {

        private Appointment appointment;
        private AppointmentController appointmentController;

        public ScheduleAppointmentCommand(Appointment appointment, AppointmentController appointmentController) {
            this.appointment = appointment;
            this.appointmentController = appointmentController;
        }

        @Override
        public void execute() {
            appointmentController.addAppointment(appointment);
        }

        @Override
        public void undo() {
            appointmentController.deleteAppointment(appointment.getAppointmentId());
        }
    }

    // Concrete command for canceling an appointment
    public static class CancelAppointmentCommand implements AppointmentCommand {

        private int appointmentId;
        private AppointmentController appointmentController;
        private Appointment originalAppointment;

        public CancelAppointmentCommand(int appointmentId, AppointmentController appointmentController) {
            this.appointmentId = appointmentId;
            this.appointmentController = appointmentController;
            this.originalAppointment = appointmentController.getAppointmentById(appointmentId);
        }

        @Override
        public void execute() {
            Appointment appointment = new Appointment(
                    appointmentId,
                    originalAppointment.getPatientId(),
                    originalAppointment.getDoctorId(),
                    originalAppointment.getAppointmentDate(),
                    originalAppointment.getAppointmentTime(),
                    originalAppointment.getAppointmentType(),
                    originalAppointment.getReason(),
                    "Cancelled"
            );
            appointmentController.updateAppointment(appointment);
        }

        @Override
        public void undo() {
            appointmentController.updateAppointment(originalAppointment);
        }
    }

    // Invoker class that executes commands
    public static class AppointmentInvoker {

        private List<AppointmentCommand> history = new ArrayList<>();

        public void executeCommand(AppointmentCommand command) {
            command.execute();
            history.add(command);
        }

        public void undoLastCommand() {
            if (!history.isEmpty()) {
                AppointmentCommand lastCommand = history.remove(history.size() - 1);
                lastCommand.undo();
            }
        }
    }
}
