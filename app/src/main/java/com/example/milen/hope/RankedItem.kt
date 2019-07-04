package com.example.milen.hope

// TODO: Exceptions, check ranks.
class RankedItem internal constructor(val description: String, var rank: Short) {

    override fun toString(): String {
        return "$description $rank"
    }
}
