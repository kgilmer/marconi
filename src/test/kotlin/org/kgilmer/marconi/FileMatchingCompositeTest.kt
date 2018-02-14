package org.kgilmer.marconi

import org.jaudiotagger.tag.FieldKey
import org.junit.Assert.assertTrue
import org.junit.Test

class FileMatchingCompositeTest {

    data class TrackDescriptor(val albumArtist: String, val album: String, val title: String, val index: String, val trackArtist: String = albumArtist)

    @Test
    fun testNoneArtistAlbumIndexTrack() {
        val input = generateTestData()

        input.forEach { (descriptor, input) ->
            println("testing $input")
            val matches = searchFilenameForTrackMatch(input.split(delimiters = *charArrayOf('/')).toTriple())

            when (descriptor) {
                null -> assertTrue("No match", matches.isEmpty())
                else -> assertTrue("At least one match.", matches.isNotEmpty())
            }

            descriptor?.let {
                assertTrue("matching albumArtist", matches.mapNotNull { it[FieldKey.ALBUM_ARTIST] }.contains(it.albumArtist))
                assertTrue("matching album", matches.mapNotNull { it[FieldKey.ALBUM] }.contains(it.album))
                assertTrue("matching title", matches.mapNotNull { it[FieldKey.TITLE] }.contains(it.title))
                assertTrue("matching track", matches.mapNotNull { it[FieldKey.TRACK] }.contains(it.index))

                if (it.albumArtist != it.trackArtist) {
                    assertTrue("matching artist", matches.mapNotNull { it[FieldKey.ARTIST] }.contains(it.trackArtist))
                }
            }
        }
    }

    private fun generateTestData(): List<Pair<TrackDescriptor?, String>> =
            listOf(Pair(TrackDescriptor("Yo-Yo Ma", "Vivaldi's Cello", "Concerto for Viola d'Amore, Lute and Orchestra - II. Largo", "10"), "somedir/Yo-Yo Ma - Vivaldi's Cello/10 Concerto for Viola d'Amore, Lute and Orchestra - II. Largo.mp3"),
                   Pair(TrackDescriptor("VA", "The Hidden City SR219 (2004) (v0)", "Oh Lord", "05", "Paul Bothen"), "somedir/VA - The Hidden City SR219 (2004) (v0)/05 Paul Bothen - Oh Lord.mp3")
                   /*,Pair(TrackDescriptor("Various Artists", "Denver Afterdark - Fall 2010", "Blance", "08", ""), "somedir/Various Artists - Denver Afterdark - Fall 2010 [MP3 V0]/08. Blance.mp3")*/)
}