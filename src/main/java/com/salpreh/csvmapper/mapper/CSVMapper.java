package com.salpreh.csvmapper.mapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.salpreh.csvmapper.annotation.CSVField;
import com.salpreh.csvmapper.annotation.CSVSerializable;
import com.salpreh.csvmapper.exception.CSVSerializationException;

public class CSVMapper {
	
	private static char WRAPPER_CHAR = '"';
	private static char SEP_CHAR = ',';
	
	public String mapAsString(Object o) throws CSVSerializationException {
		checkIfSerializable(o);

		if (isCollection(o)) {
			throw new CSVSerializationException("Collections serialization not supported yet");
		}
		
		try {
			return convertSingleObjectToString(o, true);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new CSVSerializationException("Unable to serialize object", e);
		}
	}

	private void checkIfSerializable(Object o) throws CSVSerializationException {
		if (o == null) {
			throw new CSVSerializationException("Attempted to serialize null reference. Object passed is null");
		}

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
	
	private String convertSingleObjectToString(Object o, boolean addHeaders) throws IllegalArgumentException, IllegalAccessException {
		String serialized = getHeadersString(o);
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
