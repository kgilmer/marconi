package org.kgilmer.marconi.lastfm.model

import com.beust.klaxon.Json

data class TrackGetInfoResponse(val track: Track)

data class Track(
    val name: String,
    val url: String,
    val duration: String,
    val streamable: Streamable,
    val listeners: String,
    val playcount: String,
    val artist: Artist,
    val album: Album?,
    val toptags: Toptags
)

data class Toptags(val tag: List<Tag>)

data class Tag(
    val name: String,
    val url: String
)

data class Streamable(
    @Json(name = "#text")
    val text: String,
    val fulltrack: String
)

data class Image(
    @Json(name = "#text")
    val text: String,
    val size: String
)

data class Artist(
    val name: String,
    val mbid: String,
    val url: String
)

data class Album(
    val artist: String,
    val title: String,
    val url: String,
    val image: List<Image>
)