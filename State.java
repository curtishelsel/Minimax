import java.util.List;
import java.awt.Point;
import java.util.ArrayList;


public class State {


	private List<State> childrenStates = null;
	private int value;
	private List<Point> moves;

	public State(Point move){
		this.childrenStates = new ArrayList<>();
		this.moves = new ArrayList<Point>();
		moves.add(move);
	}
	public State(Point move, List<Point> ghosts){
		this.childrenStates = new ArrayList<>();
		this.moves = new ArrayList<Point>();
		moves.add(move);
		for(int i = 0; i < ghosts.size(); i++){
			moves.add(ghosts.get(i));
		}
	}

	public State(){
		this.childrenStates = new ArrayList<>();
		this.moves = new ArrayList<Point>();
	}

	public void addState(State child){
		childrenStates.add(child);
	}

	public void setValue(int value){
		this.value = value;
	}
	
	public void addMove(Point Move){
		moves.add(Move);
	}
	
	public List<State> getChildren(){
		return childrenStates;
	}

	public Point getMove(int i){
		return moves.get(i);
	}

	public List<Point> getMoves(){
		return moves;
	}

	public int getValue(){
		return value;
	}
}
