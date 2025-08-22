export interface EncodeEnginePlugin {
  checkDigits(options: { input: string }): Promise<{ value: string }>;
  decode(options: { key: string; value: string }): Promise<{ value: string }>;
  encode(options: { key: string; value: string }): Promise<{ value: string }>;
  sign(options: { value: string }): Promise<{ value: string }>;
}
