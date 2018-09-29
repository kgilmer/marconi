package org.kgilmer.marconi.lastfm

import com.beust.klaxon.Klaxon
import org.jaudiotagger.tag.FieldKey
import org.kgilmer.identist.httpGet
import org.kgilmer.marconi.lastfm.model.TrackGetInfoResponse
import java.io.PrintWriter
import java.net.URL

/**
 * Return true if track data is sufficient to query lastFm, false otherwise.
 */
fun sufficientTrackMetadataForLastFm(input: Map<FieldKey, String>?): Boolean {
    if (input == null || input.isEmpty()) return false

    if (input[FieldKey.ARTIST].isNullOrEmpty() || input[FieldKey.TITLE].isNullOrEmpty()) return false

    return true
}

/**
 * Query last.fm for track match.
 *
 * @return a ranking of 0F for no match, or 1F for at least one match.  Returns response body
 * containing match data as a JSON String in the case of a successful match.
 */
fun getLastFmRank(lastFMApiKey: String, input: Map<FieldKey, String>): Pair<Float, String?> {
    if (!sufficientTrackMetadataForLastFm(input)) return 0F to null

    val lastFmUrl = URL("http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=$lastFMApiKey&artist=${input[FieldKey.ARTIST]}&track=${input[FieldKey.TITLE]}&format=json")

    return lastFmUrl.httpGet(logger = PrintWriter(System.out)) { status, _, body ->
        if (status > 299) return@httpGet 0F to null

        val responseString = body!!.bufferedReader().readText()

        if (responseString.contains("playcount")) return@httpGet 1F to responseString // TODO here potentially add data since we have a match

        return@httpGet 0F to null
    }
}

fun deserializeLastFmTrackInfo(input: String): TrackGetInfoResponse? = Klaxon().parse<TrackGetInfoResponse>(input)