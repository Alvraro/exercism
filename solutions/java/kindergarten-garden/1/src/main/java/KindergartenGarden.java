import java.util.ArrayList;
import java.util.List;

enum Child {
	Alice, Bob, Charlie, David, Eve, Fred, Ginny, Harriet, Ileana, Joseph, Kincaid, Larry
};

class KindergartenGarden {
	// static int NUM_WINDOWS = 3;
	static int NUM_ROWS = 2;
	static int NUM_CAPS_PER_ROW = 2;

	String garden;

	KindergartenGarden(String garden) {
		this.garden = garden;
	}

	List<Plant> getPlantsOfStudent(String student) {
		ArrayList<Plant> plants = new ArrayList<Plant>(NUM_CAPS_PER_ROW * NUM_ROWS);

		int studentIndex = -1;
		try {
			studentIndex = getStudentIndex(student);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		String[] gardenRows = garden.split("\n");
		for (String gardenRow : gardenRows) {
			for (int numCap = 0; numCap < NUM_CAPS_PER_ROW; ++numCap)
				plants.add(Plant.getPlant(gardenRow.charAt(studentIndex * NUM_CAPS_PER_ROW + numCap)));
		}

		return plants;
	}

	private int getStudentIndex(String student) throws Exception {
		int index = 0;
		for (Child value : Child.values()) {
			if (value.toString().equalsIgnoreCase(student))
				return index;
			++index;
		}
		throw new Exception(String.format("Student '%s' not found!", student));
	}

}
