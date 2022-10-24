import React from 'react';
import {
  I18nManager,
  StyleProp,
  StyleSheet,
  ViewProps,
  ViewStyle,
} from 'react-native';
import { FastShadowView, FastShadowViewProps } from './FastShadowView';

export class ShadowedView extends React.Component<ViewProps> {
  render(): React.ReactNode {
    const { style, children } = this.props;
    const child = React.isValidElement(children)
      ? (children as React.ReactElement<ViewProps>)
      : undefined;
    const cornerRadii = getCornerRadiiFromStyle(style) ??
      getCornerRadiiFromStyle(child?.props.style) ?? {
        topLeft: 0,
        topRight: 0,
        bottomLeft: 0,
        bottomRight: 0,
      };
    return <FastShadowView {...this.props} cornerRadii={cornerRadii} />;
  }
}

function getCornerRadiiFromStyle(
  style: StyleProp<ViewStyle> | undefined
): FastShadowViewProps['cornerRadii'] | undefined {
  const borderRadiusProps = [
    'borderRadius',
    'borderTopLeftRadius',
    'borderTopStartRadius',
    'borderTopRightRadius',
    'borderTopEndRadius',
    'borderBottomLeftRadius',
    'borderBottomStartRadius',
    'borderBottomRightRadius',
    'borderBottomEndRadius',
  ] as const;

  const s = StyleSheet.flatten(style) ?? {};
  if (!s.backgroundColor && !borderRadiusProps.some((prop) => !!s[prop])) {
    return undefined;
  }

  return {
    topLeft:
      (I18nManager.isRTL ? s.borderTopEndRadius : s.borderTopStartRadius) ??
      s.borderTopLeftRadius ??
      s.borderRadius ??
      0,
    topRight:
      (I18nManager.isRTL ? s.borderTopStartRadius : s.borderTopEndRadius) ??
      s.borderTopRightRadius ??
      s.borderRadius ??
      0,
    bottomLeft:
      (I18nManager.isRTL
        ? s.borderBottomEndRadius
        : s.borderBottomStartRadius) ??
      s.borderBottomLeftRadius ??
      s.borderRadius ??
      0,
    bottomRight:
      (I18nManager.isRTL
        ? s.borderBottomStartRadius
        : s.borderBottomEndRadius) ??
      s.borderBottomRightRadius ??
      s.borderRadius ??
      0,
  };
}
