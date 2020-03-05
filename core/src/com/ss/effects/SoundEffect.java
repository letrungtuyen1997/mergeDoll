package com.ss.effects;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.ss.core.util.GAssetsManager;

/* renamed from: com.ss.effect.SoundEffect */
public class SoundEffect {
    public static int MAX_COMMON = 14;
    public static int MAX_MUSIC = 2;
    public static Music bgSound = null;
    public static Music bgSound2 = null;
    public static Sound[] commons = null;
    public static Music[] Music = null;
    public static boolean music = false;
    public static boolean music2 = false;
    public static boolean mute = false;

    public  static int doll1 = 0;
    public  static int doll2 = 1;
    public  static int doll3 = 2;
    public  static int click = 3;
    public  static int merge = 4;
    public  static int unlock = 5;
    public  static int getcoin = 6;
    public  static int panel_in = 7;
    public  static int panel_out = 8;

    public static int bg0 = 0;
    public static int bg1 = 1;

    public static Audio bg = null;

    public static void initSound() {
        commons = new Sound[MAX_COMMON];
        Music = new Music[MAX_MUSIC];
        commons[doll1] = GAssetsManager.getSound("doll1.mp3");
        commons[doll2] = GAssetsManager.getSound("doll2.mp3");
        commons[doll3] = GAssetsManager.getSound("doll3.mp3");
        commons[click] = GAssetsManager.getSound("click.mp3");
        commons[merge] = GAssetsManager.getSound("merge.mp3");
        commons[unlock] = GAssetsManager.getSound("unlock.mp3");
        commons[getcoin] = GAssetsManager.getSound("getCoin.mp3");
        commons[panel_in] = GAssetsManager.getSound("panel_open.mp3");
        commons[panel_out] = GAssetsManager.getSound("panel_close.mp3");
        Music[bg0] = GAssetsManager.getMusic("bg.mp3");
        Music[bg1] = GAssetsManager.getMusic("bg2.mp3");


    }

    public static void Play(int i) {
        if (!mute) {
            commons[i].play();
        }
    }

    public static void Playmusic(int i) {
        Music[i].play();
        Music[i].setLooping(true);
        Music[i].setVolume(5);

    }

    public static void Stopmusic(int i){
        Music[i].stop();
    }
    public static void Pausemusic(int i){
        Music[i].pause();
    }
}
