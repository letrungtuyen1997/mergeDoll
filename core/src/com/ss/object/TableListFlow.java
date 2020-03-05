package com.ss.object;

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
import com.badlogic.gdx.utils.Json;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;

public class TableListFlow {
    private TextureAtlas atlas;
    private Group group = new Group();
    private  Image table;
    private Table container;
    private ScrollPane scroll1;
    private Group groupChil = new Group();
    private BitmapFont font;


    public TableListFlow(TextureAtlas atlas){
        GStage.addToLayer(GLayer.top,group);
        GStage.addToLayer(GLayer.top,groupChil);
        this.atlas = atlas;
        initFont();
        darkScreen();
        table = GUI.createImage(atlas,"Table");
        table.setPosition(0,0,Align.center);
        group.addActor(table);
        group.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2);
        groupChil.setPosition(0,0);
        groupChil.setWidth(600);
        groupChil.setHeight(1300);
        groupChil.setOrigin(Align.center);
        render();
        Image btnClose = GUI.createImage(atlas,"btnClose");
        btnClose.setPosition(-table.getWidth()/2+btnClose.getWidth()/3,-table.getHeight()/2+btnClose.getHeight()/3,Align.center);
        group.addActor(btnClose);

        ////// btn close /////
        eventBtnclose(btnClose);

    }
    private void darkScreen(){
        final GShapeSprite blackOverlay = new GShapeSprite();
        blackOverlay.createRectangle(true, -GStage.getWorldWidth()/2,-GStage.getWorldHeight()/2, GStage.getWorldWidth()*2, GStage.getWorldHeight()*2);
        blackOverlay.setColor(0,0,0,0.5f);
        group.addActor(blackOverlay);
    }
    private void render(){
        for(int i=1;i<8;i++){
            Image frmInfo = GUI.createImage(atlas,"frmInfo");
            frmInfo.setPosition(frmInfo.getWidth()/2+30,20+(frmInfo.getHeight()+20)*i,Align.center);
            groupChil.addActor(frmInfo);
            /////// img Flower ////
            Image imgFlow = GUI.createImage(atlas,"lv"+(8-i));
            imgFlow.setScaleY(-1);
            imgFlow.setOrigin(Align.center);
            imgFlow.setPosition(frmInfo.getX()+80,frmInfo.getY()+frmInfo.getHeight()/2,Align.center);
            groupChil.addActor(imgFlow);
            animationFlow(imgFlow);
            //////// name ///////
            Label name = new Label(""+makeInfo((8-i)).getName(),new Label.LabelStyle(font, Color.ORANGE));
            name.setAlignment(Align.right);
            name.setPosition(frmInfo.getX()+170,frmInfo.getY()+frmInfo.getHeight()/2+30,Align.center);
            name.setFontScale(0.5f,-0.5f);
            name.setOrigin(Align.center);
            groupChil.addActor(name);
            //////// lv ////////
            Label Lv = new Label("(LV:"+makeInfo((8-i)).getLv()+")",new Label.LabelStyle(font, Color.RED));
            Lv.setPosition(frmInfo.getX()+470,frmInfo.getY()+frmInfo.getHeight()/2+30,Align.center);
            Lv.setFontScale(0.5f,-0.5f);
            Lv.setOrigin(Align.center);
            groupChil.addActor(Lv);
            ////// exp /////
            Label Exp = new Label("Cần:        "+makeInfo((8-i)).getExp(),new Label.LabelStyle(font, Color.ORANGE));
            Exp.setPosition(frmInfo.getX()+240,frmInfo.getY()+frmInfo.getHeight()/2-15,Align.center);
            Exp.setFontScale(0.5f,-0.5f);
            Exp.setOrigin(Align.center);
            Exp.setAlignment(Align.center);
            groupChil.addActor(Exp);
            Image imgExp = GUI.createImage(atlas,"exp");
            imgExp.setScale(0.6f);
            imgExp.setOrigin(Align.center);
            imgExp.setPosition(Exp.getX()+Exp.getWidth()/2+10,Exp.getY()+Exp.getHeight()/2,Align.center);
            groupChil.addActor(imgExp);

            /////// make money /////
            Label Money = new Label("Kiếm:        "+makeInfo((8-i)).getPrice(),new Label.LabelStyle(font, Color.ORANGE));
            Money.setPosition(frmInfo.getX()+250,frmInfo.getY()+frmInfo.getHeight()/2-50,Align.center);
            Money.setFontScale(0.5f,-0.5f);
            Money.setOrigin(Align.center);
            Money.setAlignment(Align.center);
            groupChil.addActor(Money);
            Image imgMoney = GUI.createImage(atlas,"coin");
            imgMoney.setScale(0.4f);
            imgMoney.setOrigin(Align.center);
            imgMoney.setPosition(Money.getX()+Money.getWidth()/2+20,Money.getY()+Money.getHeight()/2,Align.center);
            groupChil.addActor(imgMoney);


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
    private InfoFlower makeInfo(int id){
        InfoFlower info ;
        Json js = new Json();
        switch(id){
            case 1:{
                info = js.fromJson(InfoFlower.class,"{name: Mầm non, lv : 1, Exp: 1, Price: 10}");
                break;
            }
            case 2:{
                info = js.fromJson(InfoFlower.class,"{name: Hoa Cẩm Chướng, lv : 2, Exp: 10, Price: 20}");
                break;
            }
            case 3:{
                info = js.fromJson(InfoFlower.class,"{name: Hoa Ly, lv : 3, Exp: 20, Price: 30}");
                break;
            }
            case 4:{
                info = js.fromJson(InfoFlower.class,"{name: Hoa Tô Liên, lv : 4, Exp: 30, Price: 40}");
                break;
            }
            case 5:{
                info = js.fromJson(InfoFlower.class,"{name: Hoa Diên Vĩ, lv : 5, Exp: 40, Price: 50}");
                break;
            }
            case 6:{
                info = js.fromJson(InfoFlower.class,"{name: Hoa Linh Lan, lv : 6, Exp: 50, Price: 60}");
                break;
            }
            default:{
                info = js.fromJson(InfoFlower.class,"{name: Hoa Hồng Mai, lv : 7, Exp: 60, Price: 70}");
                break;
            }
        }
        return info;
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
    private void animationFlow(Image flow){
        flow.addAction(Actions.sequence(
                Actions.scaleTo(1.2f,-0.9f,1f),
                Actions.scaleTo(1,-1,1f),
                GSimpleAction.simpleAction((d, a)->{
                    animationFlow(flow);
                    return true;
                })
        ));
    }
    private void initFont(){
        font = GAssetsManager.getBitmapFont("font_white.fnt");
    }

}
