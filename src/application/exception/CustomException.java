package application.exception;

public class CustomException extends RuntimeException {

  private static final long serialVersionUID = 7937629862108861884L;

  public CustomException() {
    
  }
  
  public CustomException(String message) {
    super(message);
  }
  
  public CustomException(Throwable throwable) {
    super(throwable);
  }
  
  public CustomException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
