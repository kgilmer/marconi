package org.kgilmer.marconi

import org.junit.Assert.assertTrue
import org.junit.Test

class FilenameMatchingTest {

    @Test
    fun testMatchArtistAlbumIndexTitle() {
        var inputs = getArtistAlbumIndexTitleVariants()

        val goodMatchResults = inputs
                .mapIndexed { index, s -> index to matchArtistAlbumIndexTitle(s.split("/").toTriple()) }
                .toMap()

        (0 until goodMatchResults.size).forEach {
            println("$it - ${inputs[it]} - ${goodMatchResults[it]}")
            assertTrue("Successful match", goodMatchResults[it] != null)
        }

        inputs = getArtistAlbumDiscIndexTitleVariants()

        val badMatchResults = inputs
                .mapIndexed { index, s -> index to matchArtistAlbumIndexTitle(s.split("/").toTriple()) }
                .toMap()

        (0 until badMatchResults.size).forEach {
            println("$it - ${inputs[it]} - ${badMatchResults[it]}")
            assertTrue("No false positives", badMatchResults[it] == null)
        }
    }

    @Test
    fun testMatchNoneArtistAlbumDateIndexTitle() {
        val inputs = getNoneArtistAlbumDateIndexTitleVariants()

        val goodMatchResults = inputs
                .mapIndexed { index, s -> index to matchNoneArtistAlbumDateIndexTitle(s.split("/").toTriple()) }
                .toMap()

        (0 until goodMatchResults.size).forEach {
            println("$it - ${inputs[it]} - ${goodMatchResults[it]}")
            assertTrue("Successful match", goodMatchResults[it] != null)
        }
    }

    @Test
    fun testMatchNoneArtistAlbumIndexTitle() {
        val inputs = getNoneArtistAlbumIndexTitleVariants()

        val goodMatchResults = inputs
                .mapIndexed { index, s -> index to matchNoneArtistAlbumIndexTitle(s.split("/").toTriple()) }
                .toMap()

        (0 until goodMatchResults.size).forEach {
            println("$it - ${inputs[it]} - ${goodMatchResults[it]}")
            assertTrue("Successful match", goodMatchResults[it] != null)
        }
    }

    @Test
    fun testMatchArtistAlbumDiscIndexTitle() {
        var inputs = getArtistAlbumDiscIndexTitleVariants()

        val goodMatchResults = inputs
                .mapIndexed { index, s -> index to matchArtistAlbumDiscIndexTitle(s.split("/").toTriple()) }
                .toMap()

        (0 until goodMatchResults.size).forEach {
            println("$it - ${inputs[it]} - ${goodMatchResults[it]}")
            assertTrue("Successful match", goodMatchResults[it] != null)
        }

        inputs = getArtistAlbumIndexTitleVariants()

        val badMatchResults = inputs
                .mapIndexed { index, s -> index to matchArtistAlbumDiscIndexTitle(s.split("/").toTriple()) }
                .toMap()

        (0 until badMatchResults.size).forEach {
            println("$it - ${inputs[it]} - ${badMatchResults[it]}")
            assertTrue("No false positives", badMatchResults[it] == null)
        }
    }

    //TODO: parse 'somedir/Various Artists - Denver Afterdark - Fall 2010 [MP3 V0]/08. Blance.mp3'

    private fun getArtistAlbumIndexTitleVariants() = listOf(
            "Stars of the Lid/Music for Nitrous Oxide/03 Madison.mp3",
            "Stereolab/Mars Audiac Quintet/07 Three Longers Later.mp3",
            "Ludwig van Beethoven/The Piano - The ultimate piano collection of the century, CD 12 - CD 21/16-14 Sonata for Piano No. 9 in E major, Op. 14 No. 1: I. Allegro.mp3"
            )

    private fun getArtistAlbumDiscIndexTitleVariants() = listOf(
            "Stereolab/Oscillions Fron The Anti Sun/1-08 Ping Pong.mp3",
            "Ravin & David Visan/Buddha-Bar VII/1-14 Peppe Barra - Core Nire (Azoia remix).mp3")

    private fun getNoneArtistAlbumDateIndexTitleVariants() = listOf(
            "somedir/Various Artists - Denver Afterdark - Fall 2010 [MP3 V0]/01. Nite Chimes For Lucid Lovers.mp3")

    private fun getNoneArtistAlbumIndexTitleVariants() = listOf(
            "somedir/Various - Uncut Still Smiling VO/05 Mikal CroninIs It Alright.mp3",
            "somedir/VA - Kyoto Jazz Massive\u200E (1994) MP3 V0/04. B-Bandj - The Habit (feat. DJ Krush).mp3")

}