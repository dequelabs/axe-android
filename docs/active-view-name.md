# ActiveViewName

Active elements must have a Name that can be spoken by Screen Readers.

## The Algorithm in Simple Terms

Looks for elements that 

- Would be *Accessibility Focusable* if they had text
- Have a basic *Click Action*

and requires that have text that can be spoken by a *Screen Reader*. 

## User Impact

Violations of this rule mostly impact *TalkBack* users. Violations would manifest themselves
in the following ways, depending on the Version of Android, Device Manufacturer, and Version of
Assistive Technology.

- Inability to focus the View.
- No [Name](active-view-name.md#Name) spoken along with the View.

## How to Fix?

The fix depends on the type of control. There are two basic fixes for this problem.

### Views that Support On Screen Text

```
android.widget.Button someButton = .......

someButton.setText("Button's Name");
```

### Views that Don't have On Screen Text
```
android.widget.ImageButton someButton = .......

someButton.setContentDescription("Button's Name");
```

## Resources

[Rule Implementation](https://github.com/dequelabs/axe-android/blob/5cbbddd48be53af11c82406d670dd199a5548f3b/src/main/java/com/deque/axe/android/rules/hierarchy/ActiveViewName.java#L1-L44)
