/**
 * @author Snir Hazan
 * @version 1.1 Beta
 */
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Main class of the project.
 */
public class java_ex2 {

	/**
	 * Main function.
	 * @param args.
	 * @throws IOException.
	 */
	public static void main(String[] args) throws IOException {

		
		//READ DATA
		Reader_and_writer IO = new Reader_and_writer();
		List<String[]> train_data = IO.Read("train.txt");
		List<String[]> test_data = IO.Read("test.txt");

		//GET THE ATTRIBUTES OF THE DATA AND REMOVE IT FROM THE ALL DATA
		String [] attributes = train_data.get(0);
		train_data.remove(0);
		test_data.remove(0);

		//NAIVE BAIS CLSSIFIER
		Naive_Bais_classifier nb = new Naive_Bais_classifier();
		HashMap<String, HashMap<String,Double>> percentage = nb.Train(train_data, attributes);
		nb.Test(test_data, percentage);


		//KNN CLASSIFIER
		KNN_classifier knn = new KNN_classifier();
		knn.Train_and_test(train_data,test_data);

		//DT CLASSIFIER
		Decision_tree_classifier dt = new Decision_tree_classifier(train_data);
		//dt.Train(train_data, Arrays.asList(attributes));



		//WRITE OUTPUT
		IO.Write(knn,nb,dt);
		 
	}

}
/*
Node root = new Node("Sex", null);
root.setDepth(0);
root.add_child(new Node("Pclass", "female"));
Node d = new Node("Pclass", "male");
root.add_child(d);
d.add_child(new Node("Yes", "crew"));


Stack<Node> s = new Stack<>();
s.push(root);
int counter = 0;
while(!s.isEmpty()){
	Node a = s.pop();
	s.addAll(a.get_children());
	if(counter != 0){
		for(int i = 0 ; i<a.getDepth() - 1;i++){
			System.out.print("\t");
			System.out.print("|");
		}
		System.out.print(a.getFather().getAttribute() + "="+a.getValue_of_father() + ":" + a.getAttribute());
		System.out.println();
	}counter++;
}
*/