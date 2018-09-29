package org.kgilmer.marconi

import org.jaudiotagger.tag.FieldKey

internal val alphaRegex = Regex("[^A-Za-z ]")

fun stripNonAlpha(input: Map<FieldKey, String>): Map<FieldKey, String> =
    input.entries.map { entry: Map.Entry<FieldKey, String> ->
        entry.key to entry.value.replace(
            alphaRegex,
            ""
        ).trim()
    }.toMap()