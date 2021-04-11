package com.bridgelabz.addressbooktest;

import com.bridgelabz.addressbook.AddressBook;
import com.bridgelabz.addressbook.AddressBookData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AddressBookTest {
    AddressBook addressBook;
    List<AddressBookData> addressBookDataList;

    @Test
    public void givenThreeContactsInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        addressBook = new AddressBook();
        addressBookDataList = addressBook.readAddressBookData(AddressBook.IOService.DB_IO);
        Assertions.assertEquals(3, addressBookDataList.size());
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
        List<AddressBookData>  addressBookDataList = addressBook.countByCity("Cambridge");
        Assertions.assertEquals(3, addressBookDataList.size());
    }

    @Test
    public void givenContactDataInDB_whenCountByState_ShouldMatchWithExpectedValue() {
        addressBook = new AddressBook();
        List<AddressBookData>  addressBookDataList = addressBook.countByState("TX");
        Assertions.assertEquals(2, addressBookDataList.size());
    }
}
