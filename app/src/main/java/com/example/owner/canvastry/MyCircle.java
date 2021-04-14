package com.example.owner.canvastry;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class MyCircle {
    public int x,y,rad;
    public Paint paint = new Paint();
    private int speed,width,height;
    private double degree = Math.PI/(Math.random())+10;   //球體角度


    public MyCircle(int x1,int y1,int rad,int speed,int width,int height){
        this.x = x1 ;
        this.y = y1 ;
        this.speed = speed ;
        this.rad = rad ;
        // 螢幕的長寬
        this.width = width;
        this.height = height;

        this.paint.setFilterBitmap(true);
        this.paint.setAntiAlias(true);
        this.paint.setColor(Color.RED);
    }
    //判斷是否碰撞邊界 並重新定位球體 x y
    public void  move(){
        Log.v("degree"," : " + degree);
        int x2 = x+(int)(speed * Math.cos(degree));
        int y2 = y+(int)(speed * Math.sin(degree));
        if(y2 + rad > height || y2-rad <0){
            degree = -degree;
            x2 = x+(int)(speed*Math.cos(degree));
            y2 = y+(int)(speed*Math.sin(degree));
        }
        if(x2 -rad <(width*0.25f)+50 || x+rad > width){
            degree = Math.PI - degree ;
            x2 = x+(int)(speed*Math.cos(degree));
            y2 = y+(int)(speed*Math.sin(degree));
        }

        x = x2;
        y = y2;
    }
    public Rect getRect(){
        return new Rect(x-rad/2,y-rad/2,x+rad,y+rad);
    }
}
