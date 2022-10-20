package com.reactnativefastshadow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class ShadowBitmap {
  public static Bitmap createShadowBitmap(Context context, int width, int height, float[] borderRadius, float blurRadius) {
    int inset = (int)Math.ceil(blurRadius);
    int bitmapWidth = width + 2 * inset;
    int bitmapHeight = height + 2 * inset;
    Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ALPHA_8);
    Canvas canvas = new Canvas(bitmap);

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(0xff000000);

    Path roundedRect = new Path();
    roundedRect.addRoundRect(inset, inset, inset + width, inset+ height, new float[] {
      borderRadius[0],
      borderRadius[0],
      borderRadius[1],
      borderRadius[1],
      borderRadius[2],
      borderRadius[2],
      borderRadius[3],
      borderRadius[3],
    }, Path.Direction.CW);
    canvas.drawPath(roundedRect, paint);

    try {
      if (blurRadius > 0) {
        blurBitmap(context, bitmap, blurRadius);
      }
      return bitmap;
    } catch (Exception e) {
      bitmap.recycle();
      return null;
    }
  }

  private static void blurBitmap(Context context, Bitmap bitmap, float radius) {
    RenderScript rs = null;
    Allocation input = null;
    Allocation output = null;
    ScriptIntrinsicBlur blurEffect = null;
    try {
      rs = RenderScript.create(context);
      input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
      output = Allocation.createTyped(rs, input.getType());
      blurEffect = ScriptIntrinsicBlur.create(rs, input.getElement());
      blurEffect.setRadius(radius);
      blurEffect.setInput(input);
      blurEffect.forEach(output);
      output.copyTo(bitmap);
    } finally {
      if (blurEffect != null) blurEffect.destroy();
      if (output != null) output.destroy();
      if (input != null) input.destroy();
      if (rs != null) rs.destroy();
    }
  }
}
