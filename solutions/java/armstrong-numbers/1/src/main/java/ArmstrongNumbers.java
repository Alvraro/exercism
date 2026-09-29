class ArmstrongNumbers {

    boolean isArmstrongNumber(int numberToCheck) {
		try {
			int aux = numberToCheck;
			int numDigits;
			numDigits = getNumberOfDigits(numberToCheck);
			int armstrongNumberSum = 0;
			while(aux > 0) {
				int digit = aux % 10;
				armstrongNumberSum += Math.pow(digit, numDigits);
				aux = aux / 10;
			}
			return numberToCheck == armstrongNumberSum;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }

    int getNumberOfDigits(int number) throws Exception {
    	if(number < 0)
    		throw new Exception("Number must be >= 0");
    	
    	if(number == 0)
    		return 1;

    	return 1 + (int) Math.floor(Math.log10(number));
    }
    
}
