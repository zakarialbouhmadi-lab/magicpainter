package components.pscrollbar;

import windows.App;
import processing.core.PApplet;

public class HorizontalScrollBar extends PScrollBar{


    /**
     * Constructor
     * @param  prt   reference of the PApplet(parent)
     */
    public HorizontalScrollBar(App prt){
        super(prt);
        x=0;
        hei=10;
        y=prt.height-hei;
    }

    /**
     * To use when the mouse presses the bar or when the mouse is released in any position.
     * @param b true for pressed, false for released.
     */
    @Override
    public void setPressed(boolean b){
        super.setPressed(b);
        if(b)
            pressedPoint=prt.mouseX-x;
    }

    /**
     * Adjusts the dimensions of the bar, move it and reframe it.
     */
    public void update(float imgWidth){
        //adjust width
        wid=prt.width*(prt.width/(float)imgWidth);
        if(pressed){
            //move
            x=prt.mouseX-pressedPoint;
        }
        //reframe
        if(x<0)
            x=0;
        else if(x+wid>prt.width)
            x=prt.width-wid;
        if(y!=prt.height-hei)
            y=prt.height-hei;
    }

    /**
     * Defines the new X coordinate of the associated graphics. To use when drawing the graphics
     * @return The value to add to the graphics or image X.
     */
    public float getOffset(float imgWidth){
        return (imgWidth>prt.width)? -PApplet.map(x,0,prt.width-wid,0,imgWidth-prt.width):0;
    }

    public void display(float imgWidth){
        if(imgWidth>prt.width )
            super.display();
    }

}
