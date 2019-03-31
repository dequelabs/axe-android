# ActiveViewName

Active `Views` must have a **Name** that can be spoken by **Screen Readers**.

## The Algorithm in Simple Terms

Identifies `Views` that 

- Would be **Accessibility Focusable** if they had speakable text
- Have a basic **Click Action**

and **requires** they have text that can be spoken by a **Screen Reader**. 

## User Impact

Violations of this rule mostly impact **TalkBack** users. Violations would manifest themselves
in different ways, depending on the Version of Android, Device Manufacturer, and Version of
Assistive Technology.

- Inability to focus the `View` in TalkBack.
- No [**Name**](name-role-value.md#Name) spoken along with the `View`.

## How to Fix?

The fix depends on the type of control. Both `Text` and `ContentDescription` can be used to 
suppply a [**Name**](name-role-value.md#Name) for a control. However, it is important to pick 
the correct one for the correct control.

```
Button button = .......
button.setText("Button's Name");

ImageButton imageButton = .......
imageButton.setContentDescription("Button's Name");
```



## Resources

[Rule Implementation](https://github.com/dequelabs/axe-android/blob/5cbbddd48be53af11c82406d670dd199a5548f3b/src/main/java/com/deque/axe/android/rules/hierarchy/ActiveViewName.java)
