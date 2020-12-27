package parking
fun main() {
    Parking.listen()
}

object Parking {
    var spots = Array<Slot>(0) { Slot(arrayOf("", "")) }

    fun create(num: Int) {
        spots = Array<Slot>(num) { Slot(arrayOf("", "")) }
        println("Created a parking lot with $num spots.")
    }

    fun status() {
        var count = 0
        for ((i, slot) in spots.withIndex()) {
            if (slot?.color?.length > 0) {
                println("${i + 1} ${slot.color} ${slot.reg}")
                count++
            }
        }
        if (count == 0) {
            println("Parking lot is empty.")
        }
    }

    fun getFreeSlot(): Int {
        for ((i, slot) in spots.withIndex()) {
            if (slot?.color?.length == 0) return i
        }
        return -1
    }

    fun listen() {
        val input = readLine()!!.split(" ").toTypedArray()
        val cmd = input[0]
        if (cmd == "exit") return
        if (cmd == "create") {
            create(input[1].toInt())
        } else if (spots.isEmpty()) {
            println("Sorry, a parking lot has not been created.")
        } else {
            when (cmd){
                "status" -> status()
                "park" -> park(input.drop(1).toTypedArray())
                "leave" -> leave(input[1].toInt())
                "spot_by_color" -> get_by_color("spot", input[1])
                "reg_by_color" -> get_by_color("reg", input[1])
                "spot_by_reg" -> get_by_reg("spot", input[1])
                else -> println("Unknown command. Try again.")
            }
        }
        listen()
    }

    fun get_by_color(type: String, color: String) {
        val slot = spots.find { it.color.toUpperCase() == color.toUpperCase() }
        if (slot != null) {
            var found = ""
            for ((i, s) in spots.withIndex()) {
                if (s.color.toUpperCase() == color.toUpperCase()) {
                    found += when (type) {
                        "spot" -> "${i + 1} "
                        "reg" -> "${s.reg} "
                        else -> throw Exception("Unknown type in get_by_color!")
                    }
                }
            }
            found = found.trimEnd().replace(" ", ", ")
            println(found)
        } else {
            println("No cars with color $color were found.")
        }
    }

    fun get_by_reg(type: String, reg: String) {
        val slot = spots.find {it.reg == reg }
        if (slot != null) {
            var found = ""
            for ((i, s) in spots.withIndex()) {
                if (s.reg == reg) {
                    found += when (type) {
                        "spot" -> "${i + 1} "
                        else -> throw Exception("Unknown type in get_by_reg function!")
                    }
                }
            }
            found = found.trimEnd().replace(" ", ", ")
            println(found)
        } else {
            println("No cars with registration number $reg were found.")
        }
    }


    fun park(data: Array<String>) {
        val slot = getFreeSlot()
        if (slot != -1) {
            val car = Slot(data)
            spots[slot] = car
            println("${car.color} car parked in spot ${slot + 1}.")
        } else {
            println("Sorry, the parking lot is full.")
        }
    }

    fun leave(slot: Int) {
        when {
            slot !in spots.indices -> println("There's no lot with number $slot")
            spots[slot - 1].color.isNotEmpty() -> {
                spots[slot - 1] = Slot(arrayOf("", ""))
                println("Spot $slot is free.")
            }
            else -> println("There is no car in spot $slot.")
        }
    }
}

data class Slot(val data: Array<String>) {

    var color = data[1]
    var reg = data[0]
}
