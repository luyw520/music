package com.music.presenter;

import android.content.Context;

import com.music.model.MusicModel;
import com.music.ui.view.IMusicView;

/**
 * Created by lyw on 2017/12/10.
 */

public class LocalMusicPresenter extends BasePresenter<MusicModel,IMusicView> {


    public void sortMp3InfosByTitle(Context context){
        mModel.sortMp3InfosByTitle(context);
    }

}
