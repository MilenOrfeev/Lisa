package com.example.milen.hope

import java.util.Date

abstract class DatedItem(var date: Date?, var description: String?) {

    override fun toString(): String {
        return date!!.toString() + " " + description
    }
}
