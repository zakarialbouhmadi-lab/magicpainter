package components.pscrollbar;

import windows.App;

public class VerticalScrollBar extends PScrollBar{

    /**
     * Constructor
     * @param  prt   reference of the PApplet(parent)
     */
    public VerticalScrollBar(App prt){
        super(prt);
        y=0;
        wid=10;
        x=prt.width-wid;
    }

    /**
     * Adjusts the dimensions of the bar, move it and reframe it.
     */
    public void update(float imgHeight){
        //adjust height
        hei=prt.height*(prt.height/(float)imgHeight);
        if(pressed){
            //move
            y=prt.mouseY-pressedPoint;
        }
        //reframe
        if(y<0)
            y=0;
        else if(y+hei>prt.height)
            y=prt.height-hei;
        if(x!=prt.width-wid)
            x=prt.width-wid;
    }

    @Override
    public void display(float imgHeight){
        if(imgHeight>prt.height)
            super.display();
    }

    /**
     * To use when the mouse presses the bar or when the mouse is released in any position.
     * @param b true for pressed, false for released.
     */
    @Override
    public void setPressed(boolean b){
        super.setPressed(b);
        if(b)
            pressedPoint=prt.mouseY-y;
    }

    /**
     * Defines the new Y coordinate of the associated graphics. To use when drawing the graphics
     * @return The value to add to the graphics or image Y.
     */
    public float getOffset(float imgHeight){
        return (imgHeight>prt.height)? -prt.map(y,0,prt.height-hei,0,imgHeight-prt.height):0;
    }
}
