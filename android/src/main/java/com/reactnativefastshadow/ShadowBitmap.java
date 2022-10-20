package com.reactnativefastshadow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class ShadowBitmap {
  public static Bitmap createShadowBitmap(Context context, int rectWidth, int rectHeight, float blurRadius) {
    int margin = (int)Math.ceil(blurRadius);
    int width = rectWidth + 2 * margin;
    int height = rectHeight + 2 * margin;
    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(0xff000000);
    canvas.drawRect(margin, margin, margin + rectWidth, margin + rectHeight, paint);

    blurBitmap(context, bitmap, blurRadius);

    return bitmap;
  }

  private static void blurBitmap(Context context, Bitmap bitmap, float radius) {
    RenderScript rs = RenderScript.create(context);
    Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
    Allocation output = Allocation.createTyped(rs, input.getType());
    ScriptIntrinsicBlur blurEffect = ScriptIntrinsicBlur.create(rs, input.getElement());
    blurEffect.setRadius(radius);
    blurEffect.setInput(input);
    blurEffect.forEach(output);
    output.copyTo(bitmap);
    blurEffect.destroy();
    output.destroy();
    input.destroy();
    rs.destroy();
  }
}
