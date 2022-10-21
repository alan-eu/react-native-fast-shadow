import React from 'react';
import { I18nManager, StyleSheet, View, ViewStyle } from 'react-native';
import { FastShadowView } from './FastShadowView';

export function createShadowedComponent<T extends React.ComponentType<any>>(
  Component: T
): T {
  const ShadowedComponent = (({ style, ...viewProps }) => {
    const flattenedStyle = StyleSheet.flatten(style) ?? {};
    if ((flattenedStyle.shadowOpacity ?? 0) <= 0) {
      return <Component style={style} {...viewProps} />;
    }

    const [outerStyle, innerStyle] = splitStyle(flattenedStyle);
    const {
      shadowRadius = 3,
      shadowOpacity = 0,
      shadowColor = 'black',
      shadowOffset = { width: 0, height: -3 },
    } = outerStyle;

    // Renderscript does not support a blur radius greater than 25.
    // Note for later: we could probably support them with downscaling
    const maxShadowRadius = 25;
    const radius = Math.min(Math.max(shadowRadius, 0), maxShadowRadius);
    const inset = Math.ceil(radius);

    const borderRadii = {
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
        <FastShadowView
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
          borderRadii={borderRadii}
          pointerEvents="none"
        />
        <Component
          style={[innerStyle, { flexGrow: 1, flexShrink: 1 }]}
          {...viewProps}
        />
      </View>
    );
  }) as T;
  return ShadowedComponent;
}

function splitStyle(style: ViewStyle): [ViewStyle, ViewStyle] {
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

  const styleEntries = Object.entries(style);
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
