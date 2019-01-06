import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * KNN classifier class
 */
public class KNN_classifier {
	private double accuracy;
	private int number_of_attribute;
	private List<String> predicts;

	/**
	 * The constructor of the class
	 */
	public KNN_classifier(){
		this.predicts = new ArrayList<>();
	}

	/**
	 * This function predicts for each test example its classification.
	 * @param train_data - train data.
	 * @param test_data - test data.
	 * @throws IOException.
	 */
	public void Train_and_test(List<String[]> train_data, List<String[]> test_data) throws IOException{
		this.number_of_attribute = test_data.get(0).length - 1;
		int k = 5;

		for (String[] test : test_data) {
			List<int[]> distance = new ArrayList<>();
			for (int i = 0 ; i < train_data.size() ; i++) {
				int counter = 0;
				for(int j = 0 ; j < number_of_attribute ; j++){
					if(!test[j].equals(train_data.get(i)[j]))
						counter++;
				}
				int [] d = {i,counter};
				distance.add(d);
			}
			Collections.sort(distance, new Comparator<int[]>() { //sort the answer for taking the best 5

				@Override
				public int compare(int[] o1, int[] o2) {
					return o1[1] - o2[1];
				}

			});
			List<String> classification = new ArrayList<>();
			for(int i = 0 ; i < k ; i++){
				classification.add(train_data.get(distance.get(i)[0])[number_of_attribute]);
			}
			String most_Repeated_label = classification.stream()
					.collect(Collectors.groupingBy(w -> w, Collectors.counting()))
					.entrySet()
					.stream()
					.max(Comparator.comparing(Entry::getValue))
					.get()
					.getKey();
			this.predicts.add(most_Repeated_label);
		}
		calculate_accuracy(test_data);
	}

	/**
	 * This function calculate the accuracy of knn.
	 * @param test_data - test data.
	 */
	private void calculate_accuracy(List<String[]> test_data) {
		int counter = 0;
		for(int i = 0 ; i < this.predicts.size() ; i++){
			if(this.predicts.get(i).equals(test_data.get(i)[this.number_of_attribute]))
				counter++;
		}
		this.accuracy = (double)counter/test_data.size();

	}
	/**
	 * Getter of accuracy.
	 * @return double accuracy of knn.
	 */
	public double getAccuracy(){
		return (double)Math.round(this.accuracy * 100) / 100;
	}
	
	/**
	 * Getter of predicts.
	 * @return List<String> predicts - the predictions for the test data.
	 */
	public List<String> getPredict() {
		return predicts;
	}

}
