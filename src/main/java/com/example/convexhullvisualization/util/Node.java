package com.example.convexhullvisualization.util;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class Node extends Point2D {
    private Point2D location;
    private double x;
    private double y;

    public static Canvas canvas;

     public Node(Point2D point) {
         super(point.getX(), point.getY());
         this.location=point;
         this.x=point.getX();
         this.y=point.getY();
     }

     public Point2D getLocation() {
         return location;
     }


     public static void clearCanvas() {
         GraphicsContext gc=canvas.getGraphicsContext2D();
         gc.setFill(Color.WHITE);
         gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
     }
    public void draw() {
        GraphicsContext gc=canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillOval(location.getX()-3,location.getY()-3,6,6);
    }

    public void draw(Color color) {
         GraphicsContext gc=canvas.getGraphicsContext2D();
         gc.setFill(Color.BLACK);
         gc.fillOval(location.getX()-3,location.getY()-3,6,6);
    }

    public static int ccw(Node a,Node b,Node c) {
         int value= (int)((int)(a.x*b.y+b.x*c.y+c.x*a.y)-(a.y*b.x+b.y*c.x+c.y*a.x));
         if(value==0) {
             return 0;
         } else if(value>0) {
             return 1;
         } else {
             return -1;
         }
    }

    public static void drawLine(Node a,Node b) {
         GraphicsContext gc=canvas.getGraphicsContext2D();
         gc.setStroke(Color.RED);
         gc.setLineWidth(3);
         gc.strokeLine(a.x,a.y,b.x,b.y);
    }

    public static void eraseLine(Node a,Node b) {
         GraphicsContext gc=canvas.getGraphicsContext2D();
         gc.setStroke(Color.WHITE);
         gc.setLineWidth(2);
         gc.strokeLine(a.x,a.y,b.x,b.y);
    }
}
