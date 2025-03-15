import { WebPlugin } from '@capacitor/core';

import type { EncodeEnginePlugin } from './definitions';

export class EncodeEngineWeb extends WebPlugin implements EncodeEnginePlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
