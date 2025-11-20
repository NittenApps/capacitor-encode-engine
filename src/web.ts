import { WebPlugin } from '@capacitor/core';

import type { EncodeEnginePlugin } from './definitions';

export class EncodeEngineWeb extends WebPlugin implements EncodeEnginePlugin {
  checkDigits(_options: { input: string }): Promise<{ value: string }> {
    throw new Error('Method not implemented.');
  }

  decode(_options: { key: string; value: string }): Promise<{ value: string }> {
    throw new Error('Method not implemented.');
  }

  encode(_options: { key: string; value: string }): Promise<{ value: string }> {
    throw new Error('Method not implemented.');
  }

  sign(_options: { value: string }): Promise<{ value: string }> {
    throw new Error('Method not implemented.');
  }
}
