import React from 'react';
import { I18nManager, StyleSheet, ViewProps } from 'react-native';
import { FastShadowView } from './FastShadowView';

export class ShadowedView extends React.Component<ViewProps> {
  render(): React.ReactNode {
    const style = StyleSheet.flatten(this.props.style) ?? {};
    const { elevation, ...viewStyle } = style;

    const borderRadii = {
      topLeft:
        (I18nManager.isRTL
          ? style.borderTopEndRadius
          : style.borderTopStartRadius) ??
        style.borderTopLeftRadius ??
        style.borderRadius ??
        0,
      topRight:
        (I18nManager.isRTL
          ? style.borderTopStartRadius
          : style.borderTopEndRadius) ??
        style.borderTopRightRadius ??
        style.borderRadius ??
        0,
      bottomLeft:
        (I18nManager.isRTL
          ? style.borderBottomEndRadius
          : style.borderBottomStartRadius) ??
        style.borderBottomLeftRadius ??
        style.borderRadius ??
        0,
      bottomRight:
        (I18nManager.isRTL
          ? style.borderBottomStartRadius
          : style.borderBottomEndRadius) ??
        style.borderBottomRightRadius ??
        style.borderRadius ??
        0,
    };

    return (
      <FastShadowView
        {...this.props}
        style={viewStyle}
        borderRadii={borderRadii}
      />
    );
  }
}
