import arrow.aql.instances.list.groupBy.groupBy
import arrow.aql.instances.list.select.*
import arrow.aql.instances.list.where.*
import arrow.aql.instances.listk.select.select
import arrow.aql.instances.listk.select.selectAll
import arrow.aql.instances.id.select.value


data class Student(val name: String, val age: Int)

val john = Student("John", 30)
val jane = Student("Jane", 32)
val jack = Student("Jack", 32)


fun main(args: Array<String>) {
    val result1: List<Int> =
            listOf(1, 2, 3).query {
                select { this + 1 }
            }.value()
    println(result1)

    val result2: List<String> =
            listOf(john, jane, jack).query {
                select { name } where { age > 20 }
            }.value()

    println(result2)


    val result3 =
            listOf(john, jane, jack).query {
                selectAll() where { age > 30 } groupBy { age }
            }.value()
    println(result3)


}