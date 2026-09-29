import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

class PalindromeCalculator {

	SortedMap<Long, List<List<Integer>>> getPalindromeProductsWithFactors(int minFactor, int maxFactor) {
		if(minFactor > maxFactor) {
			throw new IllegalArgumentException("invalid input: min must be <= max");
		}
		
		SortedMap<Long, List<List<Integer>>> result = new TreeMap<Long, List<List<Integer>>>();
		
		long minPalindrome = Long.MAX_VALUE;
		long maxPalindrome = Long.MIN_VALUE;
		for(int factor1=minFactor; factor1<=maxFactor; ++factor1) {
			for(int factor2=factor1; factor2<=maxFactor; ++factor2) {
				long number = factor1 * factor2;
				if ((number <= minPalindrome || number >= maxPalindrome) && isPalindrome(number)) {
					List<Integer> factors = List.of(factor1, factor2);
					if(number < minPalindrome) {
						minPalindrome = number;
						ArrayList<List<Integer>> minList = new ArrayList<List<Integer>>();
						minList.add(factors);
						result.put(number, minList);
					}
					else if (number == minPalindrome) {
						List<List<Integer>> minList = result.get(number);
						minList.add(factors);
					}

					if(number > maxPalindrome) {
						maxPalindrome = number;
						ArrayList<List<Integer>> maxList = new ArrayList<List<Integer>>();
						maxList.add(factors);
						result.put(number, maxList);
					}
					else if (number == maxPalindrome) {
						List<List<Integer>> maxList = result.get(number);
						maxList.add(factors);
					}
				}
			}
		}
		
		return result;
	}
	
	boolean isPalindrome(long number) {
		int[] digits = new int[1 + (int)Math.floor(Math.log10(number))];
		int i = 0;
		while(number > 0) {
			digits[i++] = (int) (number % 10);
			number = number / 10;
		}
		
		for(i=0; i<digits.length; ++i) {
			if(digits[i] != digits[digits.length-1-i])
				return false;
		}
		
		return true;
	}

}