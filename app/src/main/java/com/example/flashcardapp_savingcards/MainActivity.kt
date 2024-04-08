package com.example.flashcardapp_savingcards

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit
import kotlin.math.max

class MainActivity : AppCompatActivity() {
    var currentCardDisplayedIndex = 0
    lateinit var flashcardDatabase: FlashcardDatabase
    private var allFlashcards = mutableListOf<Flashcard>()
    var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flashcardDatabase = FlashcardDatabase(this)
        flashcardDatabase.initFirstCard()
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        val isShowingAnswers = findViewById<ImageView>(R.id.toggle123)
        val flashcard_question = findViewById<TextView>(R.id.flashcard_question)
        val flashcard_reponse = findViewById<TextView>(R.id.flashcard_reponse)
        val flashcard_reponse1 = findViewById<TextView>(R.id.flashcard_reponse1)
        val flashcard_reponse2 = findViewById<TextView>(R.id.flashcard_reponse2)
        val editButton = findViewById<ImageView>(R.id.edit_bouton)
        val next_btn = findViewById<ImageView>(R.id.next_button)
        val viewKonfetti = findViewById<KonfettiView>(R.id.konfettiView)
        val rmv_btn = findViewById<ImageView>(R.id.delete_bouton)
        val leftOutAnim = AnimationUtils.loadAnimation(this, R.anim.left_out)
        val rightInAnim = AnimationUtils.loadAnimation(this, R.anim.right_in)


        countDownTimer = object : CountDownTimer(16000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                findViewById<TextView>(R.id.miniteur).text = "Duration\n"+ millisUntilFinished / 1000
            }

