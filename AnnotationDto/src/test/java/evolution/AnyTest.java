package evolution;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Test;

import evolution.dto.AnotherAnnotation;
import evolution.dto.AnyAnnotation;
import evolution.dto.AnyDto;
import util.AnyUtil;

public class AnyTest {
	@Test
	public void testSetVariable() {
		AnyDto anyDto = new AnyDto();
		AnyUtil anyUtil = new AnyUtil();
		anyUtil.setVariable(anyDto, "theOtherVariable", "HoHo");
		System.out.println(anyUtil.getVariable(anyDto, "theOtherVariable"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testGetVariable() {
		AnyDto anyDto = new AnyDto();
		AnyUtil anyUtil = new AnyUtil();
		List<String> heros = (List<String>) anyUtil.getVariable(anyDto, "heros");
		System.out.println(heros);
	}

	@Test
	public void testGetFieldsByAnnotationAndKeyValuePairs() {
		AnyDto anyDto = new AnyDto();
		AnyUtil anyUtil = new AnyUtil();
		List<Field> fields = anyUtil.getFieldsByAnnotationAndKeyValuePairs(anyDto, null, AnyAnnotation.class, "value", "anyValue");
		System.out.println(fields);
	}
	
	@Test
	public void testGetInstanceVariablesByAnnotationAndKeyValuePairs() {
		AnyDto anyDto = new AnyDto();
		AnyUtil anyUtil = new AnyUtil();
		List<Object> objects = anyUtil.getInstanceVariablesByAnnotationAndKeyValuePairs(anyDto, null, AnyAnnotation.class, "value", "anyValue");
		System.out.println(objects);
		objects = anyUtil.getInstanceVariablesByAnnotationAndKeyValuePairs(anyDto, null, AnotherAnnotation.class, "value", 0);
		System.out.println(objects);
	}
}
