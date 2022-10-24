import { requireNativeComponent, ViewProps } from 'react-native';

type FastShadowViewProps = ViewProps & {
  borderRadii: {
    topLeft: number;
    topRight: number;
    bottomLeft: number;
    bottomRight: number;
  };
};

export const FastShadowView =
  requireNativeComponent<FastShadowViewProps>('FastShadowView');
