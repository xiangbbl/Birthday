package com.example.birthday;

public class Items {
    private int mImage;
    private String mText1;
    private String mText2;

    public Items(int mImage, String t1, String t2){
        this.mImage = mImage;
        this.mText1 = t1;
        this.mText2 = t2;
    }

    public int getImage(){
        return mImage;
    }
    public String getText1(){
        return mText1;
    }

    public String getText2(){
        return mText2;
    }

}
