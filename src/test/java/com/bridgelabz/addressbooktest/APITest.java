package com.bridgelabz.addressbooktest;

import com.bridgelabz.addressbook.AddressBook;
import com.bridgelabz.addressbook.AddressBookData;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

public class APITest {

    public AddressBookData[] getContactList() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
        Response response = RestAssured.get("/address_book");
        System.out.println("Address Book Entries in JSONSever:\n" + response.asString());
        AddressBookData[] arrayOfContacts = new Gson().fromJson(response.asString(), AddressBookData[].class);
        return arrayOfContacts;
    }

    private Response addContactToJsonServer(AddressBookData addressBookData) {
        String empJson = new Gson().toJson(addressBookData);
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(empJson);
        return request.post("/address_book");
    }

    @Test
    public void givenEmployeeDataInJsonServer_WhenRetrieved_ShouldMatchTheCount() {
        AddressBookData[] arrayOfContacts = getContactList();
        AddressBook addressBook;
        addressBook = new AddressBook(Arrays.asList(arrayOfContacts));
        long entries = addressBook.countEntries(AddressBook.IOService.REST_IO);
        Assertions.assertEquals(3, entries);
    }

    @Test
    public void givenListOfNewEmployee_WhenAdded_ShouldMatch201ResponseAndCount() {
        AddressBookData[] arrayOfContact = getContactList();
        AddressBook addressBook = new AddressBook(Arrays.asList(arrayOfContact));
        AddressBookData[] arrayOfContacts = {
                new AddressBookData(0, "Sakshi", "Shende", "Sane Guruji Nagar", "Wardha", "MH", 633258, "7896541239", "shende.sakshi@gmail.com"),
                new AddressBookData(0, "Mukesh", "Kalambe", "Indira Nagar", "Nagpur", "MH", 256987, "8965743698", "mukesh05@gmail.com"),
                new AddressBookData(0, "Pratiksha", "Hatwar", "Mude Layout", "Wardha", "MH", 896574, "9874568967", "pratikshahatwar@gmail.com")
        };
        for (AddressBookData addressBookData : arrayOfContacts) {
            Response response = addContactToJsonServer(addressBookData);
            int statusCode = response.getStatusCode();
            Assertions.assertEquals(201, statusCode);

            addressBookData = new Gson().fromJson(response.asString(), AddressBookData.class);
            addressBook.addContact(addressBookData, AddressBook.IOService.REST_IO);
        }
        long entries = addressBook.countEntries(AddressBook.IOService.REST_IO);
        Assertions.assertEquals(6, entries);
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldMatch() {
        AddressBookData[] arrayOfContacts = getContactList();
        AddressBook addressBook = new AddressBook(Arrays.asList(arrayOfContacts));
        addressBook.updateContactAddress("Samiksha", "Sai Nagar", AddressBook.IOService.REST_IO);
        AddressBookData addressBookData = addressBook.getAddressBookData("Samiksha");
        String contJson = new Gson().toJson(addressBookData);
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(contJson);
        Response response = request.put("/address_book/" + addressBookData.id);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200, statusCode);
    }
}
