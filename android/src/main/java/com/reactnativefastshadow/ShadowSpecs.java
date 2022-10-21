package com.reactnativefastshadow;

public class ShadowSpecs {
  int shapeWidth;
  int shapeHeight;
  float[] borderRadii;
  float blurRadius;
  NinePatchInsets ninePatchInsets;
  boolean useNinePatchHorizontally;
  boolean useNinePatchVertically;

  public ShadowSpecs(
    int shapeWidth, int shapeHeight, float[] borderRadii, float blurRadius,
    NinePatchInsets ninePatchInsets, boolean useNinePatchHorizontally, boolean isUseNinePatchVertically
  ) {
    this.shapeWidth = shapeWidth;
    this.shapeHeight = shapeHeight;
    this.borderRadii = borderRadii;
    this.blurRadius = blurRadius;
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
      borderRadii[i] = Math.round(borderRadii[i] * 2) / 2;
    }
  }

  public String getCacheKey() {
    return String.format(
      "%d:%d:%.1f:%.1f:%.1f:%.1f:%.1f",
      shapeWidth, shapeHeight, borderRadii[0], borderRadii[1], borderRadii[2], borderRadii[3], blurRadius
    );
  }
}
