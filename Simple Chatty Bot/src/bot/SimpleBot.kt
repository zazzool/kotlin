package bot

fun main() {
    Ai
}

object Ai {
    var botName = "Aid"
    var created = 2020
    var user = ""
    var userAge = 0

    init {
        greet()
        guessAge()
        countPlease()
        runTest()
    }


    private fun greet() {
        println("Hello! My name is Aid.")
        println("I was created in 2020.")
        println("Please, remind me your name.")
        print("> ")
        user = readLine()!!
        println("What a great name you have, $user!")
    }

    private fun guessAge() {
        println("Let me guess your age.")
        println("Enter remainders of dividing your age by 3, 5 and 7.")
        print("> ")
        userAge = (readLine()!!.toInt() * 70 + readLine()!!.toInt() * 21 + readLine()!!.toInt() * 15) % 105
        println("Your age is $userAge; that's a good time to start programming!")
    }

    private fun countPlease() {
        println("Now I will prove to you that I can count to any number you want.")
        val n = readLine()!!.toInt() + 1
        repeat(n) {
            println("$it!")
        }
        println("Completed, have a nice day!")
    }

    private fun runTest() {
        println("Let's test your programming knowledge.\n" +
                "Why do we use methods?")
        println("1. To repeat a statement multiple times.")
        println("2. To decompose a program into several small subroutines.")
        println("3. To determine the execution time of a program.")
        println("4. To interrupt the execution of a program.")
        while (readLine()!!.toInt() != 2) {
            println("Please, try again.")
        }
        println("Congratulations, have a nice day!")
    }
}
