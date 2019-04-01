# ActiveViewName

`EditText` elements must have a **Name** that can be spoken by **Screen Readers**.

## The Algorithm in Simple Terms

Looks for `EditText` elements and **requires** they have a **Name**. 

**Note**: Unlike many `Views` in the Android Ecosystem `Text` and `ContentDescription` do not work 
to provide a **Name** for `EditText` controls.

### How to Fix?

```
EditText editText = .......; // Role: EditText
TextView label = .......; // Role: Label
label.setLabelFor(editText.getId()); // Associate the Checkbox with its Name
```

## User Impact

Violations of this rule mostly impact **TalkBack** users. 

- Only the [**Role**](name-role-value.md#State) and [**Value**](name-role-value.md#Value) is
spoken along with the `EditText`.

### Confirming It is a Violation

1. Turn on **TalkBack**.
2. Attempt to `AccessibilityFocus` the `EditText` control.
3. When it should announce like:
    1. Quote: Entered text, EDIT TEXT for A visible label.
    2. This rule is specifically looking for "for A visible label."

Both of the itmes in step 3 may happen, but even in isolation, both of these events are confusing to the
user and can result in **entered text** not being shared, duplicate announcements, or a loss of meaning.

**Note**: Violations may manifest themselves in different ways, depending on the Version of Android, 
Device Manufacturer, and Version of Assistive Technology.

## Resources

[Rule Implementation](https://github.com/dequelabs/axe-android/blob/5cbbddd48be53af11c82406d670dd199a5548f3b/src/main/java/com/deque/axe/android/rules/hierarchy/EditTextName.java)
