package windows;

import flowfield.FlowField;
import particles.Painter;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import components.pscrollbar.HorizontalScrollBar;
import components.pscrollbar.VerticalScrollBar;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class App extends PApplet {
    private PGraphics canvasGr;
    private HorizontalScrollBar hScroll;
    private VerticalScrollBar vScroll;
    private ArrayList<Painter> painters;
    private FlowField flowField;
    private ControlPanel controlPanel;
    private boolean pausePainters,pauseForces,erase,resize,saveCanvas,loadImg;
    private File jarFolder;

    public void settings() {
        size(displayWidth,displayHeight);
    }

    public void setup(){
        background(200);
        surface.setTitle("Magic Painter");
        surface.setResizable(true);
        painters=new ArrayList<>();
        pauseForces=false;
        pausePainters=false;
        erase=false;
        resize=false;
        saveCanvas=false;
        //finds the path for the folder containing the running jar
        setJarFolder();
        //create the canvas where the drawings will be done
        canvasGr=createGraphics(500,500);
        prepareCanvas();
        //create the window used as the controls panel
        controlPanel = new ControlPanel(App.this, 0, 255, 1.0f, 1.0f, 10, 100, Math.min(canvasGr.width, canvasGr.height), 100, 300);
        //load the image to redraw
        PImage originalImg=loadOriginalImage();
        //initialize the particle system
        Painter.initialize(this,originalImg);
        //initialize the f;ow field
        flowField=new FlowField(this,100);
        //initialize the custom scroll bars
        hScroll=new HorizontalScrollBar(this);
        vScroll=new VerticalScrollBar(this);
    }




    public void draw() {
        //draw the gray background on the whole window
        background(200);
        fill(255);
        rect(0,0,canvasGr.width,canvasGr.height);
        //check if the user required to resize the canvas
        if (resize) {
            resizeCanvas();
        }
        //check if the user required to erase the canvas
        if (erase) {
            eraseCanvas();
        }
        //loop through painters
        for (int i = painters.size() - 1; i >= 0; i--) {
            Painter painter = painters.get(i);
            //remove dead painters
            if (painter.isDead()) {
                painters.remove(i);
            }
            if (pausePainters)
                break;
            applyCurrentsToParticles(flowField, painter);
            //update the positions of the particles
            painter.update();
            //paint the particles to the canvas
            painter.drawToImage(canvasGr);
        }
        //update the scroll bars positions and color
        hScroll.update(canvasGr.width);
        vScroll.update(canvasGr.height);
        //draw the canvas in their position, defined by the scrolls
        image(canvasGr, hScroll.getOffset(canvasGr.width), vScroll.getOffset(canvasGr.height));

        //update the flow field
        if (!pauseForces) {
            flowField.update();
        }
        flowField.display();
        if (controlPanel.showGrid()){
            //show the grid and currents
            image(flowField.getImg(), hScroll.getOffset(canvasGr.width), vScroll.getOffset(canvasGr.height));
        }

        //display scroll bars
        hScroll.display(canvasGr.width);
        vScroll.display(canvasGr.height);
        //update the labels in the control panel
        controlPanel.update();
        //check if to save
        if(saveCanvas){
            saveCanvas();
        }
        //check if load image
        if(loadImg){
            Painter.initialize(this,loadOriginalImage());
        }
    }

    ////////////////////////////////////PRIVATE METHODS///////////////////////////////////////////////////////////////////////////

    private void setJarFolder() {
        try{
            jarFolder = new File(App.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath());
            jarFolder=jarFolder.getParentFile();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private void saveCanvas() {
        File directory=new File(jarFolder.getPath()+"/generated_images");
        if(!directory.exists())
            directory.mkdir();
        String fileName=jarFolder.getPath()+"/generated_images/"+controlPanel.getSaveFileName();

        File file=new File(fileName+".png");
        int i=1;
        while (file.exists()){
            file=new File(fileName+i+".png");
            i++;
        }
        canvasGr.save(file.getAbsolutePath());
        saveCanvas=false;
    }

    private void prepareCanvas() {
        canvasGr.smooth(8);
        canvasGr.beginDraw();
        canvasGr.colorMode(HSB,255);
        canvasGr.blendMode(BLEND);
        canvasGr.endDraw();
    }

    private void resizeCanvas(){
        int newWid=controlPanel.getCanvasWidth(),
            newHei=controlPanel.getCanvasHeight();

        canvasGr.beginDraw();
        canvasGr.setSize(newWid,newHei);
        canvasGr.fill(255);
        canvasGr.rect(0,0,newWid,newHei);
        canvasGr.endDraw();
        Painter.resizeCloneImage();
        resetFlowField(controlPanel.getScale());
        resize=false;
    }

    private void eraseCanvas(){
        canvasGr.beginDraw();
        canvasGr.fill(255);
        canvasGr.rect(0,0,canvasGr.width,canvasGr.height);
        canvasGr.endDraw();
        erase=false;
    }

    private  PImage loadOriginalImage() {
        PImage tmpImg=null;
        tmpImg=loadImage(jarFolder.getPath()+"/"+controlPanel.getLoadFileName());
        String s=(tmpImg==null)? "No image loaded !":"Original size = "+tmpImg.width+"x"+tmpImg.height;
        controlPanel.setOriginalImageSize(s);
        loadImg=false;
        return tmpImg;
    }

    private void applyCurrentsToParticles(FlowField flowField, Painter painter) {
        for (int i = 0; i <painter.getCount() ; i++) {
            PVector ppos=painter.getParticlePosition(i);
            painter.applyForceToParticle(i,flowField.getForceByPxlPosition(ppos.x,ppos.y));
        }
    }

    ///////////////////////////////////////KEY AND MOUSE LISTENERS////////////////////////////////////////////////////////////

    public void mousePressed(){
        if(mouseButton==RIGHT)
            return;

        if(hScroll.isSelected()){
            hScroll.setPressed(true);
        }else if (vScroll.isSelected()){
            vScroll.setPressed(true);
        }

        else if(mouseX<canvasGr.width && mouseY<canvasGr.height) {
            painters.add(new Painter(mouseX - hScroll.getOffset(canvasGr.width), mouseY - vScroll.getOffset(canvasGr.height)
                    , controlPanel.getNumOfParticles(), controlPanel.getMaxVelocity()));
        }
    }

    public void mouseReleased() {
        vScroll.setPressed(false);
        hScroll.setPressed(false);
    }

    ////////////////////////////////////////////PUBLIC METHODS//////////////////////////////////////////////////////////

    public void resetFlowField(float scale) {
        flowField=new FlowField(this,scale);
        flowField.update();
    }

    public void deleteSystems() {
        for (Painter p:painters){
            p.kill();
        }
    }
    public void deleteSystem(int i){
        try {
            painters.get(i).kill();
        }catch (ArrayIndexOutOfBoundsException e){}

    }

    public void deleteFirstSystem() {
        if(!painters.isEmpty() && !painters.get(0).isDead()){
            painters.get(0).kill();
        }
    }

    public void deleteLastSystem() {
        if(!painters.isEmpty() && !painters.get(painters.size()-1).isDead()){
            painters.get(painters.size()-1).kill();
        }
    }

//////////////////////////////////////////SETTERS AND GETTERS////////////////////////////////////////////////////////////

    public void setBlendMode(int mode){
        canvasGr.beginDraw();
        canvasGr.blendMode(mode);
        canvasGr.endDraw();
    }
    public void setSave(boolean b) {
        saveCanvas=b;
    }

    public void setLoad(boolean b){
        loadImg=b;
    }

    public void setResize(boolean b) {
        resize=b;
    }

    public void setErase(boolean b){
        erase=b;
    }

    public float getBrightnessCoeff() {
        return controlPanel.getBrightnessCoeff();
    }

    public float getSaturationCoeff() {
        return controlPanel.getSaturationCoeff();
    }

    public float getMaxHue() {
        return controlPanel.getMaxHue();
    }

    public float getMinHue() {
        return controlPanel.getMinHue();
    }

    public int getAlpha() {
        return controlPanel.getAlpha();
    }


    public int getSystemsCount(){
        return painters.size();
    }

    public void playPausePainters() {
       pausePainters=!pausePainters;
    }

    public void playPauseForces(){
        pauseForces=!pauseForces;
    }

    public boolean isOriginalHue() {
        return controlPanel.isOriginalHue();
    }

    public boolean isPausePainters() {
        return pausePainters;
    }
    public boolean isPauseForces(){
        return pauseForces;
    }

    public float getFlowMagnitude() {
        return controlPanel.getMagnitude();
    }

    public int getCanvasWidth() {
        return canvasGr.width;
    }

    public int getCanvasHeight(){
        return canvasGr.height;
    }


    public float getSpectrumFrames() {
        return controlPanel.getSpectrumFrames();
    }



}
