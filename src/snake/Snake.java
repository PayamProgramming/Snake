/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import javafx.scene.media.AudioClip;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.awt.Point;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
/**
 *
 * @author FaraDars
 */
public class Snake extends Application {
    private static final int WindowWidth=640;
    private static final int WindowHeight=640;
    private static final int Row=16;
    private static final int Column=16;
    private static final int CellSize=WindowHeight/Column;
    
    private List<Point> Body=new ArrayList();
    private Point Head;
    private Image TargetImage;
    private int Target_x;
    private int Target_y;
    
    private static final int Right=0;
    private static final int Left=1;
    private static final int Up=2;
    private static final int Down=3;
    private int MoveFlag;
    
    Timeline timeline;
    private boolean GameOver;
    private int Score=0;
    
    private GraphicsContext gc;
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Group root = new Group();
        Canvas c = new Canvas(WindowWidth,WindowHeight);
        root.getChildren().add(c);
        stage.setTitle("Snake");
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        
        for(int i=0;i<3;i++){
            Body.add(new Point(5,Row/2));
        }
        Head=Body.get(0);
        
        BuildTarget();
        
        gc=c.getGraphicsContext2D();
        
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code=event.getCode();
                if(code==KeyCode.RIGHT){
                    if(MoveFlag != Left){
                        MoveFlag=Right;
                    }
                }else if(code==KeyCode.LEFT){
                    if(MoveFlag != Right){
                        MoveFlag=Left;
                    }
                }else if(code==KeyCode.UP){
                     if(MoveFlag != Down){
                        MoveFlag=Up;
                    }
                }else if(code==KeyCode.DOWN){
                     if(MoveFlag != Up){
                        MoveFlag=Down;
                }
            }
            }
        });
        
        //runGame(gc);
        timeline = new Timeline(new KeyFrame(Duration.millis(130), e->runGame(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    private void windowbackground(GraphicsContext gc){
        for(int i=1;i<=Row;i++){
            for(int j=1;j<=Column;j++){
                if((i+j)%2==0){
                    gc.setFill(Color.YELLOW);
                }else{
                    gc.setFill(Color.BROWN);
                }
                gc.fillRect((i-1)*CellSize, (j-1)*CellSize, CellSize, CellSize);
            }
        }
    }
    
    private void runGame(GraphicsContext gc){
        windowbackground(gc);
        drawTarget(gc);
        drawSnake(gc);
        drawScore();
        
        for(int i=Body.size()-1;i>=1;i--){
            Body.get(i).x=Body.get(i-1).x;
            Body.get(i).y=Body.get(i-1).y;
        }
        
        switch(MoveFlag){
            case Right:
                moveRight();
                break;
            case Left:
                moveLeft();
                break;
            case Up:
                moveUp();
                break;
            case Down:
                moveDown();
                break;
        }
        
        eatTarget();
        gameOver();
        
        if(GameOver){
            AudioClip a=new AudioClip(this.getClass().getResource("2.wav").toString());
            a.play();
            gc.setFill(Color.OLIVE);
            gc.fillText(("Game Over"), WindowWidth/2, WindowHeight/2 , 900);
            timeline.pause();
        }
        
    }
    
    private void BuildTarget(){
        start:
        while(true){
            Target_x=(int)(Math.random()*Row);
            Target_y=(int)(Math.random()*Column);
            
            for(Point snake:Body){
                if(snake.getX()==Target_x && snake.getY()==Target_y){
                    continue start;
                }
            }
            TargetImage = new Image("/image/apple.png");
            break;
        }
    }
    
    private void drawTarget(GraphicsContext gc){
        gc.drawImage(TargetImage, Target_x*CellSize, Target_y*CellSize, CellSize, CellSize);
    }
    
    private void drawSnake(GraphicsContext gc){
        gc.setFill(Color.BLACK);
        gc.fillRoundRect(Head.getX()*CellSize, Head.getY()*CellSize, CellSize, CellSize, 35, 35);
        for(int i=1 ; i<Body.size();i++){
            gc.fillRoundRect(Body.get(i).getX()*CellSize, Body.get(i).getY()*CellSize, CellSize, CellSize, 20, 20);
        }
    }
    
    private void moveRight(){
        Head.x++;
    }
    
    private void moveLeft(){
        Head.x--;
    }
    
    private void moveUp(){
        Head.y--;
    }
    
    private void moveDown(){
        Head.y++;
    }
    
    private void eatTarget(){
        if(Head.getX()==Target_x && Head.getY()==Target_y){
            AudioClip a=new AudioClip(this.getClass().getResource("1.wav").toString());
            a.play();
            Body.add(new Point(-1,-1));
            BuildTarget();
            Score +=3;
        }
    }
    
    private void gameOver(){
        if(Head.x <0 || Head.y <0 || Head.x*CellSize >= WindowWidth || Head.y*CellSize >=WindowHeight){
            GameOver=true;
            //emtiaz=0;
        }
        
        for(int i=1;i<Body.size();i++){
            if(Head.x==Body.get(i).getX() && Head.y==Body.get(i).getY()){
                GameOver=true;
                //emtiaz=0;
                break;
            }
        }
    }
    
    private void drawScore(){
        gc.setFill(Color.WHITE);
        gc.fillText("Score: "+Score, 10, 33);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
