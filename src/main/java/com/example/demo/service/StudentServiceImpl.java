package com.example.demo.service;

import java.util.List;

import org.hibernate.cache.CacheException;
import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;

import com.example.demo.fault.ServiceFault;
import com.example.demo.repository.StudentRepository;
import com.example.exception.Error;
import com.example.exception.ExceptionList;
import com.example.exception.Exceptions;
import com.example.xml.school.Student;

@Service
public class StudentServiceImpl implements StudentService {
	
	@Autowired
	private StudentRepository studentRepository;
	
	private RMap cacheMap;
	
	private Config cacheConfig;
	
	private String redisKey="";
	
	protected RedissonClient redisson;
	
	public Student findStudent(String name) throws ServiceFault {
		if(studentRepository.findStudent(name)!=null)
			return studentRepository.findStudent(name);
		else {
			Exceptions ex = new Exceptions();
			List<Error> list = ex.getError();
			Error err = new Error();
			err.setCode("404");
			err.setReason("Student Not Found");
			list.add(err);
			throw new ServiceFault(SoapFaultDefinition.SERVER.toString(), ex);
		}
	}
	
	public RMap getCacheMap(){

		try {
			
			RedissonClient redisson = createRedissonClient();

			RKeys keys = redisson.getKeys();

			Iterable<String> allKeys = keys.getKeysByPattern("*demo*");
			keys.getKeysByPattern("");
			for (String mykey : allKeys) {
				if (null != mykey && (mykey.contains("lol") ||
									  mykey.contains("lel"))) {
					cacheMap = redisson.getMap(mykey, JsonJacksonCodec.INSTANCE);
				}
			}
			if(null == cacheMap){

				cacheMap = redisson.getMap(redisKey, JsonJacksonCodec.INSTANCE);
			}
		}catch (Exception ex){
			ex.printStackTrace();

		}
		return cacheMap;
	}

	public RedissonClient createRedissonClient() {
		
		if (cacheConfig == null) {
			throw new CacheException("Unable to locate Redisson configuration");
		}
		return Redisson.create(cacheConfig);
	}
}
