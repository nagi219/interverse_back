package com.interverse.demo.util;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class NullOrEmptyUtil {
	
	public boolean determineString(String string) {
        return string == null || string.trim().isEmpty();
    }
	
	public boolean determineLocalDate(LocalDate localDate) {
		return localDate == null;
	}
	
}
