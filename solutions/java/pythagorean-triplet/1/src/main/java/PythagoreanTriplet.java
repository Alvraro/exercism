import java.util.ArrayList;
import java.util.List;

class PythagoreanTriplet {
	int a;
	int b;
	int c;	
	
    PythagoreanTriplet(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if(obj instanceof PythagoreanTriplet) {
    		PythagoreanTriplet other = (PythagoreanTriplet) obj;
    		return (a == other.a && b == other.b && c == other.c); 
    	}
    	return false;
    }
    
    @Override
    public String toString() {
    	return String.format("{%d, %d, %d}", a, b, c);
    }
    
    static boolean isValid(int a, int b, int c) {
    	return (a*a + b*b) == c*c;
    }

    static TripletListBuilder makeTripletsList() {
        return new TripletListBuilder();
    }

    static class TripletListBuilder {
    	int sum = Integer.MAX_VALUE;
    	int maxFactor = Integer.MAX_VALUE;
    	
        TripletListBuilder thatSumTo(int sum) {
            this.sum = sum;
            return this; 
        }

        TripletListBuilder withFactorsLessThanOrEqualTo(int maxFactor) {
            this.maxFactor = maxFactor;
            return this;
        }

        List<PythagoreanTriplet> build() {
        	ArrayList<PythagoreanTriplet> list = new ArrayList<PythagoreanTriplet>(); 
        	
            for(int a=0; (a<=maxFactor) && (3*a + 3 <= sum); ++a)
            	for(int b=a+1; (b<=maxFactor) && (a + 2*b + 1 <= sum); ++b) {
            		int c = sum - a - b;
            		if((c<=maxFactor) && isValid(a, b, c))
            			list.add(new PythagoreanTriplet(a, b, c));
            	}
            
            return list;
        }

    }

}