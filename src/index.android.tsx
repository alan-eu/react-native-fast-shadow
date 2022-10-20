import React from 'react';
import {
  ColorValue,
  I18nManager,
  requireNativeComponent,
  StyleProp,
  StyleSheet,
  View,
  ViewProps,
  ViewStyle,
} from 'react-native';

type BorderRadius = {
  topLeft: number;
  topRight: number;
  bottomLeft: number;
  bottomRight: number;
};

type ShadowViewProps = ViewProps & {
  radius: number;
  color: ColorValue;
  borderRadius: BorderRadius;
};

const ShadowView = requireNativeComponent<ShadowViewProps>('FastShadowView');

// Renderscript does not support a blur radius greater than 25.
// Note for later: we could probably support them with downscaling
const maxShadowRadius = 25;

export const ShadowedView = ({ style, ...viewProps }: ViewProps) => {
  const [outerStyle, innerStyle] = splitStyle(style);
  const {
    shadowRadius = 3,
    shadowOpacity = 0,
    shadowColor = 'black',
    shadowOffset = { width: 0, height: -3 },
  } = outerStyle;
  const radius = Math.min(Math.max(shadowRadius, 0), maxShadowRadius);
  const inset = Math.ceil(radius);

  const borderRadius = {
    topLeft:
      (I18nManager.isRTL
        ? innerStyle.borderTopEndRadius
        : innerStyle.borderTopStartRadius) ??
      innerStyle.borderTopLeftRadius ??
      innerStyle.borderRadius ??
      0,
    topRight:
      (I18nManager.isRTL
        ? innerStyle.borderTopStartRadius
        : innerStyle.borderTopEndRadius) ??
      innerStyle.borderTopRightRadius ??
      innerStyle.borderRadius ??
      0,
    bottomLeft:
      (I18nManager.isRTL
        ? innerStyle.borderBottomEndRadius
        : innerStyle.borderBottomStartRadius) ??
      innerStyle.borderBottomLeftRadius ??
      innerStyle.borderRadius ??
      0,
    bottomRight:
      (I18nManager.isRTL
        ? innerStyle.borderBottomStartRadius
        : innerStyle.borderBottomEndRadius) ??
      innerStyle.borderBottomRightRadius ??
      innerStyle.borderRadius ??
      0,
  };

  return (
    <View style={outerStyle} pointerEvents="box-none">
      <ShadowView
        style={{
          position: 'absolute',
          left: -inset,
          right: -inset,
          top: -inset,
          bottom: -inset,
          opacity: shadowOpacity,
          transform: [
            { translateX: shadowOffset.width },
            { translateY: shadowOffset.height },
          ],
        }}
        color={shadowColor}
        radius={radius}
        borderRadius={borderRadius}
        pointerEvents="none"
      />
      <View
        style={[innerStyle, { flexGrow: 1, flexShrink: 1 }]}
        {...viewProps}
      />
    </View>
  );
};

function splitStyle(
  style: StyleProp<ViewStyle> | undefined
): [ViewStyle, ViewStyle] {
  const outerStyleProps = [
    'alignSelf',
    'display',
    'flex',
    'flexBasis',
    'flexShrink',
    'flexGrow',
    'margin',
    'marginBottom',
    'marginEnd',
    'marginHorizontal',
    'marginLeft',
    'marginRight',
    'marginStart',
    'marginTop',
    'marginVertical',
    'width',
    'height',
    'minWidth',
    'minHeight',
    'maxWidth',
    'maxHeight',
    'aspectRatio',
    'left',
    'right',
    'bottom',
    'left',
    'start',
    'end',
    'zIndex',
    'shadowRadius',
    'shadowOpacity',
    'shadowColor',
    'shadowOffset',
    'elevation',
    'transform',
    'transformMatrix',
    'translateX',
    'translateY',
    'rotation',
    'scaleX',
    'scaleY',
  ];

  const styleEntries = Object.entries(StyleSheet.flatten(style) ?? {});
  const outerEntries = styleEntries.filter((entry) =>
    outerStyleProps.includes(entry[0])
  );
  const innerEntries = styleEntries.filter(
    (entry) => !outerStyleProps.includes(entry[0])
  );
  const outerStyle = Object.fromEntries(outerEntries);
  const innerStyle = Object.fromEntries(innerEntries);
  return [outerStyle, innerStyle];
}
