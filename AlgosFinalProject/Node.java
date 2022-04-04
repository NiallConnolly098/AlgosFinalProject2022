
public class Node {
	public double cost;
	public int stopID;
	
	public Node(double cost, int stopID) {
		this.stopID = stopID;
		this.cost = cost;
	}
	public void printNode() {
		System.out.println("Stop id: " + stopID + " Cost: " + cost);
	}
}
