# Engineering Decisions & Tradeoffs

### Architecture Choices
* **MVVM with Clean Architecture:** Separated into `data`, `domain`, and `presentation` layers to ensure the app can easily scale beyond this initial exercise.
* **Offline-First via Room:** The app parses the `books.json` asset once, dumps it into a local Room database, and all ViewModels observe `StateFlows` directly from the database DAOs. This guarantees instant, reactive UI updates across screens without manual state passing.
* **Jetpack Compose & Material 3:** Used exclusively for the UI layer, leveraging Material 3 tokens for out-of-the-box Dark Mode support and responsive design.

### Tradeoffs
* **Scroll Position vs. Layout Parsing:** Instead of spending hours writing a custom text layout engine to calculate exact reading percentages or parsing raw text into "chapters", I opted for a simple `VerticalScroll` with `animateScrollTo`. This allowed me to deliver a buttery smooth core flow, text resizing, and a polished UI well within the timebox.

### Challenges & Resolutions
1.  **Compose Lifecycle vs. Coroutine Cancellation:** Initially, saving the reading progress on screen exit failed because the ViewModel's `viewModelScope` was cancelled before the database write finished. I resolved this by shifting to an active `snapshotFlow` with a 500ms `debounce`. This quietly saves progress in the background while the user is reading.
2.  **Scroll Restoration Race Condition:** Compose attempts to restore scroll state before text is fully measured. I introduced a micro-delay (`150ms`) in the restoration `LaunchedEffect` to guarantee accurate scroll placement.

### Large-Screen Thinking (Tablets/Foldables)
*If adapting this for production on large screens, I would:*
1.  Implement a `ListDetailPaneScaffold` (from Compose Material 3 Adaptive) to show the Library Grid on the left and the Book Detail view on the right simultaneously on tablets.
2.  In the Reader Screen, restrict the maximum text width to `~600dp` and center it, preventing users from having to swing their heads across a massive tablet screen to read a single line of text.

### What I would improve with 2 more days
* **Data Model Refactor:** Update the database to support explicit `Chapter` objects rather than monolithic content strings.
* **Percentage Indicators:** Implement a global layout listener on the Reader's `ScrollState` relative to the total layout height to calculate a reading percentage (0-100%).
* **Library UI Overhaul:** Add a dedicated horizontal `LazyRow` carousel at the top of the Home screen exclusively for "Saved" titles to distinguish them from the main grid.