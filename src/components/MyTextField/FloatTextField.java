package components.MyTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FloatTextField  extends JTextField implements FocusListener, KeyListener {
    private float tmpValue,min,max;
    private boolean isInt;

    public FloatTextField(float min,float max,boolean isInt){
        super();
        addFocusListener(this);
        addKeyListener(this);
        this.max=max;
        this.min=min;
        this.isInt=isInt;
    }

    public float getValue() {
        if(getText().trim().isEmpty() || isFocusOwner()) {
            return tmpValue;
        }
        return Float.parseFloat(getText());
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
        setBackground(Color.RED);
        tmpValue=Float.parseFloat(getText());
        setText("");
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {
        float d=0;
        try{
            d=Float.parseFloat(getText());
            if(d<min)
                d=min;
            else if (d>max)
                d=max;
        }catch(Exception e){
            d=tmpValue;
        }
        finally {
            setBackground(Color.WHITE);
            if(isInt)
                setText(""+(int)d);
            else
                setText(""+d);
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode()==KeyEvent.VK_ENTER){
            ((JPanel)getParent()).grabFocus();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    public void setMax(float max) {
        this.max = max;
    }
}
