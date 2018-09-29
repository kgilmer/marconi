package org.kgilmer.marconi.lastfm

import org.jaudiotagger.tag.FieldKey
import org.junit.Assert.assertTrue
import org.junit.Test

const val API_KEY = "59c74f009a8cf08bcef859b322519290"

class LastFmRankingTest {

    @Test fun `known good track integration test`() {
        val goodTrack = mapOf(
                FieldKey.TITLE to "roots",
                FieldKey.ARTIST to "bob marley"
        )

        assertTrue("track metadata sufficient to query", sufficientTrackMetadataForLastFm(goodTrack))

        val matchResult = getLastFmRank(API_KEY, goodTrack)

        assertTrue("rank shows valid match", matchResult.first == 1F)
        assertTrue("valid match contains response data", !matchResult.second.isNullOrBlank())
        println(matchResult.second)

        val match = deserializeLastFmTrackInfo(matchResult.second!!)

        println(match)
    }
}