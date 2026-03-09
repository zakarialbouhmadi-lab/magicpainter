package windows;

import components.MyTextField.FloatTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ControlPanel extends JFrame implements MouseListener {
    private JPanel contentPane,rootPane,colorsPanel,gridPanel,paintersPanel,controlsPanel,canvasPanel;
    private JSlider alphaSlider;
    private FloatTextField textFieldMagnitude,
            textFieldMaxVelocity,textFieldNumOfParticles,
            textFieldScale,textFieldHueMin, textFieldHueMax,
            textFieldSatCoef, textFieldBriCoef,textFieldCanvasHeight,
            textFieldCanvasWidth,textFieldSpectrumTime;
    private JCheckBox showGridFlowsCheckBox,originalHueCheckBox;
    private JButton buttonClearFirst,buttonClearLast, pausePaintersButton,buttonEraseImage,
            buttonClearSystems,buttonResizeCanvas,buttonSave,buttonLoad,pauseForcesButton;
    private JLabel alphaLabel,paintersLabel,fpsLabel,labelImageSize;
    private JTextField textFieldSaveFileName,textFieldLoadFileName;
    private JRadioButton blendRadioBtn,replaceRadioButton,onlyOnceRadioButton;

    private App prt;
    private Image bckImg;


    public ControlPanel(App prt,int minHue, int maxHue, float satCoeff,float briCoeff,int alpha,
                        int scale,float maxScale,int numOfParticles,int spectrumTime) {
        super();
        setTitle("Control Panel");
        bckImg=Toolkit.getDefaultToolkit().getImage(getClass().getResource("/bg.jpg"));
        setContentPane(contentPane);

        this.prt=prt;
        textFieldHueMin.setText("" + minHue);
        textFieldHueMax.setText("" + maxHue);
        textFieldBriCoef.setText("" + briCoeff);
        textFieldSatCoef.setText("" + satCoeff);
        textFieldScale.setText(""+scale);
        textFieldScale.setMax(maxScale);
        textFieldSpectrumTime.setText(""+spectrumTime);
        textFieldNumOfParticles.setText(""+numOfParticles);
        textFieldMaxVelocity.setText("3.0");
        textFieldMagnitude.setText("1.0");
        textFieldCanvasHeight.setText(""+prt.getCanvasWidth());
        textFieldCanvasWidth.setText(""+prt.getCanvasHeight());
        textFieldLoadFileName.setText("pic.jpg");
        alphaSlider.setMinimum(0);
        alphaSlider.setMaximum(255);
        alphaSlider.setValue(alpha);
        alphaLabel.setText(""+alpha);

        addMouseListener(this);

        alphaSlider.addChangeListener(changeEvent -> {
            alphaLabel.setText(""+alphaSlider.getValue());
        });
        buttonClearSystems.addActionListener(e->{
            prt.deleteSystems();
        });
        buttonClearFirst.addActionListener(e->{
            prt.deleteFirstSystem();
        });
        buttonClearLast.addActionListener(e->{
            prt.deleteLastSystem();
        });
        buttonEraseImage.addActionListener(e->{
            prt.setErase(true);
        });
        pauseForcesButton.addActionListener(e->{
            prt.playPauseForces();
            String s="PAUSE";
            if(prt.isPauseForces()) {
                s="RESUME";
            }
            ((JButton)e.getSource()).setText(s);
        });
        pausePaintersButton.addActionListener(e->{
            prt.playPausePainters();
            String s="PAUSE";
            if(prt.isPausePainters()) {
                s="RESUME";
            }
            ((JButton)e.getSource()).setText(s);
        });
        buttonResizeCanvas.addActionListener(e->{
            prt.setResize(true);
            textFieldScale.setMax(maxScale);
            buttonResizeCanvas.setBackground(new Color(30,158,255));
        });

        buttonSave.addActionListener(e->{
            prt.setSave(true);
        });

        buttonLoad.addActionListener(e->{
            prt.setLoad(true);
        });

        ButtonGroup blendModeGroup=new ButtonGroup();
        blendModeGroup.add(blendRadioBtn);
        blendModeGroup.add(replaceRadioButton);
        blendModeGroup.add(onlyOnceRadioButton);
        blendRadioBtn.addActionListener(e->{prt.setBlendMode(prt.BLEND);});
        replaceRadioButton.addActionListener(e->{prt.setBlendMode(prt.REPLACE);});


        contentPane.grabFocus();
        setAlwaysOnTop(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setBounds(prt.displayWidth-getWidth(),100,getWidth(),getHeight());
        pack();
        setResizable(false);
        setVisible(true);
    }

    //create my custom components
    public void createUIComponents() {
        contentPane=new JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bckImg, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };

        textFieldHueMax = new FloatTextField(0, 255,true);
        textFieldHueMin = new FloatTextField(0, 255,true);
        textFieldBriCoef = new FloatTextField(0, 1000,false);
        textFieldSatCoef = new FloatTextField(0, 1000,false);
        textFieldMaxVelocity=new FloatTextField(0.1f,1000,false);
        textFieldNumOfParticles=new FloatTextField(1,10000000,true);
        textFieldMagnitude=new FloatTextField(0,10000,false);
        textFieldSpectrumTime=new FloatTextField(10,10000,true);
        textFieldCanvasWidth=new FloatTextField(30,10000,true){
          @Override
          public void focusLost(FocusEvent focusEvent){
              super.focusLost(focusEvent);
              if((int)this.getValue()!=prt.getCanvasWidth()){
                  buttonResizeCanvas.setBackground(new Color(0x6DD952));
              }
          }
        };
        textFieldCanvasHeight=new FloatTextField(30,10000,true){
            @Override
            public void focusLost(FocusEvent focusEvent){
                super.focusLost(focusEvent);
                if((int)this.getValue()!=prt.getCanvasWidth()){
                    buttonResizeCanvas.setBackground(new Color(0x6DD952));
                }
            }
        };

        textFieldScale=new FloatTextField(5,10000,true){
            @Override
            public void focusLost(FocusEvent focusEvent) {
                super.focusLost(focusEvent);
                prt.resetFlowField(this.getValue());
            }
        };
    }

    public void setOriginalImageSize(String s) {
        labelImageSize.setText(s);
    }

    public boolean showGrid(){
        return showGridFlowsCheckBox.isSelected();
    }

    public float getScale() {
        return textFieldScale.getValue();
    }
    public float getMaxVelocity() {
        return textFieldMaxVelocity.getValue();
    }

    public int getNumOfParticles(){
        return (int)textFieldNumOfParticles.getValue();
    }

    public float getBrightnessCoeff() {
        return textFieldBriCoef.getValue();
    }

    public float getSaturationCoeff() {
        return textFieldSatCoef.getValue();
    }

    public float getMagnitude() {
        return textFieldMagnitude.getValue();
    }

    public float getMinHue() {
        return textFieldHueMin.getValue();
    }

    public float getMaxHue() {
        return textFieldHueMax.getValue();
    }

    public boolean isOriginalHue(){
        return originalHueCheckBox.isSelected();
    }

    public String getSaveFileName() {
        return textFieldSaveFileName.getText();
    }
    public String getLoadFileName() {
        return textFieldLoadFileName.getText().trim();
    }

    public float getSpectrumFrames() {
        return textFieldSpectrumTime.getValue();
    }

    public int getAlpha() {
        return alphaSlider.getValue();
    }

    public int getCanvasHeight() {
        return (int)textFieldCanvasHeight.getValue();
    }
    public int getCanvasWidth() {
        return (int)textFieldCanvasWidth.getValue();
    }


    public void update() {
        paintersLabel.setText("PAINTERS = "+prt.getSystemsCount());
        fpsLabel.setText("Frames Per Second = "+(int)prt.frameRate);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        contentPane.grabFocus();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }



}
