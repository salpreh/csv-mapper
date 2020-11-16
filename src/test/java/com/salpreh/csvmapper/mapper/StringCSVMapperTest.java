package com.salpreh.csvmapper.mapper;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.salpreh.csvmapper.pojo.Person;

public class StringCSVMapperTest {
	
	protected final StringCSVMapper csvMapper = new StringCSVMapper();
	
	@Test
	public void serializeSingleTest() {
		Person p = new Person("Dagoth", "Ur", 274, "dagoth@redmountain.mw");
		String expected = new StringBuilder()
			.append("\"first_name\",\"second_name\",\"age\",\"email\"\n")
			.append("\"Dagoth\",\"Ur\",\"274\",\"dagoth@redmountain.mw\"\n")
			.toString();

		String pSerialized = csvMapper.map(p);
		
		assertEquals(expected, pSerialized);
	}
	
	@Test
	public void serializeEmptyCollectionTest() {
		
		List<Person> persons = new ArrayList<>();
		
		String cSerialized = csvMapper.map(persons);
		System.out.println(cSerialized);
		
		assertEquals("", cSerialized);
	}
	
	@Test
	public void serializeCollectionTest() {
		Person p1 = new Person("Dagoth", "Ur", 274, "dagoth@redmountain.mw");
		Person p2 = new Person("Nerevar", "Indoril", 87, "nerevar@greathouses.mw");
		
		List<Person> persons = Arrays.asList(p1, p2);

		String expected = new StringBuilder()
			.append("\"first_name\",\"second_name\",\"age\",\"email\"\n")
			.append("\"Dagoth\",\"Ur\",\"274\",\"dagoth@redmountain.mw\"\n")
			.append("\"Nerevar\",\"Indoril\",\"87\",\"nerevar@greathouses.mw\"\n")
			.toString();
		
		String cSerialized = csvMapper.map(persons);
		
		assertEquals(expected, cSerialized);
	}
	
	@Test
	public void serializeArrayTest() {
		Person p1 = new Person("Dagoth", "Ur", 274, "dagoth@redmountain.mw");
		Person p2 = new Person("Nerevar", "Indoril", 87, "nerevar@greathouses.mw");
		
		Person[] persons = new Person[] {p1, p2};

		String expected = new StringBuilder()
			.append("\"first_name\",\"second_name\",\"age\",\"email\"\n")
			.append("\"Dagoth\",\"Ur\",\"274\",\"dagoth@redmountain.mw\"\n")
			.append("\"Nerevar\",\"Indoril\",\"87\",\"nerevar@greathouses.mw\"\n")
			.toString();
		
		String cSerialized = csvMapper.map(persons);
		
		assertEquals(expected, cSerialized);
	}
}
