package com.ss.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ss.GMain;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.StaticObjects.Config;
import com.ss.scenes.FarmScene;
import com.ss.scenes.GameScene;

import java.util.Set;

public class Footer {
    private Group group = new Group();
    private TextureAtlas atlas;
    private BitmapFont font;
    private GameScene gameScene;
    public Image btnRender,btnRender2, btnListDoll, btnHistory, btnFarm, btnX2Coin, btnBin, btnTutorial;
    Rectangle bodyBin=new Rectangle();
    public Label lv, Price;
    private Header header;
    private InfoDoll infoDoll;
    private Array<InfoDoll> arrayInfoDoll = new Array<>();



    public Footer(TextureAtlas atlas, BitmapFont font, GameScene gameScene,Header header){
        this.atlas = atlas;
        this.font = font;
        this.gameScene = gameScene;
        this.header = header;
        GStage.addToLayer(GLayer.ui,group);
        loadListDoll();
        //////// btnRender /////////
        btnRender = GUI.createImage(atlas,"btnRender");
        btnRender.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()-btnRender.getHeight(), Align.center);
        group.addActor(btnRender);
        /////// lv ///////
        lv = new Label("lv."+arrayInfoDoll.get(Config.TypeRender-1).id,new Label.LabelStyle(font,null));
        lv.setFontScale(0.5f);
        lv.setAlignment(Align.center);
        lv.setPosition(btnRender.getX()+btnRender.getWidth()/2,btnRender.getY()+btnRender.getHeight()/2-20,Align.center);
        group.addActor(lv);
        /////// price //////
        Price = new Label(""+Config.compressCoin((arrayInfoDoll.get(Config.TypeRender-1).price),3) ,new Label.LabelStyle(font, Color.BROWN));
        Price.setFontScale(0.4f);
        Price.setAlignment(Align.center);
        Price.setPosition(btnRender.getX()+btnRender.getWidth()/2,btnRender.getY()+btnRender.getHeight()/2+25,Align.center);
        group.addActor(Price);

