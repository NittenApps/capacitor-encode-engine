export interface EncodeEnginePlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
