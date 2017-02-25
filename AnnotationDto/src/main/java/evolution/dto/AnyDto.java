package evolution.dto;

import java.util.List;

import lombok.Data;

@Data
public class AnyDto {
	@AnyAnnotation(value = "anyValue")
	private String anyVariable = "Hello";
	@AnyAnnotation(value = "anotherValue")
	private String anotherVariable = "World";
	@AnotherAnnotation(value = 0)
	private static String theOtherVariable = "Hey";
	@AnotherAnnotation(value = 1)
	private List<String> heros;
}
