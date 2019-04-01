# ImageView Name

`ImageView` elements must have a [**Name**](name-role-value.md#Name).

## The Algorithm in Simple Terms

Looks for `ImageView` elements and **requires** that they 

- Provide their [**Name**](name-role-value.md#Name) with a `ContentDescription` **OR**
- Have set `importantForAccessibility = NO`. 

### How to Fix?

```
ImageView image = .......; // Role: Image
image.setContentDescription("Tasty Chocolates"); // Name: Tasty Chocolates.
```

## User Impact

Violations of this rule mostly impact **TalkBack** users. 

- An `ImageView` that provides context cannot be `AccessibilityFocused`.
- When An `ImageView` is `AccessibilityFocused` **TalkBack** does not announce anything.

Informative Images provide important context. An image is worth 1,000 words, do not have 1,000 words
in your `ContentDescription', but a few can help provide blind users the missing context.

### Confirming It is a Violation

1. Turn on **TalkBack**.
2. Attempt to `AccessibilityFocus` an Image.
3. Either the Image:
    1. Should not be focused because it is not informative.
    2. It should be spoken out by the **Screen Reader**.

Both of the itmes in step 3 may happen, but even in isolation, both of these events are confusing to the
user and can result in **entered text** not being shared, duplicate announcements, or a loss of meaning.

Another important consideration is **Images of Text** in which case the `ContentDescription` should
simply match the text in the image verbatim.

**Note**: Violations may manifest themselves in different ways, depending on the Version of Android, 
Device Manufacturer, and Version of Assistive Technology.



## Resources

[Rule Implementation](https://github.com/dequelabs/axe-android/blob/5cbbddd48be53af11c82406d670dd199a5548f3b/src/main/java/com/deque/axe/android/rules/hierarchy/EditTextName.java)
