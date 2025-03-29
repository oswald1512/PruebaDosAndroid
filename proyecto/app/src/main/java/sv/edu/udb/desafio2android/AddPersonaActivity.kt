package sv.edu.udb.desafio2android

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import sv.edu.udb.desafio2android.datos.Persona

class AddPersonaActivity : AppCompatActivity() {

    private lateinit var txtNombre: EditText
    private lateinit var txtApellido: EditText
    private lateinit var txtGrado: Spinner
    private lateinit var txtMateria: Spinner
    private lateinit var txtNota: EditText
    private var key = ""
    private var accion = ""
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_persona)
        inicializar()
    }

    private fun inicializar() {
        txtNombre = findViewById(R.id.txtNombre)
        txtApellido = findViewById(R.id.txtApellido)
        txtGrado = findViewById(R.id.txtGrado)
        txtMateria = findViewById(R.id.txtMateria)
        txtNota = findViewById(R.id.txtNota)

        val grados = listOf("1° Grado", "2° Grado", "3° Grado", "4° Grado", "5° Grado", "6° Grado")
        val materias = listOf("Matemáticas", "Ciencias", "Lenguaje", "Estudios Sociales")

        txtGrado.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, grados)
        txtMateria.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, materias)

        val datos = intent.extras
        datos?.let {
            key = it.getString("key", "")
            txtNombre.setText(it.getString("nombre", ""))
            txtApellido.setText(it.getString("apellido", ""))
            txtNota.setText(it.getString("nota", ""))
            accion = it.getString("accion", "")

            val gradoRecibido = it.getString("grado", "")
            val materiaRecibida = it.getString("materia", "")
            txtGrado.setSelection(grados.indexOf(gradoRecibido).takeIf { it >= 0 } ?: 0)
            txtMateria.setSelection(materias.indexOf(materiaRecibida).takeIf { it >= 0 } ?: 0)
        }
    }

    fun guardar(v: View?) {
        val nombre = txtNombre.text.toString().trim()
        val apellido = txtApellido.text.toString().trim()
        val grado = txtGrado.selectedItem.toString()
        val materia = txtMateria.selectedItem.toString()
        val notaStr = txtNota.text.toString().trim()

        if (nombre.isEmpty() || apellido.isEmpty() || notaStr.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val nota = notaStr.toDoubleOrNull()
        if (nota == null || nota < 0 || nota > 10) {
            Toast.makeText(this, "La nota debe estar entre 0 y 10", Toast.LENGTH_SHORT).show()
            return
        }

        database = FirebaseDatabase.getInstance().getReference("personas")
        val persona = Persona(nombre, apellido, grado, materia, notaStr)

        if (accion == "a") {
            val newKey = database.push().key
            newKey?.let {
                database.child(it).setValue(persona).addOnSuccessListener {
                    Toast.makeText(this, "Se guardó con éxito", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            } ?: Toast.makeText(this, "No se pudo generar una clave", Toast.LENGTH_SHORT).show()
        } else if (accion == "e" && key.isNotEmpty()) {
            database.child(key).setValue(persona).addOnSuccessListener {
                Toast.makeText(this, "Se actualizó con éxito", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No se encontró la clave del registro", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    fun cancelar(v: View?) {
        finish()
    }
}
