import { ColorValue, requireNativeComponent, ViewProps } from 'react-native';

type BorderRadii = {
  topLeft: number;
  topRight: number;
  bottomLeft: number;
  bottomRight: number;
};

type FastShadowViewProps = ViewProps & {
  radius: number;
  color: ColorValue;
  borderRadii: BorderRadii;
};

export const FastShadowView =
  requireNativeComponent<FastShadowViewProps>('FastShadowView');
