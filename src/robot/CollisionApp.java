package robot;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CollisionApp extends Application{

	
	private GraphicsContext g;
	private Entity e1, e2;
	
	private Parent createContent(){
		Pane root = new Pane();
		Canvas canvas = new Canvas (600, 600);
		g = canvas.getGraphicsContext2D();
		
		root.getChildren().add(canvas);
		
		e1 = new Entity(200, 100, 40, 40);
		e2 = new Entity(200, 200, 50, 50);
		
		e2.setRotate(49);
		
		render();
		return root;
		
	}
	
	private void render(){
	g.clearRect(0,  0, 600, 600);
	e1.draw(g);
	e2.draw(g);
	g.strokeText(e1.isColliding(e2)?"Collision": "No Collision", 500, 50);		
	}
	
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(createContent());
		
		scene.setOnKeyPressed(e -> {
			if (e.getCode () == KeyCode.W){
				e1.changeY(-5);
			} else if (e.getCode () == KeyCode.S){
				e1.changeY(5);
			} else if (e.getCode () == KeyCode.A){
				e1.changeX(-5);
			} else if (e.getCode () == KeyCode.D){
				e1.changeX(5);
			}else if (e.getCode () == KeyCode.X){
				e2.setRotate(e2.getRotate()+5);
			}else if (e.getCode () == KeyCode.C){
				e2.changeX(-5);
			}
			render();
		});
		primaryStage.setTitle("collisions");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}
	
}
