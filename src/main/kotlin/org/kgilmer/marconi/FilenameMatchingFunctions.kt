package org.kgilmer.marconi

import org.jaudiotagger.tag.FieldKey
import java.io.File

typealias TrackMatch = Map<FieldKey, String>

fun searchFilenameForTrackMatch(input: Triple<String, String, String>): List<TrackMatch> =
        listOf<(Triple<String, String, String>) -> TrackMatch?>(
                ::matchArtistAlbumDiscIndexTitle,
                ::matchArtistAlbumIndexTitle,
                ::matchNoneArtistAlbumDateIndexTitle,
                ::matchNoneArtistAlbumIndexTitle,
                ::matchNoneArtistAlbumIndexTrackArtistTitle,
                ::matchArtistAlbumIndexTrackArtistTitle,
                ::matchArtistYearAlbumTrackIndexTrackTitle
        ).mapNotNull { it.invoke(input) }

fun matchArtistAlbumIndexTitle(input: Triple<String, String, String>): TrackMatch? =
        matchPattern(input,
                listOf("^(.*)-##-(.*)-##-(\\d{2}). (.*).mp3",
                        "^(.*)-##-(.*)-##-(\\d{2}) (.*).mp3",
                        "^(.*)-##-(.*)-##-(\\d{2})-(.*).mp3"),
                listOf(FieldKey.ALBUM_ARTIST to 1,
                        FieldKey.ALBUM to 2,
                        FieldKey.TRACK to 3,
                        FieldKey.TITLE to 4))

// ex ~ somedir/VA - The Hidden City SR219 (2004) (v0)/05 Paul Bothen - Oh Lord.mp3
fun matchArtistAlbumIndexTrackArtistTitle(input: Triple<String, String, String>): TrackMatch? =
        matchPattern(input,
                listOf("^(.*)-##-(.*)-##-(\\d{2}). (.*)-(.*).mp3",
                        "^(.*)-##-(.*)-##-(\\d{2}) (.*)-(.*).mp3",
                        "^(.*)-##-(.*)-##-(\\d{2})-(.*)-(.*).mp3"),
                listOf(FieldKey.ALBUM_ARTIST to 1,
                        FieldKey.ALBUM to 2,
                        FieldKey.TRACK to 3,
                        FieldKey.ARTIST to 4,
                        FieldKey.TITLE to 5))

fun matchNoneArtistAlbumIndexTrackArtistTitle(input: Triple<String, String, String>): TrackMatch? =
        matchPattern(input, listOf("^.*-##-(.*)-(.*)-##-(\\d{2}). (.*)-(.*).mp3",
                "^.*-##-(.*)-(.*)-##-(\\d{2}) (.*)-(.*).mp3",
                "^.*-##-(.*)-(.*)-##-(\\d{2})-(.*)-(.*).mp3"),
                listOf(FieldKey.ALBUM_ARTIST to 1,
                        FieldKey.ALBUM to 2,
                        FieldKey.TRACK to 3,
                        FieldKey.ARTIST to 4,
                        FieldKey.TITLE to 5))

fun matchNoneArtistAlbumDateIndexTitle(input: Triple<String, String, String>): TrackMatch? =
        matchPattern(input, listOf(
                "^.*-##-(.*)-(.*)-(.*)-##-(\\d{2}). (.*).mp3",
                "^.*-##-(.*)-(.*)-(.*)-##-(\\d{2}) (.*).mp3",
                "^.*-##-(.*)-(.*)-(.*)-##-(\\d{2})-(.*).mp3"),
                listOf(FieldKey.ALBUM_ARTIST to 1,
                        FieldKey.ALBUM to 2,
                        FieldKey.TRACK to 4,
                        FieldKey.TITLE to 5))

fun matchNoneArtistAlbumIndexTitle(input: Triple<String, String, String>): TrackMatch? =
        matchPattern(input, listOf(
                "^.*-##-(.*)-(.*)-##-(\\d{2}). (.*).mp3",
                "^.*-##-(.*)-(.*)-##-(\\d{2}) (.*).mp3",
                "^.*-##-(.*)-(.*)-##-(\\d{2})-(.*).mp3"),
                listOf(FieldKey.ALBUM_ARTIST to 1,
                        FieldKey.ALBUM to 2,
                        FieldKey.TRACK to 3,
                        FieldKey.TITLE to 4))

fun matchArtistAlbumDiscIndexTitle(input: Triple<String, String, String>): TrackMatch? =
        matchPattern(input,
                listOf("^(.*)-##-(.*)-##-(\\d)-(\\d{2}) (.*).mp3"),
                listOf(FieldKey.ALBUM_ARTIST to 1,
                        FieldKey.ALBUM to 2,
                        FieldKey.DISC_NO to 3,
                        FieldKey.TRACK to 4,
                        FieldKey.TITLE to 5))

// Bob Marley - Complete Discography From 1967 To 2002 [33 Full Albums] (Mp3 256Kbps)/Bob Marley - 1979 - Live From Kingston/02 - Positive Vibration.mp3
fun matchArtistYearAlbumTrackIndexTrackTitle(input: Triple<String, String, String>): TrackMatch? =
        matchPattern(input,
                listOf("^(.*)-##-(.*)-(.*)-(.*)-##-(\\d{2}) -(.*).mp3"),
                listOf(FieldKey.ARTIST to 2,
                        FieldKey.ALBUM to 4,
                        FieldKey.YEAR to 3,
                        FieldKey.TRACK to 5,
                        FieldKey.TITLE to 6))

private fun matchPattern(input: Triple<String, String, String>, regExpressions: List<String>, fieldKeyRegexGroupMapping: List<Pair<FieldKey, Int>>, filePathSeparator: String = "-##-"): TrackMatch? {
    require(regExpressions.isNotEmpty()) { "Must supply at least one regex." }
    require(fieldKeyRegexGroupMapping.isNotEmpty()) { "Must supply at least one field mapping." }

    regExpressions.forEach { regex ->
        val matchResult = regex.toRegex().find(input.toList().joinToString(separator = filePathSeparator))

        matchResult?.let { matchedGroups ->
            val maxIndex = fieldKeyRegexGroupMapping.asSequence().map(Pair<FieldKey, Int>::second).max()!! + 1
            if (matchedGroups.groupValues.size <= maxIndex) {
                val tokens = matchedGroups.groupValues.map { it.trim() }

                return fieldKeyRegexGroupMapping.map { it.first to tokens[it.second] }.toMap()
            }
        }
    }

    return null
}

fun <T> List<T>.toTriple(): Triple<T, T, T> {
    require(this.size == 3) { "List must have exactly 3 elements." }

    return Triple(this[0], this[1], this[2])
}

fun extractFilePathToTriple(audioFile: File): Triple<String, String, String> {
    val parentParentName = audioFile.parentFile?.parentFile?.name ?: ""
    val parentName = audioFile.parentFile?.name ?: ""
    val filename = "${audioFile.nameWithoutExtension}.${audioFile.extension.toLowerCase()}"

    return Triple(parentParentName, parentName, filename)
}