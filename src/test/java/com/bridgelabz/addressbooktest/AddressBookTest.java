package com.bridgelabz.addressbooktest;

import com.bridgelabz.addressbook.AddressBook;
import com.bridgelabz.addressbook.AddressBookData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class AddressBookTest {
    AddressBook addressBook;
    List<AddressBookData> addressBookDataList;

    @Test
    public void givenThreeContactsInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        addressBook = new AddressBook();
        addressBookDataList = addressBook.readAddressBookData(AddressBook.IOService.DB_IO);
        Assertions.assertEquals(7, addressBookDataList.size());
    }

    @Test
    public void givenContactDataInDB_whenUpdated_ShouldSyncWithDB() {
        addressBook = new AddressBook();
        addressBookDataList = addressBook.readAddressBookData(AddressBook.IOService.DB_IO);
        addressBook.updateContact("10 Streat", "Jesse");
        boolean result = addressBook.checkAddressBookInSyncWithDB("Jesse");
        Assertions.assertTrue(result);
    }

    @Test
    public void givenContactDataInDB_whenCountByCity_ShouldMatchWithExpectedValue() {
        addressBook = new AddressBook();
        List<AddressBookData> addressBookDataList = addressBook.countByCity("Cambridge");
        Assertions.assertEquals(3, addressBookDataList.size());
    }

    @Test
    public void givenContactDataInDB_whenCountByState_ShouldMatchWithExpectedValue() {
        addressBook = new AddressBook();
        List<AddressBookData> addressBookDataList = addressBook.countByState("TX");
        Assertions.assertEquals(2, addressBookDataList.size());
    }

    @Test
    public void givenNewContactData_WhenAdded_ShouldSyncWithDB() {
        addressBook = new AddressBook();
        addressBookDataList = addressBook.readAddressBookData(AddressBook.IOService.DB_IO);
        addressBook.addContactToAddressBook("Samiksha", "Shende", "Ram Nagar", "Wardha", "MH", 442001,
                "6866577899", "shende.samiksha@gmail.com");
        boolean result = addressBook.checkAddressBookInSyncWithDB("Samiksha");
        Assertions.assertTrue(result);
    }

    @Test
    public void given6Employees_WhenAdded_Should_ShouldMatchEmpEntries() {
        AddressBookData[] arrayOfContacts = {
                new AddressBookData(0, "Neha", "Zade", "Sai Nagar","Nagpur", "MH", 789654,
                        753964125, "nehazade@gmail.com"),
        new AddressBookData(0, "Apurva", "Yende", "Keshav Nagar","Pune", "MH", 969654,
                972431556, "apurva.yende@gmail.com"),
        new AddressBookData(0, "Sakshi", "Shende", "Reddy Streat","Shriperumbudur", "TN", 549678,
                925461375, "sakshishende@gmail.com")
        };
        AddressBook addressBook = new AddressBook();
        addressBook.readAddressBookData(AddressBook.IOService.DB_IO);
        Instant start = Instant.now();
        addressBook.addContactIntoDB(Arrays.asList(arrayOfContacts));
        Instant end = Instant.now();
        System.out.println("Duration without Thread: " + Duration.between(start, end));
        Instant threadStart = Instant.now();
        addressBook.addAddressBookDataWithThread(Arrays.asList(arrayOfContacts));
        Instant threadEnd = Instant.now();
        System.out.println("Duration with thread: " + Duration.between(threadStart, threadEnd));
        Assertions.assertEquals(13, addressBook.countEntries(AddressBook.IOService.DB_IO));
    }
}
