package trafficInfrastructure;

import graphicsLoader.GraphicsConfig;

import java.util.ArrayList;

import main.MainConfig;
import simulationBuilder.TrafficManager;
import trafficInfrastructure.grid.GridBuilder;
import trafficInfrastructure.road.BlockGraphicPoint;
import trafficInfrastructure.road.HorizontalBlock;
import trafficInfrastructure.road.IntersectionBlock;
import trafficInfrastructure.road.RoadConfig;
import trafficInfrastructure.road.VerticalBlock;
import trafficInfrastructure.roadPath.Path;
import trafficInfrastructure.roadPath.PathPoint;
import trafficInfrastructure.roadPath.VisitedIntersectionsOnPath;
import util.FileRW;

/**
 * RoadBuilder is for to create and edit road infrastructure
 * The result is saved in Components directory
 * 
 * @author Maxim Vasilishin
 * @version 1.0
 */

public class RoadBuilder {
	
	private String roadInfName;
	private GridBuilder roadGrid;
	private ArrayList<Path> arrPath; //TODO Move in LOCAL to the function
	private int currentX1;
	private int currentY1;
	private int currentX2 = 0; // for feature use
	private int currentY2 = 0; // for feature use
	
	
	public RoadBuilder (String roadInfName, int currentX, int currentY){
		this.roadInfName = roadInfName;
		this.currentX1 = currentX;
		this.currentY1 = currentY;
		this.roadGrid = new GridBuilder();
		this.arrPath = new ArrayList<Path>();
	}
	
	public void buildRoad(){
		this.addRoadBlock(RoadConfig.VERTICAL_ENTER_BLOCK);
		for (int i = 0; i < 14; i++){
			this.addRoadBlock(RoadConfig.VERTICAL_BLOCK);
		}
		this.addRoadBlock(RoadConfig.VERTICAL_EXIT_BLOCK);
		
		this.currentX1 = 500;
		this.currentY1 = 0;
		
		this.addRoadBlock(RoadConfig.VERTICAL_ENTER_BLOCK);
		for (int i = 0; i < 14; i++){
			this.addRoadBlock(RoadConfig.VERTICAL_BLOCK);
		}
		this.addRoadBlock(RoadConfig.VERTICAL_EXIT_BLOCK);
		
		
		this.currentX1 = 0;
		this.currentY1 = 150;
		
		this.addRoadBlock(RoadConfig.HORIZONTAL_ENTER_BLOCK);
		for (int i = 0; i < 14; i++){
			this.addRoadBlock(RoadConfig.HORIZONTAL_BLOCK);
		}
		this.addRoadBlock(RoadConfig.HORIZONTAL_EXIT_BLOCK);
		
		/*this.currentX1 = 0;
		this.currentY1 = 200;
		
		this.addRoadBlock(RoadConfig.HORIZONTAL_ENTER_BLOCK);
		for (int i = 0; i < 3; i++){
			this.addRoadBlock(RoadConfig.HORIZONTAL_BLOCK);
		}
		this.addRoadBlock(RoadConfig.HORIZONTAL_EXIT_BLOCK);
		*/
		
		
		this.currentX1 = 0;
		this.currentY1 = 500;
		
		this.addRoadBlock(RoadConfig.HORIZONTAL_ENTER_BLOCK);
		for (int i = 0; i < 14; i++){
			this.addRoadBlock(RoadConfig.HORIZONTAL_BLOCK);
		}
		this.addRoadBlock(RoadConfig.HORIZONTAL_EXIT_BLOCK);
		
		/*this.currentX1 = 0;
		this.currentY1 = 150;
		
		this.addRoadBlock(RoadConfig.HORIZONTAL_ENTER_BLOCK);
		for (int i = 0; i < 18; i++){
			this.addRoadBlock(RoadConfig.HORIZONTAL_BLOCK);
		}
		this.addRoadBlock(RoadConfig.HORIZONTAL_EXIT_BLOCK);
		*/
		
	for (int i = 0; i<GraphicsConfig.MAIN_WIDTH/GraphicsConfig.BLOCK_SIDE_SIZE; i++){
		for (int j = 0; j<GraphicsConfig.MAIN_HEIGHT/GraphicsConfig.BLOCK_SIDE_SIZE; j++){
			System.out.print(this.roadGrid.getGrid() [i] [j]);
		}
		System.out.print("\n");
	}
	FileRW.writeObject(this.roadGrid, MainConfig.GRID_PATH + "/" + this.roadInfName + MainConfig.GRID_SUFFIX);
	this.roadGraphicBuilder();
	this.getPaths();
	TrafficManager tm = new TrafficManager(this.roadInfName);	
		
	}
	/**
	 * @param blockNb
	 */
	public void addRoadBlock (short blockNb){ 
		
		if (blockNb == RoadConfig.HORIZONTAL_ENTER_BLOCK || blockNb == RoadConfig.HORIZONTAL_BLOCK || blockNb == RoadConfig.HORIZONTAL_EXIT_BLOCK){
			if (this.roadGrid.getGrid()[this.currentX1/GraphicsConfig.BLOCK_SIDE_SIZE][this.currentY1/GraphicsConfig.BLOCK_SIDE_SIZE] == RoadConfig.VERTICAL_BLOCK){
				this.roadGrid.addRoadBlock(RoadConfig.INTERSECTION_BLOCK, this.currentX1, this.currentY1);
			} else {
				this.roadGrid.addRoadBlock(blockNb, this.currentX1, this.currentY1);
			}
			int tab [] = HorizontalBlock.nextBlockLocation(this.currentX1, this.currentY1, this.currentX2, this.currentY2);
			this.currentX1 = tab[0];
			this.currentY1 = tab[1];
		}
		
		if (blockNb == RoadConfig.VERTICAL_BLOCK || blockNb == RoadConfig.VERTICAL_ENTER_BLOCK || blockNb == RoadConfig.VERTICAL_EXIT_BLOCK){
			if (this.roadGrid.getGrid()[this.currentX1/GraphicsConfig.BLOCK_SIDE_SIZE][this.currentY1/GraphicsConfig.BLOCK_SIDE_SIZE] == RoadConfig.HORIZONTAL_BLOCK){
				this.roadGrid.addRoadBlock(RoadConfig.INTERSECTION_BLOCK, this.currentX1, this.currentY1);
			} else {
				this.roadGrid.addRoadBlock(blockNb, this.currentX1, this.currentY1);
			}
			int tab [] = VerticalBlock.nextBlockLocation(this.currentX1, this.currentY1, this.currentX2, this.currentY2);
			this.currentX1 = tab[0];
			this.currentY1 = tab[1];
		}
	}
	
