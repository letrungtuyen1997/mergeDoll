package com.ss.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.StaticObjects.Config;

public class ListDollHistory {
    private Group group = new Group();
    private Group groupScroll = new Group();
    private TextureAtlas atlas;
    private Array<Image> arrAvt = new Array<>();
    private Array<Label> arrLabel = new Array<>();
    private Array<Label> arrInfo = new Array<>();
    private Array<Image> btnTile = new Array<>();
    private Array<Image> arrDoll = new Array<>();
    private  Image Table;
    private Array<InfoDoll> arrayInfoDoll = new Array<>();
    private BitmapFont font;
    public ListDollHistory(TextureAtlas atlas){
        this.atlas =atlas;
        GStage.addToLayer(GLayer.top,group);
        initFont();
        Image bg = GUI.createImage(atlas,"bg3");
        bg.setWidth(bg.getWidth()* Config.ratioX);
        bg.setHeight(bg.getHeight()* Config.ratioY);
        group.addActor(bg);
        loadListDoll();
        renderAvt();
        renderListView();
        ////// event btn tile /////
        eventBtnTile();
        btnDone();
        setDefault();

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
    private void renderAvt(){
        Image frmAvt = GUI.createImage(atlas,"frmAvt");
        frmAvt.setPosition(frmAvt.getWidth()/2+40,frmAvt.getHeight()/2+50, Align.center);
        group.addActor(frmAvt);
        Image frmInfo = GUI.createImage(atlas,"frmInfo2");
        frmInfo.setPosition(GStage.getWorldWidth()-frmInfo.getWidth()/2-40,frmInfo.getHeight()/2+80,Align.center);
        group.addActor(frmInfo);
        for (int i=0;i<20;i++){
            Image avt = GUI.createImage(atlas,"avt"+(i+1));
            avt.setPosition(frmAvt.getX()+frmAvt.getWidth()/2,frmAvt.getY()+frmAvt.getHeight()/2,Align.center);
            group.addActor(avt);
            arrAvt.add(avt);
            /////// name /////
            Label name = new Label(""+arrayInfoDoll.get(i).name, new Label.LabelStyle(font,null));
            name.setFontScale(0.7f);
            name.setAlignment(Align.center);
            name.setPosition(frmInfo.getX()+frmInfo.getWidth()/2,frmInfo.getY()-name.getHeight(),Align.center);
            group.addActor(name);
            arrLabel.add(name);
            /////// info /////
            Label info = new Label(""+arrayInfoDoll.get(i).info, new Label.LabelStyle(font,null));
            info.setFontScale(0.5f);
            info.setAlignment(Align.left);
            info.setPosition(frmInfo.getX()+frmInfo.getWidth()-20,frmInfo.getY()+info.getHeight()/2+110,Align.center);
            group.addActor(info);
            arrInfo.add(info);
        }


    }
    private void renderListView(){
        Table = GUI.createImage(atlas,"Table2");
        Table.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2+100,Align.center);
        group.addActor(Table);

        groupScroll.setWidth(Table.getWidth()-100);
        groupScroll.setHeight(Table.getHeight()-100);
        groupScroll.setPosition(Table.getX()+Table.getWidth()/2,Table.getY()+Table.getHeight()/2,Align.center);
        ///////// scroll table ////
        Table table = new Table();
        Table tableScroll = new Table();
        int dem=0;
        for (int i=0;i<arrayInfoDoll.size;i++){
            Group grT = new Group();
            Image tile = GUI.createImage(atlas,"tile");
            grT.addActor(tile);
            Image Doll = GUI.createImage(atlas,""+(i+1));
            Doll.setScale(0.8f,-0.8f);
            Doll.setOrigin(Align.center);
            Doll.setPosition(grT.getWidth()/2+15,grT.getHeight()/2-120);
            grT.addActor(Doll);
            arrDoll.add(Doll);
            Image tile2 = GUI.createImage(atlas,"tile");
            tile2.setColor(Color.CLEAR);
            grT.addActor(tile2);
            btnTile.add(tile2);
            grT.setSize(tile.getWidth(),tile.getHeight());
            tableScroll.add(grT).center();
            dem++;
            if(dem==3)
            {
                dem=0;
                tableScroll.row();

            }
        }
        ScrollPane Scroll = new ScrollPane(tableScroll);
        table.setFillParent(true);
        table.add(Scroll).fill().expand();
        groupScroll.setScale(1,-1);
        groupScroll.setOrigin(Align.center);
        groupScroll.addActor(table);
        group.addActor(groupScroll);

    }
    private void eventBtnTile(){
        for (int i=0;i<btnTile.size;i++){
            int finalI = i;
            btnTile.get(i).addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    showAvt(finalI);
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
        }
    }
    private void showAvt(int index){
        for (int i=0;i<arrAvt.size;i++){
            if(index==i&&arrDoll.get(i).getName().equals("khoa")!=true){
                arrAvt.get(i).setVisible(true);
                arrLabel.get(i).setVisible(true);
                arrInfo.get(i).setVisible(true);
            }else {
                arrAvt.get(i).setVisible(false);
                arrLabel.get(i).setVisible(false);
                arrInfo.get(i).setVisible(false);

            }
        }
    }
    private void btnDone(){
        Image btnDone = GUI.createImage(atlas,"btnDone");
        btnDone.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()-100,Align.center);
        group.addActor(btnDone);
        btnDone.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                group.clear();
                group.remove();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    private void setDefault(){
        for (int i=0;i<arrAvt.size;i++){
            arrAvt.get(i).setVisible(false);
            arrLabel.get(i).setVisible(false);
            arrInfo.get(i).setVisible(false);
        }
        for (int i=0;i<arrDoll.size;i++){
            if(Config.TypeMax<(i+1)){
                arrDoll.get(i).setColor(Color.BLACK);
                arrDoll.get(i).setName("khoa");
            }else {
                arrDoll.get(i).setName("mo");
            }
        }
    }
    private void initFont(){

        font = GAssetsManager.getBitmapFont("font_white.fnt");
    }
}
