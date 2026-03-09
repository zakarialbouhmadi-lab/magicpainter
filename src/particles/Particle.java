package particles;

import processing.core.PVector;

class Particle{
    PVector lPos,pos,vel,acc;


    Particle(float x,float y){
        pos=new PVector(x,y);
        lPos=new PVector(x,y);
        acc=PVector.random2D();
        vel=new PVector(0,0);
    };

    public void applyForce(PVector f){
        acc.add(f);
    }


}