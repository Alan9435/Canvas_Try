package com.example.owner.canvastry;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.text.style.BulletSpan;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MainObject {

    public  int x,y,w,h;
    public Bitmap bitmap ;
    public  MainObject(Bitmap bitmap,int x,int y,int w,int h){
        this.bitmap = bitmap ;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    public Rect getRect(){
        return new Rect(x,y,x+w,y+h);
    }
    public int getX(){
        return  x ;
    }
}
