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
}