package com.markupartist.android.actionbar.example.fish.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.markupartist.android.actionbar.example.fish.ConstantUtil;
import com.markupartist.android.actionbar.example.fish.R;

/**
 * Created by PC on 2016/11/6.
 */
public class ReadyView extends BaseView {
    private Rect rect;

    private Bitmap background;
    private Bitmap text;
    private Bitmap button;
    private Bitmap button2;

    private float text_x;
    private float text_y;
    private float button_x;
    private float button_y;
    private float button_y2;
    private float strwid;
    private float strhei;
    private boolean isBtChange;				// 按钮图片改变的标记
    private boolean isBtChange2;
    private String startGame = "开始游戏";	// 按钮的文字
    private String exitGame = "退出游戏";

    public ReadyView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        paint.setTextSize(40);
        rect = new Rect();
        thread = new Thread(this);
    }
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        super.surfaceChanged(arg0, arg1, arg2, arg3);
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        super.surfaceCreated(arg0);
        initBitmap();
        if(thread.isAlive()){
            thread.start();
        }
        else{
            thread = new Thread(this);
            thread.start();
        }
    }
    @Override
    public void initBitmap() {
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bg_01);
        text = BitmapFactory.decodeResource(getResources(), R.drawable.text);

        button = BitmapFactory.decodeResource(getResources(), R.drawable.button);
        button2 = BitmapFactory.decodeResource(getResources(),R.drawable.button2);
        scalex = screen_width / background.getWidth();
        scaley = screen_height / background.getHeight();
        text_x = screen_width / 2 - text.getWidth() / 2;
        text_y = screen_height / 4 - text.getHeight();
        button_x = screen_width / 2 - button.getWidth() / 2;
        button_y = screen_height / 2 + button.getHeight();
        button_y2 = button_y + button.getHeight() + 40;
        paint.getTextBounds(startGame, 0, startGame.length(), rect);
        strwid = rect.width();
        strhei = rect.height();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN
                && event.getPointerCount() == 1){
            float x = event.getX();
            float y = event.getY();
            if(x >  button_x && x < button_x + button.getWidth()){
                if(y > button_y && y < button_y + button.getHeight()){
                    isBtChange = true;
                    drawSelf();
                    mainActivity.getHandler().sendEmptyMessage(ConstantUtil.TO_MAIN_VIEW);
                }
                else if(y > button_y2 && y < button_y2 + button.getHeight()){
                    isBtChange2 = true;
                    drawSelf();
                    mainActivity.getHandler().sendEmptyMessage(ConstantUtil.END_GAME);
                }
            }
            return true;
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            float y = event.getY();
            if (x > button_x && x < button_x + button.getWidth()
                    && y > button_y && y < button_y + button.getHeight()) {
                isBtChange = true;
            } else {
                isBtChange = false;
            }
            if (x > button_x && x < button_x + button.getWidth()
                    && y > button_y2 && y < button_y2 + button.getHeight()) {
                isBtChange2 = true;
            } else {
                isBtChange2 = false;
            }
            return true;
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            isBtChange = false;
            isBtChange2 = false;
            return true;
        }
        return false;
    }
    @Override
    public void release() {
        // TODO Auto-generated method stub
        if (!text.isRecycled()) {
            text.recycle();
        }
        if (!button.isRecycled()) {
            button.recycle();
        }
        if (!button2.isRecycled()) {
            button2.recycle();
        }
        if (!background.isRecycled()) {
            background.recycle();
        }
    }
    @Override
    public void drawSelf() {
        // TODO Auto-generated method stub
        try {
            canvas = sfh.lockCanvas();
            canvas.drawColor(Color.BLACK); 						// 绘制背景色
            canvas.save();
            canvas.scale(scalex, scaley, 0, 0);					// 计算背景图片与屏幕的比例
            canvas.drawBitmap(background, 0, 0, paint); 		// 绘制背景图
            canvas.restore();
            canvas.drawBitmap(text, text_x, text_y, paint);		// 绘制文字图片
            //当手指滑过按钮时变换图片
            if (isBtChange) {
                canvas.drawBitmap(button2, button_x, button_y, paint);
            }
            else {
                canvas.drawBitmap(button, button_x, button_y, paint);
            }
            if (isBtChange2) {
                canvas.drawBitmap(button2, button_x, button_y2, paint);
            }
            else {
                canvas.drawBitmap(button, button_x, button_y2, paint);
            }
            //开始游戏的按钮
            canvas.drawText(startGame, screen_width / 2 - strwid / 2, button_y
                    + button.getHeight() / 2 + strhei / 2, paint);
            //退出游戏的按钮
            canvas.drawText(exitGame, screen_width / 2 - strwid / 2, button_y2
                    + button.getHeight() / 2 + strhei / 2, paint);

        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);
        }
    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (threadFlag) {
            long startTime = System.currentTimeMillis();
            drawSelf();
            long endTime = System.currentTimeMillis();
            try {
                if (endTime - startTime < 400)
                    Thread.sleep(400 - (endTime - startTime));
            } catch (InterruptedException err) {
                err.printStackTrace();
            }
        }
    }
}
