package com.reactnativefastshadow;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.view.ReactViewGroup;
import com.facebook.react.views.view.ReactViewManager;

public class FastShadowViewManagerImpl {

  public static final String NAME = "FastShadowView";

  public static FastShadowView createViewInstance(ThemedReactContext context) {
    return new FastShadowView(context);
  }

  public static void onDropViewInstance(@NonNull ReactViewGroup view) {
    ((FastShadowView) view).releaseShadow();
  }

  public static void setShadowColor(FastShadowView view, int color) {
    view.setColor(color);
  }

  public static void setShadowOpacity(FastShadowView view, float opacity) {
    view.setOpacity(opacity);
  }

  public static void setShadowRadius(FastShadowView view, float radius) {
    view.setRadius(radius);
  }

  public static void setShadowOffset(FastShadowView view, ReadableMap offset) {
    if (offset == null) {
      view.resetOffset();
    } else {
      view.setOffset(
        (float) offset.getDouble("width"),
        (float) offset.getDouble("height")
      );
    }
  }

  public static void setCornerRadii(FastShadowView view, ReadableMap borderRadius) {
    view.setCornerRadii(new float[]{
      (float) borderRadius.getDouble("topLeft"),
      (float) borderRadius.getDouble("topRight"),
      (float) borderRadius.getDouble("bottomRight"),
      (float) borderRadius.getDouble("bottomLeft")
    });
  }
}
