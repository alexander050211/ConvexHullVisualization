package com.example.convexhullvisualization.util;

import com.example.convexhullvisualization.MainApp;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.Stack;

public class GrahamScan {
    private Stack<Node> hull=new Stack<>();

    public MainApp mainApp;
    private int cycleCount;

    private Node[] pts;
    public GrahamScan(Node[] pts,MainApp mainApp) {
        this.pts=pts;
        this.mainApp=mainApp;
        int N=pts.length;
        Node[] points=new Node[N];
        for(int i=0; i<N; i++)
            points[i]=pts[i];
        Arrays.sort(points,(b,a)->
        {
            if(a.getY()!=b.getY()) {
                if(a.getY()<b.getY()) {
                    return 1;
                } else {
                    return -1;
                }
            } else {
                if(a.getX()<b.getX()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        Arrays.sort(points,1,N,(b,a)->
        {
            double cotA=-(a.getX()-points[0].getX())/(a.getY()-points[0].getY());
            double cotB=-(b.getX()-points[0].getX())/(b.getY()-points[0].getY());
            if(cotA-cotB<0) {
                return 1;
            } else  {
                return -1;
            }
        });
        hull.push(points[0]);
        int k1;
        for(k1=1; k1<N; k1++)
            if(!points[0].equals(points[k1]))
                break;
        if(k1==N) return;
        int k2;
        for(k2=k1+1; k2<N; k2++)
            if(Node.ccw(points[0],points[k1],points[k2])!=0)
                break;
        hull.push(points[k2-1]);
        showTempHull();
        cycleCount=k2;
        Timeline timeline=new Timeline(new KeyFrame(Duration.millis(50),e-> {
            loop(points);
        }));
        timeline.setCycleCount(N-k2);
        timeline.play();
        timeline.setOnFinished(e-> {
            showTempHull();
            mainApp.drawPolygon(hull);
            assert isConvex();
        });
    }

    private void loop(Node[] points) {
        showTempHull();
        Node top=hull.pop();
        while(!hull.isEmpty() && Node.ccw(hull.peek(),top,points[cycleCount])<=0) {
            top=hull.pop();
        }
        hull.push(top);
        hull.push(points[cycleCount]);
        cycleCount++;
    }

    public Iterable<Node> hull() {
        Stack<Node> v=new Stack<>();
        for(Node node:hull) v.push(node);
        return v;
    }

    private boolean isConvex() {
        int N=hull.size();
        if(N<=2) return true;
        Node[] points=new Node[N];
        int n=0;
        for(Node pt:hull()) {
            points[n++]=pt;
        }
        for(int i=0; i<N; i++) {
            if(Node.ccw(points[i],points[(i+1)%N],points[(i+2)%N])<=0) {
                return false;
            }
        }
        return true;
    }

    private void showTempHull() {
        Node.clearCanvas();
        int N=hull.size();
        Node[] nodes=new Node[N];
        int idx=0;
        for(Node node:hull) {
            nodes[idx]=node;
            idx++;
        }
        if(N>=2) {
            Node.drawLine(nodes[N-1],nodes[0]);
        }
        for(int i=0; i<N-1; i++) {
            Node.drawLine(nodes[i],nodes[i+1]);
        }
        for(Node node:pts) {
            node.draw(Color.BLACK);
        }
    }

    public Stack<Node> getHull() {
        return hull;
    }
}
