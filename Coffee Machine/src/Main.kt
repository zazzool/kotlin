package machine

import java.lang.IllegalArgumentException
import java.util.*

fun main() {
    Machine.run()
}

class Machine {
    companion object {
        var state = Menu.MAIN
            set(value) {
                field = value
                run()
            }
        var water = 400
        var milk = 540
        var coffee = 120
        var disposableCups = 9
        var money = 550
        private fun getRemaining() {
            print(
            "The coffee machine has:\n" +
            "$water of water\n" +
            "$milk of milk\n" +
            "$coffee of coffee beans\n" +
            "$disposableCups of disposable cups\n" +
            "$money of money\n"
            )
        }

        fun run() {
            when (state) {
                Menu.MAIN -> {
                    print("Write action (buy, fill, take, remaining, exit): > ")
                    state = try {
                        Menu.valueOf(readLine()!!.toUpperCase())
                    } catch (e: IllegalArgumentException) {
                        Menu.MAIN
                    }
                }
                Menu.BUY -> {
                    print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: > ")
                    val command = readLine()!!
                    if (command != "back") sellCoffee(command.toInt())
                    state = Menu.MAIN
                }
                Menu.FILL -> {
                    fill()
                    state = Menu.MAIN
                }
                Menu.REMAINING -> {
                    getRemaining()
                    state = Menu.MAIN
                }
                Menu.TAKE -> {
                    takeMoney()
                    state = Menu.MAIN
                }
                Menu.EXIT -> return
            }
        }

        private fun takeMoney() {
            println("I gave you \$$money")
            money = 0
        }

        private fun fill() {
            print("Write how many ml of water do you want to add: > ")
            water += readLine()!!.toInt()
            print("Write how many ml of milk do you want to add: > ")
            milk += readLine()!!.toInt()
            print("Write how many grams of coffee beans do you want to add: > ")
            coffee += readLine()!!.toInt()
            print("Write how many disposable cups of coffee do you want to add: > ")
            disposableCups += readLine()!!.toInt()
        }

        private fun isEnough(name: String, value: Int): Int {
            var st = when (name) {
                "water" -> if (water < value) 0 else 1
                "coffee" -> if (coffee < value) 0 else 1
                "milk" -> if (milk < value) 0 else 1
                "cups" -> if (disposableCups < value) 0 else 1
                else -> 0
            }
            if (st == 0) println("Sorry, not enough $name")
            return st
        }

        private fun sellCoffee(type: Int) {
            val ct = CoffeeType.values().getOrElse(type) { CoffeeType.NULL}
            if (ct.isNull()) {
                println("Sorry, this type of coffee unavailable.")
                return
            } else {
                val resources = listOf(
                    isEnough("water", ct.water),
                    isEnough("coffee", ct.coffee),
                    isEnough("milk", ct.milk),
                    isEnough("cups", ct.cups),
                )
                if (resources.sum() >= resources.size) {
                    water -= ct.water
                    coffee -= ct.coffee
                    milk -= ct.milk
                    disposableCups -= ct.cups
                    money += ct.money
                    println("I have enough resources, making you a coffee!")
                }
            }
        }
    }
}

enum class Menu { MAIN, BUY, FILL, TAKE, REMAINING, EXIT }

enum class CoffeeType(val water: Int, val milk: Int, val coffee: Int, val cups: Int, val money: Int) {
    NULL(0,0,0,0,0),
    ESPRESSO(250, 0, 16, 1, 4 ),
    LATTE(350, 75, 20, 1, 7),
    CAPPUCCINO(200, 100, 12, 1, 6);

    fun isNull(): Boolean = listOf(water, milk, coffee, cups, money).sum() == 0
}
