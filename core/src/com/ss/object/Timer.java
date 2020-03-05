package com.ss.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ss.commons.Tweens;
import com.ss.core.util.GClipGroup;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.effects.effectWin;
import com.ss.gameLogic.StaticObjects.Config;
import com.ss.scenes.GameScene;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.addAction;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.timeScale;

public class Timer {
    private TextureAtlas atlas;
    private BitmapFont font;
    private Group group = new Group();
    private Image timeBar,timeScl,btnUpSpeed;
    private TemporalAction action;
    public GClipGroup clipGroup = new GClipGroup();
    public Runnable onComplete;
    public Timer(float x, float y,TextureAtlas atlas, BitmapFont font,Runnable runnable){
        this.atlas = atlas;
        this.font = font;
        this.onComplete = runnable;
        GStage.addToLayer(GLayer.ui,group);
        group.setPosition(x,y,Align.center);
        ///////// time bar////
        timeBar = GUI.createImage(atlas,"timeScl");
        timeBar.setOrigin(Align.center);
        timeBar.setPosition(0,0, Align.center);
        group.addActor(timeBar);
        /////// time scale//////
        timeScl = GUI.createImage(atlas,"timerBar");
        timeScl.setOrigin(Align.center);
        ////// label /////
        Label lv = new Label("lv.1",new Label.LabelStyle(font,null));
        lv.setFontScale(0.7f);
        lv.setOrigin(Align.center);
        lv.setAlignment(Align.center);
        lv.setPosition(timeBar.getX()+timeBar.getWidth()/2,timeBar.getY()+timeBar.getHeight()+20,Align.center);
        group.addActor(lv);
        /////// action time scale//////
        //actionTimer(runnable);
        this.clipGroup.setPosition(timeBar.getX()+12,timeBar.getY()+12);
        group.addActor(this.clipGroup);
        ActionScaleTime();
        //////////// btnUpSpeed/////
        btnUpSpeed = GUI.createImage(atlas,"timeScl");
        btnUpSpeed.setColor(Color.CLEAR);
        btnUpSpeed.setPosition(0,0, Align.center);
        group.addActor(btnUpSpeed);
        //////// event up speed////
        EventUpSpeed();
        particleRender();

    }
    public void ActionScaleTime(){
        this.action = new TemporalAction(10, Interpolation.linear) {
            /* access modifiers changed from: protected */
            public void update(float f) {
                Config.checkRuntime = false;
                //System.out.println("checkkk"+f);
                Timer.this.clipGroup.setClipArea(0.0f, -(Timer.this.timeScl.getHeight()+10) * f, Timer.this.timeScl.getWidth(), Timer.this.timeScl.getHeight());
                if (f == 1.0f) {
                    Timer.this.onComplete.run();
                    Config.checkRuntime = true;
                }

            }

        };
        this.clipGroup.addActor(this.timeScl);
        group.addAction(this.action);
    }
    public void resetAction(){
        this.action.restart();
    }
    private void EventUpSpeed(){
        btnUpSpeed.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                timeBar.addAction(Actions.sequence(
                        Actions.scaleTo(1.2f,1.2f,0.1f),
                        Actions.scaleTo(1,1,0.1f)
                ));
                timeScl.addAction(Actions.sequence(
                        Actions.scaleTo(1.2f,1.2f,0.1f),
                        Actions.scaleTo(1,1,0.1f)
                ));

                Timer.this.action.setTime(Timer.this.action.getTime()+1f);
                System.out.println(Timer.this.action.getTime());

                return super.touchDown(event, x, y, pointer, button);
            }
        });

    }
    private void particleRender(){
        effectWin ef = new effectWin(4,timeBar.getX()+timeBar.getWidth()/2,timeBar.getY()+timeBar.getHeight()/2);
        group.addActor(ef);
        ef.start();
    }


}
