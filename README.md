# @nittenapps/encode-engine

Encode and decode utilities

## Install

```bash
npm install @nittenapps/encode-engine
npx cap sync
```

## API

<docgen-index>

* [`checkDigits(...)`](#checkdigits)
* [`decode(...)`](#decode)
* [`encode(...)`](#encode)
* [`sign(...)`](#sign)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### checkDigits(...)

```typescript
checkDigits(options: { input: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ input: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### decode(...)

```typescript
decode(options: { key: string; value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                                         |
| ------------- | -------------------------------------------- |
| **`options`** | <code>{ key: string; value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### encode(...)

```typescript
encode(options: { key: string; value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                                         |
| ------------- | -------------------------------------------- |
| **`options`** | <code>{ key: string; value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### sign(...)

```typescript
sign(options: { value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------

</docgen-api>
