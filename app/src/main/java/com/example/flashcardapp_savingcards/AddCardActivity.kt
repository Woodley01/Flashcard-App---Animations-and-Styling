package com.example.flashcardapp_savingcards

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        val editTextField =findViewById<EditText>(R.id.editTextField)
        val editTextField1 =findViewById<EditText>(R.id.editTextField1)
        val editTextField2 =findViewById<EditText>(R.id.editTextField2)
        val editTextField3 =findViewById<EditText>(R.id.editTextField3)
        val ShowingAnswers = findViewById<ImageView>(R.id.icone_X)
        val save_btn = findViewById<ImageView>(R.id.icone_save)

        ShowingAnswers.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(R.anim.left_out, R.anim.right_in)
        }

        val question = intent.getStringExtra("question")
        val answer = intent.getStringExtra("answer")
        val wrongAnswer1 = intent.getStringExtra("wrongAnswer1")
        val wrongAnswer2 = intent.getStringExtra("wrongAnswer2")

        // Mettre à jour les EditText avec les données existantes
        editTextField.setText(question)
        editTextField1.setText(answer)
        editTextField2.setText(wrongAnswer1)
        editTextField3.setText(wrongAnswer2)



        save_btn.setOnClickListener {
            val question = editTextField.text.toString()
            val answer = editTextField1.text.toString()
            val wrongAnswer1 = editTextField2.text.toString()
            val wrongAnswer2 = editTextField3.text.toString()



            if (question.isBlank() || answer.isBlank() || wrongAnswer1.isBlank() || wrongAnswer2.isBlank()) {
                // Afficher un message d'erreur avec Snackbar si l'un des champs est vide
                Snackbar.make(findViewById(R.id.icone_save), "Veuillez remplir tous les champs", Snackbar.LENGTH_SHORT).show()
            } else {
                // Les champs sont remplis, continuer avec la sauvegarde des données
                Snackbar.make(findViewById(R.id.icone_save), "Card succesful Created", Snackbar.LENGTH_SHORT).show()
                val data = Intent()
                data.putExtra("question", question)
                data.putExtra("answer", answer)
                data.putExtra("wrongAnswer1", wrongAnswer1)
                data.putExtra("wrongAnswer2", wrongAnswer2)
                setResult(Activity.RESULT_OK, data)
                finish()

            }
        }
    }
}