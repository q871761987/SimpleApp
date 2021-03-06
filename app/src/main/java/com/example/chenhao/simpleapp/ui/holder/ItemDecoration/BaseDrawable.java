package com.example.chenhao.simpleapp.ui.holder.ItemDecoration;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * The type Base drawable.
 *  现已废弃
 */

public class BaseDrawable extends Drawable {
    private Paint mPaint;  
    private Bitmap mBitmap;
  
    private RectF rectF;

    /**
     * Instantiates a new Base drawable.
     *
     * @param bitmap the bitmap
     */
    public BaseDrawable(Bitmap bitmap) {
        mBitmap = bitmap;  
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);  
        mPaint = new Paint();
        mPaint.setAntiAlias(true);  
        mPaint.setShader(bitmapShader);  
    }  
  
    @Override  
    public void setBounds(int left, int top, int right, int bottom) {  
        super.setBounds(left, top, right, bottom);  
        rectF = new RectF(left, top, right, bottom);  
    }  
  
    @Override  
    public void draw(Canvas canvas) {
        canvas.drawRect(rectF, mPaint);  
//            canvas.drawRoundRect(rectF, 30, 30, mPaint);  
    }  
  
    @Override  
    public int getIntrinsicWidth() {  
        return mBitmap.getWidth();  
    }  
  
    @Override  
    public int getIntrinsicHeight() {  
        return mBitmap.getHeight();  
    }  
  
    @Override  
    public void setAlpha(int alpha) {  
        mPaint.setAlpha(alpha);  
    }  
  
    @Override  
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);  
    }  
  
    @Override  
    public int getOpacity() {  
        return PixelFormat.TRANSLUCENT;
    }  
  
}  