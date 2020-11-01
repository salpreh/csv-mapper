package com.salpreh.csvmapper.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.salpreh.csvmapper.pojo.Person;

public class CSVMapperTest {
	
	protected final CSVMapper csvMapper = new CSVMapper();
	
	@Test
	public void serializeTest() {
		Person p = new Person("Dagoth", "Ur", 274, "dagoth@redmountain.mw");
		String expected = new StringBuilder()
			.append("\"first_name\",\"second_name\",\"age\",\"email\"\n")
			.append("\"Dagoth\",\"Ur\",\"274\",\"dagoth@redmountain.mw\"\n")
			.toString();

		String pSerialized = csvMapper.mapAsString(p);
		
		assertEquals(expected, pSerialized);
	}

}
