import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.layout.VBox;

public class PainterController 
{
    private enum PenSize
    {
        SMALL(2),
        MEDIUM(4),
        LARGE(6);
        private final int radius;
        PenSize(int radius) {this.radius = radius;}
        public int getRadius() {return radius;}
    };
    

    @FXML
    private RadioButton blackRadioButton;
    
    @FXML
    private RadioButton redRadioButton;
    
    @FXML
    private RadioButton greenRadioButton;
    
    @FXML
    private RadioButton blueRadioButton;
    
    
    
    @FXML
    private RadioButton smallRadioButton;
    
    @FXML
    private RadioButton mediumRadioButton;    

    @FXML
    private RadioButton largeRadioButton;
    
    
    
    @FXML
    private RadioButton lineRadioButton;
    
    @FXML
    private RadioButton circleRadioButton;
    
    @FXML
    private RadioButton rectangleRadioButton;
    
    @FXML
    private RadioButton fillShapeRadioButton;
    
    @FXML
    private RadioButton dontFillShapeRadioButton;

    
    
    @FXML
    private ToggleGroup colorToggleGroup;
    
    @FXML
    private ToggleGroup sizeToggleGroup;

    @FXML
    private ToggleGroup shapeToggleGroup;
    
    @FXML
    private ToggleGroup fillShapeToggleGroup;
    
    

    @FXML
    private Pane drawingAreaPane;
    
    @FXML
    private VBox vbox;
    
    private double startX, startY;
    private boolean fillShape = true;
    private Shape shape, tempShape;
    private PenSize radius = PenSize.MEDIUM;
    private Paint brushColor = Color.BLACK;
    
    /*
     * Sets up the radio buttons accordingly.
     */
    public void initialize()
    {
        blackRadioButton.setUserData(Color.BLACK);
        redRadioButton.setUserData(Color.RED);
        greenRadioButton.setUserData(Color.GREEN);
        blueRadioButton.setUserData(Color.BLUE);
        smallRadioButton.setUserData(PenSize.SMALL);
        mediumRadioButton.setUserData(PenSize.MEDIUM);
        largeRadioButton.setUserData(PenSize.LARGE);
        circleRadioButton.setUserData(new Circle());
        rectangleRadioButton.setUserData(new Rectangle());
        lineRadioButton.setUserData(new Line());
        fillShapeRadioButton.setUserData(true);
        dontFillShapeRadioButton.setUserData(false);
    }
    
    /*
     * This function is activated when the mouse is dragged on the drawing pane.
     */
    @FXML
    void drawingAreaMouseDragged(MouseEvent event)
    {
        /*
         * Creates a shape with the starting x and y, when user started dragging the mouse, with the mouse's current position.
         */
        if (shape instanceof Circle)
        {
            shape = (Circle) createCircle(startX, startY, event.getX(), event.getY());   
        }
        else if (shape instanceof Rectangle)
        {
            shape = (Rectangle) createRectangle(startX, startY, event.getX(), event.getY()); 
        }
        else
        {
            shape = (Line) createLine(startX, startY, event.getX(), event.getY());
            shape.setStrokeWidth(radius.getRadius());
            shape.setStroke(brushColor);
        }
        
        /*
         * Colors the shape if it fill shape radio button has been selected. Works for rectangles and circles.
         */
        if (fillShape)
        {
            shape.setFill(brushColor);
        }
        else
        {
            shape.setStroke(brushColor);
            shape.setFill(Color.WHITE);
            shape.setStrokeWidth(radius.getRadius());
        } 
        
        /*
         * Adds the current shape to the drawing pane, removes the previous shape and saves the current shape to temp shape.
         * This is to avoid a series of consecutive shapes, of the same type, that will be created and added during the dragging of the mouse. 
         */        
        drawingAreaPane.getChildren().add(shape);
        drawingAreaPane.getChildren().remove(tempShape);
        tempShape = shape;
    }
    
    /*
     * This function is activated when the mouse has been released.
     */
    @FXML
    void onMouseReleased(MouseEvent event) 
    {
        // Resets temp shape for the next shape to be painted.
        tempShape = null;
    }
    
    /*
     * Creates a rectangle with the mouse starting position and its current position.
     */
    private Rectangle createRectangle(double startX, double startY, double endX, double endY)
    {
        double x = Math.min(startX,endX);
        double y = Math.min(startY,endY);
        x = Math.max(x,vbox.getPrefWidth());
        return new Rectangle(x,y,Math.abs(endX-startX),Math.abs(endY-startY));
    }
    
    /*
     * Creates a circle with the mouse starting position and its current position.
     * Returns an empty circle if mouse current position collides with the menu.
     */
    private Circle createCircle(double startX, double startY, double endX, double endY)
    {
        double r = Math.sqrt(Math.pow((startX-endX),2) + Math.pow((startY-endY),2));
        if (Math.abs(startX-vbox.getPrefWidth()) <= r)
        {
            return new Circle();
        }
        return new Circle(startX,startY,r);
    }
    
    /*
     * Creates a circle with the mouse starting position and its current position.
     * Returns an empty line if mouse current or starting position collides with the menu.
     */
    private Line createLine(double startX, double startY, double endX, double endY)
    {
        if (startX < vbox.getPrefWidth() || endX < vbox.getPrefWidth())
        {
            return new Line();
        }
        return new Line(startX,startY,endX,endY);
    }

    /*
     * Gets data from user on shape color.
     */
    @FXML
    void colorRadioButtonSelected(ActionEvent event) 
    {
        brushColor = (Color) colorToggleGroup.getSelectedToggle().getUserData();
    }

    /*
     * Gets data from user on line thickness.
     */
    @FXML
    void sizeRadioButtonSelected(ActionEvent event) 
    {
        radius = (PenSize) sizeToggleGroup.getSelectedToggle().getUserData();
    }
    
    /*
     * Gets data from user on which shape to paint and resets temp shape.
     */
    @FXML
    void shapeRadioButtonSelected(ActionEvent event) 
    {
        shape = (Shape) shapeToggleGroup.getSelectedToggle().getUserData();
        tempShape = null;
    }
    
    /*
     * Gets data from user whether to fill the next shapes or not.
     */
    @FXML
    void fillShapeRadioButtonSelected(ActionEvent event)
    {
        fillShape = (boolean) fillShapeToggleGroup.getSelectedToggle().getUserData();
    }
    
    /*
     * Removes all shapes.
     */
    @FXML
    void clearButtonPressed(ActionEvent event) 
    {
        drawingAreaPane.getChildren().clear();
    }
    
    /*
     * Saves the position of the mouse when it has been pressed.
     */
    @FXML
    void onMousePressed(MouseEvent event) 
    {
        startX = event.getX();
        startY = event.getY();
    }

    /*
     * Removes the last shape.
     */
    @FXML
    void undoButtonPressed(ActionEvent event) 
    {
        int count = drawingAreaPane.getChildren().size();
        if (count > 0)
        {
            drawingAreaPane.getChildren().remove(count-1);
        }
    }

}
