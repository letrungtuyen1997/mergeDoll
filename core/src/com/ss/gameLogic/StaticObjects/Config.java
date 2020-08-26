package com.ss.gameLogic.StaticObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.ss.core.util.GStage;
import com.ss.object.InfoDoll;

public class Config {
  //constant
  public static float ratioX = GStage.getWorldWidth()/720;
  public static float ratioY = GStage.getWorldHeight()/1280;
  public static float ratioYRoot = (float) Gdx.graphics.getWidth()/720;
  public static float WidthScreen = GStage.getWorldWidth();
  public static float HeightScreen = GStage.getWorldHeight();
  public static long monney =100000;
  public static long monneyRuntime =0;
  public static long Exp =0;
  public static int TypeRender =1;
//  public static long Price =TypeRender*10;
  public static boolean checkRuntime =false;
  public static boolean LockTime =false;
  public static boolean RemoveDoll =false;
  public static float ConverPosMouseX(float inputX){
    return (float)inputX/ratioYRoot;
  }
  public static int TypeMax = 1;
  public static boolean CheckRemoveDoll = false;
  public static int indexMusic=0;
  public static boolean X2Coin = false;
  public static int checkShowAds = 0;


  public static String compressCoin(long num, int numOf){
    String str = "0";
    String dv = "";
    int ratio = 0;
    double x = 0;

    if(num >= 1000000000){
      ratio = 1000000000;
      dv = "B";
    }
    else if(num >= 1000000){
      ratio = 1000000;
      dv = "M";
    }
    else if(num >= 1000){
      ratio = 1000;
      dv = "K";
    }
    else {
      ratio = 1;
      dv = "";
    }
    x = (double)num/ratio;
    x = Math.floor(x*Math.pow(10, numOf))/Math.pow(10, numOf);
    str = x + dv;


    String strTemp = str.substring(str.length() - 2, str.length());
    if(strTemp.equals(".0")){
      str = str.substring(0, str.length() - 2);
    }
    else {
      strTemp = str.substring(str.length() - 3, str.length()-1);
      if(strTemp.equals(".0")){
        str = str.substring(0, str.length()-3);
        str += dv;
      }
    }

    return str;
  }



}
