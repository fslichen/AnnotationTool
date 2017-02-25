package evolution.dto;

import java.util.List;

public class AnyDto {
	@AnyAnnotation(value = "anyValue")
	private String anyVariable = "Hello";
	@AnyAnnotation(value = "anotherValue")
	private String anotherVariable = "World";
	private static String theOtherVariable = "Hey";
	private List<String> heros;
}
