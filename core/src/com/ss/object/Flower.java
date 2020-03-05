package com.ss.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GClipGroup;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.StaticObjects.Config;
import com.ss.scenes.FarmScene;

public class Flower {
    public Group group = new Group();
    public Group group2 = new Group();
    public Image Flow,up_On,up_Off, exp, frmStatus, sclStatus, Coin, btnGetCoin;
    private Label LabelExp, LabelLv;
    private TextureAtlas atlas;
    public long target,monney;
    private BitmapFont font;
    private FarmScene farmScene;
    public int type;
    private Header header;
    public TemporalAction action;
    public Runnable onComplete;
    public GClipGroup clipGroup = new GClipGroup();
    public float x,y;
    public Flower(float x, float y, int type, TextureAtlas atlas, BitmapFont font, FarmScene farmScene, Header header){
        GStage.addToLayer(GLayer.top,group);
        this.atlas = atlas;
        this.font = font;
        this.farmScene = farmScene;
        this.type = type;
        this.header =header;
        this.target = type*10;
        this.monney = type*10;
        this.x = x;
        this.y = y;
        Flow = GUI.createImage(atlas,"lv"+type);
        Flow.setPosition(x,y, Align.center);
        group.addActor(Flow);
        //////// up on //////
        up_On = GUI.createImage(atlas,"up_On");
        up_On.setPosition(x-60,y+30,Align.center);
        group.addActor(up_On);
        /////// up off //////
        up_Off = GUI.createImage(atlas,"up_Off");
        up_Off.setPosition(x-60,y+30,Align.center);
        group.addActor(up_Off);
        /////// exp ////////
        exp = GUI.createImage(atlas,"exp");
        exp.setScale(0.8f);
        exp.setOrigin(Align.center);
        exp.setPosition(x-40,y+60,Align.center);
        group.addActor(exp);
        ////// Label exp /////
        LabelExp = new Label(""+target,new Label.LabelStyle(font, Color.BLUE));
        LabelExp.setFontScale(0.4f);
        LabelExp.setAlignment(Align.center);
        LabelExp.setPosition(x,exp.getY()+30,Align.center);
        group.addActor(LabelExp);
        ////// Label Lv //////
        LabelLv = new Label("Lv:"+type,new Label.LabelStyle(font,null));
        LabelLv.setFontScale(0.5f);
        LabelLv.setAlignment(Align.center);
        LabelLv.setPosition(x+70 , y+30,Align.center);
        group.addActor(LabelLv);
        ////////// Status /////// = GUI.createImage(atlas,"frmStatus");
        group.addActor(group2);
        frmStatus = GUI.createImage(atlas,"sclStatus");
        frmStatus.setPosition(x,y-50,Align.center);
        group2.addActor(frmStatus);

        sclStatus = GUI.createImage(atlas,"frmStatus");
        this.clipGroup.setPosition(frmStatus.getX(),frmStatus.getY());
        group2.addActor(this.clipGroup);
        ActionScaleTime();
        Coin = GUI.createImage(atlas,"coin");
        Coin.setScale(0.5f);
        Coin.setOrigin(Align.center);
        Coin.setPosition(frmStatus.getX()+frmStatus.getWidth()/2,frmStatus.getY()+frmStatus.getHeight()/2,Align.center);
        group2.addActor(Coin);

        btnGetCoin= GUI.createImage(atlas,"sclStatus");
        btnGetCoin.setColor(Color.CLEAR);
        btnGetCoin.setPosition(x,y-50,Align.center);
        group2.addActor(btnGetCoin);
        btnGetCoin.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                long getmoney = farmScene.updateMoney(Flower.this,monney);
                Config.monney+=getmoney;
                header.SetMonney(Config.monney);
                //Flower.this.action.setTime(0);
                ActionScaleTime();
                Label txt = new Label("+"+getmoney,new Label.LabelStyle(font,Color.BROWN));
                txt.setFontScale(0.7f);
                txt.setAlignment(Align.center);
                txt.setPosition(btnGetCoin.getX()+txt.getWidth()/2-10,btnGetCoin.getY(),Align.center);
                group.addActor(txt);
                txt.addAction(Actions.sequence(
                        Actions.moveBy(0,-100,0.5f),
                        GSimpleAction.simpleAction((d,a)->{
                            txt.clear();
                            txt.remove();
                            return true;
                        })
                ));
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        /////// annimation ///////
        animation();
        eventBtnFlow(x,y);
        animationGr2();
        ///// set Status ////
        maxLevel();
        if(Config.Exp>=target)
            up_Off.setVisible(false);

    }
    private void maxLevel(){
        if(type==7){
            up_Off.setVisible(false);
            up_On.setVisible(false);
            LabelExp.setVisible(false);
            Flow.setTouchable(Touchable.disabled);
            exp.setVisible(false);
        }
    }

    private void animation(){
        Flow.setOrigin(Align.top);
        Flow.addAction(Actions.sequence(
                Actions.scaleTo(1,0.9f,1f),
                Actions.scaleTo(1,1f,1f)
        ));
        Flow.addAction(Actions.sequence(
                Actions.rotateBy(10,1f),
                Actions.rotateBy(-20,2f),
                Actions.rotateBy(10,1f),
                GSimpleAction.simpleAction((d,a)->{
                    animation();
                    return true;
                })

        ));
    }
    private void eventBtnFlow(float xx, float yy){
        Flow.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                if(Config.Exp>=target){
                    Config.Exp-=target;
                    header.updateExp(Config.Exp);
                    farmScene.upgradeFlow(Flower.this);

                }else {
                    Label label = new Label("Không Đủ Nước",new Label.LabelStyle(font,null));
                    label.setFontScale(0.8f);
                    label.setAlignment(Align.center);
                    label.setPosition(xx,yy,Align.center);
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
    public void dispose(){
        group.clear();
        group.remove();
    }
    public void ActionScaleTime(){
        this.action = new TemporalAction(100, Interpolation.linear) {
            /* access modifiers changed from: protected */
            public void update(float f) {
                farmScene.updatePecentMoney(Flower.this);
//                Config.checkRuntime = false;
               // System.out.println("checkkk"+f);
                Flower.this.clipGroup.setClipArea(0.0f, -Flower.this.sclStatus.getHeight() * f, Flower.this.sclStatus.getWidth(), Flower.this.sclStatus.getHeight());
                if (f == 1.0) {
                    action.setTime(0.9f);
                   // Flower.this.onComplete.run();
                }

            }

        };
        this.clipGroup.addActor(this.sclStatus);
        this.sclStatus.setPosition(0.0f, 0.0f);
        this.clipGroup.setClipArea(0.0f, 0.0f, this.sclStatus.getWidth(), this.sclStatus.getHeight());
        group2.addAction(this.action);
    }
    private void animationGr2(){
        group2.addAction(Actions.sequence(
                Actions.moveBy(0,-10,0.5f),
                Actions.moveBy(0,20,1f),
                Actions.moveBy(0,-10,0.5f),
                GSimpleAction.simpleAction((d,a)->{
                    animationGr2();
                    return true;
                })
        ));
    }



}
