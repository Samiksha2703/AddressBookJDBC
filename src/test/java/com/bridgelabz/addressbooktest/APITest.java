package com.bridgelabz.addressbooktest;

import com.bridgelabz.addressbook.AddressBook;
import com.bridgelabz.addressbook.AddressBookData;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

    @Test
    public void givenEmployeeDataInJsonServer_WhenRetrieved_ShouldMatchTheCount() {
        AddressBookData[] arrayOfContacts = getContactList();
        AddressBook addressBook;
        addressBook = new AddressBook(Arrays.asList(arrayOfContacts));
        long entries = addressBook.countEntries(AddressBook.IOService.REST_IO);
        Assertions.assertEquals(3, entries);
    }
}
