package sv.edu.udb.desafio2android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    // Creamos la referencia del objeto FirebaseAuth
    private lateinit var auth: FirebaseAuth
// Referencia a componentes de nuestro layout
    private lateinit var btnLogin: Button
    private lateinit var textViewRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView (R.layout.activity_login)
    // Inicializamos el objeto FirebaseAuth
        auth = FirebaseAuth.getInstance()


        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val email = findViewById<EditText>(R.id.txtEmailAddress).text.toString()
            val password = findViewById<EditText>(R.id.txtPassword).text.toString()
            this.login(email, password)
        }
        textViewRegister = findViewById(R.id.textViewRegister)
        textViewRegister.setOnClickListener {
            this.goToRegister()
        }
    }


    private fun login (email: String, password: String)
    { auth.signInWithEmailAndPassword (email, password) .addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }.addOnFailureListener { exception ->
        Toast.makeText(
            applicationContext,
            exception.localizedMessage,
            Toast.LENGTH_LONG
        ).show()
    }
    }


    private fun goToRegister() {
        val intent = Intent( this, RegisterActivity::class.java)
        startActivity(intent)
    }


}