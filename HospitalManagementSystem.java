package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url="jdbc:mysql://localhost:3306/hospital";
    private static final String username="root";
    private static final String password="_shubham1110";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner=new Scanner(System.in);
        try {
            Connection connection= DriverManager.getConnection(url,username,password);
            Patient patient=new Patient(connection,scanner);
            Doctor doctor=new Doctor(connection);
            while (true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("0. Exit");
                System.out.println("Enter Your Choice: ");
                int choice=scanner.nextInt();
                switch (choice){
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointment(patient,doctor,connection,scanner);
                        System.out.println();
                        break;
                    case 0:
                        System.out.println("Thank You!!!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice!!!");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();

        }
    }
    public static void bookAppointment(Patient patient,Doctor doctor,Connection connection,Scanner scanner){
        System.out.println("Enter Patient Id");
        int patientId=scanner.nextInt();
        System.out.println("Enter Doctor Id");
        int doctorId=scanner.nextInt();
        System.out.println("Enter Appointment Date (YYYY-MM=DD)");
        String appointmentDate=scanner.next();
        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId,appointmentDate,connection)){
                String query="Insert into appointments(patient_id,doctor_id,appointment_date) values(?,?,?)";
                try {
                    PreparedStatement preparedStatement=connection.prepareStatement(query);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentDate);
                    int row=preparedStatement.executeUpdate();
                    if(row>0)
                        System.out.println("Appointment Booked");
                    else
                        System.out.println("Appointment Failed");
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else
                System.out.println("Doctor not Available");
        }else {
            System.out.println("Either Doctor or Patient doesn't exist!!!");
        }

    }
    public static boolean checkDoctorAvailability(int doctorId,String appointmentDate,Connection connection){
        String query="Select Count(*) from  appointments where doctor_id=? AND appointment_date=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                int cnt=resultSet.getInt(1);
                if(cnt==0) return true;
                else return false;
            }

            else return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }
}
