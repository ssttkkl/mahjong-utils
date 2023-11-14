package mahjongutils.models

import kotlin.test.*

class TestTile {
    @Test
    fun testParseTiles() {
        val tiles = Tile.parseTiles("0123456789m0123456789p0123456789s1234567z")
        assertEquals(
            buildList {
                for (i in 0..9) {
                    add(Tile.get(TileType.M, i))
                }
                for (i in 0..9) {
                    add(Tile.get(TileType.P, i))
                }
                for (i in 0..9) {
                    add(Tile.get(TileType.S, i))
                }
                for (i in 1..7) {
                    add(Tile.get(TileType.Z, i))
                }
            },
            tiles
        )

        assertFails {
            Tile.parseTiles("1234mpsinvalid1234")
        }

        assertFails {
            Tile.parseTiles("1234m1234")
        }

        assertFails {
            Tile.parseTiles("1234m1234n")
        }
    }

    @Test
    fun testGetByStr() {
        for (i in 0..9) {
            assertEquals(Tile.get(TileType.M, i), Tile["${i}m"])
        }
        for (i in 0..9) {
            assertEquals(Tile.get(TileType.P, i), Tile["${i}p"])
        }
        for (i in 0..9) {
            assertEquals(Tile.get(TileType.S, i), Tile["${i}s"])
        }
        for (i in 1..7) {
            assertEquals(Tile.get(TileType.Z, i), Tile["${i}z"])
        }

        assertFails {
            Tile["1n"]
        }

        assertFails {
            Tile["nm"]
        }

        assertFails {
            Tile["invalid"]
        }
    }

    @Test
    fun testGetByCode() {
        for (i in 0..9) {
            assertEquals(Tile.get(TileType.M, i), Tile[i])
        }
        for (i in 0..9) {
            assertEquals(Tile.get(TileType.P, i), Tile[i + 10])
        }
        for (i in 0..9) {
            assertEquals(Tile.get(TileType.S, i), Tile[i + 20])
        }
        for (i in 1..7) {
            assertEquals(Tile.get(TileType.Z, i), Tile[i + 30])
        }

        assertFails {
            Tile[-1]
        }

        assertFails {
            Tile[30]
        }

        assertFails {
            Tile[50]
        }
    }

    @Test
    fun testCompareTo() {
        assertTrue(Tile["4m"] < Tile["5p"])
        assertTrue(Tile["5p"] > Tile["4m"])
        assertTrue(Tile["4m"] < Tile["5m"])
        assertTrue(Tile["5m"] > Tile["4m"])
        assertTrue(Tile["5m"] < Tile["0m"])
        assertTrue(Tile["0m"] > Tile["5m"])
        assertTrue(Tile["0m"] < Tile["7m"])
        assertTrue(Tile["7m"] > Tile["0m"])
        assertTrue(Tile["5m"] == Tile["5m"])

        val tiles = Tile.parseTiles("1234506789m1234506789p1234506789s1234567z")
        val shuffled = tiles.shuffled()
        assertEquals(tiles, shuffled.sorted())
    }

    @Test
    fun testRealNum() {
        assertEquals(4, Tile["4m"].realNum)
        assertEquals(5, Tile["5m"].realNum)
        assertEquals(1, Tile["1z"].realNum)
        assertEquals(5, Tile["0m"].realNum)

        Tile.all.forEach {
            if (it.num == 0) {
                assertNotEquals(it.num, it.realNum)
            } else {
                assertEquals(it.num, it.realNum)
            }
        }
    }
}
