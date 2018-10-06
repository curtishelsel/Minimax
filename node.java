public class Node {


	private List<Node> children = null;
	private int value;

	public Node(int value){
	
		this.children = new ArrayList<>();
		this.value = value;
	}

	public void addNode(Node child){
		children.add(child);
	}


}
