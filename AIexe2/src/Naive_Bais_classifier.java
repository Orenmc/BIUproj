import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Naive Bais classifier class.
 */
public class Naive_Bais_classifier {
	private double accuracy;
	private int number_of_attribute;
	private List<String> classifications;
	private List<String> predicts;
	private HashMap<String, Integer> classifications_frequency;
	private HashMap<String, Double> priors;
	
	/**
	 * The constructor of the class.
	 */
	public Naive_Bais_classifier() {
		this.classifications = new ArrayList<>();
		this.predicts = new ArrayList<>();
		this.classifications_frequency = new HashMap<>();
		this.priors = new HashMap<>();
	}
	/**
	 * This function create a model for the NB classifier using the train data.
	 * @param train_data - train data
	 * @param attributes - the attributes of each example in the train data.
	 * @return percentage<string, HashMap<String,Double> - dictionary of probability for each case
	 * 		   for example ---> { male, {yes=0.33,no=0.6} } using the naive bais formula.
	 */
	public HashMap<String, HashMap<String,Double>> Train(List<String[]> train_data, String[] attributes) {
		this.number_of_attribute = train_data.get(0).length - 1;
		this.classifications = Options_of_classification(train_data);
		this.priors = Calculate_prior_each_classification(train_data);
		HashMap<String, List<String>> attribute_options = Check_options_of_each_attribute(train_data,attributes);
		HashMap<String, HashMap<String,Double>> percentage = Calculate_percentage(train_data,attribute_options,attributes);
		//percentage.forEach((k,v)-> System.out.println(k+", "+v));
		return percentage;
	}
	
	/**
	 * 
	 * @param test_data - test data.
	 * @param percentage - dictionary of probability for each case.
	 */
	public void Test(List<String[]> test_data,HashMap<String, HashMap<String,Double>> percentage){
		for (String[] data : test_data) {
			List<String> list = new ArrayList<String>(Arrays.asList(data));
			list.remove(number_of_attribute);
			double max = 0;
			String predict_label = "";
			for (String label : classifications) {
				double cal = 1;
				for (String string : list) {
					cal = (double)(cal*percentage.get(string).get(label));
				}
				cal = (double)(cal*priors.get(label));
				if(cal > max) {
					max = cal;
					predict_label = label;
				}
			}
			this.predicts.add(predict_label);
		}
		Calculate_accuracy(test_data);
	}
	/**
	 * This function calculate the accuracy of nb.
	 * @param test_data - test data.
	 */
	private void Calculate_accuracy(List<String[]> test_data) {
		int counter = 0;
		for(int i = 0 ; i < this.predicts.size() ; i++){
			if(this.predicts.get(i).equals(test_data.get(i)[this.number_of_attribute]))
				counter++;
		}
		this.accuracy = (double)counter/test_data.size();
	}
	/**
	 * This function calculate the probability for each case using the naive bais formula.
	 * @param train_data - train data.
	 * @param attribute_options - dictionary of options per attribute, for example: {color:{blue,red,black}}.
	 * @param attributes - the attributes of each example in the train data.
	 * @return HashMap<String, HashMap<String, Double>> percentage<string, HashMap<String,Double> - dictionary of probability
	  		   for each case, for example ---> { male, {yes=0.33,no=0.6} }.
	 */
	private HashMap<String, HashMap<String, Double>> Calculate_percentage(List<String[]> train_data,
			HashMap<String, List<String>> attribute_options, String[] attributes) {

		HashMap<String, HashMap<String, Double>> percent_nb = new HashMap<>();
		int serial_num = 0;
		int count_att_label = 1;
		while(serial_num < this.number_of_attribute){
			List<String> attribute = attribute_options.get(attributes[serial_num]);
			for (String att : attribute) {
				HashMap<String, Double> p = new HashMap<>();
				for (String label : classifications) {
					for (String[] data : train_data) {
						if(data[serial_num].equals(att) && data[number_of_attribute].equals(label)){
							count_att_label++;
						}
					}
					double precentage = (double)count_att_label/((this.classifications_frequency.get(label) + attribute.size()));
					p.put(label, precentage);
					count_att_label = 1;
				}
				percent_nb.put(att, p);
			}
			serial_num++;
		}
		return percent_nb;
	}
	
	/**
	 * This function finds all the options for each attribute.
	 * @param train_data - train data.
	 * @param attributes - the attributes of each example in the train data.
	 * @return HashMap<String, List<String>> attribute_options - dictionary of options per attribute,
	  		   for example: {color:{blue,red,black}}.
	 */
	private HashMap<String, List<String>> Check_options_of_each_attribute(List<String[]> train_data,String[] attributes) {
		HashMap<String, List<String>> attributes_options = new HashMap<>();
		int serial_number = 0;
		for (String string : attributes) {
			List<String> one_attribute_options = new ArrayList<>();
			if(!string.equals(attributes[number_of_attribute])){
				for (String[] data : train_data) {
					if(!one_attribute_options.contains(data[serial_number]))
						one_attribute_options.add(data[serial_number]);
				}
				attributes_options.put(string, one_attribute_options);
				serial_number++;
			}
		}
		return attributes_options;
	}
	/**
	 * 
	 * @param train_data - train data.
	 * @return HashMap<String, Double> priors - probability for each classification.
	 */
	private HashMap<String, Double> Calculate_prior_each_classification(List<String[]> train_data) {
		HashMap<String, Double> priors = new HashMap<>();
		for (String label : classifications) {
			int counter = 0;
			for (String[] data : train_data) {
				if(data[number_of_attribute].equals(label))
					counter++;
			}
			this.classifications_frequency.put(label, counter);
			double prior = (double)counter/train_data.size();
			priors.put(label, prior);
		}
		return priors;		
	}
	/**
	 * This function finds all the options for classification.
	 * @param train_data - train data.
	 * @return List<String> classifications_options - all the possible classifications.
	 */
	private List<String> Options_of_classification(List<String[]> train_data) {
		List<String> classifications_options = new ArrayList<>();
		for (String[] data : train_data) {
			if(!classifications_options.contains(data[number_of_attribute]))
				classifications_options.add(data[number_of_attribute]);
		}
		return classifications_options;
	}
	/**
	 * Getter of accuracy.
	 * @return double accuracy of knn.
	 */
	public double getAccuracy() {
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
