package com.ss.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.ss.GMain;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GArcMoveToAction;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GLayerGroup;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.data.Dolldata;
import com.ss.effects.SoundEffect;
import com.ss.effects.effectWin;
import com.ss.gameLogic.StaticObjects.Config;
import com.ss.object.DateUtils;
import com.ss.object.Doll2;
import com.ss.object.Flower;
import com.ss.object.Footer;
import com.ss.object.Header;
import com.ss.object.InfoDoll;
import com.ss.object.ListDollHistory;
import com.ss.object.Timer;
import com.ss.object.TimerCountDown;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static com.ss.object.DateUtils.DATE_PATTERN;
import static com.ss.object.DateUtils.getMillisFromDateString;

public class GameScene extends GScreen{
    private TextureAtlas atlas;
    private BitmapFont font;
    private GLayerGroup mainGroup;
    private Group GroupTop;
    private Image bg, footer;
    private Array<Doll2> arrDoll = new Array<>();
    private Array<Vector2> arrPosDoll = new Array<>();
    private Array<Vector2> arrPosDoll2 = new Array<>();
    private Array<Integer> listNull = new Array<>();
    private Timer timer;
    public Header header;
    public Footer foter;
    public boolean checkRuntime = false;
    public Image test;
    public Rectangle body;
    public FarmScene farmScene;
    private Array<InfoDoll> arrayInfoDoll = new Array<>();
    private ArrayList<Dolldata> dolldata = new ArrayList<>();
    private ArrayList<Dolldata> listDoll = new ArrayList<>();
    private TimerCountDown timerCountDown;


    @Override
    public void dispose() {

    }

    @Override
    public void init() throws Exception {
        SoundEffect.Playmusic(SoundEffect.bg0);
        Config.indexMusic = SoundEffect.bg0;
        initTexture();
        initGroup();
        initUI();
        loadListDoll();
        farmScene = new FarmScene(this);
        ////// set default/////
        eventBtnFarm(false);
        ///////
//        sumMoneyinOff();
        setMoneyonline();

    }

    private void initTexture(){

        atlas = GAssetsManager.getTextureAtlas("uiGame.atlas");
        font = GAssetsManager.getBitmapFont("font_white.fnt");
    }

    private void initGroup(){
        mainGroup = new GLayerGroup();
        GroupTop = new Group();
        GStage.addToLayer(GLayer.ui, mainGroup);
    }
    private void loadListDoll(){
        Gson Gs = new Gson();
        FileHandle js = Gdx.files.internal("data/Doll.json");
        String jsonStr = js.readString();
        JsonArray arrDollJs = Gs.fromJson(jsonStr, JsonArray.class);
        for (int i=0;i<arrDollJs.size();i++){
            InfoDoll info = Gs.fromJson(arrDollJs.get(i), InfoDoll.class);
            arrayInfoDoll.add(info);

        }
    }

