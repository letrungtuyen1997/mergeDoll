package com.ss.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
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
import com.ss.GMain;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.effects.effectWin;
import com.ss.gameLogic.StaticObjects.Config;
import com.ss.object.Flower;
import com.ss.object.TableListFlow;
import com.ss.object.TimerCountDown;

public class FarmScene  {
    private TextureAtlas atlas;
    private BitmapFont font;
    public Group group = new Group();
    private Image Lock;
    private Array<Image> ListLock = new Array<>();
    public Array<Flower> arrayFlower = new Array<>();
    public Array<Float> arrPercentMoney = new Array<>();
    public Array<Integer> arrFlowLv = new Array<>();
    private GameScene gameScene;
    private Label TimeWater;
    private Image labelWatch;
    private int second =20;
    private int tic=0;
    FarmScene(GameScene gameScene){
        this.gameScene = gameScene;
        initAtlas();
        loadBg();
        Fotter();
        body();
        setLvLock();
    }

    private void initAtlas(){
        atlas = GAssetsManager.getTextureAtlas("uiGame.atlas");
        font = GAssetsManager.getBitmapFont("font_white.fnt");
    }
    private void loadBg(){
        GStage.addToLayer(GLayer.top,group);
        Image bg = GUI.createImage(atlas,"bg2");
        bg.setWidth(bg.getWidth()*Config.ratioX);
        bg.setHeight(bg.getHeight()*Config.ratioY);
        group.addActor(bg);

    }
    private void Fotter(){
        Image fotter = GUI.createImage(atlas,"fotter2");
        fotter.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()-180, Align.center);
        group.addActor(fotter);
        Image btnWater = GUI.createImage(atlas,"btnWatering");
        btnWater.setPosition(GStage.getWorldWidth()/2,fotter.getY()+fotter.getHeight()/2-20,Align.center);
        group.addActor(btnWater);
        /////// label watch //////
        labelWatch = GUI.createImage(atlas,"labelWatch");
        labelWatch.setPosition(btnWater.getX(),btnWater.getY(),Align.center);
        group.addActor(labelWatch);


