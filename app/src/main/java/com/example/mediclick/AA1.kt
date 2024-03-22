package com.example.mediclick

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import com.airbnb.lottie.LottieAnimationView

@Suppress("NAME_SHADOWING", "UNREACHABLE_CODE")
open class AA1 : FeatureActivity() {

    private lateinit var progressBar: LottieAnimationView
    private lateinit var anim: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aa1)

        val muscleSpinner: Spinner = findViewById(R.id.exerciseTypeSpinner)
        val exerciseSpinner: Spinner = findViewById(R.id.targetedMuscleSpinner)
        val b: LinearLayout = findViewById(R.id.era1)
        progressBar = findViewById(R.id.progressBar)
        anim = findViewById(R.id.breatheView)

        // Handle the click action here, navigate to Page0Activity
        b.setOnClickListener {

            val selectedMuscle = muscleSpinner.selectedItem.toString()
            val selectedExercise = exerciseSpinner.selectedItem.toString()


            if (selectedMuscle.isEmpty() || selectedExercise.isEmpty()) {
                showToast("Please select a muscle and an exercise.")
                progressBar.visibility = View.INVISIBLE
                muscleSpinner.visibility = View.VISIBLE
                exerciseSpinner.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // Start the Lottie animation
            progressBar.playAnimation()
            progressBar.visibility = View.VISIBLE
            muscleSpinner.visibility = View.INVISIBLE
            exerciseSpinner.visibility = View.INVISIBLE
            anim.visibility = View.INVISIBLE

            val intent = Intent(this, AA2::class.java)
            intent.putExtra("muscleN", muscleSpinner.selectedItem.toString())
            intent.putExtra("exerciseN", exerciseSpinner.selectedItem.toString())
            startActivity(intent)

        }

        // Define the mapping of muscle names to exercise names
        val muscleToExercisesMap = mapOf(
            "Abdominals" to arrayOf("Landmine twist", "Elbow plank", "Bottoms Up", "Suspended ab fall-out", "Dumbbell V-Sit Cross Jab", "Standing cable low-to-high twist", "Dumbbell spell caster", "Decline reverse crunch", "Spider crawl", "Cocoons"),
            "Abductors" to arrayOf("Hip Circles (Prone)", "Standing Hip Circles", "Clam", "Iliotibial band SMR", "Thigh abductor", "Fire Hydrant", "Windmills", "Monster Walk", "IT Band and Glute Stretch", "Single-leg lying cross-over stretch"),
            "Adductors" to arrayOf("Groiners", "Band Hip Adductions", "Side Leg Raises", "Lateral hop", "Groin and Back Stretch", "Adductor SMR", "Side Lying Groin Stretch"),
            "Biceps" to arrayOf("Incline Hammer Curls", "Wide-grip barbell curl", "EZ-bar spider curl", "Hammer Curls", "EZ-Bar Curl", "Zottman Curl", "Biceps curl to shoulder press", "Barbell Curl", "Concentration curl", "Flexor Incline Dumbbell Curls"),
            "Calves" to arrayOf("Smith Machine Calf Raise", "Standing Calf Raises", "Seated Calf Raise", "Calf Press On The Leg Press Machine", "Rocking Standing Calf Raise", "Calf Press", "Standing barbell calf raise", "Barbell Seated Calf Raise", "Balance Board", "Weighted donkey calf raise"),
            "Chest" to arrayOf("Dumbbell Bench Press", "Pushups", "Close-grip bench press", "Dumbbell Flyes", "Incline dumbbell bench press", "Low-cable cross-over", "Barbell Bench Press - Medium Grip", "Chest dip", "Decline Dumbbell Flyes", "Bodyweight Flyes"),
            "Forearms" to arrayOf("Rickshaw Carry", "Palms-down wrist curl over bench", "Straight-bar wrist roll-up", "Dumbbell farmer's walk", "Palms-up wrist curl over bench", "Standing behind-the-back wrist curl", "Seated finger curl", "Seated Two-Arm Palms-Up Low-Pulley Wrist Curl", "Wrist Roller", "Seated One-Arm Dumbbell Palms-Up Wrist Curl"),
            "Glutes" to arrayOf("Barbell glute bridge", "Barbell Hip Thrust", "Single-leg cable hip extension", "Glute bridge", "Single-leg glute bridge", "Step-up with knee raise", "Kettlebell thruster", "Kneeling Squat", "Flutter Kicks", "Glute Kickback"),
            "Hamstrings" to arrayOf("Barbell Deadlift", "Romanian Deadlift With Dumbbells", "Clean Deadlift", "Sumo deadlift", "Romanian Deadlift from Deficit", "Power Snatch", "Power Clean from Blocks", "Natural Glute Ham Raise", "Glute ham raise-", "Snatch Deadlift"),
            "Lats" to arrayOf("Weighted pull-up", "Pullups", "Rocky Pull-Ups/Pulldowns", "Close-grip pull-down", "Muscle Up", "Shotgun row", "Close-Grip Front Lat Pulldown", "V-bar pull-up", "Rope climb"),
            "Lower Back" to arrayOf("Atlas Stones", "Barbell deficit deadlift", "Back extension", "Axle Deadlift", "Hyperextensions With No Hyperextension Bench", "Deadlift with Bands", "Deadlift with Chains", "Rack Pull with Bands", "Superman"),
            "Middle_back" to arrayOf("T-Bar Row with Handle", "Reverse-grip bent-over row", "One-Arm Dumbbell Row", "One-Arm Long Bar Row", "T-Bar Row", "Bent Over Two-Arm Long Bar Row", "Alternating sit-through with crunch", "Rower", "Seated Cable Rows", "Incline dumbbell row"),
            "Neck" to arrayOf("Lying Face Down Plate Neck Resistance", "Lying Face Up Plate Neck Resistance", "Seated Head Harness Neck Resistance", "Isometric Neck Exercise - Sides", "Isometric Neck Exercise - Front And Back", "Neck Bridge Prone", "Side Neck Stretch", "Chin To Chest Stretch", "Neck-SMR"),
            "Quadriceps" to arrayOf("Single-Leg Press", "Clean from Blocks", "Barbell Full Squat", "Tire flip", "Barbell back squat to box", "Push-press", "Power snatch-", "Hang Clean", "Reverse Band Box Squat", "Jumping rope"),
            "Traps" to arrayOf("Smith machine shrug", "Leverage Shrug", "Standing dumbbell shrug", "Standing dumbbell upright row", "Kettlebell sumo deadlift high pull", "Calf-Machine Shoulder Shrug", "Kettlebell sumo deadlift high pull", "Calf-Machine Shoulder Shrug", "Barbell shrug", "Barbell behind-the-back shrug"),
            "Triceps" to arrayOf("Triceps dip", "Decline EZ-bar skullcrusher", "Dumbbell floor press", "Cable V-bar push-down", "Weighted bench dip", "EZ-Bar Skullcrusher", "Reverse Grip Triceps Pushdown", "Push-Ups - Close Triceps Position", "Kneeling cable triceps extension", "Single-arm cable triceps extension")
        )

        // Set up the adapter for the muscle spinnerCalf Pres
        val muscleAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.targeted_muscles,
            android.R.layout.simple_spinner_item
        )
        muscleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        muscleSpinner.adapter = muscleAdapter

        // Set up the adapter for the exercise spinner
        val defaultExerciseAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.default_array,
            android.R.layout.simple_spinner_item
        )
        defaultExerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        exerciseSpinner.adapter = defaultExerciseAdapter

        // Set a listener for the muscle spinner
        muscleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Update the exercise spinner based on the selected muscle
                val selectedMuscle = muscleSpinner.selectedItem.toString()
                val exercisesForMuscle = muscleToExercisesMap[selectedMuscle]
                val exerciseAdapter = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    exercisesForMuscle ?: emptyArray()
                )
                exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                exerciseSpinner.adapter = exerciseAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        val backButton: ImageView = findViewById(R.id.imageNext)

        backButton.setOnClickListener {
            super.onBackPressed()
            val intent = Intent(this, FeatureActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, FeatureActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

}
