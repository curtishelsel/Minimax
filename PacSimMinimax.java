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

	public List<Point> minimax(PacmanCell pc, PacCell[][] grid){
		
		System.out.println(getMoves(pc.getLoc().x, pc.getLoc().y, grid));
		Point p = PacUtils.nearestFood(pc.getLoc(), grid);
		return BFSPath.getPath(grid, pc.getLoc(), p);
	}

	// Method gets the list of possible moves from current location
	public List<Point> getMoves(int x, int y, PacCell[][] grid){
		
		List<Point> moves = new ArrayList();

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
			path = minimax(pc, grid); 
		}	
		
		Point next = path.remove(0);
		newFace = PacUtils.direction(pc.getLoc(), next);

		
		return newFace;
	}
}
