import { View } from 'react-native';
import { createShadowedComponent } from './createShadowedComponent';

export { createShadowedComponent };
export const ShadowedView = createShadowedComponent(View);
