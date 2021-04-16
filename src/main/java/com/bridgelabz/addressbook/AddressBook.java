package com.bridgelabz.addressbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBook {

    public AddressBookDBService getAddressBookDBService() {
        return addressBookDBService;
    }

    public enum IOService {
        DB_IO, REST_IO
    }

    private List<AddressBookData> addressBookList;
    public final AddressBookDBService addressBookDBService;

    public AddressBook() {
        addressBookDBService = AddressBookDBService.getInstance();
    }

    public AddressBook(List<AddressBookData> addressBookList) {
        this();
        this.addressBookList = addressBookList;
    }

    public List<AddressBookData> readAddressBookData(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            return this.addressBookList = addressBookDBService.getAddressBookDataUsingDB();
        return null;
    }

    public void updateContact(String address, String name) {
        addressBookDBService.updateContactDetails(name, address);
    }

    public boolean checkAddressBookInSyncWithDB(String name) {
        List<AddressBookData> addressBookDataList = addressBookDBService.getEmployeePayrollData(name);
        return addressBookDataList.get(0).equals(getEmployeePayrollData(name));
    }

    private AddressBookData getEmployeePayrollData(String name) {
        return this.addressBookList.stream().filter(employeePayrollDataItem -> employeePayrollDataItem.firstName.equals(name)).findFirst().orElse(null);
    }

    public List<AddressBookData> countByCity(String city) {
        return addressBookDBService.getCount(city);
    }

    public List<AddressBookData> countByState(String state) {
        return addressBookDBService.getCountByState(state);
    }

    public void addContactToAddressBook(String firstName, String lastName, String address, String city, String state, int zip, String phoneNumber, String email) {
        addressBookList.add(addressBookDBService.addContact(firstName, lastName, address, city, state, zip, phoneNumber, email));
    }

    public void addContactIntoDB(List<AddressBookData> addressBookDataList) {
        addressBookDataList.forEach(data -> {
            this.addContactToAddressBook(data.firstName, data.lastName, data.address, data.city, data.state, data.zip, String.valueOf(data.phone), data.email);
        });
    }

    public void addAddressBookDataWithThread(List<AddressBookData> addressBookDataList) {
        Map<Integer, Boolean> contactAdditionStatus = new HashMap<Integer, Boolean>();
        addressBookDataList.forEach(data -> {
            Runnable task = () -> {
                contactAdditionStatus.put(data.hashCode(), false);
                System.out.println("Contact is Being Added: " + Thread.currentThread().getName());
                this.addContactToAddressBook(data.firstName, data.lastName, data.address, data.city, data.state, data.zip, String.valueOf(data.phone), data.email);
                contactAdditionStatus.put(data.hashCode(), true);
                System.out.println("Employee Added: " + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, data.firstName);
            thread.setPriority(10);
            thread.start();
        });
        while (contactAdditionStatus.containsValue(false)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(this.addressBookList);
    }

    public long countEntries(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            return this.addressBookList.size();
        return 0;
    }
}
