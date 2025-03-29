package sv.edu.udb.desafio2android

import android.app.Activity import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import sv.edu.udb.desafio2android.datos.Persona

class AdaptadorPersona(private val context: Activity, var personas: List<Persona>) : ArrayAdapter<Persona?>(context,
    R.layout.activity_main, personas) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val layoutInflater = context.layoutInflater
        var rowview: View? = null

        rowview = view ?: layoutInflater.inflate(R.layout.persona_layout, null)

        val tvNombre = rowview!!.findViewById<TextView>(R.id.tvNombre)
        val tvApellido = rowview!!.findViewById<TextView>(R.id.tvApellido)
        val tvGrado = rowview!!.findViewById<TextView>(R.id.tvGrado)
        val tvMateria = rowview!!.findViewById<TextView>(R.id.tvMateria)
        val tvNota = rowview!!.findViewById<TextView>(R.id.tvNota)
        tvNombre.text = "Nombre : " + personas[position].nombre
        tvApellido.text = "Apellido : " + personas[position].apellido
        tvGrado.text = "Grado : " + personas[position].grado
        tvMateria.text = "Materia : " + personas[position].materia
        tvNota.text = "Nota : " + personas[position].nota
        return rowview
    }
}