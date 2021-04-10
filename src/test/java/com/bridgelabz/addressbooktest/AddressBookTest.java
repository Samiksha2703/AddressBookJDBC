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
}
