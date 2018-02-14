package org.kgilmer.marconi

import org.jaudiotagger.tag.FieldKey
import java.io.File

fun searchFilenameForTrackMatch(input: Triple<String, String, String>): List<Map<FieldKey, String>> =
        listOf<(Triple<String, String, String>) -> Map<FieldKey, String>?>(
                ::matchArtistAlbumDiscIndexTitle,
                ::matchArtistAlbumIndexTitle,
                ::matchNoneArtistAlbumDateIndexTitle,
                ::matchNoneArtistAlbumIndexTitle,
                ::matchNoneArtistAlbumIndexTrackArtistTitle,
                ::matchArtistAlbumIndexTrackArtistTitle).mapNotNull { it.invoke(input) }

fun matchArtistAlbumIndexTitle(input: Triple<String, String, String>): Map<FieldKey, String>? =
        matchPattern(input,
                listOf("^(.*)-##-(.*)-##-(\\d{2}). (.*).mp3",
                        "^(.*)-##-(.*)-##-(\\d{2}) (.*).mp3",
                        "^(.*)-##-(.*)-##-(\\d{2})-(.*).mp3"),
                listOf(FieldKey.ALBUM_ARTIST to 1,
                        FieldKey.ALBUM to 2,
                        FieldKey.TRACK to 3,
                        FieldKey.TITLE to 4))

// ex ~ somedir/VA - The Hidden City SR219 (2004) (v0)/05 Paul Bothen - Oh Lord.mp3
fun matchArtistAlbumIndexTrackArtistTitle(input: Triple<String, String, String>): Map<FieldKey, String>? =
        matchPattern(input,
                listOf("^(.*)-##-(.*)-##-(\\d{2}). (.*)-(.*).mp3",
                        "^(.*)-##-(.*)-##-(\\d{2}) (.*)-(.*).mp3",
                        "^(.*)-##-(.*)-##-(\\d{2})-(.*)-(.*).mp3"),
                listOf(FieldKey.ALBUM_ARTIST to 1,
                        FieldKey.ALBUM to 2,
                        FieldKey.TRACK to 3,
                        FieldKey.ARTIST to 4,
                        FieldKey.TITLE to 5))

fun matchNoneArtistAlbumIndexTrackArtistTitle(input: Triple<String, String, String>): Map<FieldKey, String>? =
        matchPattern(input, listOf("^.*-##-(.*)-(.*)-##-(\\d{2}). (.*)-(.*).mp3",
                "^.*-##-(.*)-(.*)-##-(\\d{2}) (.*)-(.*).mp3",
                "^.*-##-(.*)-(.*)-##-(\\d{2})-(.*)-(.*).mp3"),
                listOf(FieldKey.ALBUM_ARTIST to 1,
                        FieldKey.ALBUM to 2,
                        FieldKey.TRACK to 3,
                        FieldKey.ARTIST to 4,
                        FieldKey.TITLE to 5))

fun matchNoneArtistAlbumDateIndexTitle(input: Triple<String, String, String>): Map<FieldKey, String>? =
        matchPattern(input, listOf(
                "^.*-##-(.*)-(.*)-(.*)-##-(\\d{2}). (.*).mp3",
                "^.*-##-(.*)-(.*)-(.*)-##-(\\d{2}) (.*).mp3",
                "^.*-##-(.*)-(.*)-(.*)-##-(\\d{2})-(.*).mp3"),
                listOf(FieldKey.ALBUM_ARTIST to 1,
                        FieldKey.ALBUM to 2,
                        FieldKey.TRACK to 4,
                        FieldKey.TITLE to 5))

fun matchNoneArtistAlbumIndexTitle(input: Triple<String, String, String>): Map<FieldKey, String>? =
        matchPattern(input, listOf(
                "^.*-##-(.*)-(.*)-##-(\\d{2}). (.*).mp3",
                "^.*-##-(.*)-(.*)-##-(\\d{2}) (.*).mp3",
                "^.*-##-(.*)-(.*)-##-(\\d{2})-(.*).mp3"),
                listOf(FieldKey.ALBUM_ARTIST to 1,
                        FieldKey.ALBUM to 2,
                        FieldKey.TRACK to 3,
                        FieldKey.TITLE to 4))

fun matchArtistAlbumDiscIndexTitle(input: Triple<String, String, String>): Map<FieldKey, String>? =
        matchPattern(input,
                listOf("^(.*)-##-(.*)-##-(\\d)-(\\d{2}) (.*).mp3"),
                listOf(FieldKey.ALBUM_ARTIST to 1,
                        FieldKey.ALBUM to 2,
                        FieldKey.DISC_NO to 3,
                        FieldKey.TRACK to 4,
                        FieldKey.TITLE to 5))

private fun matchPattern(input: Triple<String, String, String>, regExpressions: List<String>, fieldKeyRegexGroupMapping: List<Pair<FieldKey, Int>>, filePathSeparator: String = "-##-"): Map<FieldKey, String>? {
    require(regExpressions.isNotEmpty(), { "Must supply at least one regex." })
    require(fieldKeyRegexGroupMapping.isNotEmpty(), { "Must supply at least one field mapping."} )

    regExpressions.forEach { regex ->
        val matchResult = regex.toRegex().find(input.toList().joinToString(separator = filePathSeparator))

        matchResult?.let { matchedGroups ->
            val maxIndex = fieldKeyRegexGroupMapping.map(Pair<FieldKey, Int>::second).max()!! + 1
            if (matchedGroups.groupValues.size <= maxIndex) {
                val tokens = matchedGroups.groupValues.map { it.trim() }

                return fieldKeyRegexGroupMapping.map { it.first to tokens[it.second] }.toMap()
            }
        }
    }

    return null
}

fun <T> List<T>.toTriple(): Triple<T, T, T> {
    require(this.size == 3, { "List must have exactly 3 elements." })

    return Triple(this[0], this[1], this[2])
}

fun extractFilePathToTriple(audioFile: File): Triple<String, String, String> {
    val parentParentName = audioFile.parentFile?.parentFile?.name ?: ""
    val parentName = audioFile.parentFile?.name ?: ""
    val filename = "${audioFile.nameWithoutExtension}.${audioFile.extension.toLowerCase()}"

    return Triple(parentParentName, parentName, filename)
}