    private void initUI(){
        bg = GUI.createImage(atlas, "bg1");
        bg.setWidth(bg.getWidth()*Config.ratioX);
        bg.setHeight(bg.getHeight()*Config.ratioY);
        mainGroup.addActor(bg);
        footer = GUI.createImage(atlas,"footer");
        footer.setWidth(footer.getWidth()*Config.ratioX);
        footer.setHeight(footer.getHeight()*Config.ratioY);
        footer.setOrigin(Align.center);
        footer.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()-footer.getHeight()/2,Align.center);
        mainGroup.addActor(footer);
        ////// render Arr Doll//////
        //new boardGame(atlas);
        createDoll();
        LoadOldData();
        timer = new Timer(GStage.getWorldWidth()-80,GStage.getWorldHeight()/2+300,atlas,font,()->{
            for(int i=0;i<arrDoll.size;i++){
              //  System.out.println("check: "+arrDoll.get(i));
            }
            if(checkNull()==1){
                createNewDoll(1);
                timer.ActionScaleTime();
            }
        });
        ///////// header /////
        header = new Header(atlas,font);
        //////// Footer //////
        foter = new Footer(atlas,font,this,header);

    }
    private void createDoll(){
        for(int i = 0;i<3;i++){
            for(int j =0; j<4;j++){
               // System.out.println(" create:"+(int)Math.floor(Math.random()*2+1));
                int type = 1;
                Doll2 doll2 = new Doll2(i,j,"spine/ske"+type+".atlas","spine/ske"+type+".json",type);
                doll2.addDrag(this);
                arrDoll.add(doll2);
                arrPosDoll.add(new Vector2(doll2.row,doll2.col));
                arrPosDoll2.add(new Vector2(doll2.group.getX(),doll2.group.getY()));
            }
        }
    }
    public int checkPosition(Doll2 cardC, Vector2 p1){
        Array<Integer> indexs = new Array<>();
        int i0;
        int indexResult = -1;
        for(i0 = 0; i0 < 12; i0++){
            if(cardC != arrDoll.get(i0)&&arrDoll.get(i0)!=null){
                if(checkRect(cardC, arrDoll.get(i0))){
                    indexs.add(i0);
                }
            }
        }
        if(indexs.size == 0) {
            return indexResult;
        }
        else {
            float min = getSize2Cards(cardC, arrDoll.get(indexs.get(0)));
            int indexMin = indexs.get(0);
            for(int i = 1; i < indexs.size; i++){
                float temp = getSize2Cards(cardC, arrDoll.get(indexs.get(i)));
                if(temp < min){
                    min = temp;
                    indexMin = indexs.get(i);
                }
            }
            indexResult = indexMin;
            if(cardC!=null&&arrDoll.get(indexResult)!=null){
                if(cardC.type!= arrDoll.get(indexResult).type||cardC.type==20&&arrDoll.get(indexResult).type==20){
                    swapCard(arrDoll, cardC, arrDoll.get(indexResult), p1);

                }else {
                    mergeDoll(arrDoll,cardC,arrDoll.get(indexResult));

                }
            }
            System.out.println("chose:"+cardC.type);
            System.out.println("be chose:"+arrDoll.get(indexResult).type);


            //swap card
          //  int index1 = arrDoll.indexOf(cardC, true);
           // Vector2 p = new Vector2(arrDoll.get(index1).group.getX(),arrDoll.get(index1).group.getY());

            //swapCard(arrDoll, cardC, arrDoll.get(indexResult), p1);

            return indexResult;
        }
    }

    private float getSize2Cards(Doll2 card1, Doll2 card2){
        float range;
        Vector2 vt1 = new Vector2(card1.group.getX(), card1.group.getY());
        Vector2 vt2 = new Vector2(card2.group.getX(), card2.group.getY());
        range = getSize2Point(vt1, vt2);
        return range;
    }

    private float getSize2Point(Vector2 vt1, Vector2 vt2){
        float range;
        float dx = (float) Math.pow(vt1.x - vt2.x, 2);
        float dy = (float) Math.pow(vt1.y - vt2.y, 2);
        range = (float) Math.sqrt(dx + dy);
        return range;
    }

    private boolean checkRect(Doll2 card1, Doll2 card2){
        float w = card1.group.getWidth();
        float h = card1.group.getHeight();
        float dx1 = Math.abs(card1.group.getX() - w/2 - card2.group.getX());
        float dy1 = Math.abs(card1.group.getY() - h/2 - card2.group.getY());
        float dx2 = Math.abs(card1.group.getX() + w/2 - card2.group.getX());
        float dy2 = Math.abs(card1.group.getY() - h/2 - card2.group.getY());
        float dx3 = Math.abs(card1.group.getX() - w/2 - card2.group.getX());
        float dy3 = Math.abs(card1.group.getY() + h/2 - card2.group.getY());
        float dx4 = Math.abs(card1.group.getX() + w/2 - card2.group.getX());
        float dy4 = Math.abs(card1.group.getY() + h/2 - card2.group.getY());

        return (dx1 < w/2 && dy1 < h/2) || (dx2 < w/2 && dy2 < h/2)
                || (dx3 < w/2 && dy3 < h/2) || (dx4 < w/2 && dy4 < h/2);
    }

    private void swapCard(Array<Doll2> cards, Doll2 card1, Doll2 card2, Vector2 p1){

        int index1 = cards.indexOf(card1, true);
        int index2 = cards.indexOf(card2, true);
        int rowTemp = card1.row;
        int colTemp = card1.col;

        int zIndexTemp = card1.group.getZIndex();
        try {
            cards.swap(index1, index2);
            card1.row = card2.row;
            card1.col = card2.col;
            card2.row = rowTemp;
            card2.col = colTemp;
            setTouchCards(Touchable.disabled);

            card1.group.addAction(Actions.sequence(
                    Actions.moveTo(card2.group.getX(), card2.group.getY(), 0.25f, Interpolation.fastSlow)
            ));

            card2.group.addAction(Actions.sequence(
                    Actions.moveTo(p1.x, p1.y, 0.25f, Interpolation.fastSlow),
                    GSimpleAction.simpleAction((d, a)->{
                        setTouchCards(Touchable.enabled);
                        return true;
                    })
            ));

            card1.group.setZIndex(card2.group.getZIndex());
            card2.group.setZIndex(zIndexTemp);

        }catch (Exception e){
            System.out.println(e);
        }

    }
    private void mergeDoll(Array<Doll2> arrDoll , Doll2 doll1, Doll2 doll2){
        doll1.clearListeners();
        doll2.clearListeners();
        Group group = new Group();
        Array<Image> arrImg = new Array<>();
        GStage.addToLayer(GLayer.top,group);
        for (int i=0;i<2;i++){
            Image img = GUI.createImage(atlas,""+doll1.type);
            img.setPosition(doll2.group.getX()-doll2.group.getWidth()/2+(doll2.group.getWidth()*i),doll2.group.getY());
            group.addActor(img);
            arrImg.add(img);
        }
        doll1.dispose();
        doll2.visible();
        arrImg.get(0).addAction(
                Actions.sequence(
                        Actions.moveBy(-30,0,0.1f),
                        Actions.moveBy(arrImg.get(0).getWidth()/2,0,0.15f)
                ));
        arrImg.get(1).addAction(Actions.sequence(
                Actions.moveBy(30,0,0.1f),
                Actions.moveBy(-arrImg.get(0).getWidth()/2,0,0.15f),
                GSimpleAction.simpleAction((d,a)->{
                    for (int i=0;i<arrImg.size;i++){
                        arrImg.get(i).remove();
                    }
                    arrImg.clear();
                        SoundEffect.Play(SoundEffect.merge);
                        int type = (doll2.type+1);
                        if(type>20){
                            type=20;
                        }
                        Doll2 doll = new Doll2(doll2.row,doll2.col,"spine/ske"+type+".atlas","spine/ske"+type+".json",(doll2.type+1));
                        doll.addDrag(this);
                        int i1= arrDoll.indexOf(doll1,true);
                        int i2= arrDoll.indexOf(doll2,true);
                        arrDoll.set(i1,null);
                        arrDoll.set(i2,doll);
                        doll2.dispose();
                        doll.group.setOrigin(Align.center);
                        doll.group.addAction(Actions.sequence(
                                Actions.scaleTo(1.2f,1.2f,0.15f),
                                Actions.scaleTo(1f,1f,0.15f)
                        ));
                        ///// particle love /////
                        paticleLight(doll.group.getX()+doll.group.getWidth()/2,doll.group.getY()+doll.group.getHeight()/2);
                        paticleLight2(doll.group.getX()+doll.group.getWidth()/2,doll.group.getY()+doll.group.getHeight()/2);
                        paticleLove(doll.group.getX()+doll.group.getWidth()/2,doll.group.getY()+doll.group.getHeight()/2);
                        Tweens.setTimeout(group,0.3f,()->{
                            setTouchCards(Touchable.enabled);
                        });
                        if(Config.checkRuntime==true)
                            timer.ActionScaleTime();
                        ////// donate exp //////
                        DonateExp(doll.group.getX()+doll.group.getWidth()/2,doll.group.getY()+doll.group.getHeight());
                        //////// annimation unlock new doll ////
                        if(checkTypeDollMax()==1){
                            animationUnlock();
                        }

                    return true;
                })
                ));
        ////// update new data//////////
//        saveData();
            saveStageDoll();
    }
    private void paticleLight(float x, float y){
       Image light = GUI.createImage(atlas,"light");
       light.setScale(0.6f);
       light.setOrigin(Align.center);
       light.setPosition(x,y,Align.center);
       mainGroup.addActor(light);
       light.addAction(Actions.sequence(
               Actions.parallel(
                       Actions.rotateTo(100,1f),
                       Actions.alpha(0,1f),
                       Actions.scaleTo(0,0,1f)
                       ),
               Actions.delay(1f),
               GSimpleAction.simpleAction((d,a)->{
                   light.clear();
                   light.remove();
                   return true;
               })
       ));

    }
    private void paticleLight2(float x, float y){
        Group group = new Group();
        GStage.addToLayer(GLayer.top,group);
        effectWin ef = new effectWin(2,0,30);
        group.addActor(ef);
        group.setPosition(x,y,Align.center);
        ef.start();
        Tweens.setTimeout(group,1.5f,()->{
            group.clear();
            group.remove();
        });
    }
    private void paticleLove(float x, float y){
        Group group = new Group();
        GStage.addToLayer(GLayer.top,group);
        effectWin ef = new effectWin(1,0,30);
        group.addActor(ef);
        group.setPosition(x,y,Align.center);
        ef.start();
        Tweens.setTimeout(group,1.5f,()->{
            group.clear();
            group.remove();
        });
    }
    private void particleUnlock(){
        Array<Group> particle = new Array<>();
        for(int i=0;i<10;i++){
            Group group = new Group();
            effectWin ef = new effectWin(3,0,0);
            group.addActor(ef);
            particle.add(group);

        }
        for (int i=0;i<particle.size;i++){
            int finalI = i;
                Tweens.setTimeout(mainGroup,0.1f*i,()->{
                    float x = (float) ((Math.random()*(GStage.getWorldWidth()-150))+150);
                    float y = (float) ((Math.random()*(GStage.getWorldHeight()-400))+200);
                    effectWin ef = (effectWin)particle.get(finalI).getChildren().get(0);
                    GStage.addToLayer(GLayer.top,particle.get(finalI));
                    ef.setPosition(x,y);
                    ef.start();
                    System.out.println("o999999999999999");
                    if(finalI==particle.size){
                        particle.clear();
                    }
                });
        }
    }

    public int checkNull(){
        listNull.clear();
        for (int i =0;i<arrDoll.size;i++){
            if(arrDoll.get(i) ==null){
                listNull.add(i);
            }
        }
        if(listNull.size!=0){
            return 1;
        }else {
            return 0;
        }
    }
    public void createNewDoll(int type){
        int index = (int)(Math.random()*listNull.size);
        Doll2 doll = new Doll2(5,4,"spine/ske"+type+".atlas","spine/ske"+type+".json",type);
        arrDoll.set(listNull.get(index),doll);

        float moveX = arrPosDoll2.get(listNull.get(index)).x;
        float moveY = arrPosDoll2.get(listNull.get(index)).y;
        float neoX1 = GStage.getWorldWidth()-50;
        float neoY1 = GStage.getWorldHeight()-100;
        float neoX2 = GStage.getWorldWidth()/2;
        float neoY2 = GStage.getWorldHeight()-100;
        float neoX=0,neoY=0;
        int id =(int)(Math.random()*2+1);
        if(id ==1){
            neoX = neoX1;
            neoY = neoY1;
        }else {
            neoX = neoX2;
            neoY = neoY2;
        }
        doll.row = (int)arrPosDoll.get(listNull.get(index)).x;
        doll.col = (int)arrPosDoll.get(listNull.get(index)).y;
        setTouchCards(Touchable.disabled);
            doll.group.addAction(Actions.sequence(
                   // Actions.moveTo(moveX,moveY,0.5f),
                    GArcMoveToAction.arcMoveTo(moveX,moveY,neoX,neoY,0.7f,Interpolation.slowFast),
                    GSimpleAction.simpleAction((d,a)->{
                        setTouchCards(Touchable.enabled);
                        doll.addDrag(this);
                        return true;
                    })
            ));
            //////// save data ////////
        saveStageDoll();
    }

    public void setTouchCards(Touchable touchable){
        for(Doll2 card : arrDoll){
            if(card!=null)
                card.group.setTouchable(touchable);
        }
    }
    public void setTouchDoll(Touchable touchable ,Doll2 doll){
        int index = arrDoll.indexOf(doll,true);
        for (int i=0;i<arrDoll.size;i++){
            if(arrDoll.get(i)!=null&&index!=i){
                arrDoll.get(i).setTouchable(touchable);
            }
        }
        for(Doll2 card : arrDoll){
            if(card!=null&&card!=doll)
                card.group.setTouchable(touchable);
        }
    }
    public void RemoveDoll(Doll2 doll){
        int i1= arrDoll.indexOf(doll,true);
        arrDoll.set(i1,null);
        doll.dispose();
      //  System.out.println("checkkkk:"+i1);
        foter.btnBin.setTouchable(Touchable.enabled);
        timer.resetAction();
        //timer.ActionScaleTime();
        DonateExp(foter.btnBin.getX(),foter.btnBin.getY());
    }
    private void DonateExp(float x,float y){
        GStage.addToLayer(GLayer.top, GroupTop);
        Image exp = GUI.createImage(atlas,"exp");
        exp.setPosition(x,y,Align.center);
        GroupTop.addActor(exp);
        exp.addAction(Actions.sequence(
                Actions.moveTo(header.textExp.getX(),header.textExp.getY(),1f),
                GSimpleAction.simpleAction((d,a)->{
                    Config.Exp+=1;
                    header.updateExp(Config.Exp);
                    System.out.println("kinh nghiem"+Config.Exp);
                    exp.clear();
                    exp.remove();
                    return true;
                })
        ));
    }
    public void renderDoll(int Type){
        if(checkNull()==1){
            createNewDoll(Type);
        }
    }

    public void eventBtnFarm(boolean Status){
        try {
            farmScene.checkNewFlow();

        }catch (Exception e){
            System.out.println(e);
        }
        farmScene.setStatus();
        //farmScene.header.updateExp(Config.Exp);
      //  farmScene.header.group.setVisible(Status);
        header.group.setZIndex(farmScene.group.getZIndex()+1);
        farmScene.group.setVisible(Status);
        for(Flower fl: farmScene.arrayFlower)
            fl.group.setVisible(Status);
    }
    private int checkTypeDollMax(){
        for(int i=0;i<arrDoll.size;i++){
            if(arrDoll.get(i)!=null){
                if(Config.TypeMax<arrDoll.get(i).type){
                    Config.TypeMax = arrDoll.get(i).type;
                    return 1;
                }
            }
        }
        return -1;
    }
    private void animationUnlock(){
        SoundEffect.Play(SoundEffect.unlock);
        setTypeRender();
        Group group = new Group();
        GStage.addToLayer(GLayer.top,group);
        darkScreen(group);
        //////// light //////
        Image light = GUI.createImage(atlas,"light");
        light.setOrigin(Align.center);
        light.setPosition(0,-light.getHeight()/2,Align.center);
        group.addActor(light);
        animationLight(light);
        /////// frame un lock///////
        Image frmUnlock = GUI.createImage(atlas,"frmUnlock");
        frmUnlock.setPosition(0,0,Align.center);
        group.addActor(frmUnlock);
        ////// frm avt //////
        Image frmAvt =GUI.createImage(atlas,"frmAvt");
        frmAvt.setPosition(0,-(frmAvt.getHeight()),Align.center);
        group.addActor(frmAvt);
        ////// avt /////
        Image avt = GUI.createImage(atlas,"avt"+Config.TypeMax);
        avt.setPosition(frmAvt.getX()+frmAvt.getWidth()/2,frmAvt.getY()+frmAvt.getHeight()/2,Align.center);
        group.addActor(avt);
        ////// name //////
        Label name = new Label(""+arrayInfoDoll.get(Config.TypeMax-1).name,new Label.LabelStyle(font,null));
        name.setFontScale(0.7f);
        name.setAlignment(Align.center);
        name.setPosition(0,frmAvt.getY()+frmAvt.getHeight()+40,Align.center);
        group.addActor(name);
        ///// info /////
        Label info = new Label(""+arrayInfoDoll.get(Config.TypeMax-1).info,new Label.LabelStyle(font,null));
        info.setFontScale(0.4f);
        info.setAlignment(Align.left);
        info.setPosition(150,-120,Align.center);
        group.addActor(info);
        /////// btn close ////
        Image btnClose = GUI.createImage(atlas,"btnClose");
        btnClose.setOrigin(Align.center);
        btnClose.setPosition(frmUnlock.getWidth()/2-20,-frmUnlock.getHeight()/2+20,Align.center);
        group.addActor(btnClose);
        ////// event btn close //////
        btnClose.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btnClose.addAction(Actions.sequence(
                        Actions.scaleTo(0.9f,0.9f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f)
                ));
                group.clear();
                group.remove();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        group.setScaleX(0);
        group.setOrigin(Align.center);
        group.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,Align.center);
        group.addAction(Actions.scaleTo(1,1,0.3f,Interpolation.swingOut));
        particleUnlock();

    }
    private void darkScreen(Group group){
        final GShapeSprite blackOverlay = new GShapeSprite();
        blackOverlay.createRectangle(true, -GStage.getWorldWidth(),-GStage.getWorldHeight()/2, GStage.getWorldWidth()*2, GStage.getWorldHeight()*2);
        blackOverlay.setColor(0,0,0,0.7f);
        group.addActor(blackOverlay);
    }
    private void animationLight(Image img){
        img.addAction(Actions.sequence(
                Actions.rotateBy(360,5f),
                GSimpleAction.simpleAction((d,a)->{
                    animationLight(img);
                    return true;
                })
                ));
    }
    private void setTypeRender(){
        if(Config.TypeMax>=7){
            Config.TypeRender = Config.TypeMax-5;
            foter.lv.setText(""+arrayInfoDoll.get(Config.TypeRender-1).id);
            foter.Price.setText(""+arrayInfoDoll.get(Config.TypeRender-1).price);
        }

    }

    // TODO: 2020-02-20 save status///


    private void saveStageDoll(){
        if(dolldata.size()!=0){
            dolldata.clear();
        }
        for(Doll2 doll2 : arrDoll){
            Dolldata data = new Dolldata();
            if(doll2!=null){
                data.setRow(doll2.row);
                data.setCol(doll2.col);
                data.setType(doll2.type);
                data.setIndex(arrDoll.indexOf(doll2,true));
                dolldata.add(data);
            }
        }

        Gson gson = new Gson();
        String S = gson.toJson(dolldata);
        GMain.prefs.putString("ListDollinGame",S);
        //////// save monney //////
        GMain.prefs.putLong("monney",Config.monney);
        /////// save exp /////
        GMain.prefs.putLong("exp",Config.Exp);
        /////// save typeMax /////
        GMain.prefs.putInteger("typeMax",Config.TypeMax);
        /////// save typeRender /////
        GMain.prefs.putInteger("typeRender",Config.TypeRender);
        GMain.prefs.flush();



    }

    private void LoadOldData(){
        int checkfirst = GMain.prefs.getInteger("typeMax");
        if(checkfirst!=0){
            for (Doll2 d : arrDoll ){
                d.dispose();
            }
            arrDoll.clear();
            //arrPosDoll.clear();
            //arrPosDoll2.clear();
            LoadData();
        }

    }

    public void LoadData(){
        Gson gson = new Gson();
        String ss = GMain.prefs.getString("ListDollinGame");
        JsonArray arr = gson.fromJson(ss, JsonArray.class);

        for (JsonElement ob : arr){
            Dolldata dt = gson.fromJson(ob,Dolldata.class);
            listDoll.add(dt);
        }
        for (int i=0;i<12;i++){
            arrDoll.add(null);
        }
        for (Dolldata dt2 : listDoll){
            Doll2 doll2 = new Doll2(dt2.getRow(),dt2.getCol(),"spine/ske"+dt2.getType()+".atlas","spine/ske"+dt2.getType()+".json",dt2.getType());
            doll2.addDrag(this);
            arrDoll.set(dt2.getIndex(),doll2);
            arrPosDoll.set(dt2.getIndex(),new Vector2(doll2.row,doll2.col));
            arrPosDoll2.set(dt2.getIndex(),new Vector2(doll2.group.getX(),doll2.group.getY()));
        }
        Config.monney = GMain.prefs.getLong("monney");
        Config.Exp = GMain.prefs.getLong("exp");
        Config.TypeMax = GMain.prefs.getInteger("typeMax");
        Config.TypeRender = GMain.prefs.getInteger("typeRender");
    }
    public void saveTime(){
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date resultdate = new Date(yourmilliseconds);
                String datetime = sdf.format(resultdate);
//                String datetime = "2020-02-29 9:50:00";
                GMain.prefs.putString("Timeoffline",datetime);
                GMain.prefs.flush();

    }
    public void setMoneyonline() throws Exception {
        String date1 = GMain.prefs.getString("Timeoffline");
        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date resultdate = new Date(yourmilliseconds);
        String date2 = sdf.format(resultdate);
        long d1 = getMillisFromDateString(date1, DATE_PATTERN);
        long d2 = getMillisFromDateString(date2, DATE_PATTERN);
        int dayDiff = DateUtils.hourDiff(d1,d2);
        ////// money make in 5s //////
        Long moneyRunTime = GMain.prefs.getLong("moneyRuntime");
        ///// money make when offline ////

        Long moneyOffLine = ((((dayDiff*60)/5)*3)/2)*moneyRunTime;
        ///////////// frame take money////////
        if(moneyOffLine>0){
            Config.monney+=moneyOffLine;
            header.SetMonney(Config.monney);
            Group group = new Group();
            GStage.addToLayer(GLayer.top,group);
            final GShapeSprite blackOverlay = new GShapeSprite();
            blackOverlay.createRectangle(true, -GStage.getWorldWidth(),-GStage.getWorldHeight()/2, GStage.getWorldWidth()*2, GStage.getWorldHeight()*2);
            blackOverlay.setColor(0,0,0,0.5f);
            group.addActor(blackOverlay);
            group.setScaleX(0);
            group.setOrigin(Align.center);
            group.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,Align.center);
            group.addAction(Actions.scaleTo(1,1,0.3f, Interpolation.swingOut));
            Image frmTakeMoney = GUI.createImage(atlas,"frmTakeMoney");
            frmTakeMoney.setPosition(0,0, Align.center);
            group.addActor(frmTakeMoney);
            /////// label money ////
            Label label = new Label(""+Config.compressCoin(moneyOffLine,2),new Label.LabelStyle(font, Color.BROWN));
            label.setFontScale(0.8f);
            label.setOrigin(Align.center);
            label.setAlignment(Align.center);
            label.setPosition(0,120,Align.center);
            group.addActor(label);
            ////// btn Close /////
            Image btnClose = GUI.createImage(atlas,"btnClose");
            btnClose.setPosition(-frmTakeMoney.getWidth()/2+20,-frmTakeMoney.getHeight()/2+20,Align.center);
            group.addActor(btnClose);
            btnClose.addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    group.clear();
                    group.remove();
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
            //////// btn x2 coin //////
            Image btnX2Coin = GUI.createImage(atlas,"btnX2Coin2");
            btnX2Coin.setOrigin(Align.center);
            btnX2Coin.setPosition(0,frmTakeMoney.getHeight()/2-btnX2Coin.getHeight()-20,Align.center);
            group.addActor(btnX2Coin);
            btnX2Coin.addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    SoundEffect.Play(SoundEffect.click);
                    updateMoneyOffline(moneyOffLine,group);
                    return super.touchDown(event, x, y, pointer, button);
                }
            });

        }


    }
    private void updateMoneyOffline(long money,Group group){
        if(GMain.platform.isVideoRewardReady()) {
            GMain.platform.ShowVideoReward((boolean success) -> {
                if (success) {
                    GMain.platform.ShowFullscreen();
                    group.clear();
                    group.remove();
                    SoundEffect.Play(SoundEffect.click);
                    Config.monney+=money;
                    header.SetMonney(Config.monney);
                }else {
                    GMain.platform.ShowFullscreen();
                    group.clear();
                    group.remove();
                }
            });
        }else {
            Label notice = new Label("Kiểm tra kết nối",new Label.LabelStyle(font, Color.RED));
            notice.setPosition(0,0,Align.center);
            group.addActor(notice);
            notice.addAction(Actions.sequence(
                    Actions.moveBy(0,-50,0.5f),
                    GSimpleAction.simpleAction((d, a)->{
                        notice.clear();
                        notice.remove();
                        return true;
                    })
            ));

        }
    }
    public void X2Coin(){
        Group group = new Group();
        GStage.addToLayer(GLayer.top,group);
        final GShapeSprite blackOverlay = new GShapeSprite();
        blackOverlay.createRectangle(true, -GStage.getWorldWidth(),-GStage.getWorldHeight()/2, GStage.getWorldWidth()*2, GStage.getWorldHeight()*2);
        blackOverlay.setColor(0,0,0,0.5f);
        group.addActor(blackOverlay);
        group.setScaleX(0);
        group.setOrigin(Align.center);
        group.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,Align.center);
        group.addAction(Actions.scaleTo(1,1,0.3f, Interpolation.swingOut));
        ////// frame watch //////
        Image frm = GUI.createImage(atlas,"frmWatchAds");
        frm.setPosition(0,0,Align.center);
        group.addActor(frm);
        //// button  watch ////
        Image btnWath = GUI.createImage(atlas,"btnWatch");
        btnWath.setPosition(0,frm.getHeight()/2-btnWath.getHeight(),Align.center);
        group.addActor(btnWath);
        ///// eventBtnWatch ////
        btnWath.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
               showAds(group);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        //// button  close ////
        Image btnClose= GUI.createImage(atlas,"btnClose");
        btnClose.setPosition(-frm.getWidth()/2+20,-frm.getHeight()/2+20,Align.center);
        group.addActor(btnClose);
        btnClose.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                foter.btnX2Coin.setTouchable(Touchable.enabled);
                group.clear();
                group.remove();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

    }
    void showAds(Group group){
        if(GMain.platform.isVideoRewardReady()) {
            GMain.platform.ShowVideoReward((boolean success) -> {
                if (success) {
                    GMain.platform.ShowFullscreen();
                    group.clear();
                    group.remove();
                    SoundEffect.Play(SoundEffect.click);
                    Config.X2Coin=true;
                    timerCountDown = new TimerCountDown(foter.btnX2Coin.getX()+foter.btnX2Coin.getWidth()/2,foter.btnX2Coin.getY()+foter.btnX2Coin.getHeight(),()->{
                        Config.X2Coin=false;
                        timerCountDown.dispose();
                        foter.btnX2Coin.setTouchable(Touchable.enabled);
                    });
                }else {
                    GMain.platform.ShowFullscreen();
                    group.clear();
                    group.remove();
                    foter.btnX2Coin.setTouchable(Touchable.enabled);
                }
            });
        }else {
            Label notice = new Label("Kiểm tra kết nối",new Label.LabelStyle(font, Color.RED));
            notice.setPosition(0,0,Align.center);
            group.addActor(notice);
            notice.addAction(Actions.sequence(
                    Actions.moveBy(0,-50,0.5f),
                    GSimpleAction.simpleAction((d, a)->{
                        notice.clear();
                        notice.remove();
                        return true;
                    })
            ));

        }
    }


    @Override
    public void run() {

    }
}