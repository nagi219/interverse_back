package com.interverse.demo.util;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

@Component
public class AgeCalculator {
	
	public Integer calculateAge(LocalDate birthDate) {
		LocalDate currentDate = LocalDate.now();
		
        return Period.between(birthDate, currentDate).getYears();
    }

}