        Image btnListFl = GUI.createImage(atlas,"btnListFlow");
        btnListFl.setPosition(GStage.getWorldWidth()/2+btnListFl.getWidth()*3-40,btnWater.getY()+btnListFl.getHeight(),Align.center);
        group.addActor(btnListFl);
        Image btnDoll = GUI.createImage(atlas,"btnDoll");
        btnDoll.setPosition(GStage.getWorldWidth()/2-btnDoll.getWidth()*3+40,btnWater.getY()+btnDoll.getHeight(),Align.center);
        group.addActor(btnDoll);
//        Image btnMission = GUI.createImage(atlas,"btnMission");
//        btnMission.setPosition(GStage.getWorldWidth()/2+btnMission.getWidth()*2-30,btnWater.getY()+btnMission.getHeight(),Align.center);
//        group.addActor(btnMission);
//        Image btnX2coin = GUI.createImage(atlas,"btnX2Coin");
//        btnX2coin.setPosition(GStage.getWorldWidth()/2+btnX2coin.getWidth()*3-20,btnWater.getY()+btnMission.getHeight(),Align.center);
//        group.addActor(btnX2coin);
        //////// event btn /////
        eventBtnDoll(btnDoll);
        eventListFlow(btnListFl);
        eventbtnWater(btnWater);

    }
    private void body(){
        for (int i =0; i<4;i++){
            for(int j=0;j<3;j++){
                Image plot = GUI.createImage(atlas,"Landplot");
                plot.setPosition(GStage.getWorldWidth()/2-(plot.getWidth()+30)+((plot.getWidth()+30)*j),GStage.getWorldHeight()/2-(plot.getHeight()*2)+((plot.getHeight()+60)*i),Align.center);
                group.addActor(plot);
                Lock = GUI.createImage(atlas,"Lock");
                Lock.setPosition(plot.getX()+plot.getWidth()/2,plot.getY()+plot.getHeight()/2,Align.center);
                group.addActor(Lock);
                ListLock.add(Lock);
            }
        }
        for (int i=0;i<ListLock.size;i++){
            if(CheckLock(i)==1){
                //////// load flow //////
                Gson gson = new Gson();
                String ss = GMain.prefs.getString("ListFlowLv");
                JsonArray arr = gson.fromJson(ss, JsonArray.class);
                ListLock.get(i).setVisible(false);
                System.out.println("check:"+arr);
                if(arr!=null){
                    int type = arr.get(i).getAsInt();
                    Flower flower = new Flower(ListLock.get(i).getX()+ListLock.get(i).getWidth()/2,ListLock.get(i).getY()-ListLock.get(i).getHeight()/2+20,type,atlas,font,this,gameScene.header);
                    arrayFlower.add(flower);
                    arrPercentMoney.add(flower.action.getTime());
                    arrFlowLv.add(flower.type);
                }else {
                    Flower flower = new Flower(ListLock.get(i).getX()+ListLock.get(i).getWidth()/2,ListLock.get(i).getY()-ListLock.get(i).getHeight()/2+20,1,atlas,font,this,gameScene.header);
                    arrayFlower.add(flower);
                    arrPercentMoney.add(flower.action.getTime());
                    arrFlowLv.add(flower.type);
                }

            }
        }
    }
    public void checkNewFlow(){
        for (int i=0;i<ListLock.size;i++){
            if(CheckLock(i)==1&&i>=arrayFlower.size){
                ListLock.get(i).setVisible(false);
                Flower flower = new Flower(ListLock.get(i).getX()+ListLock.get(i).getWidth()/2,ListLock.get(i).getY()-ListLock.get(i).getHeight()/2+20,1,atlas,font,this,gameScene.header);
                arrayFlower.add(flower);
                arrPercentMoney.add(flower.action.getTime());
                arrFlowLv.add(flower.type);
                arrFlowLv.add(flower.type);
            }
        }
        saveArrFlow();

    }
    private int CheckLock(int index){
        if(index<3&&Config.TypeMax<=8)
            return 1;
        if(index<5&&Config.TypeMax>8&&Config.TypeMax<=12)
            return 1;
        if(index<7&&Config.TypeMax>12&&Config.TypeMax<=14)
            return 1;
        if(index<9&&Config.TypeMax>14&&Config.TypeMax<=17)
            return 1;
        if(index<10&&Config.TypeMax==18)
            return 1;
        if(index<11&&Config.TypeMax==19)
            return 1;
        if(index<12&&Config.TypeMax==20)
            return 1;
        return -1;
    }
    private void setLvLock(){
        if(ListLock!=null){
            for (int i=0;i<ListLock.size;i++){
                if(i<2){
                    ListLock.get(i).setName("Lv1");
                }
                if(i>=2&&i<5){
                    ListLock.get(i).setName("Lv9");
                }
                if(i>=5&&i<7){
                    ListLock.get(i).setName("Lv12");
                }
                if(i>=7&&i<=8){
                    ListLock.get(i).setName("Lv14");
                }
                if(i==9){
                    ListLock.get(i).setName("Lv18");
                }
                if(i==10){
                    ListLock.get(i).setName("Lv19");
                }
                if(i==11){
                    ListLock.get(i).setName("Lv20");
                }
            }
            for (int i=0;i<ListLock.size;i++){
                if(ListLock.get(i).isVisible()==true){
                    Label Lv = new Label(ListLock.get(i).getName(),new Label.LabelStyle(font,Color.BROWN));
                    Lv.setFontScale(0.5f);
                    Lv.setOrigin(Align.center);
                    Lv.setAlignment(Align.center);
                    Lv.setPosition(ListLock.get(i).getX()+20,ListLock.get(i).getY()+90,Align.center);
                    group.addActor(Lv);
                }
            }
        }

    }
    public void upgradeFlow(Flower Flow){
          int index = arrayFlower.indexOf(Flow,true);
          Flower flower = new Flower(ListLock.get(index).getX()+ListLock.get(index).getWidth()/2,ListLock.get(index).getY()-ListLock.get(index).getHeight()/2+20,arrayFlower.get(index).type+=1,atlas,font,this,gameScene.header);
         arrayFlower.get(index).dispose();
         arrayFlower.set(index,flower);
         arrFlowLv.set(index,flower.type);
         setStatus();
         saveArrFlow();
         paticleLight2(flower.x,flower.y);

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
    private void saveArrFlow(){
//        Gson gson = new Gson();
//        String S = gson.toJson(arrFlowLv);
        GMain.prefs.putString("ListFlowLv",arrFlowLv.toString());
        GMain.prefs.flush();

    }
    public void setStatus(){
        for (int i=0;i<arrayFlower.size;i++){
            if(arrayFlower.get(i).target<=Config.Exp){
                arrayFlower.get(i).up_Off.setVisible(false);
            }else {
                arrayFlower.get(i).up_Off.setVisible(true);

            }
        }
    }
    private void eventBtnDoll(Image btn){
        btn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gameScene.foter.showFullScreen();
                SoundEffect.Play(SoundEffect.click);
                gameScene.eventBtnFarm(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

    }
    public void updatePecentMoney(Flower flower){
        int index = arrayFlower.indexOf(flower,true);
        arrPercentMoney.set(index,arrayFlower.get(index).action.getTime());

    }
    public long updateMoney(Flower flower,long monney){
        int index = arrayFlower.indexOf(flower,true);
        double Monney = (arrPercentMoney.get(index)/100)*monney;
        System.out.println(Math.round(Monney));
        return  Math.round(Monney);
    }
    private void eventListFlow(Image btn){
        btn.setOrigin(Align.center);
        btn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gameScene.foter.showFullScreen();
                SoundEffect.Play(SoundEffect.click);
                btn.addAction(Actions.sequence(
                        Actions.scaleTo(0.8f,0.8f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f)
                ));
                new TableListFlow(atlas);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    private void eventbtnWater(Image btn){
        btn.setOrigin(Align.center);
        btn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                btn.setTouchable(Touchable.disabled);
                btn.addAction(Actions.sequence(
                        Actions.scaleTo(1.2f,1.2f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f),
                        GSimpleAction.simpleAction((d,a)->{
                            btn.setTouchable(Touchable.enabled);
                            showfrmWatch();
                            return true;
                        })
                ));

                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    private void showfrmWatch(){
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
        /////// frame Watch ////
        Image frmWatch = GUI.createImage(atlas,"frmWatch");
        frmWatch.setPosition(0,0,Align.center);
        group.addActor(frmWatch);
        /////// btn Watch /////
        Image btnWatch = GUI.createImage(atlas,"btnWatch");
        btnWatch.setOrigin(Align.center);
        btnWatch.setPosition(0,frmWatch.getHeight()/2-btnWatch.getHeight(),Align.center);
        group.addActor(btnWatch);
        ///// btn Close ////
        Image btnClose = GUI.createImage(atlas,"btnClose");
        btnClose.setOrigin(Align.center);
        btnClose.setPosition(-frmWatch.getWidth()/2+20,-frmWatch.getHeight()/2+20,Align.center);
        group.addActor(btnClose);
        ///// event btn close ////
        eventbtnclose(btnClose,group);
        eventbtnWatch(btnWatch,group);


    }
    private void eventbtnclose(Image btn,Group group){
        btn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                btn.addAction(Actions.sequence(
                        Actions.scaleTo(0.9f,0.9f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f)
                ));
                group.clear();
                group.remove();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    private void eventbtnWatch(Image btn,Group group){
        btn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                btn.addAction(Actions.sequence(
                        Actions.scaleTo(0.9f,0.9f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f)
                ));
                showAds(group);
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
                    harvest();
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
    private void harvest(){
        if(SoundEffect.music==false){
            SoundEffect.Pausemusic(Config.indexMusic);
            SoundEffect.Playmusic(SoundEffect.bg1);
            Config.indexMusic = SoundEffect.bg1;
        }
        Group group = new Group();
        GStage.addToLayer(GLayer.top,group);
        Image lightArrow = GUI.createImage(atlas,"harvest");
        lightArrow.setWidth(lightArrow.getWidth()*Config.ratioX);
        lightArrow.setHeight(lightArrow.getHeight()*Config.ratioY);
        group.addActor(lightArrow);
        /// animation ////
        aniLightArrow(lightArrow);
        /////// set visible group get monney ////
        setVisibleGrGetMoney(false);
        ////// action count down ////
        CountDown(group,()->{
            System.out.println("done!!!!!!!");
            group.clear();
            group.remove();
            setVisibleGrGetMoney(true);
            tic=0;
            second=20;
            if(SoundEffect.music==false){
                SoundEffect.Stopmusic(Config.indexMusic);
                SoundEffect.Playmusic(SoundEffect.bg0);
                Config.indexMusic = SoundEffect.bg0;
            }

        });
        ////// free dom //////
        freedomMoney(group);
        ////// animation Watering /////
        Image sprayer = GUI.createImage(atlas,"sprayer");
        sprayer.setPosition(GStage.getWorldWidth()-sprayer.getWidth(),sprayer.getHeight(),Align.center);
        group.addActor(sprayer);
        sprayer.addAction(Actions.sequence(
                Actions.moveBy(-500,300,0.5f),
                GSimpleAction.simpleAction((d,a)->{
                    AniSprayer(sprayer);
                    return true;
                })
        ));




    }
    private void CountDown(Group group,Runnable runnable){
        Label secondLabel = new Label("thời gian còn lại: "+second,new Label.LabelStyle(font,Color.BLUE));
        secondLabel.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()-200,Align.center);
        group.addActor(secondLabel);
        group.addAction(Actions.sequence(
                GSimpleAction.simpleAction((d,a)->{
                    tic++;
                    if(tic==60){
                        second-=1;
                        if(second>=0){
                            secondLabel.setText("thời gian còn lại: "+second);
                        }else {
                            group.addAction(Actions.run(runnable));
                        }
                        tic=0;
                    }
                    return false;
                })
        ));

    }
    private void freedomMoney(Group group){
        for (int i=0;i<arrayFlower.size;i++){
            Image coin = GUI.createImage(atlas,"coin");
            coin.setOrigin(Align.center);
            coin.setPosition(arrayFlower.get(i).x,arrayFlower.get(i).y+50,Align.center);
            group.addActor(coin);
            animationCoins(coin);
            takeMoney(group,coin,i);
        }
    }
    private void takeMoney(Group group,Image btn,int index){
        btn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.getcoin);
                Image coin = GUI.createImage(atlas,"coin");
                coin.setOrigin(Align.center);
                coin.setPosition(arrayFlower.get(index).x,arrayFlower.get(index).y+50,Align.center);
                group.addActor(coin);
                animationCoins(coin);
                takeMoney(group,coin,index);
                btn.clearActions();
                btn.addAction(Actions.sequence(
                        Actions.moveTo(gameScene.header.textMonney.getX(),gameScene.header.textMonney.getY(),0.2f),
                        Actions.scaleTo(0.2f,0.2f,0.2f),
                        GSimpleAction.simpleAction((d,a)->{
                            btn.clear();
                            btn.remove();
                            Config.monney+=arrayFlower.get(index).monney;
                            gameScene.header.SetMonney(Config.monney);
                            return true;
                        })
                ));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    private void animationCoins(Image img){
        img.addAction(Actions.sequence(
                Actions.moveBy(0,-50,0.7f),
                Actions.moveBy(0,50,0.2f,Interpolation.bounceOut),
                Actions.delay(1),
                GSimpleAction.simpleAction((d,a)->{
                    animationCoins(img);
                    return true;
                })
        ));

    }
    private void setVisibleGrGetMoney(boolean set){
        for (int i=0;i<arrayFlower.size;i++){
            arrayFlower.get(i).group2.setVisible(set);
        }
    }
    private void aniLightArrow(Image img){
        img.addAction(Actions.sequence(
                Actions.alpha(0.3f,1f),
                Actions.alpha(0.7f,1f),
                GSimpleAction.simpleAction((d,a)->{
                    aniLightArrow(img);
                    return true;
                })
        ));
    }
    private void AniSprayer(Image img){
        img.addAction(Actions.sequence(
                Actions.moveBy(500,100,2f),
                Actions.moveBy(-500,100,2f),
                Actions.moveBy(500,100,2f),
                Actions.moveBy(-500,100,2f),
                Actions.moveBy(500,-100,2f),
                Actions.moveBy(-500,-100,2f),
                Actions.moveBy(500,-100,2f),
                Actions.moveBy(-500,-100,2f),
                GSimpleAction.simpleAction((d,a)->{
                    AniSprayer(img);
                    return true;
                })
        ));

    }




}
