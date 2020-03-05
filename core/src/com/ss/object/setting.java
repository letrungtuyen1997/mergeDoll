package com.ss.object;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.platform.ToggleHandler;
import com.ss.commons.Tweens;
import com.ss.commons._ToggleButton;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.StaticObjects.Config;

public class setting implements ToggleHandler {
    TextureAtlas atlas;
    BitmapFont font;
    Group group = new Group();
    private Image btnSetting;
    public setting(TextureAtlas atlas, Image btnSetting){
        this.atlas = atlas;
        this.btnSetting = btnSetting;
        GStage.addToLayer(GLayer.top,group);
        showframeSetting();
    }
    void showframeSetting(){
        SoundEffect.Play(SoundEffect.panel_in);
        final GShapeSprite blackOverlay = new GShapeSprite();
        blackOverlay.createRectangle(true, -GStage.getWorldWidth(),-GStage.getWorldHeight()/2, GStage.getWorldWidth()*2, GStage.getWorldHeight()*2);
        blackOverlay.setColor(0,0,0,0.5f);
        group.addActor(blackOverlay);
        group.setScaleX(0);
        group.setOrigin(Align.center);
        group.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,Align.center);
        group.addAction(Actions.scaleTo(1,1,0.3f, Interpolation.swingOut));
        Image frm = GUI.createImage(atlas,"frmSetting");
        frm.setPosition(0,0,Align.center);
        group.addActor(frm);
        Image btnClose = GUI.createImage(atlas,"btnDone");
        btnClose.setPosition(0,frm.getHeight()/2-btnClose.getHeight()-20,Align.center);
        group.addActor(btnClose);
        eventbtnClose(btnClose);

        ////////// btn TurnOn /////////
        Image btnTurnOnSound = GUI.createImage(atlas,"turnOn");
        btnTurnOnSound.setPosition(120,-55,Align.center);
        group.addActor(btnTurnOnSound);
        ///////// btn TurnOff ///////
        Image btnTurnOffSound = GUI.createImage(atlas,"turnOff");
        btnTurnOffSound.setPosition(120,-55,Align.center);
        group.addActor(btnTurnOffSound);
        if(SoundEffect.mute==false){
            btnTurnOffSound.setVisible(false);
        }else {
            btnTurnOnSound.setVisible(false);
        }
        new _ToggleButton(btnTurnOnSound,btnTurnOffSound,"sound",this);
        ////////////////// music///////////////////
        ////////// btn TurnOn /////////
        Image btnTurnOnMusic = GUI.createImage(atlas,"turnOn");
        btnTurnOnMusic.setPosition(120,25,Align.center);
        group.addActor(btnTurnOnMusic);
        ///////// btn TurnOff ///////
        Image btnTurnOffMusic = GUI.createImage(atlas,"turnOff");
        btnTurnOffMusic.setPosition(120,25,Align.center);
        group.addActor(btnTurnOffMusic);
        if(SoundEffect.music==false){
            btnTurnOffMusic.setVisible(false);
        }else {
            btnTurnOnMusic.setVisible(false);
        }
        new _ToggleButton(btnTurnOnMusic,btnTurnOffMusic,"music",this);

    }
    void eventbtnClose(Image btn){
        btn.setOrigin(Align.center);
        btn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SoundEffect.Play(SoundEffect.click);
                btn.setTouchable(Touchable.disabled);
                btn.addAction(Actions.sequence(
                        Actions.scaleTo(0.8f,0.8f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f)
                ));
                Tweens.setTimeout(group,0.2f,()->{
                    SoundEffect.Play(SoundEffect.panel_out);
                    group.addAction(Actions.sequence(
                            Actions.scaleTo(0, 1,0.3f),
                            GSimpleAction.simpleAction((d, a)->{
                                group.clear();
                                group.remove();
                                btnSetting.setTouchable(Touchable.enabled);
                                return true;
                            })

                    ));

                });
            }
        });
    }

    @Override
    public void activeHandler(String str) {
        if(str=="sound"){
            SoundEffect.mute = true;
        }
        if(str=="music"){
            SoundEffect.music = true;
            SoundEffect.Pausemusic(Config.indexMusic);
        }

    }
    @Override
    public void deactiveHandler(String str) {
        if(str=="sound"){
            SoundEffect.mute = false;
        }
        if(str=="music"){
            SoundEffect.music = false;
            SoundEffect.Playmusic(Config.indexMusic);
        }
    }
}
