# ActiveViewName

`EditText` elements must reliably share their [**Value**](name-role-value.md#Value).

## The Algorithm in Simple Terms

Looks for `EditText` elements and **requires** they do not override their entered `Text`
[**Value**](name-role-value.md#Value) with a `ContentDescription`.

### How to Fix?

```
EditText editText = .......; // Role: EditText
TextView label = .......; // Role: Label
label.setLabelFor(editText.getId()); // Associate the Checkbox with its Name
```

## User Impact

Violations of this rule mostly impact **TalkBack** users. 

- When an `EditText` is `AccessibilityFocused` it does not read out the entered text.
- When an `EditText` is `AccessibilityFocused` is reads out the `ContentDescription`.

Both of these things may happen, but even in isolation, both of these events is confusing to the
user and can result in entered text not being shared, duplicate announcements, or a loss of meaning.

**Note**: Violations may manifest themselves in different ways, depending on the Version of Android, 
Device Manufacturer, and Version of Assistive Technology.



## Resources

[Rule Implementation](https://github.com/dequelabs/axe-android/blob/5cbbddd48be53af11c82406d670dd199a5548f3b/src/main/java/com/deque/axe/android/rules/hierarchy/EditTextName.java)
