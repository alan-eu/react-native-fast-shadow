import React from 'react';
import {
  ColorValue,
  requireNativeComponent,
  StyleProp,
  StyleSheet,
  View,
  ViewProps,
  ViewStyle,
} from 'react-native';

type ShadowViewProps = ViewProps & {
  radius: number;
  color: ColorValue;
};

const ShadowView = requireNativeComponent<ShadowViewProps>('FastShadowView');

export const ShadowedView = ({ style, ...viewProps }: ViewProps) => {
  const [outerStyle, innerStyle] = splitStyle(style);
  const {
    shadowRadius = 3,
    shadowOpacity = 0,
    shadowColor = 'black',
    shadowOffset = { width: 0, height: -3 },
  } = outerStyle;
  const shadowInset = Math.ceil(shadowRadius);

  return (
    <View style={outerStyle} pointerEvents="box-none">
      <ShadowView
        style={{
          position: 'absolute',
          left: -shadowInset,
          right: -shadowInset,
          top: -shadowInset,
          bottom: -shadowInset,
          opacity: shadowOpacity,
          transform: [
            { translateX: shadowOffset.width },
            { translateY: shadowOffset.height },
          ],
        }}
        radius={shadowRadius}
        color={shadowColor}
        pointerEvents="none"
      />
      <View style={[innerStyle, { flexGrow: 1 }]} {...viewProps} />
    </View>
  );
};

function splitStyle(
  style: StyleProp<ViewStyle> | undefined
): [ViewStyle, ViewStyle] {
  const outerStyleProps: (keyof ViewStyle)[] = [
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
