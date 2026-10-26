---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* _{List the sources of reused or adapted ideas, code, documentation, and third-party libraries here, with links to the originals.}_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

The following provides a quick overview of the main components and their interactions.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* provides its functionality through a concrete `{Component Name}Manager` class that implements the corresponding API interface.

For example, the `Logic` component defines its API in `Logic.java` and implements it in `LogicManager.java`. Other components interact with a component through its interface rather than its concrete class, preventing them from coupling to that component's implementation, as illustrated in the following partial class diagram.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` and its parts, such as `CommandBox`, `ResultDisplay`, `PersonListPanel`, and `StatusBarFooter`. All of these, including `MainWindow`, inherit from the abstract `UiPart` class, which captures common behavior among classes that represent visible GUI parts.

The `UI` component uses the JavaFX UI framework. The layouts of these UI parts are defined in matching `.fxml` files in `src/main/resources/view`. For example, [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml) specifies the layout of [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java).

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component because it displays `Person` objects from the model.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

The following sequence diagram shows the same flow for the `filter` command. The command parser creates a
`StudentMatchesCriteriaPredicate` from the supplied school-level, subject, and tag criteria. The command then asks
the model to apply that predicate to its filtered student list.

<puml src="diagrams/FilterSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the filter Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>


How the `Logic` component works:

1. When `Logic` is called upon to execute a command, the command is passed to an `AddressBookParser` object, which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above for simplicity, the code can require several interactions between the command object and the `Model` to complete the operation.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name, e.g., `AddCommandParser`). The parser uses the other classes shown above to parse the user command and create an `XYZCommand` object (e.g., `AddCommand`). The `AddressBookParser` returns that object as a `Command` object.
* All `XYZCommandParser` classes, such as `AddCommandParser` and `DeleteCommandParser`, implement the `Parser` interface so they can be treated similarly where appropriate, for example during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the `Person` objects selected by the current filter, such as search results, in a separate _filtered_ list. It exposes this list as an unmodifiable `ObservableList<Person>` that the UI can observe and bind to, so the UI updates when the list changes.
* stores a `UserPrefs` object that represents the user’s preferences (currently, just the GUI settings). This is exposed to the outside as a `ReadOnlyUserPrefs` object.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)


<box type="info" seamless>

**Note:** The alternative, arguably more object-oriented, design below keeps a unique list of tags in `AddressBook`, and each `Person` references tags from that list. This lets `AddressBook` maintain one `Tag` object per unique tag instead of each `Person` holding its own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />
</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* is implemented by `StorageManager`, which delegates the actual JSON file access to `JsonAddressBookStorage` and `JsonUserPrefsStorage` (one class per data file).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Timetable-clash detection

Both `NextLessonCommand` and `RecurringLessonCommand` use `TimetableClashChecker` before replacing a student record. They check existing next lessons first, then recurring schedules, using all student records rather than only the displayed list. Each check uses the lesson's actual duration; adjacent lessons are allowed, and comparisons account for midnight and week boundaries.

The lesson being replaced is excluded. When comparing a next lesson with the same student's recurring schedule, an exact weekday and start-time match represents the same occurrence and is allowed, even if the topic or duration differs. Other overlapping times are rejected.

The activity diagram below follows parsing, validation, clash rejection, and model updates for both scheduling commands. A rejected command leaves the student records and displayed list unchanged. The diagram ends when the command returns successfully; `LogicManager` then saves the address book through the normal Storage flow. Storage errors are outside this diagram's scope.

<puml src="diagrams/TimetableClashActivityDiagram.puml" alt="Activity diagram for timetable-clash validation and model updates" />

### Recurring lesson feature

The `recurringlesson` command records a weekly lesson comprising a day, time, and topic for a student. Its implementation spans the Model, Logic, Storage, and UI components while keeping the recurring schedule separate from the student's one-off next lesson.

#### Model representation

`RecurringLesson` is a value object containing a `DayOfWeek`, a `LocalTime`, and a topic. Its constructor requires all three values and trims and rejects a blank topic. `Person` stores the value as an `Optional<RecurringLesson>`, allowing each student to have either zero or one recurring weekly lesson.

Student records are replaced rather than mutated. `Person.withNote(...)`, `withNextLesson(...)`, and `withRecurringLesson(...)` return a new `Person` with only the specified value changed, preserving every other field. The note and lesson setting/clearing commands use these methods; an empty `Optional` explicitly clears a lesson, while null arguments are rejected.

#### Command execution

