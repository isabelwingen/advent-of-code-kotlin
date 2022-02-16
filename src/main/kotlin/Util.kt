fun getResourceAsText(path: String): String? =
    object {}.javaClass.getResource(path)?.readText()

fun getResourceAsList(path: String): Collection<String> =
    getResourceAsText(path)!!
        .split("\n")