	public void roadGraphicBuilder(){
		ArrayList<BlockGraphicPoint> roadGB = new ArrayList<BlockGraphicPoint>();
		for (int i = 0; i<GraphicsConfig.MAIN_WIDTH/GraphicsConfig.BLOCK_SIDE_SIZE; i++){
			for (int j = 0; j<GraphicsConfig.MAIN_HEIGHT/GraphicsConfig.BLOCK_SIDE_SIZE; j++){
				if (this.roadGrid.getGrid()[i][j]!=0){
					roadGB.add(new BlockGraphicPoint(i*GraphicsConfig.BLOCK_SIDE_SIZE, j*GraphicsConfig.BLOCK_SIDE_SIZE, this.roadGrid.getGrid()[i][j]));
				}
			}
		}
		FileRW.writeObject(roadGB, MainConfig.ROADBLOCK_PATH + "/" + roadInfName + MainConfig.ROADBLOCK_GRAPHICS_SUFFIX);
		
	}
	
	public void getPaths (){
		 ArrayList <BlockGraphicPoint> endPoints = new ArrayList <BlockGraphicPoint>();
		 for (int i = 0; i<GraphicsConfig.MAIN_WIDTH/GraphicsConfig.BLOCK_SIDE_SIZE; i++){
				for (int j = 0; j<GraphicsConfig.MAIN_HEIGHT/GraphicsConfig.BLOCK_SIDE_SIZE; j++){
					if (this.roadGrid.getGrid()[i][j]==RoadConfig.HORIZONTAL_ENTER_BLOCK || this.roadGrid.getGrid()[i][j]==RoadConfig.HORIZONTAL_EXIT_BLOCK || this.roadGrid.getGrid()[i][j]==RoadConfig.VERTICAL_ENTER_BLOCK || this.roadGrid.getGrid()[i][j]==RoadConfig.VERTICAL_EXIT_BLOCK){
						endPoints.add(new BlockGraphicPoint(i, j, this.roadGrid.getGrid()[i][j]));
					}
				}
		 }
		 
		 for (BlockGraphicPoint blockGP : endPoints){
			 
			 ArrayList<PathPoint> arrPathPoint = new ArrayList<PathPoint>();
			 short direction = 0;
			 int x = blockGP.getX();
			 int y = blockGP.getY();
			 
			 int previousX = x;
			 int previousY = y; 
				
				if ( this.roadGrid.getGrid() [x] [y]== RoadConfig.HORIZONTAL_ENTER_BLOCK ){
					direction = 1;
					arrPathPoint.addAll(HorizontalBlock.getPathPoints(x*GraphicsConfig.BLOCK_SIDE_SIZE, y*GraphicsConfig.BLOCK_SIDE_SIZE, this.roadGrid.getGrid() [x] [y]));
					x++;
				}
				
				if (this.roadGrid.getGrid() [x] [y] == RoadConfig.VERTICAL_ENTER_BLOCK){
					direction = 1;
					arrPathPoint.addAll(VerticalBlock.getPathPoints(x*GraphicsConfig.BLOCK_SIDE_SIZE, y*GraphicsConfig.BLOCK_SIDE_SIZE, this.roadGrid.getGrid() [x] [y]));
					y++;
				}
				
				if ( this.roadGrid.getGrid() [x] [y]== RoadConfig.HORIZONTAL_EXIT_BLOCK) {
					direction = -1;
					arrPathPoint.addAll(HorizontalBlock.getInversePathPoints(x*GraphicsConfig.BLOCK_SIDE_SIZE-1, y*GraphicsConfig.BLOCK_SIDE_SIZE-1, this.roadGrid.getGrid() [x] [y]));
					x--;
				}
				if (this.roadGrid.getGrid() [x] [y] == RoadConfig.VERTICAL_EXIT_BLOCK){
					direction = -1;
					arrPathPoint.addAll(VerticalBlock.getInversePathPoints(x*GraphicsConfig.BLOCK_SIDE_SIZE-1, y*GraphicsConfig.BLOCK_SIDE_SIZE-1, this.roadGrid.getGrid() [x] [y]));
					y--;
				}
				
				this.discoverPaths(arrPathPoint, new VisitedIntersectionsOnPath(), direction, x, y, previousX , previousY);
				
		 }
		 
		 FileRW.writeObject(this.arrPath, MainConfig.PATHS_PATH + "/" + this.roadInfName + MainConfig.PATH_SUFFIX);
	}
	
