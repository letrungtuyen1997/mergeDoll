package com.ss.object;

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
import com.ss.GMain;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.StaticObjects.Config;
import com.ss.scenes.GameScene;

public class Header {
    private TextureAtlas atlas;
    private BitmapFont font;
    public Group group = new Group();
    public Group groupText = new Group();
    public Label textMonney, textExp;
    private GameScene gameScene;
    public Header(TextureAtlas atlas, BitmapFont font){
        this.font = font;
        this.atlas = atlas;
        GStage.addToLayer(GLayer.top,group);
        render();
    }
    private void render(){
        ////// frame monney ///////
        Image frmMonney = GUI.createImage(atlas,"frmMonney");
        frmMonney.setPosition(GStage.getWorldWidth()/2 - frmMonney.getWidth()/2,frmMonney.getHeight()+100, Align.center);
        group.addActor(frmMonney);
        /////// frame exp ////////
        Image frmExp = GUI.createImage(atlas,"frmExp");
        frmExp.setPosition(GStage.getWorldWidth()/2+100,frmExp.getHeight()+100,Align.center);
        group.addActor(frmExp);

        //////////////Text monney /////////
        group.addActor(groupText);
        textMonney = new Label(""+Config.compressCoin(Config.monney,3),new Label.LabelStyle(font,null));
        textMonney.setFontScale(0.5f);
//        textMonney.setOrigin(Align.center);
        textMonney.setAlignment(Align.center);
        textMonney.setPosition(0,-10);
        groupText.addActor(textMonney);
        groupText.setSize(textMonney.getWidth(),textMonney.getPrefHeight());
        groupText.setOrigin(Align.center);
        groupText.setPosition(frmMonney.getX()+frmMonney.getWidth()/2,frmMonney.getY()+frmMonney.getHeight()/2,Align.center);

        ////////// Text exp /////////
        textExp = new Label(""+Config.compressCoin(Config.Exp,3),new Label.LabelStyle(font,null));
        textExp.setFontScale(0.5f);
        textExp.setOrigin(Align.center);
        textExp.setAlignment(Align.center);
        textExp.setPosition(frmExp.getX()+frmExp.getWidth()/2,frmExp.getY()+frmExp.getHeight()/2,Align.center);
        group.addActor(textExp);
        updateMonney(Config.monney);
        updateExp(Config.Exp);
        //////// setting /////
        Image btnSetting = GUI.createImage(atlas,"btnSetting");
        btnSetting.setOrigin(Align.center);
        btnSetting.setPosition(GStage.getWorldWidth()-btnSetting.getWidth()+20,frmExp.getY()+frmExp.getHeight()/2,Align.center);
        group.addActor(btnSetting);
        btnSetting.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btnSetting.setTouchable(Touchable.disabled);
                SoundEffect.Play(SoundEffect.click);
                btnSetting.addAction(Actions.sequence(
                        Actions.scaleTo(0.9f,0.9f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f),
                        GSimpleAction.simpleAction((d,a)->{
                            new setting(atlas,btnSetting);
                            return true;
                        })
                ));
                return super.touchDown(event, x, y, pointer, button);
            }
        });


    }

    public void updateMonney(long monney){
        groupText.addAction(Actions.sequence(
                Actions.scaleTo(1.2f,1.2f,0.1f),
                Actions.scaleTo(1f,1f,0.1f),
                GSimpleAction.simpleAction((d,a)->{
                    textMonney.setText(""+Config.compressCoin(monney,3));
                    return true;
                })
        ));
        Tweens.setTimeout(group,5f,()->{
            Config.monney+=Config.monneyRuntime;
            GMain.prefs.putLong("money",Config.monney);
            GMain.prefs.putLong("exp",Config.Exp);
            GMain.prefs.putLong("moneyRuntime",Config.monneyRuntime);
            GMain.prefs.flush();
            Config.monneyRuntime=0;
            updateMonney(Config.monney);
        });
    }
    public void SetMonney(long monney){
        groupText.addAction(Actions.sequence(
                Actions.scaleTo(1.2f,1.2f,0.1f),
                Actions.scaleTo(1f,1f,0.1f),
                GSimpleAction.simpleAction((d,a)->{
                    textMonney.setText(""+Config.compressCoin(monney,3));
                    return true;
                })
        ));
    }

    public void updateExp(long Exp){
        textExp.setText(""+Config.compressCoin(Exp,2));
    }
}
