package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/app-config.xml"})
public class ApplicationJunit4Test {

	@Test(expected = Exception.class)
	public void shouldRaiseAnException() throws Exception {
	    
	}
}
