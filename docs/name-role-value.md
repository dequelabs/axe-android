# Name, Role, State and Value in Android

**Name**, **Role**, and **Value** is an important concept in accessibility. It is the separation and careful handling 
of this information that allows for the maintainable support of the wide range of potential **Assistive Technologies** 
in the Android ecosystem.

Let's start by summarizing these concepts in some Android Code.

```java
CheckBox aCheckBox = .....; //Role: CheckBox
aCheckBox.setChecked(true); //State: true

TextView aTextView = .......; //Role: TextView
aTextView.setLabelFor(iLikePuppies.getId()); //This control is acting as a name for another control.
aTextView.setText("Do you like puppies?"); // Name: Do you like puppies?

EditText aEditText = .......; //Role: EditText
aEditText.setHint("Dodger"); //Value: Dodger
```

### TalkBack Announcements for these Controls

#### aCheckBox

> checkbox, checked for Do you like puppies?

- [**Name**](#name): Do you like puppies?
- [**Role**](#role): CheckBox
- [**State**](#state): true

#### aTextView

> Do you like puppies?

- [**Name**](#name): Do you like puppies?
- [**Role**](#role): TextView

#### aEditText

> Dodger, edit box
- [**Value**](#value): Dodger
- [**Name**](#name): This view inaccessible, it DOES NOT have a name.
- [**Role**](#role): EditText

## Name

The *Name* of a thing represents its description. It replaces the information sited users get by
glancing at an element in isolation... without the context of other elements in the application. 

### For Example

```java
button.setText("Go"); //Name: Go
```

The `Button` above has the *Name* of "Go". It may have other important properties and context given 
where it is used and other surrounding elements, but its *Name* is only "Go" and that cannot be 
changed without changing its `Text`.

## Role

The *Role* of a thing represents what it does and how it should behave. Assistive Technologies
respond to the *Role* of a thing by changing what they do.

### For Example

```java
Button goButton = ....; //Role: Button
goButton.setText("Go"); //Name: Go
``` 

In the example above the *Role* of the `goButton` is represented by the fact that it is an 
`android.widget.Button`. By allowing the *Role* of the thing to be expressed in its `ClassName` we 
allow various Assistive Technologies to respond to it in their own way. 

1. **TalkBack**
    1. Buttons can be double tapped to be interacted with.
    2. Buttons are followed by the announcement "Double tap to Activate"
2. **Switch Control**
    1. Buttons are scanned over using the "Next Element" action.
    2. Buttons can be interacted with and will respond to the "Select" action.

There are other ways that *TalkBack* an *SwitchControl* can identify `Button` elements, but the 
`ClassName` is the most reliably way to communicate *Role* based information, especially when 
considering more complicated *Role*s such as EditText, CheckBoxes, and many others.

## Value

The *Value* of a control represents `Text` or other value that can change.

### For Example

```java
EditText nameField = ......; //Role: EditText 
nameField.setHint("John Snow"); //Value: John Snow
``` 

The *Value* of the above `EditText` is currently "John Snow". However, after text is entered into
the `EditText` field, the *Value* will change to the entered text.

## State

The *State* of a `View` can be thought of as a special type of *Value* that is pre-defined given
the type of `View`.

### For Example

```java
CheckBox iLikePuppies = .....; //Role: CheckBox
iLikePuppies.setChecked(true); //State: true
```

The *State* of a `CheckBox` is whether it is checked or not. In this case the state of the checkbox
is `true`. TalkBack will then communicate this `State` to a control by announcing *checked* after
the control is announced... "Checkbox checked".

Note: like *Value* the *State* of a thing changes depending on user input. In fact *State* is 
nothing more than a special type of *Value*, that comes with pre-defined *Values*. 
