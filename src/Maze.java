/* Michael White
 * CS335
 * 11/8/2020
 * Main Driver class for Maze project
 * Bugs:
 *      Sometimes when running there will be a graphical glitch and the program must be restarted
 *      Sometimes cells on the outer edges of the maze will not render their walls but the solver still sees them
 *      When stopping then starting the solve the percent visited is incorrect
 */
public class Maze {

    public static void main(String[] args){
        MazeModel mazeModel = new MazeModel();
        MazeView mazeView = new MazeView();
        MazeController mazeController = new MazeController(mazeModel, mazeView);
    }
}