        btnRender2 = GUI.createImage(atlas,"btnRender");
        btnRender2.setColor(Color.CLEAR);
        btnRender2.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()-btnRender.getHeight(), Align.center);
        group.addActor(btnRender2);

        /////// btnListDoll //////
        btnListDoll = GUI.createImage(atlas,"btnListDoll");
        btnListDoll.setPosition(GStage.getWorldWidth()/2-btnListDoll.getWidth()*2,GStage.getWorldHeight()-btnListDoll.getHeight()-20,Align.center);
        group.addActor(btnListDoll);
        /////// btnHistory //////
        btnHistory = GUI.createImage(atlas,"btnHistory");
        btnHistory.setPosition(btnListDoll.getX()-btnHistory.getWidth(),GStage.getWorldHeight()-btnHistory.getHeight()-20,Align.center);
        group.addActor(btnHistory);
        /////// btnFarm ///////
        btnFarm = GUI.createImage(atlas,"btnFarm");
        btnFarm.setPosition(GStage.getWorldWidth()/2+btnFarm.getWidth()*2,GStage.getWorldHeight()-btnFarm.getHeight()-20,Align.center);
        group.addActor(btnFarm);
        //////// btn X2Coin /////
        btnX2Coin = GUI.createImage(atlas,"btnX2Coin");
        btnX2Coin.setPosition(btnX2Coin.getWidth()/2+20,GStage.getWorldHeight()/2+410,Align.center);
        group.addActor(btnX2Coin);
        /////// btn Bin //////
        btnBin = GUI.createImage(atlas,"btnBin");
        btnBin.setPosition(btnX2Coin.getWidth()*2,GStage.getWorldHeight()/2+410,Align.center);
        group.addActor(btnBin);
        bodyBin.setSize(btnBin.getWidth(),btnBin.getHeight());
        bodyBin.setPosition(btnBin.getX(),btnBin.getY());
        //////// btn tuTorial /////
        btnTutorial = GUI.createImage(atlas,"btnTutorial");
        btnTutorial.setOrigin(Align.center);
        btnTutorial.setPosition(btnFarm.getX()+btnFarm.getWidth()*2,GStage.getWorldHeight()-btnTutorial.getHeight()-30,Align.center);
        group.addActor(btnTutorial);
        Anibtn(btnTutorial);
        //////// event Bin ///////
        eventBin();
        eventBtnRender();
        eventBtnFarm();
        eventBtnShop();
        enventBtnHistory();
        enventBtnX2coin();
        eventTutorial();

    }
    private void loadListDoll(){
        boolean checkFirst = GMain.prefs.getBoolean("checkFirstListDoll");

        Gson Gs = new Gson();
        FileHandle js = Gdx.files.internal("data/Doll.json");
        String jsonStr = js.readString();
        JsonArray arrDollJs = Gs.fromJson(jsonStr, JsonArray.class);
        for (int i=0;i<arrDollJs.size();i++){
            InfoDoll info = Gs.fromJson(arrDollJs.get(i), InfoDoll.class);
            arrayInfoDoll.add(info);
        }
        if(checkFirst==true){
            String S = GMain.prefs.getString("listShopDoll");
            JsonObject obj = Gs.fromJson(S, JsonObject.class);
            Set<String> keys = obj.keySet();
            String key = keys.iterator().next();
            if (key != null) {
                JsonArray arr = obj.getAsJsonArray(key);
                for (int i = 0; i < arrayInfoDoll.size; i++) {
                    if (arr.get(i) != null) {
                        InfoDoll info = Gs.fromJson(arr.get(i), InfoDoll.class);
                        arrayInfoDoll.set(i, info);
                    }
                }
            }
        }


    }
    private void eventBin(){
        btnBin.setOrigin(Align.center);
        btnBin.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                showFullScreen();
                SoundEffect.Play(SoundEffect.click);
                btnBin.setTouchable(Touchable.disabled);
                btnBin.addAction(Actions.sequence(
                        Actions.scaleTo(0.8f,0.8f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f),
                        GSimpleAction.simpleAction((d,a)->{
                            showNoticeDlt();
                            return  true;
                        })
                ));

                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    private void eventTutorial(){
        btnTutorial.setOrigin(Align.center);
        btnTutorial.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                showFullScreen();
                SoundEffect.Play(SoundEffect.click);
                btnTutorial.addAction(Actions.sequence(
                        Actions.scaleTo(0.8f,0.8f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f),
                        GSimpleAction.simpleAction((d,a)->{
                            new Tutorial();
                            return  true;
                        })
                ));

                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    private void Anibtn(Image btn){
        btn.addAction(Actions.sequence(
                Actions.scaleTo(0.8f,0.8f,0.5f),
                Actions.scaleTo(1f,1f,0.5f),
                Actions.delay(2),
                GSimpleAction.simpleAction((d,a)->{
                    Anibtn(btn);
                    return true;
                })
        ));
    }
    public void animationDlt(){
        btnBin.addAction(Actions.sequence(
                Actions.scaleTo(1.2f,1.2f,0.1f),
                Actions.rotateTo(30,0.1f),
                Actions.rotateTo(-60,0.1f),
                Actions.rotateTo(30,0.1f),
                Actions.rotateTo(-30,0.1f),
                Actions.rotateTo(0,0.1f),
                Actions.scaleTo(1,1,0.1f)
        ));
    }
    private void eventBtnRender(){
        btnRender2.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                if(gameScene.checkNull()==1){
                    long P = arrayInfoDoll.get(Config.TypeRender-1).price;
                    System.out.println("get price"+P);
                    if(Config.monney>P){
                        Config.monney-=P;
                        P+=(P*20/100);
                        header.SetMonney(Config.monney);
                        Price.setText(""+Config.compressCoin(P,3));
                        arrayInfoDoll.get(Config.TypeRender-1).setPrice(P);
                        ///// save monney /////
                        GMain.prefs.putBoolean("checkFirstListDoll",true);
                        Gson Gs = new Gson();
                        String objDoll = Gs.toJson(arrayInfoDoll);
                        GMain.prefs.putString("listShopDoll", objDoll);
                        GMain.prefs.flush();
                        gameScene.renderDoll(Config.TypeRender);
                    }else {
                        Label label = new Label("Không Đủ Tiền",new Label.LabelStyle(font,null));
                        label.setFontScale(0.8f);
                        label.setAlignment(Align.center);
                        label.setPosition(btnRender2.getX()+btnRender2.getWidth()/2,btnRender2.getY(),Align.center);
                        group.addActor(label);
                        label.addAction(Actions.sequence(
                                Actions.moveBy(0,-100,1f),
                                GSimpleAction.simpleAction((d,a)->{
                                    label.clear();
                                    label.remove();
                                    return true;
                                })
                        ));
                    }
                }else {
                    Label label = new Label("Không có chỗ Trống",new Label.LabelStyle(font,null));
                    label.setFontScale(0.8f);
                    label.setOrigin(Align.center);
                    label.setAlignment(Align.center);
                    label.setPosition(btnRender2.getX()+btnRender2.getWidth()/2,btnRender2.getY(),Align.center);
                    group.addActor(label);
                    label.addAction(Actions.sequence(
                            Actions.moveBy(0,-100,1f),
                            GSimpleAction.simpleAction((d,a)->{
                                label.clear();
                                label.remove();
                                return true;
                            })
                    ));
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    private void eventBtnFarm(){
        btnFarm.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                showFullScreen();
                SoundEffect.Play(SoundEffect.click);
                try{
                    gameScene.eventBtnFarm(true);

                }catch (Exception e){
                    System.out.println(e);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    private void eventBtnShop(){
        btnListDoll.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                showFullScreen();
                new TableShopDoll(atlas,gameScene,header);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }


    private void enventBtnHistory(){
        btnHistory.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                showFullScreen();
                new ListDollHistory(atlas);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    private void showNoticeDlt(){
        Group group = new Group();
        GStage.addToLayer(GLayer.top,group);
        SoundEffect.Play(SoundEffect.panel_in);
        final GShapeSprite blackOverlay = new GShapeSprite();
        blackOverlay.createRectangle(true, -GStage.getWorldWidth(),-GStage.getWorldHeight()/2, GStage.getWorldWidth()*2, GStage.getWorldHeight()*2);
        blackOverlay.setColor(0,0,0,0.5f);
        group.addActor(blackOverlay);
        group.setScaleX(0);
        group.setOrigin(Align.center);
        group.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,Align.center);
        group.addAction(Actions.scaleTo(1,1,0.3f, Interpolation.swingOut));
        Image frm = GUI.createImage(atlas,"noticeDlt");
        frm.setPosition(0,0,Align.center);
        group.addActor(frm);
        Image btnClose = GUI.createImage(atlas,"btnDone");
        btnClose.setPosition(0,frm.getHeight()/2-btnClose.getHeight(),Align.center);
        group.addActor(btnClose);
        btnClose.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                btnBin.setTouchable(Touchable.enabled);
                group.clear();
                group.remove();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    private void enventBtnX2coin(){
        btnX2Coin.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                btnX2Coin.setTouchable(Touchable.disabled);
                gameScene.X2Coin();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    public void showFullScreen(){
        Config.checkShowAds+=1;
        if(Config.checkShowAds==7){
            GMain.platform.ShowFullscreen();
            Config.checkShowAds=0;
        }
    }
}
