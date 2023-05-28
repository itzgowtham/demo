package com.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/app-config.xml"})
public class ApplicationJunit5Test {

	@Test
	public void shouldRaiseAnException() throws Exception {
	    Assertions.assertThrows(Exception.class, () -> {
	        //...
	    });
	}
}
