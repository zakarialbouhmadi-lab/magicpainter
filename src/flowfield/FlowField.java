package flowfield;

import windows.App;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class FlowField {
    private PVector[][] currents;
    private float scl,xYinc,zInc,z;
    private App prt;
    private PGraphics gr;


    public FlowField(App parent, float scl){
        this.prt=parent;
        xYinc=0.1f;
        zInc=0.005f;
        z=0;
        this.scl=scl;

        int cols=(int)(prt.getCanvasWidth()/scl);
        if(prt.getCanvasWidth()%scl>0)
            cols++;
        int rows=(int)(prt.getCanvasHeight()/scl);
        if(prt.getCanvasHeight()%scl>0)
            rows++;
        currents=new PVector[cols][rows];

        gr=prt.createGraphics(prt.getCanvasWidth(),prt.getCanvasHeight());
    }

    public void update(){
        z+=zInc;
        float y=0;
        int rows=currents[0].length,
            cols=currents.length;
        for(int i=0;i<rows;i++){
            float x=0;
            for(int j=0;j<cols;j++){
                PVector va=PVector.fromAngle(prt.noise(x,y,z)*PApplet.TWO_PI*4);
                currents[j][i]=va.setMag(prt.getFlowMagnitude());
                x+=xYinc;
            }
            y+=xYinc;
        }
    }

    public PImage getImg() {
        return gr;
    }

    public void display(){
        float sclH=scl/2;
        gr.smooth(8);
        gr.beginDraw();
        gr.fill(0);
        gr.stroke(0);
        gr.clear();
        for (int r = 0; r <currents[0].length ; r++) {
            gr.line(0,r*scl,scl*currents.length,r*scl);
            for (int c = 0; c <currents.length ; c++) {
                gr.line(c*scl,0,c*scl,scl*currents[0].length);
                PVector position=new PVector((c*scl)+sclH,(r*scl)+sclH);
                PVector force=currents[c][r].copy();
                force.setMag(sclH);
                gr.line(position.x,position.y,position.x+force.x,position.y+force.y);
                gr.ellipse(position.x,position.y,sclH/3,sclH/3);
            }
        }
        gr.endDraw();
    }

    public PVector getForceByPxlPosition(float pxlsX, float pxlsY){
        int posX= (int) (pxlsX/scl);
        int posY= (int) (pxlsY/scl);
        try{
            return currents[posX][posY].copy();
        }catch (Exception e){
            return new PVector(0,0);
        }
    }
}
