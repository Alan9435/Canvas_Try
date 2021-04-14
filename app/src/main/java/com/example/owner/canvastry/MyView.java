package com.example.owner.canvastry;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.Attributes;


public class MyView extends View
{
    int width,height;                       //接螢幕長寬
    //球體
    WinThread thread = new WinThread();     //建立執行續 用於更新圖畫及球移動
    int maxball ;                           //定義產生的球數
    ArrayList circleList = new <MyCircle>ArrayList();       //宣告陣列供產生球體物件存放
    Paint paint = new Paint();              //建立畫筆

    //主物件
    MainObject mainObject ;                 //主物件宣告
    Boolean controlThread = true;
    private Boolean Clickpassage = true;        //防止連續跳躍

    //紀錄
    Paint paintScoreString = new Paint();

    //終點
    Boolean Boolnext = false ;

    //左右按鈕
    Boolean LRBtn = false;
    Boolean LRch = false;
    Paint paintBtn = new Paint();
    Paint printLR = new Paint();
    Rect Lbtn ;
    Rect Rbtn ;
//    public MyView(Context con, AttributeSet set)
//    {
//        super(con, set);
//        this.width = 1700;
//        this.height = 1000;
//        this.maxball = 5;
//        paint.setColor(Color.GREEN);   //繪製橫向綠線宣告
//        paintBtn.setColor(Color.WHITE);
//        paintBtn.setAntiAlias(true);       //防止邊緣鋸齒
//        paintBtn.setFilterBitmap(true);    //對圖進行濾波處裡
//        paint.setAntiAlias(true);       //防止邊緣鋸齒
//        paint.setFilterBitmap(true);    //對圖進行濾波處裡
//        mainObject = new MainObject(BitmapFactory.decodeResource(getResources(),R.drawable.mainob),30,700,50,100); //100:900
//        thread.start();                 //開啟執行續
//        Log.v("***","****222221");
//
//    }
    public MyView(Context context,int width,int height,int maxball)
    {
        super(context);
        this.width = width;
        this.height = height;
        this.maxball = maxball;
        // 紀錄
        paintScoreString.setColor(Color.BLACK);
        paintScoreString.setTextSize(200);


        //按鈕
        paintBtn.setColor(Color.WHITE);     //按鈕顏色
        paintBtn.setAntiAlias(true);       //防止邊緣鋸齒
        paintBtn.setFilterBitmap(true);    //對圖進行濾波處裡
        printLR.setColor(Color.BLACK);      //文字顏色
        Lbtn = new Rect(50, height-250, 200, height);
        Rbtn = new Rect(240,height-250,390,height);
        printLR.setTextSize(200);

        // 背景
        paint.setColor(Color.GREEN);   //繪製直橫向綠線宣告
        paint.setAntiAlias(true);       //防止邊緣鋸齒
        paint.setFilterBitmap(true);    //對圖進行濾波處裡

        //主物件
        mainObject = new MainObject(BitmapFactory.decodeResource(getResources(),R.drawable.mainob),(int)(width*0.25f)-150,height-415,50,100); //企鵝的位置 碰觸範圍
        thread.start();                 //開啟執行續
    }
    public class WinThread extends Thread{
        @Override
        public void run(){
            super.run();
            while (true){
                CheakTouch();
                CheakIfAddBall();
                moveball();
                try {
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    Log.v("******","*** : " + e);
                }
            }
        }
    }
    public class  LRThread extends Thread{
        @Override
        public void run(){
            while (LRBtn){
                try {
                    if(mainObject.x >= width){
                        nextgame();
                    }
                    if(mainObject.x <= 0){mainObject.x = 5;}
                    if(LRch){
                        mainObject.x += 13 ;
                        Thread.sleep(50);
                    }
                    else {
                        mainObject.x -= 13;
                        Thread.sleep(50);
                    }

                }catch (InterruptedException e){
                    Log.v("*error*","*** : " + e);
                }

            }
        }
    }
    public  class  obThread extends Thread{
        @Override
        public void run(){
            super.run();
            while (controlThread){
                //跳躍動作
                try {
                    for (int a=0;a<20;a++){
                        mainObject.y -= 10 ;
                        Thread.sleep(20);
                    }
                    while (true){
                    mainObject.y += 10 ;
                    Thread.sleep(20);
                        if(mainObject.y >= height-415){
                            break;
                        }
                    }
                    mainObject.y = height-415 ;
                    controlThread =false;
                    Clickpassage =true;
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);                                   //背景顏色
        canvas.drawRect((width*0.25f), 0, (width*0.25f)+50, height, paint);       //綠色直線
        canvas.drawRect((width*0.25f), height-290, width, height, paint);       //綠色橫線
        canvas.drawRect(50, height-250, 200, height, paintBtn);       //繪製按鈕
        canvas.drawRect(240, height-250, 390, height, paintBtn);       //繪製按鈕
        canvas.drawText("L",75,height-100,printLR);                         //按鈕文字
        canvas.drawText("R",265,height-100,printLR);                        //按鈕文字
        canvas.drawText("  累積球數 : " + String.valueOf(maxball),(width*0.25f),height-100,paintScoreString);                        //得分文字


        // 繪製球 並放入陣列儲存
        MyCircle mc;
        for (int i =0;i<circleList.size();i++){
                mc = (MyCircle) circleList.get(i);
                canvas.drawCircle(mc.x,mc.y,mc.rad,mc.paint);
        }
        //繪製圖片主物件
        canvas.drawBitmap(mainObject.bitmap ,mainObject.x,mainObject.y,null);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int TouchCount = event.getPointerCount();
        if(TouchCount == 2 && Clickpassage){
            Clickpassage =false;                    //關閉通道 限制跳躍完成前 無法再次點擊跳躍
            controlThread = true;
            obThread Obthread = new obThread();     //控制主物件的執行續
            Obthread.start();               // mainobject 跳躍的執行續
        }
        if(event.getAction() == MotionEvent.ACTION_UP &&
                (event.getX() >= Lbtn.left && event.getX() <= Lbtn.right && event.getY() <= Lbtn.bottom && event.getY() >= Lbtn.top ||
                event.getX() >= Rbtn.left && event.getX() <= Rbtn.right && event.getY() <= Rbtn.bottom && event.getY() >= Rbtn.top)){
            LRBtn = false ;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if(event.getX() >= Lbtn.left && event.getX() <= Lbtn.right && event.getY() <= Lbtn.bottom && event.getY() >= Lbtn.top){
               LRch = false;
               LRBtn =true;
                LRThread lrThread = new LRThread();
                lrThread.start();
            }
            else  if(event.getX() >= Rbtn.left && event.getX() <= Rbtn.right && event.getY() <= Rbtn.bottom && event.getY() >= Rbtn.top){
               LRch = true;
                LRBtn =true;
                LRThread lrThread = new LRThread();
                lrThread.start();
            }
            else {
                if(Clickpassage){
                    Clickpassage =false;                    //關閉通道 限制跳躍完成前 無法再次點擊跳躍
                     controlThread = true;
                    obThread Obthread = new obThread();     //控制主物件的執行續
                    Obthread.start();               // mainobject 跳躍的執行續
                }
            }
        }

        invalidate();
        return  true;
    }
    //判斷球體邊界碰撞
    private void CheakIfAddBall(){
        int speed = (int) (Math.random()*30 + 20);
        if(circleList.size()<maxball && (int)(Math.random()*50)<2){
                MyCircle myCircle = new MyCircle(700,100,10,speed,this.width,this.height-290);     //控制球產生的範圍、速度、大小、出現地點
                circleList.add(myCircle);
        }

    }
    private  void  CheakTouch(){
        MyCircle tempc;
        for (int c=0 ; c<circleList.size();c++){
            tempc = (MyCircle) circleList.get(c);
            if(mainObject.getRect().intersect(tempc.getRect())){
                gameover();
            }

        }
    }
    //移動球體
    public void moveball() {
        MyCircle mcm;
        for (int i = 0; i < circleList.size(); i++) {
            mcm = (MyCircle) circleList.get(i);
            mcm.move();
            invalidate();
        }
    }
    public void nextgame(){
        mainObject.x = 5 ;
        maxball ++ ;
        Boolnext = true ;
        if(Boolnext){
            for(int i =0 ;i<maxball-1;i++)
            {
                int speed = (int) (Math.random()*30 + 20);
                MyCircle myCircle = new MyCircle(700,100,10,speed,this.width,this.height-290);     //控制球產生的範圍、速度、大小、出現地點
                circleList.set(i,myCircle);
            }
            Boolnext = false;
        }
    }
    private void gameover(){
        mainObject.x = 5 ;
        mainObject.y = height-415 ;
    }
}