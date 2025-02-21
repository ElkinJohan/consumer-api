# Mock response with status 200 and 500

Example status 200 in logger:

```json
{
  "attribute01": "one",
  "attribute02": {
    "sub-attribut02": "sub-two"
  },
  "attribute03": "three"
}
```

Example status 500 in logger:

```json
{
  "message": "mock error example"
}
```

Example uncontrolled status: view in debug mode or logger.

# Diagram
![Diagrama](diagrams\logger-signature.png)