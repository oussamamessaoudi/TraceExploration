### Search format

```spl
source="http:Testing Exploration Trace" | search "properties.traceId"=656d19ee0ded3a3adc35ba207412d391 
| stats list(severity), list(logger), list(message.typeLog) by "properties.spanId"
```