package com.example.kacha;

import android.graphics.Bitmap;

public class stack {
    public Bitmap[] BitmapStack=new Bitmap[20];
    public int top=-1;
    public int empty(){
        if(top==-1)
            return 1;
        else
            return 0;
    }
    public void push(Bitmap Image){
        BitmapStack[++top]=Image;
    }
    public Bitmap pop(){
        return BitmapStack[top--];
    }
    public Bitmap topBitmap(){
        return BitmapStack[top];
    }
}
