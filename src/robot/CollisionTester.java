package robot;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CollisionTester extends Application {


    private ArrayList<Rectangle> rectangleArrayList;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("The test");
        Group root = new Group();
        Scene scene = new Scene(root, 400, 400);

        rectangleArrayList = new ArrayList<Rectangle>();
        rectangleArrayList.add(new Rectangle(30.0, 30.0, Color.GREEN));
        rectangleArrayList.add(new Rectangle(30.0, 30.0, Color.RED));
        rectangleArrayList.add(new Rectangle(30.0, 30.0, Color.CYAN));
        for(Rectangle block : rectangleArrayList){
            setDragListeners(block);
        }
        root.getChildren().addAll(rectangleArrayList);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setDragListeners(final Rectangle block) {
        final Delta dragDelta = new Delta();

        block.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = block.getTranslateX() - mouseEvent.getSceneX();
                dragDelta.y = block.getTranslateY() - mouseEvent.getSceneY();
                block.setCursor(Cursor.NONE);
            }
        });
        block.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                block.setCursor(Cursor.HAND);
            }
        });
        block.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                block.setTranslateX(mouseEvent.getSceneX() + dragDelta.x);
                block.setTranslateY(mouseEvent.getSceneY() + dragDelta.y);
                checkBounds(block);

            }
        });
    }

    private void checkBounds(Rectangle block) {
        for (Rectangle static_bloc : rectangleArrayList)
            if (static_bloc != block) {
                if (block.getBoundsInParent().intersects(static_bloc.getBoundsInParent())) {
                    block.setFill(Color.BLUE);        //collision
                } else {
                    block.setFill(Color.GREEN);    //no collision
                }
            } else {
                block.setFill(Color.GREEN);    //no collision -same block
            }
    }

    class Delta {
        double x, y;
    }
}
