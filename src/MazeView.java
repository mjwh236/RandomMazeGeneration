import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MazeView extends JFrame{

    private JPanel mazePanel, rowPanel, colPanel, sliderPanel, buttonPanel;
    private JPanel controlPanel;
    private JButton StartButton, StopButton, GenerateButton;
    private JTextField Rows, Columns;
    private JSlider AnimationSpeed;
    private JLabel PercentVisited, RowLabel, ColumnLabel, SliderLabel;
    private boolean animating = false;
    int row = 0, col = 0, timerCount = 0, waitTime = 15, numBackTracked = 0;
    ArrayList<ArrayList<MazeModel.Cell>> graph = new ArrayList<>();
    ArrayList<MazeModel.Cell> path = new ArrayList<>();
    ArrayList<MazeModel.Cell> cellsVisited = new ArrayList<>();
    MazeModel.Cell currentInPath;
    javax.swing.Timer timer = new Timer(150,null);
    Boolean renderPathFlag = false, renderMazeFlag = false;
    Rectangle mazeBorder =  new Rectangle();

    MazeView(){
        super("Maze");
        addWindowListener (
                new WindowAdapter() {
                    public void windowClosing ( WindowEvent e) {
                        System.exit(0);
                    }
                }
        );

        mazePanel = new JPanel();
        controlPanel = new JPanel();
        rowPanel = new JPanel();
        colPanel = new JPanel();
        sliderPanel = new JPanel();
        buttonPanel = new JPanel();
        StartButton = new JButton("Start");
        StopButton = new JButton("Stop");
        GenerateButton = new JButton("Generate");
        RowLabel = new JLabel("Rows:");
        ColumnLabel = new JLabel("Columns:");
        PercentVisited = new JLabel("Visited: 0.0");
        SliderLabel = new JLabel("Speed");
        Rows = new JTextField(10);
        Columns = new JTextField(10);
        AnimationSpeed = new JSlider(4,16,10);

        timerCount = 0;

        GridBagConstraints c = new GridBagConstraints();

        setLayout(new GridBagLayout());

        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 720;
        c.ipadx = 720;
        add(mazePanel, c);
        mazePanel.setBackground(Color.BLACK);
        c.ipady = 0;
        c.ipadx = 0;
        buttonPanel.add(StartButton);
        buttonPanel.add(StopButton);
        buttonPanel.add(GenerateButton);
        buttonPanel.setLayout(new GridLayout(1,3));
        controlPanel.add(buttonPanel);
        rowPanel.add(RowLabel);
        rowPanel.add(Rows);
        colPanel.add(ColumnLabel);
        colPanel.add(Columns);
        sliderPanel.add(SliderLabel);
        sliderPanel.add(AnimationSpeed);
        controlPanel.add(rowPanel);
        controlPanel.add(colPanel);
        controlPanel.add(sliderPanel);
        controlPanel.add(PercentVisited);
        controlPanel.setLayout(new GridLayout(5,1));

        c.gridx = 3;
        c.gridy = 0;
        add(controlPanel,c);
        setSize(1600,900);
        setBackground(Color.black);
        setVisible(true);
    }

    // accessors
    public JButton getStartButton(){
        return StartButton;
    }
    public JButton getStopButton(){
        return StopButton;
    }
    public JButton getGenerateButton(){
        return GenerateButton;
    }
    public JSlider getAnimationSpeed(){
        return AnimationSpeed;
    }
    public int getRows(){
        if(Integer.parseInt(Rows.getText()) >= 10 && Integer.parseInt(Rows.getText()) <= 50){
            return Integer.parseInt(Rows.getText());
        } else {
            return 10;
        }
    }
    public int getCols(){
        if(Integer.parseInt(Columns.getText()) >= 10 && Integer.parseInt(Rows.getText()) <= 50){
            return Integer.parseInt(Columns.getText());
        } else {
            return 10;
        }
    }

    // mutators
    public void startAnimation(){
        animating = true;
    }
    public void stopAnimation(){
        animating = false;
    }
    public void setPercentVisited(float p){
        PercentVisited.setText("Visited: " + p + "%");
    }
    public void requestMaze(ArrayList<ArrayList<MazeModel.Cell>> g){
        graph = g;
    }
    public void requestPath(ArrayList<MazeModel.Cell> p){
        path = p;
        cellsVisited = new ArrayList<>();
        numBackTracked = 0;
    }
    public void setRenderPathFlag(){
        waitTime = (AnimationSpeed.getValue()*(5000/path.size())/4);

        timer = new Timer(waitTime,null);

        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                waitTime = AnimationSpeed.getValue();
                if(animating){
                    if(timerCount == path.size() - 1 || path.get(timerCount) == graph.get(getRows() - 1).get(getCols() - 1)){
                        stopAnimation();
                    }
                    currentInPath = path.get(timerCount);
                    timerCount++;
                    repaint();

                } else {
                    timer.stop();
                    setPercentVisited(100 * (cellsVisited.size() - numBackTracked)/(getRows()*getCols()));
                    revalidate();
                }
            }
        });
        renderPathFlag = true;
        currentInPath = path.get(0);
    }
    public void setRenderMazeFlag(){
        renderMazeFlag = true;

        timerCount = 0;
        currentInPath = null;
    }

    public void drawMaze(Graphics graphics,ArrayList<ArrayList<MazeModel.Cell>> g ) {
        int xOffset = 10, yOffset = 25;
        mazeBorder =  new Rectangle(mazePanel.getX() + xOffset,mazePanel.getY() + yOffset,mazePanel.getWidth() - xOffset - 25,mazePanel.getHeight() - yOffset);

        graphics.setColor(Color.BLACK);
        // background
        graphics.fillRect(mazePanel.getX(),mazePanel.getY(), mazePanel.getWidth(), mazePanel.getHeight());
        graphics.setColor(Color.WHITE);
        graphics.drawRect(mazeBorder.x,mazeBorder.y ,mazeBorder.width,mazeBorder.height);
        int width = (int) (mazeBorder.getWidth()/getCols()), height = (int) (mazeBorder.getHeight()/getRows());
        if(width > height){
            width = height;
        } else {
            height = width;
        }

        graphics.setColor(Color.GREEN);
        graphics.fillRect(mazeBorder.x,mazeBorder.y,width,height);
        graphics.setColor(Color.RED);
        graphics.fillRect(mazeBorder.x + ((getRows() - 1 ) * width),mazeBorder.y + ((getCols() - 1) * height), width, height);
        graphics.setColor(Color.white);
        // paints the walls for each cell
        for(int i = 0; i < path.size(); i++){
                    MazeModel.Cell cell = path.get(i);
                    int xStart = (width * cell.x)  + mazePanel.getX() + xOffset, yStart = (height * cell.y)  + mazePanel.getY() + yOffset;
                    if(cell.getWalls()[0]) { // draw north wall
                        graphics.drawLine(xStart,yStart,xStart + width,yStart);
                    }
                    repaint();
                    if(cell.getWalls()[1]) { // draw east wall
                        graphics.drawLine(xStart + width, yStart, xStart + width, yStart + height);
                    }
                    repaint();
                    if(cell.getWalls()[2]) { // draw south wall
                        graphics.drawLine(xStart, yStart + height, xStart + width, yStart + height);
                    }
                    repaint();
                    if(cell.getWalls()[3]) { // draw west wall
                        graphics.drawLine(xStart,yStart,xStart,yStart + height);
                    }
                    repaint();
                try {
                    TimeUnit.MILLISECONDS.sleep((5000/path.size()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }

    // function that renders each cell
    public void drawCell(Graphics graphics,MazeModel.Cell c){
        int width = (int) (mazeBorder.getWidth()/getCols()), height = (int) (mazeBorder.getHeight()/getRows());
        if(width > height){
            width = height;
        } else {
            height = width;
        }
        int xOffset = width/4, yOffset = height/4;

        int xStart = (int) ((width * c.x)  + mazeBorder.getX() + xOffset), yStart = (int) ((height * c.y)  + mazeBorder.getY() + yOffset);
        graphics.setColor(Color.yellow);

        int numWalls = 0;
        for(int i = 0 ; i < 4; i++){
            if(c.getWalls()[i]){
                numWalls++;
            }
        }
        if(cellsVisited.contains(c) || numWalls == 3){
            graphics.setColor(Color.gray);
        } else {
            graphics.setColor(Color.yellow);
            cellsVisited.add(c);
        }
        graphics.fillRect(xStart, yStart, width/2 , height/2 );

    }
    public void paint(Graphics graphics){
        if(graph.size() > 0){
            if(renderMazeFlag){
                drawMaze(graphics, graph);
                renderMazeFlag = false;
            }
            if(renderPathFlag){
                drawCell(graphics,currentInPath);
            }
        }
    }
}