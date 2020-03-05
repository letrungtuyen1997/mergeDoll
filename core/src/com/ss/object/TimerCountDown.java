package com.ss.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GLayerGroup;
import com.ss.core.util.GStage;

public class TimerCountDown {
        BitmapFont timmer;
        public static GLayerGroup group = new GLayerGroup();
        int tic=0;
        int minute=0;
        int second =0;
        Label timerStart;
        Runnable runnable;

        public boolean checkDone=false;
        private boolean isStop = false;

        public TimerCountDown( float x, float y,Runnable runnable){
            this.runnable= runnable;
            initFont();
            GStage.addToLayer(GLayer.ui,group);
            minute=2;
            second=59;
            timerStart = new Label(minute+":"+second, new Label.LabelStyle(timmer, Color.WHITE));
            countDown();
            timerStart.setFontScale(0.5f);
            timerStart.setPosition(0,0, Align.center);
            timerStart.setAlignment(Align.center);
            group.addActor(timerStart);
            group.setPosition(x,y,Align.center);
        }

        public static void setPause(boolean isPause){
            group.setPause(isPause);
        }

    public void countDown(){
        group.addAction(GSimpleAction.simpleAction((d,a)->{
            if(isStop){
            }
            else {
                tic++;
                if(tic==60){
                    tic=0;
                    second--;
                    timerStart.setText(minute+":"+second);
                    if(second==0&&minute>0){
                        second=59;
                        minute--;
                        timerStart.setText(minute+":"+second);

                    }else if(minute==0&&second==0){
                        group.addAction(Actions.run(runnable));
                        checkDone=true;
                    }
                }
            }
            return checkDone;
        }));
    }

        void initFont(){
            timmer = GAssetsManager.getBitmapFont("font_white.fnt");
        }

        public void dispose(){
            group.clearChildren();
            group.remove();
        }
}
