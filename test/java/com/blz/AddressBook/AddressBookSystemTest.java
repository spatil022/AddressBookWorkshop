package com.blz.AddressBook;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blz.AddressBook.AddressBookService.IOService;

public class AddressBookSystemTest {
	
	private static AddressBookService addressBookService;
	
	@BeforeClass
	public static void createAddressBookObj() {
		addressBookService = new AddressBookService();
		System.out.println("Welcome to the Address Book System");
	}
	
	@Test
	public void givenAddressBookDetails_WhenRetrieved_ShouldMachPersonsCount() throws AddressBookException {
		List<Person> list = addressBookService.readAddressBookData(IOService.DB_IO);
		Assert.assertEquals(2, list.size());
	}
	
	@Test
	public void givenAddressBookDetails_WhenUpdated_ShouldSyncWithDB() throws AddressBookException {
		List<Person> data = addressBookService.readAddressBookData(IOService.DB_IO);
		addressBookService.updateDBRecord("Sana", "Ether");
		boolean result = addressBookService.checkUpdatedRecordSyncWithDatabase("Sana");
		Assert.assertEquals(true, result);
	}
	
	@Test
	public void givenAddressBookDetails_WhenRetrieved_ForGivenRange_ShouldMatchPersonsCount() throws AddressBookException {
		List<Person> list = addressBookService.readAddressBookData(IOService.DB_IO, "2020-11-01", "2020-11-22");
		Assert.assertEquals(2, list.size());
	}
	
	@Test
	public void givenAddressBookDetails_WhenRetrieved_ShouldReturnTotalNumberOfContacts() throws AddressBookException {
		Assert.assertEquals(1, addressBookService.readAddressBookData("Count", "KNR"));
	}
	
	@Test
	public void givenAddressBookDetails_WhenaddedNewcontact_ShouldSyncWithDB() throws AddressBookException {
		addressBookService.readAddressBookData(IOService.DB_IO);
		addressBookService.addNewContact("Manju", "Reddy", "Kalas", "Pune", "MH", "500039", "9581440658", "manju@gmail.com");
		boolean result = addressBookService.checkUpdatedRecordSyncWithDatabase("Manju");
		Assert.assertEquals(true, result);
	}
	
	@Test
	public void givenPersons_WhenAddedToDBUsingThread_ShouldMatchNumOfEntries() throws AddressBookException {
		Person[] arrayOfContacts = {
				new Person("Ravi", "Shyam", "Vish", "HYD", "MH", "500012", "6032807811", "Ravi@gmail.com"),
				new Person("Pranu", "Radha", "Rampur", "KNR", "MH", "505001", "9234150756", "Pranu@gmail.com")
		};
		addressBookService.readAddressBookData(IOService.DB_IO);
		Instant start = Instant.now();
		addressBookService.addMultipleContacts(Arrays.asList(arrayOfContacts));
		Instant end = Instant.now();
		System.out.println("Duration with thread: "+Duration.between(start, end));
		Assert.assertEquals(3, addressBookService.countEntries(IOService.DB_IO));
	}
}