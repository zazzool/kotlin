package phonebook

import java.io.File
import java.util.*
import kotlin.math.sqrt

fun main() {
    PhoneBook.start()
}

object PhoneBook {
    private lateinit var file: File
    private var sorted = mutableListOf<String>()
    private var indexed = mutableMapOf<String, MutableList<Long>>()
    private var hashTable = mutableMapOf<String, String>()
    private var data = listOf<String>()
    private var queries = arrayOf<String>()
    private val userInput = Scanner(System.`in`)
    private var linearSearchTime = 0L

    fun start() {
        file = File("C:\\tst\\directory.txt")
        data = file.readLines().toList()
        queries = File("C:\\tst\\find.txt").readLines().toTypedArray()
        runSearchByType("linear")
        println()
        runSearchByType("bubble")
        println()
        runSearchByType("quick")
        println()
        runSearchByType("hash")
////        menu()
    }

    private fun runSearchByType(type: String) {
        val max = queries.size
        val start = System.currentTimeMillis()
        var end = 0L
        var endSort = 0L
        var startSearch = 0L
        var bubbleSorted = false
        var found = mutableListOf<String>()
        sorted = data.toMutableList()

        when (type) {
            "native" -> {
                println("Start searching (native search)...")
                startSearch = System.currentTimeMillis()
                for (q in queries) {
                    searchNative(q, data)?.let { found.add(it.first()) }
                }
            }
            "linear" -> {
                println("Start searching (linear search)...")
                startSearch = System.currentTimeMillis()
                for (q in queries) {
                    linearSearch(q, data)?.let { found.add(it) }
                }
                linearSearchTime = System.currentTimeMillis() - startSearch
            }
            "bubble" -> {
                println("Start searching (bubble sort + jump search)...")
                bubbleSorted = bubbleSort()
                startSearch = System.currentTimeMillis()
                endSort = startSearch - start
                for (query in queries) {
                    if (bubbleSorted) {
                        jumpSearch(query, sorted)?.let { found.add(it) }
                    } else {
                        linearSearch(query, data)?.let { found.add(it) }
                    }
                }
            }
            "quick" -> {
                println("Start searching (quick sort + binary search)...")
                val successSorting = quickSort(0, sorted.size-1, sorted)
                startSearch = System.currentTimeMillis()
                endSort = startSearch - start
                for (query in queries) {
                    if (successSorting) {
                        binarySearch(query, 0, sorted.size, sorted)?.let { found.add(it) }
                    } else {
                        linearSearch(query, data)?.let { found.add(it) }
                    }
                }
            }
            "hash" -> {
                println("Start searching (hash table)...")
                createHashTable(data)
                startSearch = System.currentTimeMillis()
                endSort = startSearch - start
                for (query in queries) {
                    val res = hashTable[hash(query)]
                    if (res != null) found.add(res)
                }
            }

        }
        end = System.currentTimeMillis()
        val foundCount = if (found.size > max) max else found.size
        println("Found $foundCount / $max entries. Time taken: " +
                "${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", end - start)}")
        when (type) {
            "bubble" -> {
                println("Sorting time: ${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", endSort)
                        + if(bubbleSorted) "" else " - STOPPED, moved to linear search"}")
                println("Searching time: ${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", end - startSearch)}")
            }
            "quick" -> {
                println("Sorting time: ${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", endSort)}")
                println("Searching time: ${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", end - startSearch)}")
            }
            "hash" -> {
                println("Creating time: ${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", endSort)}")
                println("Searching time: ${String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", end - startSearch)}")
            }
        }

    }

    private fun hash(string: String): String {
        return string.map { it.toInt() % 13 }.joinToString("")
    }

    private fun createHashTable(array: List<String>) {
        array.map {
            hashTable.put(hash(it.removePref()), it)
        }
    }

    private fun bubbleSort(): Boolean {
        var length = sorted.size
        val start = System.currentTimeMillis()
        while  (length > 0) {
            for (i in 0 until length - 1) {
                if(System.currentTimeMillis() - start > linearSearchTime * 3) return false
                if (sorted[i].removePref() > sorted[i + 1].removePref()) {
                    sorted[i] = sorted[i + 1].also { sorted[i + 1] = sorted[i] }
                }
            }
            length--
        }
        return true
    }

    private fun jumpSearch(query: String, array: List<String>): String? {
        /** Реализация алгоритма jumpsearch
         * Принимает на вход поисковый запрос @param[query]
         * И массив @param[array]
         * Возвращает найденную строку или null
         * */
        val stp = sqrt(array.size.toDouble()).toInt() // Определяем шаг прыжка. Лучший вариант - квадратный корень из длины массива
        if (stp > 0) {
            for (s in array.indices step stp) {
                val item = array[s].removePref()
                if (query > item) continue
                return if (item == query) array[s] else linearSearch(query, array.subList(0, s))
            }
        }
        return null
    }

    private fun linearSearch(query: String, array: List<String>): String? {
        /** Линейный поиск по массиву
         * @param[query] строка запроса
         * @param[array] массив, в котором ищем
         * @return возвращает найденную строку или null
         * */
        for (i in array.indices) {
            if (array[i].removePref() == query) return array[i]
        }
        return null
    }

    private fun quickSort(low: Int, high: Int, array: MutableList<String>): Boolean {
        /**
         * Быстрая сортировка массива quicksort
         * Принимает аргументы:
         * @param low нижняя граница сортировки
         * @param high верхняя граница сортировки
         * @param array изменяемый массив, в котором происходит сортировка
         * @return Возвращает true если сортровка успешно окончена
         * */
        if (array.isEmpty()) return false // если передан пустой массив, остановить сортировку
        //
        if (low < high) {
            val p = partition(low, high, array)
            quickSort(low, p, array)
            quickSort(p + 1, high, array)
        }
        return true
    }

    private fun partition(low: Int, high: Int, array: MutableList<String>): Int {
        /**
         * Перераспределяет элементы в массиве относительно центральной (средней) части: влево (меньшие)
         * и вправо (большие):
         * @param low нижняя граница перестановок
         * @param high верхняя граница перестановок
         * @param array весь изменяемый массив, в котором происходят перестановки
         * @return возвращает верхний индекс или обменивает значения массива с верхним и нижним индексом
         * */
        val pivot = array[(low + high) / 2].removePref() // Ось перестановок. Лучше выбирать медиану между верхней и нижней границей
        var i = low
        var j = high
        while (true) {
            while (array[i].removePref() < pivot) i++
            while (array[j].removePref() > pivot) j--
            if (i >= j) return j
            array[i] = array[j].also { array[j] = array[i] } // Обмен значениями
        }
    }

    private fun binarySearch(query: String, low: Int, high: Int, array: List<String>): String? {
        /**
         * @binarySearch
         * Реализация бинарного поиска
         * @param query текст запроса
         * @param low нижняя граница поиска
         * @param high верхняя граница поиска
         * @param array массив, в котором ведётся поиск
         * removePref() используется только тут, для того, чтобы искать только по именам
         * @return найденное значение или уход в рекурсию
         * * */
        if (low == high) return null // Закончить поиск
        val pivot = (low + high) / 2 // Делим массив на 2 части
        val str = array[pivot].removePref() // Получаем значение центрального элемента массива
        // Сравниваем запрос с центральным значением @str
        return when {
            query == str -> array[pivot] // Возвращаем найденное значение
            query < str -> binarySearch(query, low, pivot, array) // Продолжаем поиск в левой подмассиве
            else -> binarySearch(query, pivot, high, array) // Продолжаем поиск в правом подмассиве
        }
    }

    /**
     * @String.removePref() метод для строки, позволяющий убрать из неё первое слово
     * */
    private fun String.removePref() = this.split(" ").drop(1).joinToString(" ")


    private fun menu() {
        println("""
            1. Search with index
            2. Search native
            0. Exit
        """.trimIndent())
        when (userInput.nextLine().toInt()) {
            1 -> {
                print("Enter a query: ")
                val request = userInput.nextLine()
                searchIndexed(request)
            }
            2 -> {
                print("Enter a query: ")
                val request = userInput.nextLine()
                println(searchNative(request, data).joinToString("\n"))
            }
            0 -> return
        }
        menu()
    }

    private fun searchNative(query: String, array: List<String>): List<String> {
        /** Нативный котлин-поиск */
        return array.filter { it.contains(query) }

    }

    private fun searchIndexed(value: String): List<String> {
        /** Поиск по индексированному массиву @param[indexed]
         * Предварительно необходимо проиндексировать данные @param[data]
         * В данном примере индексация происходит в функции @param[indexArray]
         * */
        if (indexed.isEmpty()) indexed = indexArray(data) // Если индекс пуст, то проиндексировать
        val indexedValue = indexed[value.toLowerCase()] ?: listOf<Long>()
        return data.filterIndexed { index, _ -> indexedValue.contains(index.toLong()) }
    }

    private fun indexArray(array: List<String>): MutableMap<String, MutableList<Long>> {
        var idx = 0L
        var indexedArray = mutableMapOf<String, MutableList<Long>>()
        array.map {
            it.split(" ").drop(1).map { el ->
                val i = el.toLowerCase()
                if (indexedArray.containsKey(i)) {
                    indexedArray[i]?.add(idx)
                } else indexedArray.put(i, mutableListOf(idx))
            }
            idx++
        }
        return indexedArray
    }
}

