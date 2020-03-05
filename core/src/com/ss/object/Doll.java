package com.ss.object;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.StaticObjects.Config;

public class Doll {
    private TextureAtlas atlas;
    private Group group;
    private int  row,col;
    private String key;
    private Image Doll;
    private int Zindex =0;
    Doll(TextureAtlas atlas,Group group,int row,int col , String key){
        this.group = group;
        this.atlas = atlas;
        this.row = row;
        this.col = col;
        this.key = key;
        ////// create A Doll//////
        createDoll();
       // listennerDoll();
    }
    private void createDoll(){
        Doll = GUI.createImage(atlas,key);
        Doll.setWidth(Doll.getWidth()* Config.ratioX);
        Doll.setHeight(Doll.getHeight()* Config.ratioY);
        Doll.setOrigin(Align.center);
        Doll.setPosition(120+Doll.getWidth()*col,400+(Doll.getHeight()-40)*row,Align.center);
        group.addActor(Doll);
        System.out.println("pos");
    }
    public void listennerDoll(){
        Doll.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Doll.addAction(Actions.scaleTo(1.2f,1.2f,0.1f));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Doll.addAction(Actions.scaleTo(1f,1f,0.1f));


            }
        });
        Doll.addListener(new DragListener(){
            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                Doll.addAction(Actions.scaleTo(1.2f,1.2f,0.1f));
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                Zindex = Doll.getZIndex();
                Doll.moveBy(x - Doll.getWidth() / 2, y - Doll.getHeight() / 2);
                Doll.setZIndex(50);

            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                Doll.addAction(Actions.scaleTo(1f,1f,0.1f));
                Doll.setZIndex(Zindex);

            }
        });
    }
}
