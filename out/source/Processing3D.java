import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Processing3D extends PApplet {

PShape mysphere;
Orb[] orbs;
Orb[] points;
public void setup() {
    
    camera(100.0f, 200.0f, 300.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    ortho();
    // frameRate(30);
    
    sphereDetail(50, 50);
    mysphere = createShape(SPHERE, width/3);
    orbs = new Orb[mysphere.getVertexCount()];

    points = new Orb[10];
    

    for (int i = 0; i < mysphere.getVertexCount(); i++){
        PVector loc = mysphere.getVertex(i);
        Orb orb = new Orb(loc);
        orbs[i] = orb;
        orb.display();
    }

    for (int i = 0; i < points.length; i++){
        float inter = PApplet.parseFloat(width*2)/PApplet.parseFloat(points.length - 1);
        Orb point = new Orb(-(i*inter) - width, 0);
        points[i] = point;
        // point.display();
    }
}

public void draw() {
    background(255);

    for (int i = 0; i < points.length; i++)
    {
        points[i].move();
        // points[i].display();
    }
    for (int i = 0; i < orbs.length; i++){
        orbs[i].calc_size();
        orbs[i].display();
    }
    
}
int flow_count = 0;
class Orb {
    PVector loc;
    float size = 10;
    float curr_size;
    float size_calc_dist = width/4;
    public Orb(float x, float y){
        loc = new PVector(x, y, 0);
        curr_size = size;
    }

    public Orb(PVector loc0){
        loc = loc0;
    }

    public void calc_size(){
        float prev_dist = width*2;
        boolean smaller = false;
        for (int i = 0; i < points.length; i++){
            float dist = abs(loc.x - points[i].loc.x);//xy.dist(this.loc) + 20;
            if (dist < size_calc_dist && dist < prev_dist){
                curr_size = (dist / size_calc_dist) * size;
                smaller = true;
            } else if (!smaller){
                curr_size = size;
            }
            dist = prev_dist;
        }
    }

    public void display(){
        pushMatrix();
        translate(0, 0, loc.z);
        fill(0);
        ellipse(loc.x, loc.y, curr_size, curr_size);
        popMatrix();
    }

    public void move(){
        loc.x += 5;
        if (loc.x > width && flow_count < 50){
            loc.x -= 2*width;
            flow_count += 1;
        }
    }
}
  public void settings() {  size(512, 512, P3D);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Processing3D" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
