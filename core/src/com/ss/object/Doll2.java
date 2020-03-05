package com.ss.object;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.StaticObjects.Config;
import com.ss.gameLogic.objects.Spine;
import com.ss.scenes.GameScene;

public class Doll2 extends Group {
   GameScene gameScene;
   Timer timer;
   TextureAtlas atlas;
   BitmapFont font;
   Rectangle body;
    public Group group = new Group();
    Spine s;
    public int type=0;
    public int row=0,col=0;
    public long monney = 0;
    public Doll2(int row, int col, String Atlas, String Json,int type){
        initAtlas();
        GStage.addToLayer(GLayer.ui,group);
        this.row=row;
        this.col = col;
        this.type = type;
//        Spine s = new Spine("spine/skeleton.atlas","spine/skeleton.json");
        s = new Spine(Atlas,Json);
        group.addActor(s);
        group.setWidth(100);
        group.setHeight(200);
        group.setPosition((GStage.getWorldWidth()/2-60-group.getWidth()*3/2)+((group.getWidth()+40)*col),400+(group.getHeight()+40)*row, Align.center);
//        group.setPosition(200,200, Align.center);
//        System.out.println("check!!!!!!!!!!!!!!!!!!"+group.getX()+"====="+group.getY());
//        group.debug();
        ///////// body//////
        body = new Rectangle(group.getX(),group.getY(),group.getWidth(),group.getHeight());
        body.setPosition(group.getX(),group.getY());

        /// label/////
        Image heart = GUI.createImage(atlas,"ic_Heart");
        heart.setPosition(0,group.getHeight(),Align.center);
        group.addActor(heart);
        Label lv = new Label(""+type,new Label.LabelStyle(font,null));
        lv.setFontScale(0.4f);
        lv.setOrigin(Align.center);
        lv.setPosition(heart.getX()+heart.getWidth()/2,heart.getY()+heart.getHeight()/2,Align.center);
        lv.setAlignment(Align.center);
        group.addActor(lv);
        /////// set monney ////
        price();
        ////// meke monney ////
         makeMonney();

    }
    public void addDrag(GameScene gameScene1){
        gameScene = gameScene1;

        group.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                int id = (int)(Math.random()*3);
                System.out.println("music: "+id);
                SoundEffect.Play(id);
                gameScene.saveTime();
                s.state.setTimeScale(2f);
                group.setOrigin(Align.center);
                group.addAction(Actions.scaleTo(1.3f,1.3f));
                Config.LockTime = true;
                gameScene.setTouchDoll(Touchable.disabled,Doll2.this);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                s.state.setTimeScale(0.5f);
                group.setOrigin(Align.center);
                group.addAction(Actions.scaleTo(1f,1f));
                Config.LockTime = false;
                if(Config.RemoveDoll==true){
                    gameScene.RemoveDoll(Doll2.this);
                    gameScene.foter.animationDlt();
                }
                gameScene.setTouchCards(Touchable.enabled);


            }
        });

        group.addListener(new DragListener(){
            int zIndex;
            float pX, pY;
            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                super.dragStart(event, x, y, pointer);
//                boardConfig.isDrag=true;
                zIndex = group.getZIndex();
                pX = group.getX();
                pY = group.getY();
                group.setZIndex(50);
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                super.drag(event, x, y, pointer);
                group.setX(group.getX() + x - group.getWidth()/2);
                group.setY(group.getY() + y - group.getHeight());
                body.setX(group.getX());
                body.setY(group.getY());
                if(body.overlaps(gameScene.foter.bodyBin)){
                    System.out.println("xoaaaaaaaaa!!!!  "+Config.RemoveDoll);
                    if(Config.RemoveDoll==false){
                        Config.RemoveDoll = true;
                        group.addAction(Actions.scaleTo(0.8f,0.8f));
                    }

                }else {
                    group.addAction(Actions.scaleTo(1.3f,1.3f));
                    Config.RemoveDoll = false;
                    if(Doll2.this!=null){
                        Config.CheckRemoveDoll=false;
                    }

                }
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                super.dragStop(event, x, y, pointer);
//                boardConfig.isDrag=false;
                if(Config.RemoveDoll==false){
                    if(gameScene.checkPosition(Doll2.this, new Vector2(pX, pY)) == -1){
                        backCard(pX, pY, zIndex);
                        gameScene.setTouchCards(Touchable.enabled);
                    }
                }


            }
        });
    }


    private void backCard(float pX, float pY, int zIndex){
        group.addAction(Actions.sequence(
                Actions.moveTo(pX, pY, 0.1f, Interpolation.fastSlow),
                GSimpleAction.simpleAction((d, a)->{
                    group.setZIndex(zIndex);
                    return true;
                })
        ));
    }
    public void dispose(){
        group.clear();
        group.remove();
    }
    public void visible(){
        group.setVisible(false);
    }
    private void price(){
       monney = type;
    }
    public void makeMonney(){
        if(Config.X2Coin==true){
            price();
            monney=monney*2;

        }else {
            price();
        }
        Group groupMonney  = new Group();
        group.addActor(groupMonney);
        Label LabelMoney = new Label("+"+monney,new Label.LabelStyle(font,null));
        LabelMoney.setFontScale(0.5f);
        LabelMoney.setAlignment(Align.center);
        LabelMoney.setOrigin(Align.center);
        LabelMoney.setPosition(0,30);
        groupMonney.addActor(LabelMoney);
        groupMonney.setOrigin(Align.center);
        groupMonney.addAction(Actions.sequence(
                Actions.moveBy(0,-50,0.5f,Interpolation.pow2),
                Actions.delay(0.5f),
                GSimpleAction.simpleAction((d,a)->{
                    groupMonney.clear();
                    groupMonney.remove();
                    return true;
                })
                //Actions.scaleTo(0.2f,0.2f,2f),
//                Actions.alpha(0,0.5f)
        ));
        Tweens.setTimeout(group,5,()->{
            Config.monneyRuntime +=monney;
            makeMonney();
        });
    }
    private void initAtlas(){
        atlas = GAssetsManager.getTextureAtlas("uiGame.atlas");
        font = GAssetsManager.getBitmapFont("font_white.fnt");
    }
}
