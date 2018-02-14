# marconi
An assortment of tools for working with audio files written in Kotlin.

# tools

## `FilenameMatchingFunctionsKt.searchFilenameForTrackMatch()`

Return possible mappings to well-known audio file attributes from common file and path naming conventions.

Usage:

```kotlin
val matches = searchFilenameForTrackMatch(someAudioFile)

println("Matches: $matches")
```