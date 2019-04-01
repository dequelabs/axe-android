# ActiveViewName

`CheckBox` elements must have a **Name** that can be spoken by **Screen Readers**.

## The Algorithm in Simple Terms

Looks for `CheckBox` elements and **requires** they have an associated `TextView`. 

### How to Fix?

```
CheckBox checkBox = .......; // Role: CheckBox
TextView label = .......; // Role: Label
label.setLabelFor(checkBox.getId()); // Associate the Checkbox with its Name
```

Unlike many `Views` in the Android Ecosystem `Text` and `ContentDescription` do not work 
to provide a [**Name**](name-role-value.md#Name) for `CheckBox` controls. The best way to provide a 
[**Name**](name-role-value.md#Name) is to associate the `CheckBox` with its visible `TextView`.

## User Impact

Violations of this rule mostly impact **TalkBack** users. 

- Only the [**State**](name-role-value.md#State) and [**Role**](name-role-value.md#Role) is
spoken along with the `CheckBox`.

### Confirming It is a Violation

1. Turn on **TalkBack**.
2. Attempt to `AccessibilityFocus` the `CheckBox`.
3. When it is focused it should do both of the following:
    1. Announce its [**Name**](name-role-value.md#Name).
    2. Announce its state [**State**](name-role-value.md#State).

**Note**: Violations may manifest themselves in different ways, depending on the Version of Android, 
Device Manufacturer, and Version of Assistive Technology. 

## Resources

[Rule Implementation](https://github.com/dequelabs/axe-android/blob/5cbbddd48be53af11c82406d670dd199a5548f3b/src/main/java/com/deque/axe/android/rules/hierarchy/CheckBoxName.java)
