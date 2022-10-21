import type React from 'react';

export function createShadowedComponent<T extends React.ComponentType<any>>(
  Component: T
): T {
  return Component;
}
