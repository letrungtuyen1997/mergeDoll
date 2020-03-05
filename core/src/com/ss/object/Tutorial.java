package com.ss.object;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.StaticObjects.Config;

public class Tutorial {
    private TextureAtlas atlas;
    private Group group = new Group();
    private Array<Image> arrBg = new Array<>();
    public Tutorial(){
        initatlas();
        GStage.addToLayer(GLayer.top,group);
        /////// render //////
        for (int i=1;i<12;i++){
            Image bg = GUI.createImage(atlas,""+i);
            bg.setWidth(bg.getWidth()* Config.ratioX);
            bg.setHeight(bg.getHeight()* Config.ratioY);
            group.addActor(bg);
            arrBg.add(bg);
        }
        ////// et default/////
        setDefault();
        ////// eventClick /////
        for (int i=0;i<arrBg.size;i++){
            int finalI = i;
            arrBg.get(i).addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    SoundEffect.Play(SoundEffect.click);
                    if((finalI+1)>arrBg.size){
                        group.clear();
                        group.remove();
                    }
                    setVisible(finalI+1);
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
        }



    }
    private void setDefault(){
        for (int i=0;i<arrBg.size;i++){
            if(i!=0){
                arrBg.get(i).setVisible(false);
            }
        }

    }
    private void setVisible(int index){
        for (int i=0;i<arrBg.size;i++){
            if(i==index){
                arrBg.get(i).setVisible(true);
            }else {
                arrBg.get(i).setVisible(false);
            }
        }

    }


    private void initatlas(){
        atlas = GAssetsManager.getTextureAtlas("tutorial.atlas");
    }
}