`AddressBookParser` delegates the command arguments to `RecurringLessonCommandParser`. The command parser rejects missing or duplicate `day/`, `time/`, and `topic/` prefixes, parses the displayed student index, accepts a case-insensitive day of the week, and requires time to use the `HH:mm` format. It then creates the validated `RecurringLesson` and `RecurringLessonCommand` objects.

When executed, `RecurringLessonCommand` obtains the currently displayed student list and checks that the specified index exists. It passes the original and replacement `Person` objects to `Model#setPerson(...)`, then resets the displayed-list predicate to `PREDICATE_SHOW_ALL_PERSONS`. If parsing fails, a `ParseException` is thrown; if the displayed index is invalid, a `CommandException` is thrown. In either case, the model is not changed and the address book is not saved.

The following sequence diagram shows the successful execution of `recurringlesson 1 day/MONDAY time/16:30 topic/Algebra`.

<puml src="diagrams/RecurringLessonSequenceDiagram.puml" alt="Sequence diagram for setting a recurring lesson" />

#### Clearing a recurring lesson

`AddressBookParser` delegates `clearrecurringlesson` to `ClearRecurringLessonCommandParser`, which parses the displayed student index and creates a `ClearRecurringLessonCommand`. The command validates the index against the currently displayed student list and checks whether the selected student has a recurring lesson.

If a recurring lesson exists, the command creates a replacement `Person` with `Optional.empty()` in the recurring-lesson field, preserves the student's other details, and passes both records to `Model#setPerson(...)`. If no recurring lesson exists, the student record is left unchanged and the command reports that no recurring lesson is scheduled. Both outcomes reset the displayed-list predicate to `PREDICATE_SHOW_ALL_PERSONS`. An invalid displayed index causes a `CommandException` before the filter is reset or the address book is saved.

The following sequence diagram shows the execution of `clearrecurringlesson 1`, including both possible outcomes for a valid displayed index.

<puml src="diagrams/ClearRecurringLessonSequenceDiagram.puml" alt="Sequence diagram for clearing a recurring lesson" />

Both diagrams treat the Model and Storage components as black boxes. They omit lower-level calls involving argument tokenization, JSON conversion, and exception propagation to keep the command flows readable.

#### Storage and UI updates

After either command completes successfully, `LogicManager` passes the address book to the `Storage` component. This normal save also occurs when `clearrecurringlesson` finds no recurring lesson and leaves the model unchanged. `JsonAdaptedPerson` represents the optional recurring lesson using `JsonAdaptedRecurringLesson`, which stores its day, time, and topic as JSON-compatible strings. When the data is loaded again, the adapter validates those strings before reconstructing the `RecurringLesson` model object.

`PersonListPanel` displays the `ObservableList<Person>` exposed by Logic and backed by the model's filtered list. Replacing the `Person` in the model updates this list, after which `PersonCard` obtains and formats the student's recurring lesson for display.

#### Design considerations

**Aspect: How a recurring schedule is represented:**

* **Alternative 1 (current choice): Store one weekly recurrence rule in each student record.**
  * Pros: Uses a compact value object, avoids generating an unbounded number of future lesson records, and keeps the schedule with the corresponding student.
  * Cons: Supports only one weekly recurring lesson per student and cannot represent exceptions such as a cancellation on a specific date.

* **Alternative 2: Generate and store individual future lesson occurrences.**
  * Pros: Allows a particular occurrence to be cancelled or rescheduled independently.
  * Cons: Requires an end date or generation limit, stores more data, and can become inconsistent with the original recurrence rule.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` -- Saves the current address book state in its history.
* `VersionedAddressBook#undo()` -- Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` -- Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.
</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.
</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X), but due to a limitation of PlantUML, it continues to the end of the diagram.
</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.
</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo execute:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command is correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**: Private tutors who manage multiple students across different subjects, school levels, and lesson schedules, and who are comfortable using a keyboard-driven desktop application.

