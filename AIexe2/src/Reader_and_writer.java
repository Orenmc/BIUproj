import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Reader and writer class.
 */
public class Reader_and_writer {

	/**
	 * This function read data from text file.
	 * @param file_name - the file to read from.
	 * @return List<String[]> data - the data the function read fron the file.
	 * @throws IOException
	 */
	public List<String[]> Read(String file_name) throws IOException{
		List<String[]> data = new ArrayList<String[]>();

		List<String> lines = Files.readAllLines(Paths.get(file_name));
		for (String s : lines) {
			data.add(s.split("\t"));
		}
		return data;
	}
	
	/**
	 * This function write a new text file with the all predictions from the 3 classifiers.
	 * @param knn - knn classifier.
	 * @param nb - naive bais classifier.
	 * @param dt - decision tree classifier.
	 * @throws IOException
	 */
	public void Write(KNN_classifier knn, Naive_Bais_classifier nb, Decision_tree_classifier dt) throws IOException {
		List<String> knn_predict = knn.getPredict();
		List<String> nb_predict = nb.getPredict();
		int test_size = knn_predict.size();
		
		File output = new File("output.txt");
		FileWriter writer = new FileWriter(output);
		writer.write("Num\tDT\tKNN\tnaiveBais\n");
		writer.write(System.lineSeparator());
		for(int i = 0 ; i < test_size ; i++){
			writer.write(i+1 + "\t" + "DT" + "\t" + knn_predict.get(i) + "\t" + nb_predict.get(i));
			writer.write(System.lineSeparator());
		}
		writer.write("\t" + "miss" + "\t" + knn.getAccuracy() + "\t" + nb.getAccuracy());
		writer.close();
	}

}
