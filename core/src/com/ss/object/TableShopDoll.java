package com.ss.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.StaticObjects.Config;
import com.ss.scenes.GameScene;

import java.util.Set;

public class TableShopDoll {
    private TextureAtlas atlas;
    private Group group = new Group();
    private Image table;
    private Table container;
    private ScrollPane scroll1;
    private Group groupChil = new Group();
    private BitmapFont font;
    private GameScene gameScene;
    private Header header;
    private Array<Label> arrPriceDoll = new Array<>();
    private Array<Image> arrImgDoll = new Array<>();
    private Array<Image> arrbtnDoll = new Array<>();
    private Array<InfoDoll> arrayInfoDoll = new Array<>();
    TableShopDoll(TextureAtlas atlas, GameScene gameScene, Header header){
        GStage.addToLayer(GLayer.top,group);
        this.atlas = atlas;
        this.gameScene = gameScene;
        this.header = header;
        initFont();
        loadListDoll();
        darkScreen();
        table = GUI.createImage(atlas,"Table");
        table.setPosition(0,0, Align.center);
        group.addActor(table);
        group.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2);
        groupChil.setPosition(0,0);
        groupChil.setWidth(600);
        groupChil.setHeight(2650);
        groupChil.setOrigin(Align.center);
        render();
        Image btnClose = GUI.createImage(atlas,"btnClose");
        btnClose.setPosition(-table.getWidth()/2+btnClose.getWidth()/3,-table.getHeight()/2+btnClose.getHeight()/3,Align.center);
        group.addActor(btnClose);
        ///// event btn Close //////
        eventBtnclose(btnClose);
        ///// set Default/////
        setDefault();

    }
    private void darkScreen(){
        final GShapeSprite blackOverlay = new GShapeSprite();
        blackOverlay.createRectangle(true, -GStage.getWorldWidth()/2,-GStage.getWorldHeight()/2, GStage.getWorldWidth()*2, GStage.getWorldHeight()*2);
        blackOverlay.setColor(0,0,0,0.5f);
        group.addActor(blackOverlay);
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
//                System.out.println("StringJson: "+S);
                JsonObject obj = Gs.fromJson(S, JsonObject.class);
                Set<String> keys = obj.keySet();
                String key = keys.iterator().next();
                if (key != null) {
                    JsonArray arr = obj.getAsJsonArray(key);
                    for (int i=0;i<arrayInfoDoll.size;i++){
                        if(arr.get(i)!=null){
                            InfoDoll info = Gs.fromJson(arr.get(i), InfoDoll.class);
                            arrayInfoDoll.set(i,info);
                        }
                    }
                }
            }


    }
    private void render(){
        for(int i=1;i<16;i++){
            Image frmInfo = GUI.createImage(atlas,"frmInfo");
            frmInfo.setPosition(frmInfo.getWidth()/2+30,20+(frmInfo.getHeight()+20)*i,Align.center);
            groupChil.addActor(frmInfo);
            //////// img Doll //////
            Image imgDoll = GUI.createImage(atlas,""+(16-i));
            imgDoll.setScale(0.65f,-0.65f);
            imgDoll.setOrigin(Align.center);
            imgDoll.setPosition(frmInfo.getX()+90,frmInfo.getY()+frmInfo.getHeight()/2-30,Align.center);
            groupChil.addActor(imgDoll);
            arrImgDoll.add(imgDoll);
            //////// btn buy Doll //////
            Image btnDoll = GUI.createImage(atlas,"btnBuyDoll");
            btnDoll.setOrigin(Align.center);
            btnDoll.setPosition(frmInfo.getX()+360,frmInfo.getY()+frmInfo.getHeight()/2,Align.center);
            groupChil.addActor(btnDoll);
            /////// Heart ///////
            Image heart = GUI.createImage(atlas,"ic_Heart");
            heart.setScale(-1.2f);
            heart.setOrigin(Align.center);
            heart.setPosition(frmInfo.getX()+30,frmInfo.getY()+30,Align.center);
            groupChil.addActor(heart);
            ////// label lv //////
            Label lv = new Label(""+arrayInfoDoll.get((15-i)).id,new Label.LabelStyle(font, null));
            lv.setFontScale(0.5f,-0.5f);
            lv.setOrigin(Align.center);
            lv.setAlignment(Align.center);
            lv.setPosition(heart.getX()+heart.getWidth()/2,heart.getY()+heart.getHeight()/2,Align.center);
            groupChil.addActor(lv);
            ////// name doll /////
            Label name = new Label(""+arrayInfoDoll.get((15-i)).name,new Label.LabelStyle(font,Color.BROWN));
            name.setFontScale(0.5f,-0.5f);
            name.setAlignment(Align.right);
            name.setOrigin(Align.center);
            name.setPosition(frmInfo.getX()+160,frmInfo.getY()+frmInfo.getHeight()/2,Align.center);
            groupChil.addActor(name);
            ////// price Doll /////
            Label Price = new Label(""+Config.compressCoin(arrayInfoDoll.get((15-i)).price,1),new Label.LabelStyle(font,Color.BROWN));
            Price.setFontScale(0.5f,-0.5f);
            Price.setOrigin(Align.center);
            Price.setAlignment(Align.center);
            Price.setPosition(btnDoll.getX()+btnDoll.getWidth()/2,btnDoll.getY()+btnDoll.getHeight()/2,Align.center);
            groupChil.addActor(Price);
            arrPriceDoll.add(Price);
            ///// icon coin ////
            Image coin = GUI.createImage(atlas,"coin");
            coin.setScale(0.5f);
            coin.setOrigin(Align.center);
            coin.setPosition(btnDoll.getX()+btnDoll.getWidth()-30,btnDoll.getY()+btnDoll.getHeight()/2,Align.center);
            groupChil.addActor(coin);
            ///// btnBuyDoll//////
            Image btnBuyDoll = GUI.createImage(atlas,"btnBuyDoll");
            btnBuyDoll.setColor(Color.CLEAR);
            btnBuyDoll.setPosition(frmInfo.getX()+360,frmInfo.getY()+frmInfo.getHeight()/2,Align.center);
            groupChil.addActor(btnBuyDoll);
            arrbtnDoll.add(btnBuyDoll);
            eventBuyDoll(btnBuyDoll,btnDoll,(16-i));

        }
        container = new Table();
        container.setSize(600, table.getHeight()-100);
        container.setPosition(-table.getWidth()/2,table.getHeight()/2-50);
        group.addActor(container);
        Table table = new Table();
        scroll1 = new ScrollPane(table);
        container.add(scroll1);
        table.add(groupChil);
        scroll1.setScale(1,-1);
    }

    private void eventBtnclose(Image btn){
        btn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                btn.setTouchable(Touchable.disabled);
                group.clear();
                group.remove();
                groupChil.clear();
                groupChil.remove();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    private void eventBuyDoll(Image btn,Image btn2,int index1){
        btn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                btn2.addAction(Actions.sequence(
                        Actions.scaleTo(0.9f,0.9f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f)
                ));
                int index = index1-1;
                if(gameScene.checkNull()==1){
                    if(Config.monney>arrayInfoDoll.get(index).price){
                        Config.monney-=arrayInfoDoll.get(index).price;
                        arrayInfoDoll.get(index).setPrice(arrayInfoDoll.get(index).getPrice()+(arrayInfoDoll.get(index).getPrice()*20/100));
                        header.SetMonney(Config.monney);
                        arrPriceDoll.get(14-index).setText(""+Config.compressCoin(arrayInfoDoll.get(index).price,1));
                        gameScene.renderDoll(arrayInfoDoll.get(index).getId());
                        /////// save //////
                        GMain.prefs.putBoolean("checkFirstListDoll",true);
                        Gson Gs = new Gson();
                        String objDoll = Gs.toJson(arrayInfoDoll);
                        System.out.println("checkk save:"+objDoll);
                        System.out.println("ArrDoll:"+arrayInfoDoll);
                        GMain.prefs.putString("listShopDoll", objDoll);
                        GMain.prefs.flush();
                    }else {
                        Label label = new Label("Không Đủ Tiền",new Label.LabelStyle(font,null));
                        label.setFontScale(0.8f);
                        label.setAlignment(Align.center);
                        label.setPosition(0,0,Align.center);
                        group.addActor(label);
                        label.addAction(Actions.sequence(
                                Actions.moveBy(0,-100,1f),
                                GSimpleAction.simpleAction((d, a)->{
                                    label.clear();
                                    label.remove();
                                    return true;
                                })
                        ));
                    }
                }else {
                    Label label = new Label("Không Có Chỗ Trống",new Label.LabelStyle(font,null));
                    label.setFontScale(0.8f);
                    label.setOrigin(Align.center);
                    label.setAlignment(Align.center);
                    label.setPosition(0,0,Align.center);
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
    private void setDefault(){
        for (int i=0;i<arrPriceDoll.size;i++){
            if(i>=Config.TypeRender){
                arrPriceDoll.get(14-i).setText("???");
                arrbtnDoll.get(14-i).setTouchable(Touchable.disabled);
                arrImgDoll.get(14-i).setColor(Color.BLACK);
            }
        }
    }

    private void initFont(){
        font = GAssetsManager.getBitmapFont("font_white.fnt");
    }
}
