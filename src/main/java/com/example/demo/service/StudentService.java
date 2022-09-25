package com.example.demo.service;

import com.example.demo.fault.ServiceFault;
import com.example.xml.school.Student;

public interface StudentService {

	public Student findStudent(String name) throws ServiceFault;
}
