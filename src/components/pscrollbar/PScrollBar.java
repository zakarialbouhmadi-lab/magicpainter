package components.pscrollbar;

import windows.App;

abstract class PScrollBar{
    protected int color;
    protected float wid,hei,x,y,pressedPoint=0;
    protected boolean pressed;
    protected App prt;

    protected PScrollBar(App prt){
        this.prt=prt;
        pressed=false;
        color=prt.color(120);
    }

    abstract public void display(float x);
    abstract public void update(float x);
    abstract public float getOffset(float x);

    protected void display(){
        prt.stroke(100);

        if(isSelected() || pressed){
            prt.fill(color,255);
        }else{
            prt.fill(color,30);
        }
        prt.rect(x,y,wid,hei);
    }


    public boolean isSelected(){
        return prt.mouseX>=x && prt.mouseX<=x+wid && prt.mouseY>=y && prt.mouseY<=y+hei;
    }

    protected void setPressed(boolean b){
        pressed=b;
    }

    public void setColor(int c){
        this.color=c;
    }
}