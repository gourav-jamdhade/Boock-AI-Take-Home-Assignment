# Boock Prototype - Android Take-Home

A lightweight Android prototype for a consumer reading app, built to demonstrate professional engineering quality, scalable architecture, and thoughtful tradeoffs within a strict timebox.

## Setup Steps
1. Open the project in Android Studio (built using AGP 8+ and Kotlin 2.3+).
2. Sync Gradle dependencies. The project uses a modern Version Catalog (`libs.versions.toml`).
3. Run the app on an emulator or physical device (API 26+).
   *Note: The mock JSON data is bundled directly in the `assets/books.json` folder and populates the local database automatically on the first launch.*

## Assumptions Made
* **Data Structure:** I assumed books are delivered as single, continuous strings of text for this prototype. In a production app, I would expect the backend to deliver structural arrays (e.g., chapters or paginated blocks) to handle massive files without memory issues.
* **Progress Tracking:** Tracking the exact pixel scroll state continuously is battery-intensive. I assumed saving the scroll state when the user stops scrolling (via a 500ms debounce flow) was a reasonable tradeoff for a prototype.
* **Settings Persistence:** Text size preferences are treated as global app settings (persisted via `SharedPreferences`), whereas reading progress and library status are entity-specific (persisted via Room).

## Known Gaps
* No pagination: The reader uses a continuous vertical scroll rather than discrete pages.
* No exact percentage tracking (utilizes generic Read/Unread/Reading statuses).