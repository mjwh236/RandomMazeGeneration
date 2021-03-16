import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* Michael White
 * CS335
 * 11/8/2020
 * MazeController.java
 * This class is an interface between the model and view classes
 */
public class MazeController {
    private MazeModel Model;
    private MazeView View;
    MazeController(MazeModel m, MazeView v){
        Model = m;
        View = v;
        addListeners();
    }
    public void addListeners(){
        View.getStartButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Model.soveMazeWithBackTrack();
                if(View.cellsVisited.size() == 0){
                    View.requestPath(Model.getSolvedPath());
                }
                View.setRenderPathFlag();
                View.startAnimation();
                View.timer.start();
            }
        });
        View.getStopButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                View.stopAnimation();
            }
        });
        View.getGenerateButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                View.setPercentVisited(0);
                View.revalidate();
                System.out.println("Generating");

                Model.setRows(View.getRows());
                Model.setColumns(View.getCols());
                Model.generateMaze();

                View.requestMaze(Model.getGraph());
                View.requestPath(Model.getSolvedPath());
                View.setRenderMazeFlag();
                View.startAnimation();

                View.repaint();
                View.stopAnimation();
            }
        });
    }
}
