package com.reactnativefastshadow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ShadowFactory {
  public ShadowSpecs getShadowSpecs(int width, int height, float[] cornerRadii, float blurRadius) {
    NinePatchInsets ninePatchInsets = getNinePatchInsets(cornerRadii, blurRadius);
    int ninePatchWidth = ninePatchInsets.left + ninePatchInsets.right + 1;
    int ninePatchHeight = ninePatchInsets.top + ninePatchInsets.bottom + 1;
    boolean useNinePatchHorizontally = width >= ninePatchWidth;
    boolean useNinePatchVertically = height >= ninePatchHeight;
    int shapeWidth = useNinePatchHorizontally ? ninePatchWidth : width;
    int shapeHeight = useNinePatchVertically ? ninePatchHeight : height;
    return new ShadowSpecs(
      shapeWidth, shapeHeight, cornerRadii, blurRadius,
      ninePatchInsets, useNinePatchHorizontally, useNinePatchVertically
    );
  }

  public Shadow createShadow(Context context, ShadowSpecs specs) {
    if (specs.shapeWidth <= 0 || specs.shapeHeight <= 0) {
      return null;
    }

    float[] cornerRadii = specs.cornerRadii;
    float blurRadius = specs.blurRadius;
    NinePatchInsets ninePatchInsets = specs.ninePatchInsets;
    boolean useNinePatchHorizontally = specs.useNinePatchHorizontally;
    boolean useNinePatchVertically = specs.useNinePatchVertically;
    int inset = (int)Math.ceil(blurRadius);
    int bitmapWidth = specs.shapeWidth + 2 * inset;
    int bitmapHeight = specs.shapeHeight + 2 * inset;

    Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
    bitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);

    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(0xff000000);
    Path roundedRect = new Path();
    roundedRect.addRoundRect(
      new RectF(inset, inset, bitmapWidth - inset, bitmapHeight - inset),
      new float[]{
        cornerRadii[0],
        cornerRadii[0],
        cornerRadii[1],
        cornerRadii[1],
        cornerRadii[2],
        cornerRadii[2],
        cornerRadii[3],
        cornerRadii[3],
      },
      Path.Direction.CW
    );
    canvas.drawPath(roundedRect, paint);

    try {
      if (blurRadius > 0) {
        blurBitmap(context, bitmap, blurRadius);
      }
      Drawable drawable;
      if (useNinePatchHorizontally || useNinePatchVertically) {
        NinePatch ninePatch = createNinePatch(bitmap, ninePatchInsets, inset, useNinePatchHorizontally, useNinePatchVertically);
        drawable = new NinePatchDrawable(context.getResources(), ninePatch);
      } else {
        drawable = new BitmapDrawable(context.getResources(), bitmap);
      }
      return new Shadow(specs, drawable, bitmap);
    } catch (Exception e) {
      bitmap.recycle();
      return null;
    }
  }

  private void blurBitmap(Context context, Bitmap bitmap, float blurRadius) {
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

  // The format of the chunk param is not documented by Android.
  // Here, it is inferred from Android source code:
  // https://android.googlesource.com/platform/frameworks/base/+/master/libs/androidfw/include/androidfw/ResourceTypes.h#80
  // https://android.googlesource.com/platform/frameworks/base/+/master/libs/androidfw/ResourceTypes.cpp#221
  private NinePatch createNinePatch(Bitmap bitmap, NinePatchInsets insets, int extraInset, boolean horizontally, boolean vertically) {
    int numXDivs = horizontally ? 2 : 0;
    int numYDivs = vertically ? 2 : 0;
    int numColors = (numXDivs + 1) * (numYDivs + 1);
    boolean isNinePatch = horizontally && vertically;

    int length = 4 + (4 * 4) + ((numXDivs + numYDivs) * 4) + (numColors * 4) + (3 * 4); // Header + paddings + divs + colors + unused bytes
    ByteBuffer chunk = ByteBuffer.allocate(length);
    chunk.order(ByteOrder.nativeOrder());
    chunk.put((byte) 1); // wasDeserialized
    chunk.put((byte) numXDivs); // numXDivs
    chunk.put((byte) numYDivs); // numYDivs
    chunk.put((byte) numColors); // numColors
    chunk.position(chunk.position() + 8); // skip 8 bytes
    chunk.putInt(0); // paddingLeft: unused as there will be no child views
    chunk.putInt(0); // paddingRight: unused as there will be no child views
    chunk.putInt(0); // paddingTop: unused as there will be no child views
    chunk.putInt(0); // paddingBottom: unused as there will be no child views
    chunk.position(chunk.position() + 4); // skip 4 bytes
    if (horizontally) {
      chunk.putInt(insets.left + extraInset); // xDivs[0]
      chunk.putInt(bitmap.getWidth() - insets.right - extraInset); // xDivs[1]
    }
    if (vertically) {
      chunk.putInt(insets.top + extraInset); // yDivs[0]
      chunk.putInt(bitmap.getHeight() - insets.bottom - extraInset); // yDivs[1]
    }
    if (isNinePatch) chunk.putInt(0x1); // top-left corner color: 0x1 as it's not a solid color
    if (vertically) chunk.putInt(0x1); // top edge color: 0x1 as it's not a solid color
    if (isNinePatch) chunk.putInt(0x1); // top-right corner color: 0x1 as it's not a solid color
    if (horizontally) chunk.putInt(0x1); // left edge color: 0x1 as it's not a solid color
    chunk.putInt(isNinePatch ? 0xff000000 : 0x1); // center: this is solid black in 9-patches
    if (horizontally) chunk.putInt(0x1); // right edge color: 0x1 as it's not a solid color
    if (isNinePatch) chunk.putInt(0x1); // bottom-left corner color: 0x1 as it's not a solid color
    if (vertically) chunk.putInt(0x1); // bottom edge color: 0x1 as it's not a solid color
    if (isNinePatch) chunk.putInt(0x1); // bottom-right corner color: 0x1 as it's not a solid color
    return new NinePatch(bitmap, chunk.array());
  }

  private NinePatchInsets getNinePatchInsets(float[] cornerRadii, float blurRadius) {
    return new NinePatchInsets(
      getNinePatchInsetForCorner(Math.max(cornerRadii[0], cornerRadii[3]), blurRadius),
      getNinePatchInsetForCorner(Math.max(cornerRadii[1], cornerRadii[2]), blurRadius),
      getNinePatchInsetForCorner(Math.max(cornerRadii[0], cornerRadii[1]), blurRadius),
      getNinePatchInsetForCorner(Math.max(cornerRadii[2], cornerRadii[3]), blurRadius)
    );
  }

  private int getNinePatchInsetForCorner(float cornerRadius, float blurRadius) {
    return (int) Math.ceil(blurRadius + cornerRadius);
  }
}
