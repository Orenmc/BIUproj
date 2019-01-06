import java.security.KeyStore.Entry.Attribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Decision_tree_classifier {
	private List<String> classification_options;
	private int classification_index;
	
	public Decision_tree_classifier(List<String[]> train_data){
		this.classification_index = train_data.get(0).length - 1;
		this.classification_options = Options_of_classification(train_data);
	}
	
	public void Train(List<String[]> train_data,List<String> attributes) {
		String most_common_classification = Find_most_common(train_data);
		DTL(train_data,attributes,most_common_classification);
	}

	private Node DTL(List<String[]> train_data, List<String> attributes, String most_common_classification) {
		if(train_data.isEmpty())
			return new Node(most_common_classification);
		if(check_if_same_class(train_data))
			return new Node(train_data.get(0)[classification_index]);
		if(attributes.isEmpty())
			return new Node(Find_most_common(train_data));

			
		
		return null;
	}

	private boolean check_if_same_class(List<String[]> train_data) {
		List<String> classification = new ArrayList<>();
		for (String[] example : train_data) {
			if(!classification.contains(example[classification_index])){
				classification.add(example[classification_index]);
			}		
		}
		return classification.size() == 1 ? true : false;
	}

	private String Find_most_common(List<String[]> train_data) {
		HashMap<String, Integer> frequency_of_each_classification = new HashMap<>();
		for (String classi : classification_options) {
			frequency_of_each_classification.put(classi, 0);
		}
		for (String[] example : train_data) {
			Integer freq = frequency_of_each_classification.get(example[classification_index]);
			frequency_of_each_classification.put(example[classification_index],++freq);
		}
		String common = Collections.max(frequency_of_each_classification.entrySet(), Map.Entry.comparingByValue()).getKey();
		return common;
	}
	
	private List<String> Options_of_classification(List<String[]> train_data) {
		List<String> classifications_options = new ArrayList<>();
		for (String[] data : train_data) {
			if(!classifications_options.contains(data[classification_index]))
				classifications_options.add(data[classification_index]);
		}
		return classifications_options;
	}

}
