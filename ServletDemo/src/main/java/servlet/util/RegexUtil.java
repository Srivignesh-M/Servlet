package servlet.util;

import java.util.regex.Pattern;

public class RegexUtil {
	    private static final String usernameRegex = "^[a-zA-Z0-9_]{5,15}$";
	    private static final String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	    private static final String passwordRegex= "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
	    private static final String amountRegex= "^([1-9]\\d*)(\\.\\d{1,2})?$";
	    private static final Pattern usernamePattern = Pattern.compile(usernameRegex);
	    private static final Pattern emailPattern= Pattern.compile(emailRegex);
	    private static final Pattern passwordPattern = Pattern.compile(passwordRegex);
	    private static final Pattern amountPattern = Pattern.compile(amountRegex);
	    
	    public boolean isValidUsername(String username) {
	        return username != null && usernamePattern.matcher(username).matches();
	    }
	    
	    public boolean isValidEmail(String email) {
	        return email != null && emailPattern.matcher(email).matches();
	    }
	    
	    public boolean isValidPassword(String password) {
	        return password != null && passwordPattern.matcher(password).matches();
	    }
	    
	    public boolean isValidAmount(String amount) {
	        return amount != null && amountPattern.matcher(amount).matches();
	    }
	    
}