	public void discoverPaths(ArrayList<PathPoint> arrPathPoint, VisitedIntersectionsOnPath viop, int direction, int x, int y ,int prevX,int prevY){
		int previousX = prevX;
		int previousY = prevY;
		while (this.roadGrid.getGrid() [x] [y]!= RoadConfig.HORIZONTAL_ENTER_BLOCK && this.roadGrid.getGrid() [x] [y] != RoadConfig.VERTICAL_ENTER_BLOCK && this.roadGrid.getGrid() [x] [y]!= RoadConfig.HORIZONTAL_EXIT_BLOCK && this.roadGrid.getGrid() [x] [y] != RoadConfig.VERTICAL_EXIT_BLOCK){
			System.out.println(x + " " + y +" "+ this.roadGrid.getGrid() [x] [y]);
			
			
			if ( this.roadGrid.getGrid() [x] [y] == RoadConfig.HORIZONTAL_BLOCK){
				previousX = x;
				previousY = y;
				if (direction == 1){
					arrPathPoint.addAll(HorizontalBlock.getPathPoints(x*GraphicsConfig.BLOCK_SIDE_SIZE, y*GraphicsConfig.BLOCK_SIDE_SIZE, this.roadGrid.getGrid() [x] [y]));
					x++;
					
					
	
				} else {
					arrPathPoint.addAll(HorizontalBlock.getInversePathPoints(x*GraphicsConfig.BLOCK_SIDE_SIZE-1, y*GraphicsConfig.BLOCK_SIDE_SIZE-1, this.roadGrid.getGrid() [x] [y]));
					x--;
					
				}
			} else if (this.roadGrid.getGrid() [x] [y] == RoadConfig.VERTICAL_BLOCK){
				
				previousX = x;
				previousY = y;
				
				if (direction == 1){
					arrPathPoint.addAll(VerticalBlock.getPathPoints(x*GraphicsConfig.BLOCK_SIDE_SIZE, y*GraphicsConfig.BLOCK_SIDE_SIZE, this.roadGrid.getGrid() [x] [y]));
					y++;
					
					
					
					
				} else {
					arrPathPoint.addAll(VerticalBlock.getInversePathPoints(x*GraphicsConfig.BLOCK_SIDE_SIZE-1, y*GraphicsConfig.BLOCK_SIDE_SIZE-1, this.roadGrid.getGrid() [x] [y]));
					y--;
					
					
					
				}
			} 
			
			else if (this.roadGrid.getGrid() [x][y] == RoadConfig.INTERSECTION_BLOCK){
				if (viop.isVisited(x, y)){
					break;
				}
				else {
					viop.addIntersection(x, y);
				}
				System.out.println("intersection detected");
				if (this.roadGrid.getGrid()[previousX][previousY] == RoadConfig.HORIZONTAL_BLOCK || this.roadGrid.getGrid()[previousX][previousY] == RoadConfig.HORIZONTAL_ENTER_BLOCK || this.roadGrid.getGrid()[previousX][previousY] == RoadConfig.HORIZONTAL_EXIT_BLOCK){
					
					previousX = x;
					previousY = y;
					
					if (direction == 1){
						ArrayList<PathPoint> arr1 = new ArrayList<PathPoint>();
						arr1.addAll(arrPathPoint);
						arr1.addAll(IntersectionBlock.getLeftToBottomPath(x*GraphicsConfig.BLOCK_SIDE_SIZE,y*GraphicsConfig.BLOCK_SIDE_SIZE, this.roadGrid.getGrid() [x] [y]));
						ArrayList<PathPoint> arr2 = new ArrayList<PathPoint>();
						arr2.addAll(arrPathPoint);
						arr2.addAll(IntersectionBlock.getLeftToTopPath(x*GraphicsConfig.BLOCK_SIDE_SIZE,y*GraphicsConfig.BLOCK_SIDE_SIZE, this.roadGrid.getGrid() [x] [y]));
						this.discoverPaths(arr1,viop, 1, x, y+1, previousX , previousY);
						this.discoverPaths(arr2,viop, -1, x, y-1, previousX , previousY);
						arrPathPoint.addAll(HorizontalBlock.getPathPoints(x*GraphicsConfig.BLOCK_SIDE_SIZE, y*GraphicsConfig.BLOCK_SIDE_SIZE, RoadConfig.HORIZONTAL_BLOCK));
						x++;
						
						
						
					}
					
					else {
						ArrayList<PathPoint> arr1 = new ArrayList<PathPoint>();
						arr1.addAll(arrPathPoint);
						arr1.addAll(IntersectionBlock.getRightToBottomPath(x*GraphicsConfig.BLOCK_SIDE_SIZE,y*GraphicsConfig.BLOCK_SIDE_SIZE, this.roadGrid.getGrid()[x] [y]));
						ArrayList<PathPoint> arr2 = new ArrayList<PathPoint>();
						arr2.addAll(arrPathPoint);
						arr2.addAll(IntersectionBlock.getRightToTopPath(x*GraphicsConfig.BLOCK_SIDE_SIZE,y*GraphicsConfig.BLOCK_SIDE_SIZE, this.roadGrid.getGrid()[x] [y]));
						this.discoverPaths(arr1,viop, 1, x, y+1, previousX , previousY);
						this.discoverPaths(arr2,viop, -1, x, y-1, previousX , previousY);
						arrPathPoint.addAll(HorizontalBlock.getInversePathPoints(x*GraphicsConfig.BLOCK_SIDE_SIZE-1, y*GraphicsConfig.BLOCK_SIDE_SIZE-1, RoadConfig.HORIZONTAL_BLOCK));
						x--;
						
						
					}
					
					
					
				} else
				
				if (this.roadGrid.getGrid()[previousX][previousY] == RoadConfig.VERTICAL_BLOCK || this.roadGrid.getGrid()[previousX][previousY] == RoadConfig.VERTICAL_ENTER_BLOCK || this.roadGrid.getGrid()[previousX][previousY] == RoadConfig.VERTICAL_EXIT_BLOCK){
					
					previousX = x;
					previousY = y;
					
					if (direction == 1){
						ArrayList<PathPoint> arr1 = new ArrayList<PathPoint>();
						arr1.addAll(arrPathPoint);
						arr1.addAll(IntersectionBlock.getTopToRightPath(x*GraphicsConfig.BLOCK_SIDE_SIZE,y*GraphicsConfig.BLOCK_SIDE_SIZE, this.roadGrid.getGrid()[x] [y]));
						ArrayList<PathPoint> arr2 = new ArrayList<PathPoint>();
						arr2.addAll(arrPathPoint);
						arr2.addAll(IntersectionBlock.getTopToLeftPath(x*GraphicsConfig.BLOCK_SIDE_SIZE,y*GraphicsConfig.BLOCK_SIDE_SIZE, this.roadGrid.getGrid()[x] [y]));
						this.discoverPaths(arr1, viop, 1, x+1, y, previousX , previousY);
						this.discoverPaths(arr2, viop, -1, x-1, y, previousX , previousY);
						arrPathPoint.addAll(VerticalBlock.getPathPoints(x*GraphicsConfig.BLOCK_SIDE_SIZE, y*GraphicsConfig.BLOCK_SIDE_SIZE, RoadConfig.VERTICAL_BLOCK));
						y++;
						
						
						
					} else {
						ArrayList<PathPoint> arr1 = new ArrayList<PathPoint>();
						arr1.addAll(arrPathPoint);
						arr1.addAll(IntersectionBlock.getBottomToRightPath(x*GraphicsConfig.BLOCK_SIDE_SIZE,y*GraphicsConfig.BLOCK_SIDE_SIZE, this.roadGrid.getGrid()[x] [y]));
						ArrayList<PathPoint> arr2 = new ArrayList<PathPoint>();
						arr2.addAll(arrPathPoint);
						arr2.addAll(IntersectionBlock.getBottomToLeftPath(x*GraphicsConfig.BLOCK_SIDE_SIZE,y*GraphicsConfig.BLOCK_SIDE_SIZE, this.roadGrid.getGrid()[x] [y]));
						this.discoverPaths(arr1, viop, 1, x+1, y, previousX , previousY);
						this.discoverPaths(arr2, viop, -1, x-1, y, previousX , previousY);
						arrPathPoint.addAll(VerticalBlock.getInversePathPoints(x*GraphicsConfig.BLOCK_SIDE_SIZE-1, y*GraphicsConfig.BLOCK_SIDE_SIZE-1, RoadConfig.VERTICAL_BLOCK));
						y--;
						
					}
					
				}
			}
			
			
			
			
		}
		
		
		if ( this.roadGrid.getGrid() [x] [y]== RoadConfig.HORIZONTAL_ENTER_BLOCK || this.roadGrid.getGrid() [x] [y]== RoadConfig.HORIZONTAL_EXIT_BLOCK){
			if (direction == 1){
				arrPathPoint.addAll(HorizontalBlock.getPathPoints(x*GraphicsConfig.BLOCK_SIDE_SIZE, y*GraphicsConfig.BLOCK_SIDE_SIZE, this.roadGrid.getGrid() [x] [y]));
			} else {
				arrPathPoint.addAll(HorizontalBlock.getInversePathPoints(x*GraphicsConfig.BLOCK_SIDE_SIZE-1, y*GraphicsConfig.BLOCK_SIDE_SIZE-1, this.roadGrid.getGrid() [x] [y]));
			}
			
			this.arrPath.add(new Path(arrPathPoint));
		}
		if ( this.roadGrid.getGrid() [x] [y]== RoadConfig.VERTICAL_ENTER_BLOCK ||this.roadGrid.getGrid() [x] [y]== RoadConfig.VERTICAL_EXIT_BLOCK){
			if (direction == 1){
				arrPathPoint.addAll(VerticalBlock.getPathPoints(x*GraphicsConfig.BLOCK_SIDE_SIZE, y*GraphicsConfig.BLOCK_SIDE_SIZE, this.roadGrid.getGrid() [x] [y]));
			} else {
				arrPathPoint.addAll(VerticalBlock.getInversePathPoints(x*GraphicsConfig.BLOCK_SIDE_SIZE-1, y*GraphicsConfig.BLOCK_SIDE_SIZE-1, this.roadGrid.getGrid() [x] [y]));
			}
			
			this.arrPath.add(new Path(arrPathPoint));
		}
		
		
		for (PathPoint p : arrPathPoint){
			System.out.print(p.getX()+" ");
		}
		System.out.print("\n");
		
		for (PathPoint p : arrPathPoint){
			System.out.print(p.getY()+" ");
		}
		System.out.print("\n");
		System.out.print("Path\n");
		
		
	}
	

}