**Value proposition**: TutorTrack lets private tutors retrieve and update student information quickly by keeping contact details, academic context, and upcoming lesson information together in one local application.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …        | I want to …                                                            | So that I can…                                                |
|----------|---------------|------------------------------------------------------------------------|---------------------------------------------------------------|
| `* * *`  | new user      | view concise usage instructions                                        | start using TutorTrack without external assistance            |
| `* * *`  | private tutor | add a student with contact details and academic information            | keep the information needed for tutoring in one place         |
| `* * *`  | private tutor | list all students                                                      | see the students currently under my care                      |
| `* * *`  | private tutor | find students by name                                                  | retrieve a student's record quickly                           |
| `* * *`  | private tutor | view a student's complete record                                       | prepare for a lesson efficiently                              |
| `* * *`  | private tutor | edit a student's details                                               | keep the student record accurate                              |
| `* * *`  | private tutor | record the date, time, and topic of a student's next lesson            | know when the lesson is scheduled and what to prepare         |
| `* * *`  | private tutor | delete a student                                                       | remove an obsolete, incorrect, or duplicate record            |
| `* *`    | private tutor | add free-form notes to a student's record                              | remember student-specific information that has no fixed field |
| `* *`    | private tutor | tag students using meaningful categories                               | group students by attributes such as subject or school level  |
| `* *`    | private tutor | view my earliest upcoming lesson across all students                   | know which lesson to prepare for first                        |
| `* *`    | private tutor | set a recurring lesson schedule for a student                          | avoid entering the same lesson details repeatedly             |
| `*`      | private tutor | sort students by their next lesson                                     | prioritise lesson preparation                                 |
| `*`      | private tutor | identify timetable clashes                                             | avoid arranging overlapping lessons                           |
| `*`      | private tutor | view a student's lesson history and learning-progress summary          | understand the student's progress over time                   |

### Use cases

(For all use cases below, the **System** is TutorTrack and the **Actor** is a private tutor.)

#### Use case: UC01 — Add a student

**MSS**

1. Tutor enters the details of a new student.
2. TutorTrack validates the supplied details.
3. TutorTrack adds the student record.
4. TutorTrack displays a confirmation containing the added student's details.

   Use case ends.

**Extensions**

* 2a. One or more required fields are missing or invalid.
  * 2a1. TutorTrack displays an error describing the expected input.
  * 2a2. Use case resumes at step 1.
* 2b. Another student has the same name.
  * 2b1. TutorTrack informs the tutor that the student already exists.
  * 2b2. Use case resumes at step 1.

#### Use case: UC02 — Find and update a student

**MSS**

1. Tutor searches for a student by name.
2. TutorTrack displays all matching students.
3. Tutor selects a student from the displayed list and supplies the updated details.
4. TutorTrack validates the supplied details.
5. TutorTrack updates the student record.
6. TutorTrack displays a confirmation containing the updated student's details.

   Use case ends.

**Extensions**

* 2a. No student matches the search terms.
  * 2a1. TutorTrack informs the tutor that no students were found.
  * 2a2. Use case ends.
* 3a. The selected index does not refer to a student in the displayed list.
  * 3a1. TutorTrack displays an invalid-index error.
  * 3a2. Use case resumes at step 3.
* 4a. One or more updated fields are invalid.
  * 4a1. TutorTrack displays an error describing the expected input.
  * 4a2. Use case resumes at step 3.

#### Use case: UC03 — Record a student's next lesson

**MSS**

1. Tutor searches for the student by name.
2. TutorTrack displays all matching students.
3. Tutor selects the student and enters the next lesson's date, time, and topic.
4. TutorTrack validates the lesson information.
5. TutorTrack records the next lesson in the student's record.
6. TutorTrack displays the updated student record.

   Use case ends.

**Extensions**

* 2a. No student matches the search terms.
  * 2a1. TutorTrack informs the tutor that no students were found.
  * 2a2. Use case ends.
* 3a. The selected index does not refer to a student in the displayed list.
  * 3a1. TutorTrack displays an invalid-index error.
  * 3a2. Use case resumes at step 3.
* 4a. The lesson date, time, or topic is invalid.
  * 4a1. TutorTrack displays an error describing the expected input.
  * 4a2. Use case resumes at step 3.

#### Use case: UC04 — Delete a student

**MSS**

1. Tutor requests to list all students.
2. TutorTrack displays the student list.
3. Tutor selects a student from the displayed list for deletion.
4. TutorTrack deletes the student record.
5. TutorTrack displays a confirmation containing the deleted student's details.

   Use case ends.

**Extensions**

* 2a. The student list is empty.
  * 2a1. TutorTrack informs the tutor that there are no students to delete.
  * 2a2. Use case ends.
* 3a. The selected index does not refer to a student in the displayed list.
  * 3a1. TutorTrack displays an invalid-index error.
  * 3a2. Use case resumes at step 3.

### Non-Functional Requirements

