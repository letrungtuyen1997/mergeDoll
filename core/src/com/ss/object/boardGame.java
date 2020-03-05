package com.ss.object;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.StaticObjects.Config;

public class boardGame {
    private TextureAtlas uiGame ;
    private Group uiGroup = new Group();
    private Array<Doll> arrayDoll = new Array<>();

   public boardGame(TextureAtlas uiGame){
        this.uiGame = uiGame;
       GStage.addToLayer(GLayer.ui,uiGroup);
       createArrDoll();

    }

    private void createArrDoll(){
       for(int i=0;i<3;i++){
           for(int j=0;j<4;j++){
               Doll doll = new Doll(uiGame,uiGroup,i,j,"1");
               arrayDoll.add(doll);
           }
       }
    }
    private int CheckRec(Doll doll1 , Doll doll2){
       return -1;
    }


}
