package HospitalManagementSystem;

import com.mysql.cj.protocol.a.BinaryResultsetReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;
    public Patient(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }
    public void addPatient(){
        System.out.println("Enter Patient Name :");
        scanner.nextLine();
        String name =scanner.nextLine();
        System.out.println("Enter age : ");
        int age=scanner.nextInt();
        System.out.println("Enter Gender : ");
        String gender =scanner.next();
        try {
            String query="Insert into patients(name,age ,gender) values(?,?,?)";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int rows=preparedStatement.executeUpdate();
            if(rows>0)
                System.out.println("Patient Admission Successful");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void viewPatient(){
        String query="Select * from patients;";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            ResultSet resultSet= preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+------------+-------------------+--------------+----------------+");
            System.out.println("| Patient ID | Patient Name      | Age          | Gender         |");
            System.out.println("+------------+-------------------+--------------+----------------+");
            while (resultSet.next()){
                int id=resultSet.getInt("id");
                String name=resultSet.getString("name");
                int age=resultSet.getInt("age");
                String gender=resultSet.getString("gender");
                System.out.printf("| %-11s| %-19s| %-13s| %-15s|\n",id ,name,age,gender);
                System.out.println("+------------+-------------------+--------------+----------------+");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public boolean getPatientById(int id){
        String query="Select * from patients where id=?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
             return resultSet.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