1. TutorTrack should work on any _mainstream OS_ with Java `25` or later installed.
2. TutorTrack should respond to a command within 1 second when managing up to 1,000 student records under normal operating conditions.
3. TutorTrack should persist data from every successful data-modifying command and recover that data on the next launch after a normal shutdown.
4. TutorTrack should store all student data locally and should not require an internet connection for normal operation.
5. A tutor with above-average typing speed for regular English text should be able to complete common operations faster using commands than by navigating an equivalent mouse-driven interface.
6. A tutor who is familiar with command-line applications should be able to learn the syntax for the core commands within 30 minutes using only the in-app help and User Guide.
7. TutorTrack should remain usable on screens with a resolution of 1280 × 720 or higher without information required for common operations being permanently inaccessible.

### Glossary

* **Academic information**: Tutoring-related information about a student, such as the student's subject and school level.
* **Displayed student list**: The numbered list of students currently shown in TutorTrack. It may contain every student or only the results of the latest search.
* **Mainstream OS**: A currently supported version of Windows, Linux, or macOS that can run Java `25` or later.
* **Next lesson**: The nearest future lesson recorded for a student, including its date, time, and topic.
* **Private tutor**: A person who independently provides lessons to one or more students and manages the corresponding student information and schedules.
* **Student index**: The positive integer displayed beside a student in the current displayed student list. It is not a permanent identifier and may change when the displayed list changes.
* **Student record**: The collection of contact details, academic information, notes, tags, and lesson information that TutorTrack stores for one student.
* **Tag**: A user-defined label attached to a student record for categorisation.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.
</box>

### Launch and shutdown

1. Initial launch

   1. Download the JAR file and copy it into an empty folder.

   1. Double-click the JAR file.<br>
      Expected: The GUI opens with a set of sample contacts. The window size may not be optimal.

1. Saving window preferences

   1. Resize the window to an optimal size. Move the window to a different location. Close the window.

   1. Relaunch the app by double-clicking the JAR file.<br>
       Expected: The most recent window size and location are retained.

1. _{ more test cases … }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command, with multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: The first contact is deleted from the list. The status message shows the deleted contact's details.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. The status message shows error details.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases … }_

### Filtering students

Use a separate test copy of TutorTrack in an empty folder so these checks do not affect real student records.
Run the following commands once to add four test students. The `filtercheck` tag distinguishes them from the
sample students loaded on first launch.

```text
add n/Filter Alice p/90000001 e/filteralice@example.com a/Block 1 l/Secondary 4 s/Mathematics t/filtercheck t/weekday t/exam
add n/Filter Ben p/90000002 e/filterben@example.com a/Block 2 l/Secondary 4 s/Mathematics t/filtercheck t/weekday
add n/Filter Cara p/90000003 e/filtercara@example.com a/Block 3 l/Secondary 4 s/Physics t/filtercheck t/weekday t/exam
add n/Filter Dylan p/90000004 e/filterdylan@example.com a/Block 4 l/Secondary 3 s/Mathematics t/filtercheck t/weekday t/exam
```

Run `filter t/filtercheck` before each case below. It should display all four test students.

| Test case | Expected result |
| --- | --- |
| `filter l/Secondary 4 s/Mathematics t/filtercheck t/weekday t/exam` | Only Filter Alice is displayed. All criteria must match; Ben lacks `exam`, Cara has a different subject, and Dylan has a different school level. |
| `filter t/filtercheck t/weekday t/exam` | Alice, Cara, and Dylan are displayed. Ben is excluded even though he has one of the two requested lesson tags. |
| `filter t/filtercheck s/Biology` | No students are displayed, and the result reports zero matches. No records are deleted. |
| `filter` or `filter students s/Mathematics` | The command usage error is displayed, and the four test students remain displayed. |
| `filter l/` | `School level must not be blank.` is displayed, and the displayed list is unchanged. |
| `filter s/` | `Subject must not be blank.` is displayed, and the displayed list is unchanged. |
| `filter t/`, `filter t/week day`, or `filter t/weekday t/exam!` | `Tag names should be alphanumeric` is displayed, and the displayed list is unchanged. |

To test resetting the displayed list:

1. Run `list` and note the total number of students, including the sample students and the four test students.
1. Run `filter l/Secondary 4 s/Mathematics t/filtercheck t/exam`. Only Filter Alice should be displayed.
1. Run `list` again. All students should reappear with the original total and unchanged details.
1. Run `filter t/filtercheck s/Biology`, followed by `list`. All students should also reappear after an empty result.

### Saving data

1. Dealing with missing/corrupted data files

   1. _{Explain how to simulate missing or corrupted data files and state the expected behavior.}_

1. _{ more test cases … }_
