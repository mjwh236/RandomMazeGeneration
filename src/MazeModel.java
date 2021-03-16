import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/* Michael White
 * CS335
 * 11/8/2020
 * MazeModel.java
 * This class stores the data for the maze and preforms any
 * needed calculations on that data
 */
public class MazeModel {
    private int rows = 0,columns = 0, numVisited = 0;
    Stack<Cell> stack = new Stack<>();
    ArrayList<Cell> renderPath = new ArrayList<>();
    ArrayList<ArrayList<Cell>>  graph = new ArrayList<>();
    MazeModel(){
    }
    public void generateMaze(){
        // stack based dfs to generate maze
        createGraph();
        renderPath = new ArrayList<>();
        graph.get(0).get(0).setVisited(); // starting cell
        stack.push(graph.get(0).get(0));
        while(!stack.isEmpty()){
            Cell currentCell = stack.peek();
            stack.pop();
            currentCell.unvisitedAdjacent();
            if(currentCell.unvisitedAdj.size() > 0){
                stack.push(currentCell);
                Random rand = new Random();
                Cell nextCell = currentCell.unvisitedAdj.get(rand.nextInt(currentCell.unvisitedAdj.size()));
                currentCell.removeWall(nextCell);
                nextCell.setVisited();
                renderPath.add(currentCell);
                stack.push(nextCell);
            }
        }
    }

    // method to solve the maze with backtracking
    public void soveMazeWithBackTrack(){
        Stack<Cell> s = new Stack<Cell>();
        renderPath = new ArrayList<>();
        ArrayList<Cell> visited = new ArrayList<>();
        s.push(graph.get(0).get(0));
        while (!s.isEmpty()){
            Cell v = s.peek();
            visited.add(v);
            boolean deadEnd = true;
            if(v.getX() == graph.size() - 1 && v.getY() == graph.get(0).size() - 1){
                System.out.println("Found exit");
                break;
            }
            for(int i = 0; i < v.getWalls().length; i++){
                if(v.adj[i] != null ) {
                    if (v.getWalls()[i] == false &&  !visited.contains(v.adj[i])) {
                        System.out.println("Cell: " + v.adj[i].getX() + " " + v.adj[i].getY());
                        deadEnd = false;
                        s.push(v.adj[i]);
                        renderPath.add(v);
                        break;
                    }
                }
            }
            if(deadEnd){ // dead end
                s.pop();
                renderPath.add(v);
            }
        }
    }
    public void createGraph(){
        graph = new ArrayList<>();
        for(int i = 0; i < rows; i++){
            ArrayList<Cell> r = new ArrayList<>();
            for(int j = 0; j < columns; j++){
               r.add(new Cell(i,j));
            }
            graph.add(r);
        }
        for(int i = 0; i < graph.size(); i++){
            for(int j = 0; j < graph.get(i).size(); j++){
                graph.get(i).get(j).findAdj();
            }
        }
    }
    // accessors
    public double getPercentVisited(){
        return (numVisited/(rows*columns));
    }
    // helper function used in debugging graph based dfs
    public void printGraph(){
        for (int i = 0; i < graph.size(); i++){
            for (int j = 0; j < graph.get(i).size(); j++){
                System.out.println("(" + graph.get(i).get(j).getX() + "," + graph.get(i).get(j).getY() + ")");
                System.out.println("Adj Length: " + graph.get(i).get(j).getAdj().length);
                System.out.println("Walls: ");
                for(int k = 0; k < graph.get(i).get(j).getWalls().length; k++){
                    System.out.println(graph.get(i).get(j).getWalls()[k]);
                }
                if(!graph.get(i).get(j).isVisited()){
                    System.out.println("Unvisited cell: " + "(" + graph.get(i).get(j).getX() + "," + graph.get(i).get(j).getY() + ")");
                }
            }
        }
    }
    public ArrayList<ArrayList<Cell>> getGraph(){
        return graph;
    }
    public ArrayList<Cell> getSolvedPath(){
        return renderPath;
    }
    // Mutators
    public void setRows(int r){
        rows = r;
    }
    public void setColumns(int c){
        columns = c;
    }

    // helper class to hold information about each cell of the maze
    public class Cell{
        int x,y;
        private ArrayList<Cell> unvisitedAdj = new ArrayList<>();
        private Cell[] adj = new Cell[4];
        boolean[] walls = {true, true, true, true};
        boolean visited = false;
        boolean active = true;
        Cell(int a, int b){
            x = a;
            y = b;
        }
        private int getX(){
            return x;
        }
        private int getY(){
            return y;
        }
        public boolean[] getWalls(){
            return walls;
        }
        public Cell[] getAdj(){
            return adj;
        }
        private boolean isVisited(){
            return visited;
        }
        private void setVisited(){
            visited = true;
        }
        private void resetVisited(){
            visited = false;
        }
        private void findAdj(){
            if(y - 1 > 0){ // north
                adj[0] = graph.get(x).get(y - 1);
            }
            if(x + 1 < rows){ // east
                adj[1] = graph.get(x + 1).get(y);
            }
            if(y + 1 < columns){ // south
                adj[2] = graph.get(x).get(y + 1);
            }
            if(x - 1 > 0){ // west
                adj[3] = graph.get(x - 1).get(y);
            }
        }
        private void unvisitedAdjacent(){
            unvisitedAdj = new ArrayList<>();
            for(int i = 0; i < adj.length; i++){
                if(adj[i]!= null && !adj[i].isVisited()){
                    //System.out.println(adj[i].x + " "+ adj[i].x);
                    unvisitedAdj.add(adj[i]);
                }
            }
        }
        // remove the wall between two cells
        private void removeWall(Cell c){
            int x = this.getX() - c.getX();
            int y = this.getY() - c.getY();
            if(x == -1){
                walls[1] = false;
                c.walls[3] = false;
            }
            if(x == 1){
                walls[3] = false;
                c.walls[1] = false;
            }
            if(y == 1){
                walls[0] = false;
                c.walls[2] = false;
            }
            if(y == -1){
                walls[2] = false;
                c.walls[0] = false;
            }
        }
    }
}
