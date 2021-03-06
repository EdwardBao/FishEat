package com.markupartist.android.actionbar.example.fish;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.markupartist.android.actionbar.example.fish.View.EndView;
import com.markupartist.android.actionbar.example.fish.View.MainView;
import com.markupartist.android.actionbar.example.fish.View.ReadyView;

public class MainActivity extends AppCompatActivity {
    private ReadyView readyView;
    private EndView endView;
    private MainView mainView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == ConstantUtil.TO_MAIN_VIEW){
                toMainView();
            }
            else  if(msg.what == ConstantUtil.TO_END_VIEW){
                toEndView(msg.arg1);
            }
            else  if(msg.what == ConstantUtil.END_GAME){
                endGame();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        readyView = new ReadyView(this);

        setContentView(readyView);
    }
    public Handler getHandler() {
        return handler;
    }
    public void toEndView(int score){
        if(endView == null){
            endView = new EndView(this);
            endView.setScore(score);
        }
        setContentView(endView);
        mainView = null;
    }
    public void endGame(){
        if(readyView != null){
            readyView.setThreadFlag(false);
        }
        else if(mainView != null){
            mainView.setThreadFlag(false);
        }
        else if(endView != null){
            endView.setThreadFlag(false);
        }
        this.finish();
    }
    public void toMainView(){
        if(mainView == null){
            mainView = new MainView(this);
        }
        setContentView(mainView);
        readyView = null;
        endView = null;
    }
}
