# Edit Text Name

`EditText` elements must expose their **entered text** [**Value**](name-role-value.md#Value) to **TalkBack**.

## The Algorithm in Simple Terms

Looks for `EditText` elements and **requires** they do not override their entered `Text`
[**Value**](name-role-value.md#Value) with a `ContentDescription`.

### How to Fix

```
EditText editText = .......; // Role: EditText
TextView label = .......; // Role: Label
label.setLabelFor(editText.getId()); // Associate the Checkbox with its Name
```

## User Impact

Violations of this rule mostly impact **TalkBack** users by hiding important context and information the blind. 
An image is worth 1000 words at least a short summary should be available in text.

### Confirming It is a Violation

1. Turn on **TalkBack**.
2. Attempt to `AccessibilityFocus` the Control.
3. When it is focused either:
    1. It does not read out the entered text.
    2. It reads out the `ContentDescription` instead.

Both of the itmes in step 3 may happen, but even in isolation, both of these events are confusing to the
user and can result in **entered text** not being shared, duplicate announcements, or a loss of meaning.

**Note**: Violations may manifest themselves in different ways, depending on the Version of Android, 
Device Manufacturer, and Version of Assistive Technology.



## Resources

[Rule Implementation](https://github.com/dequelabs/axe-android/blob/5cbbddd48be53af11c82406d670dd199a5548f3b/src/main/java/com/deque/axe/android/rules/hierarchy/EditTextName.java)
