package com.reactnativefastshadow;

public class ShadowSpecs {
  // Renderscript does not support a blur radius greater than 25.
  // Thought for later: we could probably support them with downscaling
  static final float MAX_BLUR_RADIUS = 25;

  int shapeWidth;
  int shapeHeight;
  float[] cornerRadii;
  float blurRadius;
  NinePatchInsets ninePatchInsets;
  boolean useNinePatchHorizontally;
  boolean useNinePatchVertically;

  public ShadowSpecs(
    int shapeWidth, int shapeHeight, float[] cornerRadii, float blurRadius,
    NinePatchInsets ninePatchInsets, boolean useNinePatchHorizontally, boolean isUseNinePatchVertically
  ) {
    this.shapeWidth = shapeWidth;
    this.shapeHeight = shapeHeight;
    this.cornerRadii = cornerRadii;
    this.blurRadius = Math.min(Math.max(blurRadius, 0), MAX_BLUR_RADIUS);
    this.ninePatchInsets = ninePatchInsets;
    this.useNinePatchHorizontally = useNinePatchHorizontally;
    this.useNinePatchVertically = isUseNinePatchVertically;
    roundFloatValues();
  }

  // Round float values to the nearest 0.5.
  // Precision will be good enough and it might improve cache hits
  private void roundFloatValues() {
    blurRadius = Math.round(blurRadius * 2) / 2;
    for (int i = 0; i < 4; i++) {
      cornerRadii[i] = Math.round(cornerRadii[i] * 2) / 2;
    }
  }

  public String getCacheKey() {
    return String.format(
      "%d:%d:%.1f:%.1f:%.1f:%.1f:%.1f",
      shapeWidth, shapeHeight, cornerRadii[0], cornerRadii[1], cornerRadii[2], cornerRadii[3], blurRadius
    );
  }
}
