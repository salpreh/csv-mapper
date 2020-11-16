package com.salpreh.csvmapper.mapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.salpreh.csvmapper.annotation.CSVField;
import com.salpreh.csvmapper.annotation.CSVSerializable;
import com.salpreh.csvmapper.exception.CSVSerializationException;

public class StringCSVMapper implements ICSVMapper<String> {
	
	private static char WRAPPER_CHAR = '"';
	private static char SEP_CHAR = ',';
	
	public String map(Object o) throws CSVSerializationException {
		checkIfSerializable(o);

		try {
			if (isCollection(o)) {
				return convertCollectionToString(o);
			} else if (o.getClass().isArray()) {
				return convertArrayToString((Object[])o);
			}

		
			return convertSingleObjectToString(o, true);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new CSVSerializationException("Unable to serialize object", e);
		}
	}

	private void checkIfSerializable(Object o) throws CSVSerializationException {
		if (o == null) {
			throw new CSVSerializationException("Attempted to serialize null reference. Object passed is null");
		}
		
		if (o.getClass().isArray() || isCollection(o)) return;

		Class<?> clazz = o.getClass();
		if (!clazz.isAnnotationPresent(CSVSerializable.class)) {
			throw new CSVSerializationException(String.format("Class %s is not annotated as serializable", clazz.getName()));
		}
	}
	
	private boolean isCollection(Object o) {
		Class<?> clazz = o.getClass();
		Class<?> cClazz = Collection.class;
		
		return cClazz.isAssignableFrom(clazz);
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String convertCollectionToString(Object o) throws IllegalArgumentException, IllegalAccessException {
		Collection<Object> oCollection = (Collection)o;
		
		if (oCollection.size() == 0) {
			return "";
		}
		
		Object[] arr = oCollection.toArray();
		
		return convertArrayToString(arr);
	}
	
	private String convertArrayToString(Object[] oArr) throws IllegalArgumentException, IllegalAccessException {
		String serialized = "";

		Class<?> itClazz = oArr[0].getClass();
		serialized += getHeadersString(itClazz);
		for (Object o : oArr) {
			serialized += convertSingleObjectToString(o, false);
		}
		
		return serialized;
	}
	
	private String convertSingleObjectToString(Object o, boolean addHeaders) throws IllegalArgumentException, IllegalAccessException {
		String serialized = "";
		if (addHeaders) serialized += getHeadersString(o);

		Class<?> clazz = o.getClass();
		
		List<String> values = new ArrayList<>();
		for (Field f : clazz.getDeclaredFields()) {
			if (f.isAnnotationPresent(CSVField.class)) {
				f.setAccessible(true);
				values.add(f.get(o).toString());
			}
		}
		
		return serialized + getCSVStringLine(values);
	}
	
	private String getHeadersString(Object o) {
		Class<?> clazz = o.getClass();

		return getHeadersString(clazz);
	}

	private String getHeadersString(Class<?> clazz) {
		List<String> headers = new ArrayList<>();

		for (Field f : clazz.getDeclaredFields()) {
			if (f.isAnnotationPresent(CSVField.class)) {
				CSVField fieldAn = f.getAnnotation(CSVField.class);
				headers.add(fieldAn.key().isEmpty() ? f.getName() : fieldAn.key());
			}
		}
		
		return getCSVStringLine(headers);
	}
	
	private String getCSVStringLine(final List<String> elements) {
		List<String> wrapedElements = elements.stream()
			.map(el -> WRAPPER_CHAR+el+WRAPPER_CHAR)
			.collect(Collectors.toList());

		return String.join(String.valueOf(SEP_CHAR), wrapedElements) + System.lineSeparator();
	}
}
