package sonnh.opt.opt_plan.exception;

public class BusinessException extends RuntimeException {
	public BusinessException(String message) { super(message); }

	public BusinessException(String resourceName, String fieldName, Object fieldValue) {
		super(String.format("Business error: %s not found with %s : '%s'", resourceName,
				fieldName, fieldValue));
	}
}
