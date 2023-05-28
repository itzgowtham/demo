package com.example.service;

import com.example.fault.ServiceFault;
import com.example.schema.Student;

public interface StudentService {

	public Student findStudent(String name) throws ServiceFault;
}
