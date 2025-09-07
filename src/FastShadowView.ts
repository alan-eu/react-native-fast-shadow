import { requireNativeComponent, ViewProps } from 'react-native';

export type FastShadowViewProps = ViewProps & {
  cornerRadii: {
    topLeft: number;
    topRight: number;
    bottomLeft: number;
    bottomRight: number;
  };
};

const isFabricEnabled = (global as any).nativeFabricUIManager != null;

export const FastShadowView: React.FC<FastShadowViewProps> = isFabricEnabled
  ? require('./FastShadowViewNativeComponent').default
  : requireNativeComponent('FastShadowView');
