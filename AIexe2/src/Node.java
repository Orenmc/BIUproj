import java.util.ArrayList;
import java.util.List;

public class Node {
	public String classification;
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	private Node father;
	private String label;
	private List<Node> children;
	private String attribute;
	private int depth;
	
	
	public Node(String value,String value_of_father) {
		this.attribute = value;
		this.label = value_of_father;
		this.children = new ArrayList<>();
	}
	public void add_child(Node c) {
		c.set_father(this);
		int x = this.getDepth();
		c.setDepth(++x);
		this.children.add(c);
	}
	public void set_father(Node f){
		this.father = f;
	}
	public List<Node> get_children() {
		return this.children;
	}
	public String getValue_of_father() {
		return label;
	}
	public Node getFather() {
		return father;
	}
	public String getAttribute() {
		return attribute;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
}