            override fun onFinish() {}
        }
        startTimer()

        fun showConfetti() {
            var party= Party(
                speed = 0f,
                maxSpeed = 30f,
                damping = 0.9f,
                spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
                position = Position.Relative(0.5, 0.3)
            )
            viewKonfetti.start(party)
        }




        leftOutAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Ce code est exécuté lorsque l'animation démarre
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Ce code est exécuté lorsque l'animation est terminée
                findViewById<View>(R.id.flashcard_question).startAnimation(rightInAnim)
                findViewById<View>(R.id.flashcard_reponse).startAnimation(rightInAnim)
                findViewById<View>(R.id.flashcard_reponse1).startAnimation(rightInAnim)
                findViewById<View>(R.id.flashcard_reponse2).startAnimation(rightInAnim)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Nous n'avons pas besoin de cette méthode
            }
        })

        flashcard_reponse.setOnClickListener {
            if ((flashcard_reponse.text).isNotEmpty()) {
                if (flashcard_reponse.text == allFlashcards[currentCardDisplayedIndex].answer) {
                    // Bonne réponse
                    flashcard_reponse.setBackgroundColor(getResources().getColor(R.color.green))
                    // Déclencher l'animation de célébration ici
                    showConfetti()


                    // passons au question suivant
                    Handler().postDelayed({
                        flashcard_reponse.setBackgroundColor(getResources().getColor(R.color.Rose)) // Remettre la couleur de fond par défaut
                        next_btn.performClick()
                    },500)
                } else {
                    // Mauvaise réponse
                    flashcard_reponse.setBackgroundColor(getResources().getColor(R.color.red))
                }
                // Désactiver les autres réponses
                flashcard_reponse1.isEnabled = false
                flashcard_reponse2.isEnabled = false

                // Utilisation d'un Handler pour rétablir les valeurs par défaut après un délai de 2 secondes
                Handler().postDelayed({
                    // Rétablir les valeurs par défaut après 2 secondes
                    flashcard_reponse.setBackgroundColor(getResources().getColor(R.color.Rose)) // Remettre la couleur de fond par défaut
                    flashcard_reponse1.isEnabled = true // Activer à nouveau les autres réponses
                    flashcard_reponse2.isEnabled = true
                }, 1000) // Délai de 2000 ms (2 secondes)

            }
        }


        flashcard_reponse1.setOnClickListener {
            if ((flashcard_reponse1.text).isNotEmpty()) {
                if (flashcard_reponse1.text == allFlashcards[currentCardDisplayedIndex].answer) {
                    // Bonne réponse
                    flashcard_reponse1.setBackgroundColor(getResources().getColor(R.color.green))
                    // Déclencher l'animation de célébration ici
                    showConfetti()


                    // passons au question suivant
                    Handler().postDelayed({
                        flashcard_reponse1.setBackgroundColor(getResources().getColor(R.color.Rose)) // Remettre la couleur de fond par défaut
                        next_btn.performClick()
                    },500)
                } else {
                    // Mauvaise réponse
                    flashcard_reponse1.setBackgroundColor(getResources().getColor(R.color.red))
                }
                // Désactiver les autres réponses
                flashcard_reponse.isEnabled = false
                flashcard_reponse2.isEnabled = false

                // Utilisation d'un Handler pour rétablir les valeurs par défaut après un délai de 2 secondes
                Handler().postDelayed({
                    // Rétablir les valeurs par défaut après 2 secondes
                    flashcard_reponse1.setBackgroundColor(getResources().getColor(R.color.Rose)) // Remettre la couleur de fond par défaut
                    flashcard_reponse.isEnabled = true // Activer à nouveau les autres réponses
                    flashcard_reponse2.isEnabled = true
                }, 1000) // Délai de 2000 ms (2 secondes)
            }

        }


        flashcard_reponse2.setOnClickListener {
            if ((flashcard_reponse2.text).isNotEmpty()) {
                if (flashcard_reponse2.text == allFlashcards[currentCardDisplayedIndex].answer) {
                    // Bonne réponse
                    flashcard_reponse2.setBackgroundColor(getResources().getColor(R.color.green))
                    // Déclencher l'animation de célébration ici
                    showConfetti()


                    // passons au question suivant
                    Handler().postDelayed({
                        flashcard_reponse2.setBackgroundColor(getResources().getColor(R.color.Rose)) // Remettre la couleur de fond par défaut
                        next_btn.performClick()
                    },500)
                } else {
                    // Mauvaise réponse
                    flashcard_reponse2.setBackgroundColor(getResources().getColor(R.color.red))
                }
                // Désactiver les autres réponses
                flashcard_reponse.isEnabled = false
                flashcard_reponse1.isEnabled = false

                // Utilisation d'un Handler pour rétablir les valeurs par défaut après un délai de 2 secondes
                Handler().postDelayed({
                    // Rétablir les valeurs par défaut après 2 secondes
                    flashcard_reponse2.setBackgroundColor(getResources().getColor(R.color.Rose)) // Remettre la couleur de fond par défaut
                    flashcard_reponse.isEnabled = true // Activer à nouveau les autres réponses
                    flashcard_reponse1.isEnabled = true
                }, 1000) // Délai de 2000 ms (2 secondes)
            }
        }

        next_btn.setOnClickListener {5
            if (allFlashcards.isEmpty()) {
                return@setOnClickListener  // Il n'y a pas de cartes à afficher
            }

            currentCardDisplayedIndex++

            if (currentCardDisplayedIndex >= allFlashcards.size) {
                currentCardDisplayedIndex = 0  // Revenir à la première carte si nous avons atteint la fin
            }

            val (question, answer,wrongAnswer1,wrongAnswer2) = allFlashcards[currentCardDisplayedIndex]

            // Mettre à jour les TextViews avec la nouvelle carte
            flashcard_question.text = question
            flashcard_reponse.text = answer
            flashcard_reponse1.text = wrongAnswer1
            flashcard_reponse2.text = wrongAnswer2

            flashcard_question.startAnimation(leftOutAnim)
            flashcard_reponse.startAnimation(leftOutAnim)
            flashcard_reponse1.startAnimation(leftOutAnim)
            flashcard_reponse2.startAnimation(leftOutAnim)

            startTimer()
        }


        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            val extras = data?.extras

            if (extras != null) { // Check that we have data returned
                val question = extras.getString("question")
                val answer = extras.getString("answer")
                val wrongAnswer1 = extras.getString("wrongAnswer1")
                val wrongAnswer2 = extras.getString("wrongAnswer2")

                // Log the value of the strings for easier debugging
                Log.i("MainActivity", "question: $question")
                Log.i("MainActivity", "answer: $answer")
                Log.i("MainActivity", "wrongAnswer1: $wrongAnswer1")
                Log.i("MainActivity", "wrongAnswer2: $wrongAnswer2")

                // Display newly created flashcard
                flashcard_question.text = question
                flashcard_reponse.text = answer
                flashcard_reponse1.text = wrongAnswer1
                flashcard_reponse2.text = wrongAnswer2

                // Save newly created flashcard to database
                if (question != null && answer != null && wrongAnswer1 != null && wrongAnswer2 != null) {
                    flashcardDatabase.insertCard(
                        Flashcard(
                            question,
                            answer,
                            wrongAnswer1,
                            wrongAnswer2
                        )
                    )
                    // Update set of flashcards to include new card
                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                } else {
                    Log.e(
                        "TAG",
                        "Missing question or answer to input into database. Question is $question and answer is $answer and wrongAnswer1 is $wrongAnswer1 and wrongAnswer2 is $wrongAnswer2"
                    )
                }
            } else {
                Log.i("MainActivity", "Returned null data from AddCardActivity")
            }
        }

        rmv_btn.setOnClickListener {
            val currentQuestion = flashcard_question.text.toString()
            flashcardDatabase.deleteCard(currentQuestion)

            // Mettre à jour la liste des flashcards
            allFlashcards = flashcardDatabase.getAllCards().toMutableList()

            // Vérifier s'il reste des cartes
            if (allFlashcards.isNotEmpty()) {
                // Afficher la carte précédente (si disponible)
                currentCardDisplayedIndex = max(0, currentCardDisplayedIndex - 1)
                val (question, answer,wrongAnswer1,wrongAnswer2) = allFlashcards[currentCardDisplayedIndex]
                flashcard_question.text = question
                flashcard_reponse.text = answer
                flashcard_reponse1.text = wrongAnswer1
                flashcard_reponse2.text = wrongAnswer2
            } else {
                // S'il n'y a plus de cartes, afficher un état vide
                flashcard_question.text = ""
                flashcard_reponse.text = ""
                flashcard_reponse1.text = ""
                flashcard_reponse2.text = ""
            }
        }

        if (allFlashcards.size > 0) {
            flashcard_question.text = allFlashcards[0].question
            flashcard_reponse.text = allFlashcards[0].answer
            flashcard_reponse1.text = allFlashcards[0].wrongAnswer1
            flashcard_reponse2.text = allFlashcards[0].wrongAnswer2
        }



        editButton.setOnClickListener {
            val question = flashcard_question.text.toString()
            val answer = flashcard_reponse.text.toString()
            val wrongAnswer1 = flashcard_reponse1.text.toString()
            val wrongAnswer2 = flashcard_reponse2.text.toString()


            intent.putExtra("question", question)
            intent.putExtra("answer", answer)
            intent.putExtra("wrongAnswer1", wrongAnswer1)
            intent.putExtra("wrongAnswer2", wrongAnswer2)
            resultLauncher.launch(intent)
            val i = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(i)
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }

        // Lancer MainActivity en attente d'un résultat
        isShowingAnswers.setOnClickListener {
            val i = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(i)
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }




    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer?.start()
    }

}