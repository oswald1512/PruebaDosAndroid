package sv.edu.udb.desafio2android.datos


class Persona {
    fun key(key: String?) {
    }
    var nombre: String? = null
    var apellido: String? = null
    var grado: String? = null
    var materia: String? = null
    var nota: String? = null
    var key: String? = null
    var per: MutableMap<String, Boolean> = HashMap()

    constructor() {}

    constructor(nombre: String?, apellido: String?,grado: String?,materia: String?,nota: String?) {
        this.nombre = nombre
        this.apellido = apellido
        this.grado = grado
        this.materia = materia
        this.nota = nota
    }
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nombre" to nombre,
            "apellido" to apellido,
            "grado" to grado,
            "materia" to materia,
            "nota" to nota,
            "key" to key,
            "per" to per
        )
    }
}