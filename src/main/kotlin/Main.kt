fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    val a = 186430471117411L
    val b = 670237748696851L
    println(extented_euclid(a, b))
    println(executeDay13Part2())
}