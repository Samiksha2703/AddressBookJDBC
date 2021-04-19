package com.bridgelabz.addressbook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {

    private static AddressBookDBService addressBookDBService;
    private static PreparedStatement addressBookDataStatement;
    private static PreparedStatement prepareStatement;

    public static AddressBookDBService getInstance() {
        if (addressBookDBService == null)
            addressBookDBService = new AddressBookDBService();
        return addressBookDBService;
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/address_book1?useSSL=false";
        String userName = "root";
        String password = "Welcome@01";
        Connection connection;
        System.out.println("Connecting to database:" + jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection is successful!" + connection);
        return connection;
    }

    public List<AddressBookData> getAddressBookDataUsingDB() {
        String sql = "SELECT * FROM Address_Book";
        List<AddressBookData> addressBookList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            addressBookList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookList;
    }

//    private List<AddressBookData> getAddressBookDataList(ResultSet resultSet) {
//        List<AddressBookData> addressBookDataList = new ArrayList<>();
//        try {
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String firstName = resultSet.getString("firstname");
//                String lastName = resultSet.getString("lastname");
//                String address = resultSet.getString("address");
//                String city = resultSet.getString("city");
//                String state = resultSet.getString("state");
//                int zip = resultSet.getInt("zip");
//                long phone = resultSet.getLong("phoneNumber");
//                String email = resultSet.getString("email");
//                addressBookDataList.add(new AddressBookData(id, firstName, lastName, address, city, state, zip, phone, email));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return addressBookDataList;
//    }

    public int updateContactDetails(String name, String address) {
        String sql = String.format("update Address_Book set address = '%s' where firstName = '%s';", address, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<AddressBookData> getEmployeePayrollData(String name) {
        List<AddressBookData> employeePayrollList = null;
        String sql = "SELECT * FROM Address_Book WHERE firstName = ?";
        if (this.addressBookDataStatement == null)
            addressBookDataStatement = this.prepareStatementForAddressBook(sql);
        try {
            addressBookDataStatement.setString(1, name);
            ResultSet resultSet = addressBookDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private PreparedStatement prepareStatementForAddressBook(String sql) {
        try {
            Connection connection = this.getConnection();
            prepareStatement = connection.prepareStatement(sql);
            return prepareStatement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<AddressBookData> getEmployeePayrollData(ResultSet resultSet) {
        List<AddressBookData> addressBookList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                int zip = resultSet.getInt("zip");
                long phone = resultSet.getLong("phoneNumber");
                String email = resultSet.getString("email");
                addressBookList.add(new AddressBookData(id, firstName, lastName, address, city, state, zip, String.valueOf(phone), email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookList;
    }

    public List<AddressBookData> getCount(String city) {
        List<AddressBookData> addressBookDataList = new ArrayList<>();
        String sql = String.format("SELECT * FROM Address_Book where city = '%s';", city);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            addressBookDataList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookDataList;
    }

    public List<AddressBookData> getCountByState(String state) {
        List<AddressBookData> addressBookDataList = new ArrayList<>();
        String sql = String.format("SELECT * FROM Address_Book where state = '%s';", state);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            addressBookDataList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookDataList;
    }

    public AddressBookData addContact(String firstName, String lastName, String address, String city, String state, int zip, String phoneNumber, String email) {
        int contactId = -1;
        AddressBookData addressBookData = null;
        String sql = String.format("INSERT INTO Address_Book (firstName, lastName, address, city, state, zip, phoneNumber, email)" +
                "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", firstName, lastName, address, city, state, zip, Long.valueOf(phoneNumber), email);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) contactId = resultSet.getInt(1);
            }
            addressBookData = new AddressBookData(contactId, firstName, lastName, address, city, state, zip, phoneNumber, email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookData;
    }
}
