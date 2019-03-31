# Name, Role, Value and State

*Name*, *Role*, and *Value* is an important concept in accessibility. These terms will be referenced
in various capacities, particularly in rule descriptions. It is the separation and careful handling 
of this information that allows for the maintainable support of the wide range of potential
*Assistive Technologies* in the Android ecosystem.

### Name, Role, Value and State in Android

Let's start by putting this all together in Code and if you 

```
import android.widget.CheckBox;
import android.widget.TextView;

CheckBox puppiesCheckBox = (CheckBox) findViewById(R.id.puppy_lovers_checkbox);
puppiesCheckBox.setChecked(true); 

TextView puppiesLabel = (TextView) findViewById(R.id.puppy_label);
puppiesLabel.setLabelFor(iLikePuppies.getId());
puppiesLabel.setText("Do you like puppies?");

EditText puppiesName = (EditText) findViewById(R.id.puppy_name);
puppiesName.setHint("Dodger");
```

- puppiesCheckBox
    - [Name](#name): Do you like puppies?
    - [Role](#role): CheckBox
    - [State](#state): true
    - TalkBack Announcement: "checkbox, checked for Do you like puppies?"
- puppiesLabel
    - [Name](#name): Do you like puppies?
    - TalkBack Announcement: "Do you like puppies?"
- puppiesName
    - [Value](#value): Dodger
    - [Name](#name): This view inaccessible, it DOES NOT have a name.
    - TalkBack Announcement: "Dodger edit box"


## Name

The *Name* of a thing represents its description. It replaces the information sited users get by
glancing at an element in isolation... without the context of other elements in the application. 

### For Example

```
button.setText("Go");
```

The `Button` above has the *Name* of "Go". It may have other important properties and context given 
where it is used and other surrounding elements, but its *Name* is only "Go" and that cannot be 
changed without changing its `Text`.

## Role

The *Role* of a thing represents what it does and how it should behave. Assistive Technologies
respond to the *Role* of a thing by changing what they do.

### For Example

```
import android.widget.Button;
Button goButton = (Button) findViewById(R.id.go_button);
goButton.setText("Go");
``` 

In the example above the *Role* of the `goButton` is represented by the fact that it is an 
`android.widget.Button`. By allowing the *Role* of the thing to be expressed in its `ClassName` we 
allow various Assistive Technologies to respond to it in their own way. 

1. *TalkBack* 
    1. Buttons can be double tapped to be interacted with.
    2. Buttons are followed by the announcement "Double tap to Activate"
2. *Switch Control*
    1. Buttons are scanned over using the "Next Element" action.
    2. Buttons can be interacted with and will respond to the "Select" action.

There are other ways that *TalkBack* an *SwitchControl* can identify `Button` elements, but the 
`ClassName` is the most reliably way to communicate *Role* based information, especially when 
considering more complicated *Role*s such as EditText, CheckBoxes, and many others.

## Value

The *Value* of a control represents `Text` or other value that can change.

### For Example

```
import android.widget.EditText;
EditText nameField = (EditText) findViewById(R.id.username);
nameField.setHint("John Snow");
``` 

The *Value* of the above `EditText` is currently "John Snow". However, after text is entered into
the `EditText` field, the *Value* will change to the entered text.

## State

The *State* of a `View` can be thought of as a special type of *Value* that is pre-defined given
the type of `View`.

### For Example

```
import android.widget.CheckBox;
CheckBox iLikePuppies = (CheckBox) findViewById(R.id.puppy_lovers_checkbox);
iLikePuppies.setChecked(true); 
```

The *State* of a `CheckBox` is whether it is checked or not. In this case the state of the checkbox
is `true`. TalkBack will then communicate this `State` to a control by announcing *checked* after
the control is announced... "Checkbox checked".

Note: like *Value* the *State* of a thing changes depending on user input. In fact *State* is 
nothing more than a special type of *Value*, that comes with pre-defined *Values*. 

## All Together

By utilizing Name, Role, and Value together we can create expressive controls that are 
Accessible in a multitude of Assistive Technologies.




