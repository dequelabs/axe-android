# Axe REST API

Documentation of the REST API that comes along with the Axe service.

## AxeResult

This endpoint serves as a wrapper to collect all of the information from an axe run. 

### Motivation

- Provide access to results and context easily.
- Avoid timing issues that come with multiple requests.

### Simplified Sample
```
{
  "axeContext" : { @AxeContext },
  "axeResult" : { AXRL },
  "axeConf" : { TBD by Framework }  
}
```

## AxeContext

Wraps data together that is needed to identify a particular View, with a particular screenshot,
for a particular device configuration.

### Motivation

- Collect View information in one place.
- Allow for direct correlation of screenshot and ViewHierarchy
- Easy developer debugging.
- Reproducability of results.

### Simplified Sample

```
{
  "axeView" : @AxeView,
  "axeDevice" : @AxeDevice,
  "axeEvents" : @AxeEvents,
  "screenshot" : "base64png data"
}
```

## AxeView

Serves up properties about the Accessibility Hierarchy of a particular view. 

### Motivation

- All motivations of AxeContext apply.
- Rule debugging.
- Simplified test case collection.
- Simplified false positive identification.

### Simplified Sample

```
{
  "Arbitrary Property Name": "value1",
  "Arbitrary Property Name2": "value2",
  "children" : [
    {
      "Arbitrary Property Name": "value3",
      "Arbitrary Property Name2": "value24"
      "children": []
    }, 
    {
      "Arbitrary Property Name": "value3",
      "Arbitrary Property Name2": "value24",
      "children" : []
    }
  ]
}
```

### Requirements

- The Arbitrary property names of the children are identical to the root. 
- If a property is omitted from a child it is assumed to be present and NULL.
- Arbitrary property names are NOT calculated, but are values present in the respective OS APIs.

## AxeDevice

Collect prescriptive information that helps identify what type of context various information 
was collected from.

### Motivation

- Identify device version and operating system.
- Provide metrics for screen size so view hierarchy can be correlated.

### Simplified Sample

```
{
  "os": {
    "name" : "Android",
    "version" : "8.1.0"
  },
  "screen" : {
    "height" : 1920,
    "width" : 1080,
    "dpi": 2.625
  }
}

```

### Requirements

- Property names match the above exactly.

## AxeEvents

Along with a view hierarchy modern Operating Systems communicate with Assistive Technologies through
events. Should contain events since the last Screen Changed event.

### Motivation

- Similar motivations to AxeView, but applied to the event stream.
- Collect dynamic information about the view and how its evolving.

### Simplified Sample

```
[
  {
    "Arbitrary Property Name": "value1",
    "Arbitrary Property Name2": "value2"
  },
  {
    "Arbitrary Property Name": "value1",
    "Arbitrary Property Name2": "value2"
  }
]
```

### Requirements

- Array objects have the same arbitrary properties, or missing values are considered NULL.
- Arbitrary property names are NOT calculated, but are values present in the respective OS APIs.
