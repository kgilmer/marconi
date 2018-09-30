package org.kgilmer.marconi

import org.jaudiotagger.tag.FieldKey
import org.junit.Assert.assertTrue
import org.junit.Test

class StringFunctionTest {

    @Test fun canFilterNonAlphaFromAlbum() {
        val testInput = mapOf(FieldKey.ARTIST to "bob marley", FieldKey.ALBUM to "& The Wailers")

        val output = stripNonAlpha(testInput)

        assertTrue("Expected output", output[FieldKey.ALBUM] == "The Wailers")
    }

    @Test fun canFilterBracketText() {
        val testInput = mapOf(FieldKey.ARTIST to "bob marley [FOR REAL]", FieldKey.ALBUM to "& The Wailers")

        val output = stripBracketedText(testInput)

        assertTrue("Expected output", output[FieldKey.ARTIST] == "bob marley")
    }

    @Test fun canFilterParenText() {
        val testInput = mapOf(FieldKey.ARTIST to "bob marley (1978)", FieldKey.ALBUM to "& The Wailers")

        val output = stripParenText(testInput)

        assertTrue("Expected output", output[FieldKey.ARTIST] == "bob marley")
    }
}