package com.example.convexhullvisualization;

import com.example.convexhullvisualization.util.GrahamScan;
import com.example.convexhullvisualization.util.Node;
import com.example.convexhullvisualization.view.RootLayoutController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Stack;
import java.util.Vector;

public class MainApp extends Application {

    class ResizableCanvas extends Canvas {
        public ResizableCanvas() {
            widthProperty().addListener(e->draw());
            heightProperty().addListener(e->draw());
        }
        private void draw() {
            double width=getWidth();
            double height=getHeight();
            GraphicsContext gc=getGraphicsContext2D();
            gc.clearRect(0,0,width,height);
        }
        @Override
        public boolean isResizable() {
            return true;
        }
        @Override
        public double prefWidth(double height) {
            return getWidth();
        }
        @Override
        public double prefHeight(double width) {
            return getHeight();
        }
    }

    private Stage primaryStage;
    private SplitPane rootLayout;
    private boolean isClicked=false;

    private RootLayoutController rootLayoutController;

    private HashSet<Node> nodes=new HashSet<>();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage=primaryStage;
        this.primaryStage.setTitle("ConvexHull Visualization App");
        initRootLayout();
    }

    public void handleButton() {
        if(isClicked) return;
        if(nodes.isEmpty()) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No points drawn");
            alert.setContentText("Please draw points on canvas");
            alert.showAndWait();
            return;
        } else if(nodes.size()<3) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Not enough points");
            alert.setContentText("Please draw at least 3 points");
            alert.showAndWait();
            return;
        }
        System.out.println("============================");
        isClicked=true;
        int N=nodes.size();
        Node[] Nodes=new Node[N];
        int idx=0;
        for(Node node:nodes) {
            Nodes[idx]=node;
            idx++;
        }
        GrahamScan grahamScan=new GrahamScan(Nodes,this);
        Stack<Node> hull=grahamScan.getHull();
        System.out.println(hull.size());
    }

    public void drawPolygon(Stack<Node> hull) {
        int N=hull.size();
        double posX[]=new double[N];
        double posY[]=new double[N];
        int idx=0;
        for(Node node:hull) {
            posX[idx]=node.getX();
            posY[idx]=node.getY();
            idx++;
        }
        GraphicsContext gc=Node.canvas.getGraphicsContext2D();
        gc.setFill(new LinearGradient(0,0,1,1,true,CycleMethod.REPEAT,
                new Stop(0,Color.ORANGE),new Stop(1,Color.YELLOW)));
        gc.fillPolygon(posX,posY,N);
        for(Node node:nodes) node.draw();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader=new FXMLLoader();
            URL fxmlPath=new File("src/main/java/com/example/ConvexHullVisualization/view/RootLayoutView.fxml").toURL();
            loader.setLocation(fxmlPath);
            rootLayout=(SplitPane) loader.load();
            rootLayoutController=loader.getController();
            final Canvas canvas=rootLayoutController.canvas;
            final GraphicsContext gc=canvas.getGraphicsContext2D();
            reset(canvas);
            Node.canvas=canvas;
            canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(isClicked==true) return;
                    Node node=new Node((new Point2D(mouseEvent.getX(), mouseEvent.getY())));
                    nodes.add(node);
                    node.draw();
                    System.out.println(Double.toString(node.getX())+" "+Double.toString(node.getY()));
                }
            });
            Scene scene=new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            RootLayoutController rootLayoutController=loader.getController();
            rootLayoutController.setMainApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset(Canvas canvas) {
        GraphicsContext gc=canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
    }

    public static void main(String[] args) {
        launch();
    }
}