package com.reactnativefastshadow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Path;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import java.nio.ByteBuffer;

public class ShadowBitmap {
  public static Bitmap createShadowBitmap(Context context, int width, int height, float[] borderRadii, float blurRadius) {
    int inset = (int)Math.ceil(blurRadius);
    int bitmapWidth = width + 2 * inset;
    int bitmapHeight = height + 2 * inset;
    Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ALPHA_8);
    Canvas canvas = new Canvas(bitmap);

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(0xff000000);

    Path roundedRect = new Path();
    roundedRect.addRoundRect(inset, inset, inset + width, inset+ height, new float[]{
      borderRadii[0],
      borderRadii[0],
      borderRadii[1],
      borderRadii[1],
      borderRadii[2],
      borderRadii[2],
      borderRadii[3],
      borderRadii[3],
    }, Path.Direction.CW);
    canvas.drawPath(roundedRect, paint);

    try {
      if (blurRadius > 0) {
        blurBitmap(context, bitmap, blurRadius);
      }

      createNinePatch(bitmap, getNinePatchInsets(borderRadii, blurRadius));
      return bitmap;
    } catch (Exception e) {
      bitmap.recycle();
      return null;
    }
  }

  private static void blurBitmap(Context context, Bitmap bitmap, float blurRadius) {
    RenderScript rs = null;
    Allocation input = null;
    Allocation output = null;
    ScriptIntrinsicBlur blurEffect = null;
    try {
      rs = RenderScript.create(context);
      input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
      output = Allocation.createTyped(rs, input.getType());
      blurEffect = ScriptIntrinsicBlur.create(rs, input.getElement());
      blurEffect.setRadius(blurRadius);
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

  private static NinePatch createNinePatch(Bitmap bitmap, NinePatchInsets insets) {
    // The format of the chunk param is not documented by Android.
    // Here, it is inferred from Android source code:
    // https://android.googlesource.com/platform/frameworks/base/+/56a2301/include/androidfw/ResourceTypes.h#45
    ByteBuffer chunk = ByteBuffer.allocate(4 + (4 * 4) + (4 * 4) + (9 * 4));
    chunk.put((byte) 1);    // wasDeserialized. Not sure if it should be 1 or 0...
    chunk.put((byte) 2);    // numXDivs: 2 as we want a 9-patch => 3 horizontal segments and so, 3 - 1 dividers
    chunk.put((byte) 2);    // numYDivs: 2 as we want a 9-patch => 3 vertical segments and so, 3 - 1 dividers
    chunk.put((byte) 9);    // numColors: 9 as there are 9 segments in a 9-patch
    chunk.putInt(insets.left); // xDivs[0]
    chunk.putInt(bitmap.getWidth() - insets.right); // xDivs[1]
    chunk.putInt(insets.top); // yDivs[0]
    chunk.putInt(bitmap.getWidth() - insets.bottom); // yDivs[1]
    chunk.putInt(0); // paddingLeft: 0 as there are no children so it doesn't matter
    chunk.putInt(0); // paddingRight: 0 as there are no children so it doesn't matter
    chunk.putInt(0); // paddingTop: 0 as there is are children so it doesn't matter
    chunk.putInt(0); // paddingBottom: 0 as there are no children so it doesn't matter
    chunk.putInt(0x1); // top-left corner color: 0x1 as it's not a solid color
    chunk.putInt(0x1); // top edge color: 0x1 as it's not a solid color
    chunk.putInt(0x1); // top-right corner color: 0x1 as it's not a solid color
    chunk.putInt(0x1); // left edge color: 0x1 as it's not a solid color
    chunk.putInt(0xff000000); // center color: this is opaque black
    chunk.putInt(0x1); // right edge color: 0x1 as it's not a solid color
    chunk.putInt(0x1); // bottom-left corner color: 0x1 as it's not a solid color
    chunk.putInt(0x1); // bottom edge color: 0x1 as it's not a solid color
    chunk.putInt(0x1); // bottom-right corner color: 0x1 as it's not a solid color
    return new NinePatch(bitmap, chunk.array());
  }

  private static NinePatchInsets getNinePatchInsets(float[] borderRadii, float blurRadius) {
    return new NinePatchInsets(
      getNinePatchInsetForCorner(Math.max(borderRadii[0], borderRadii[3]), blurRadius),
      getNinePatchInsetForCorner(Math.max(borderRadii[1], borderRadii[2]), blurRadius),
      getNinePatchInsetForCorner(Math.max(borderRadii[0], borderRadii[1]), blurRadius),
      getNinePatchInsetForCorner(Math.max(borderRadii[2], borderRadii[3]), blurRadius)
    );
  }

  private static int getNinePatchInsetForCorner(float borderRadius, float blurRadius) {
    return (int) Math.ceil(2 * blurRadius + ((Math.sqrt(2) - 1) * borderRadius / Math.sqrt(2)));
  }
}

class NinePatchInsets {
  public int left;
  public int right;
  public int top;
  public int bottom;

  public NinePatchInsets(int left, int right, int top, int bottom) {
    this.left = left;
    this.right = right;
    this.top = top;
    this.bottom = bottom;
  }
}
