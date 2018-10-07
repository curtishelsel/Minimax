import pacsim.BFSPath;
import pacsim.PacAction;
import pacsim.PacCell;
import pacsim.PacFace;
import pacsim.PacSim;
import pacsim.PacUtils;
import pacsim.PacmanCell;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class PacSimMinimax implements PacAction{

	//option: class and instance variables
	private int depth;
	private	List<Point> path;
	private int actionNum;

	public PacSimMinimax(int depth, String fname, int te, int gran, int max){

		this.depth = depth;
		PacSim sim =  new PacSim(fname, te, gran, max);
		sim.init(this);
		path = new ArrayList();
	}
	
	public static void main(String[] args){
	
		String fname = args[0];
		int depth = Integer.parseInt(args[1]);

		int te = 0;
		int gr = 0;
		int ml = 0;

		if(args.length == 5){
			te = Integer.parseInt(args[2]);
			gr = Integer.parseInt(args[3]);
			ml = Integer.parseInt(args[4]);
		}

		new PacSimMinimax(depth, fname, te, gr, ml);

		System.out.println("\nAdversarial Search using Minimax by Curtis Helsel:");
		System.out.println("\n	Game board	: " + fname);
		System.out.println("	Search depth : " + depth + "\n");

		if(te > 0){
			System.out.println("	Preliminary runs : " + te
			+ "\n	Granularity		: " + gr
			+ "\n	Max move limit	: " + ml
			+ "\n\nPreliminary run results :\n");
		}
	}

	@Override
	public void init(){}

	public void getStates(int depth, PacCell[][] grid, State state, List<Point> ghosts){
	
		
		System.out.print("Depth " + depth + " Value: " + state.getValue() + " ");
		if(depth == 0){
			return;
		}


		List<Point> pacMoves = new ArrayList<Point>();
		List<List<Point>> ghostList = new ArrayList<List<Point>>();

		// find valid pacman moves
		pacMoves = getMoves(state.getMove(0).x, state.getMove(0).y, grid);

		for(int i = 0; i < ghosts.size(); i++){
			List<Point> ghostMoves = new ArrayList<Point>();
			ghostMoves = getMoves(ghosts.get(i).x, ghosts.get(i).y, grid);
			ghostList.add(ghostMoves);
		}

		for(int i = 0; i < ghostList.get(0).size(); i++){
			for(int j = 0; j < ghostList.get(1).size(); j++){
				for(int k = 0; k < pacMoves.size(); k++){
					state.addState(new State(pacMoves.get(k)));
					
					int len = state.getChildren().size() - 1;
					State child = new State();
					child = state.getChildren().get(len);

					child.addMove(ghostList.get(0).get(i));			
					child.addMove(ghostList.get(1).get(j));
					child.setValue(evaluate(child, grid));
					getStates(depth-1, grid, child, child.getMoves().subList(1,child.getMoves().size()));
				}
			} 
		}

	}

	public int evaluate(State state, PacCell[][] grid){
		
		Point p = PacUtils.nearestFood(state.getMove(0), grid);
		int value = BFSPath.getPath(grid, state.getMove(0), p).size();
		
		value -= BFSPath.getPath(grid, state.getMove(0), state.getMove(1)).size();
		value -= BFSPath.getPath(grid, state.getMove(0), state.getMove(2)).size();

		return value;
	}
	

	// Method gets the list of possible moves from current location
	public List<Point> getMoves(int x, int y, PacCell[][] grid){
		
		List<Point> moves = new ArrayList<Point>();

		// West
		if(PacUtils.unoccupied(x-1 , y, grid))
			moves.add(new Point(x-1, y));

		// East
		if(PacUtils.unoccupied(x+1, y, grid))
			moves.add(new Point(x+1, y));

		// South
		if(PacUtils.unoccupied(x, y-1, grid))
			moves.add(new Point(x, y-1));

		// North
		if(PacUtils.unoccupied(x, y+1, grid))
			moves.add(new Point(x, y+1));

		return moves;
	}

	@Override
	public PacFace action(Object state){
		PacCell[][] grid = (PacCell[][]) state;
		PacmanCell pc = PacUtils.findPacman(grid);
		PacFace newFace = null;
		
		if(pc == null) return null;

		if(path.isEmpty()){
			
			State current = new State(pc.getLoc(), PacUtils.findGhosts(grid));
			
			System.out.println("Action Number" + ++actionNum);
			getStates(depth, grid, current, PacUtils.findGhosts(grid)); 
			System.out.println("\n");
			path = BFSPath.getPath(grid, pc.getLoc(), PacUtils.nearestFood(pc.getLoc(), grid));
		}	
		
		Point next = path.remove(0);
		newFace = PacUtils.direction(pc.getLoc(), next);

		
		return newFace;
	}
}
