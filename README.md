# rn-sms

SMS library for RN.

Linking module on RN is flaky if a string with symbols and whitespace is used, not even `decodeURIComponent` or `decodeURI`
can help, it will show correctly on some devices but others will incorrectly show the decoded URI instead of the correct message. If you only need to support new devices, consider using Linking instead of this package, if you need to support every device possible, you can install it.

## Installation

```sh
npm install @imcsorin/rn-sms
```

## Usage

```js
import { sendSMS } from '@imcsorin/rn-sms';

// ...

const result = await sendSMS("123-1234-1234", "A very nice test message!");
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---
