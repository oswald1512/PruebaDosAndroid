package sv.edu.udb.desafio2android

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.widget.Toolbar
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.google.android.material.floatingactionbutton. FloatingActionButton
import com.google.firebase.database.*
import sv.edu.udb.desafio2android.datos.Persona


class MainActivity : AppCompatActivity() {
    var consultaOrdenada: Query = refPersonas.orderByChild("nombre")
    var personas: MutableList<Persona>? = null
    lateinit var listaPersonas: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inicializar()
        // Configura el Toolbar como ActionBar
        val toolbar: Toolbar =
            findViewById(R.id.toolbar)  // Asegúrate de que el ID del Toolbar sea correcto
        setSupportActionBar(toolbar)  // Configura el Toolbar como ActionBar de la actividad
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true  // Retorna true para indicar que el menú ha sido inflado correctamente
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
                return true  // Asegúrate de devolver true aquí para indicar que el ítem fue manejado
            }
        }
        return super.onOptionsItemSelected(item)  // Si no se maneja el ítem, llamamos al super
    }


    private fun inicializar() {
        val fab_agregar: FloatingActionButton = findViewById(R.id.fab_agregar)
        listaPersonas = findViewById(R.id.ListaPersonas)
        listaPersonas.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val intent = Intent(getBaseContext(), AddPersonaActivity::class.java)
                intent.putExtra("accion", "e") // Editar
                intent.putExtra("key", personas!![i].key)
                intent.putExtra("nombre", personas!![i].nombre)
                intent.putExtra("apellido", personas!![i].apellido)
                intent.putExtra("grado", personas!![i].grado)
                intent.putExtra("materia", personas!![i].materia)
                intent.putExtra("nota", personas!![i].nota)
                startActivity(intent)
            }
        })


        listaPersonas.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                l: Long
            ): Boolean {
                val ad = AlertDialog.Builder(this@MainActivity)
                ad.setMessage ("Está seguro de eliminar registro?")
                        .setTitle("Confirmación")
                ad.setPositiveButton("Si"
                ) { dialog, id ->
                    personas!![position].key?.let {
                    refPersonas.child(it).removeValue()
                }
                Toast.makeText(
                    this@MainActivity,
                    "Registro borrado!", Toast.LENGTH_SHORT
                ).show()
                    }
                ad.setNegativeButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        Toast.makeText(
                            this@MainActivity,
                            "Operación de borrado cancelada!", Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                ad.show()
                return true

            }
        }
        fab_agregar.setOnClickListener{
            val i = Intent(getBaseContext(), AddPersonaActivity::class.java)
            i.putExtra("accion", "a")
            i.putExtra("key", "")
            i.putExtra("nombre", "")
            i.putExtra("apellido", "")
            i.putExtra("grado", "")
            i.putExtra("materia", "")
            i.putExtra("nota", "")
            startActivity(i)
        }
        personas = ArrayList<Persona>()


    consultaOrdenada.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            personas!!.clear()
            for (dato in dataSnapshot.children) {
            val persona: Persona? = dato.getValue(Persona::class.java)
            persona?.key = dato.key
            if (persona != null) {
                personas!!.add(persona)
            }
        }
        val adapter = AdaptadorPersona(
            this@MainActivity,
            personas as ArrayList<Persona>
        )
        listaPersonas!!.adapter = adapter
    }
    override fun onCancelled(databaseError: DatabaseError) {}
    })
}

    companion object {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refPersonas: DatabaseReference = database.getReference("personas")
    }
}


