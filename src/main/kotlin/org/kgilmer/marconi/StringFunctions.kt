package org.kgilmer.marconi

import org.jaudiotagger.tag.FieldKey

internal val alphaRegex = Regex("[^A-Za-z ]")
internal val bracketRegex = Regex("\\[.*?]")
internal val parenRegex = Regex("\\(.*?\\)")

fun stripNonAlpha(input: Map<FieldKey, String>): Map<FieldKey, String> = stripRegex(alphaRegex, input)

fun stripBracketedText(input: Map<FieldKey, String>): Map<FieldKey, String> = stripRegex(bracketRegex, input)

fun stripParenText(input: Map<FieldKey, String>): Map<FieldKey, String> = stripRegex(parenRegex, input)

internal fun stripRegex(regex: Regex, input: Map<FieldKey, String>): Map<FieldKey, String> =
    input.entries.map { entry: Map.Entry<FieldKey, String> ->
        entry.key to entry.value.replace(
            regex,
            ""
        ).trim()
    }.toMap()

//  \[.*?\]