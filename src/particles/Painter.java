package particles;

import windows.App;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;



public class Painter {
    private float maxVel;
    private boolean dead;
    private static PImage originalImage,resizableImage;
    private ArrayList<Particle> particles;
    private static App prt;


    public Painter(float startX, float startY, int n,float maxVel){
        this.dead=false;
        this.maxVel=maxVel;
        particles=new ArrayList<>();
        while (n>0){
            particles.add(new Particle(startX,startY));
            n--;
        }
    }
    public static void initialize(App parent,PImage originalImg){
        prt=parent;
        originalImage=originalImg;
        resizableImage=originalImg;
        resizeCloneImage();
    }

    public static void resizeCloneImage() {
        if(originalImage==null)
            return;
        resizableImage=originalImage;
        resizableImage.resize(prt.getCanvasWidth(),prt.getCanvasHeight());
    }

    public void update(){
        for(Particle p:particles){
            p.vel.add(p.acc);
            if(p.vel.mag()>maxVel)
                p.vel.setMag(maxVel);

            p.lPos=new PVector(p.pos.x,p.pos.y);
            p.pos.add(p.vel);
            p.acc.mult(0);

            checkEdges(p);
        }
    }

    public void drawToImage(PGraphics gr){
        //display the particles
        gr.beginDraw();
        gr.noFill();
        for (Particle p:particles) {
            int c=0;
            if(resizableImage!=null)
                c=resizableImage.get((int) p.pos.x, (int) p.pos.y);
            int alpha = prt.getAlpha();
            float hue;
            if(prt.isOriginalHue() && resizableImage!=null)
                hue=prt.hue(c);
            else
                hue = calculateHue(prt.getMinHue(), prt.getMaxHue());
            float brightness=(resizableImage!=null)? (prt.brightness(c) * prt.getBrightnessCoeff()):255;
            float saturation=(resizableImage!=null)?(prt.saturation(c) * prt.getSaturationCoeff()):255;
            gr.stroke(hue, saturation, brightness, alpha);
            gr.line(p.pos.x, p.pos.y, p.lPos.x, p.lPos.y);
        }
        gr.endDraw();
    }

    private float calculateHue(float minHue, float maxHue) {
        float frms=prt.getSpectrumFrames();
        if(minHue<maxHue){
            return PApplet.map(prt.frameCount%frms,0,frms-1,minHue,maxHue);
        }else if(minHue==maxHue){
            return minHue;
        }else{
            float newMax=255+maxHue;
            float value=PApplet.map(prt.frameCount%frms,0,frms-1,minHue,newMax);
            if(value>255)
                value-=255;
            return value;
        }
    }

    private void checkEdges(Particle p){
        int W=prt.getCanvasWidth(),
            H=prt.getCanvasHeight();
        if(p.pos.x<0){
            p.lPos.x=W-1;
            p.pos.x=W-1;
        }else if(p.pos.x>=W){
            p.lPos.x=0;
            p.pos.x=0;
        }
        if(p.pos.y<0){
            p.lPos.y= H-1;
            p.pos.y=H-1;
        }
        else if(p.pos.y>=H){
            p.lPos.y=0;
            p.pos.y=0;
        }
    }



    public int getCount() {
        return particles.size();
    }

    public PVector getParticlePosition(int i) {
        return particles.get(i).pos.copy();
    }

    public void applyForceToParticle(int i, PVector force) {
        particles.get(i).applyForce(force);
    }

    public boolean isDead() {
        return dead;
    }

    public void kill() {
        dead=true;
    }

    public float getMaxVelocity() {
        return maxVel;
    }
}
