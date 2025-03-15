import { registerPlugin } from '@capacitor/core';

import type { EncodeEnginePlugin } from './definitions';

const EncodeEngine = registerPlugin<EncodeEnginePlugin>('EncodeEngine', {
  web: () => import('./web').then((m) => new m.EncodeEngineWeb()),
});

export * from './definitions';
export { EncodeEngine };
