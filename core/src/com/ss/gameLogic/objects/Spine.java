package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.ss.core.util.GStage;
import com.ss.gameLogic.StaticObjects.Config;

import ze.spineactor.esotericsoftware.spine.AnimationState;
import ze.spineactor.esotericsoftware.spine.AnimationStateData;
import ze.spineactor.esotericsoftware.spine.Skeleton;
import ze.spineactor.esotericsoftware.spine.SkeletonData;
import ze.spineactor.esotericsoftware.spine.SkeletonJson;
import ze.spineactor.esotericsoftware.spine.SkeletonRenderer;
import ze.spineactor.esotericsoftware.spine.SkeletonRendererDebug;
import ze.spineactor.esotericsoftware.spine.utils.TwoColorPolygonBatch;

public class Spine extends Actor {
    TextureAtlas atlas;
    public Skeleton skeleton;
    public AnimationState state;
    SkeletonRenderer renderer;
    SkeletonRendererDebug debugRenderer;

    TwoColorPolygonBatch tcpBatch;

    public Spine(String Atlas, String Json ){
        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(false); // PMA results in correct blending without outlines.

        atlas = new TextureAtlas(Gdx.files.internal(Atlas));
        SkeletonJson json = new SkeletonJson(atlas);

        json.setScale(0.5f); // Load the skeleton at 60% the size it was in Spine.
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(Json));

        skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
        skeleton.setScale(Config.ratioX+0.7f,-(Config.ratioX+0.7f));
        skeleton.setPosition(skeleton.getData().getWidth()/2, skeleton.getData().getHeight());
        //skeleton.set

        AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
        //stateData.setMix("run", "jump", 0.2f);
        //stateData.setMix("jump", "run", 0.2f);
        state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
        state.setTimeScale(0.5f); // Slow all animations down to 50% speed.
        // Queue animations on track 0.
        state.setAnimation(0, "animation", true);

        tcpBatch = new TwoColorPolygonBatch();
        tcpBatch.setPremultipliedAlpha(true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        state.update(delta);
        state.apply(skeleton);
        skeleton.updateWorldTransform();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        //renderer.draw(batch, skeleton);

        drawMesh(batch);
    }

    public void drawMesh(Batch batch){
        batch.end();
        tcpBatch.setProjectionMatrix(batch.getProjectionMatrix());
        tcpBatch.setTransformMatrix(batch.getTransformMatrix());
        tcpBatch.begin();

        renderer.draw(tcpBatch, skeleton);
        tcpBatch.end();

        batch.begin();
    }
}
