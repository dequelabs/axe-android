# Axe Android

Automated **WCAG 2.0 and WCAG 2.1 Accessibility** library for Android Applications. Not an Android Developer? Check out the rest of the axe family.

[![axe-web](https://img.shields.io/badge/axe-web-green.svg?logo=github&logoColor=ffffff&longCache=true&colorA=0077C8&colorB=000000&style=for-the-badge)](https://github.com/dequelabs/axe-core)

### Table of Contents

- [Rule Overview](#rule-overview)
  - [Axe Manifesto](#axe-manifesto)
  - [Two Week Pledge](#two-week-pledge)
- [Utilizing the Analysis Library](#utilizing-the-analysis-library)
  - [Implement Abstract Interfaces](#implement-abstract-interfaces)
  - [Track Event Stream](#track-event-stream)
  - [Run Axe Android](#run-axe-android)
- [Configuration Strategies](#configuration-strategies)
  - [Chipping Away](#chipping-away)
  - [Chopping it Off](#chopping-it-off)
  - [Targeting Standards](#targeting-standards)
  - [Avoiding False Positives](#avoiding-false-positives)

## Rule Overview

|  | Rule ID | Issue Type |
|--|---------|------------|
| 1 | Active View Name | WCAG 1.1.1 |
| 2 | Color Contrast | WCAG 1.4.3 |
| 3 | ImageView Name | WCAG 1.1.1 |
| 4 | Touch Size - WCAG | WCAG 2.1 |
| 5 | Touch Size - Custom | Best Practice |
| 6 | CheckBox Name | WCAG 1.3.1 |
| 7 | Don't Move Accessibility Focus | WCAG 3.2.2 WCAG 2.1 Change of Context |
| 8 | EditText Name | WCAG 1.3.1 |
| 9 | EditText Value | WCAG 4.1.2 |
| 10 | Switch Name | WCAG 2.1 |

### Axe Manifesto

Accessibility is hard. Sorting through endless reports, long explanations, and false positives make it worse. Our Rules will be:

- False positive free.
- Broad enough to be generally applicable.
- Discrete enough to be easy to identify and fix.
- Encourage Vendor, OS Version, and Assistive Technology agnostic solutions.
- Released to Beta any time we have something we are confident in release.
- Released to Production 
  - As we have substantive new features that are stable... like a New WCAG Rule.
  - Any time false positives are fixed and manual tests validated. 

## Utilizing the Analysis Library

Axe Android is simple to use, though requires a bit of knowledge of Android Accessibility to get started. Axe Android is a pure Java library. This helps encapsulate us from the volatile Android Accessibility Ecosystem and guarantees that anyone can run our library on ANY version of the operating system. There are no dependencies on any version of the Operating System nor any particular version of the Support Library. Because of this, you'll need to write a little code to get started. The process can be broken down succinctly like this:

1. Implement abstract interfaces.
2. Track the Event Stream.
3. Build AxeContext, AxeConf, and AxeImage data.
4. Run axe and consume results.

### Implement Abstract Interfaces 

Axe Android is a pure Java library. This allows us to keep our rules very concise and human readable
and encapsulates us completely from the Android Ecosystem. However, it requires more input from 
those who want to utilize the library. Namely that the provide implementations of the following 
abstract interfaces. 

#### AxeView.Builder

An `AxeView.Builder` builds an `AxeView`. You should be able to create a wrapper for this 
information quite easily by implementing this interface within a Class that contains either an 
`AccessibilityNodeInfo` instance or a `View` instance. 

Each function in the `AxeView.Builder` interface has an associated, identically named, `final` 
property in `AxeView`. The Java Doc for these properties in [AxeView.java](https://github.com/dequelabs/axe-android/blob/master/src/main/java/com/deque/axecore/AxeView.java) 
is the best place to go for documentation on what these functions are expected to return.

#### AxeEvent.Builder

The `AxeEvent.Builder` builds an `AxeEvent`. Is this sounding familiar yet? You should be able to 
create a wrapper for `AxeEvent` by implementing this interface within a class that contains an 
`AccessibilityEvent`. 

Again, the documentation for what the properties are is best found agains the JavaDoc for the final 
instance properties of their associated object... [AxeEvent.java](https://github.com/dequelabs/axe-android/blob/master/src/main/java/com/deque/axecore/AxeEvent.java).

#### AxeImage

We need access to image data to run Color Contrast analysis. An object that wraps a `Bitmap` 
instance would be the easiest way to supply this information. Look at 
[AxeImage.java](https://github.com/dequelabs/axe-android/blob/master/src/main/java/com/deque/axecore/colorcontrast/AxeImage.java).

### Track Event Stream

This sounds scary but it's really quite simple. Utilizing the `AxeEvent.Builder` interface you built 
above in conjunction with our `AxeEventStream` object, track AccessibilityEvents as they flow either 

- out of yor application
- into an `AccessibilityService`

For example:

```
AxeEventStream axeEventStream = new AxeEventStream();

@Override
public void onAccessibilityEvent(AccessibilityEvent event) {
  axeEventStream.addEvent(new AxeEvent(new YourEventBuilder(event)));
}
```

You then feed this information into the `AxeContext` when you go to run axe.

### Run Axe Android

With these interfaces implemented running axe is very simple.

```
Axe axe = new Axe(new AxeConf());

// Create an AxeView object from your AxeView.Builder implementation
AxeView axeView = new YourAxeViewBuilder(rootViewToBeAnalyzed);

// Create an AxeDevice object to help identify the device under test.
AxeDevice axeDevice = new AxeDevice(dpi, deviceName, osVersion, heightPixels, widthPixels);

// A screenshot to facilitate ColorContrast analysis and help identify issues.
AxeImage axeImage = new YourAxeImageClass(screenshotData);

// Run Axe and Get Results.
AxeResult axeResult = axe.run(new AxeContext(axeView, axeDevice, axeImage, axeEventStream));
```

### Avoiding False Positives

We do our best, but we are not perfect. The BEST response when you run into a False Positive is to 
[report it](https://github.com/dequelabs/axe-android/issues/new/choose). We are very quick to fix 
issues in the library. 

- Accepted False Positive tickets are the absolute highest priority. 
- If we cannot create a 100% false positive free rule, we will demote it to a Best Practice.

HOWEVER, sometimes you cannot wait for a false positive free library. In this case, it is best to 
remove the associated standard, and add back in any Rules that are working.

```
AxeConf axeConf = new AxeConf()
    .removeStandard(AxeStandard.WCAG_20)
    .addRule(ImageViewName.class);
```

Note: We will make this easier in future releases!
