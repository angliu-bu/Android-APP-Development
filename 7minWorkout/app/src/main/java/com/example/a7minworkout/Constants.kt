package com.example.a7minworkout

object Constants {
    fun defaultExcerciseList(): ArrayList<ExerciseModel>{
        val excerciseList = ArrayList<ExerciseModel>()
        val stretching = ExerciseModel(
            1 , "Stretching" ,
            R.drawable.stretchingg ,
            false , false
        )
        excerciseList.add(stretching)
        val pushups = ExerciseModel(
            2 , "PushUps" ,
            R.drawable.pushups ,
            false , false
        )
        excerciseList.add(pushups)
        val hnumnpushups = ExerciseModel(
            3 , "Hanuman PushUps" ,
            R.drawable.hanuman_pushup ,
            false , false
        )
        excerciseList.add(hnumnpushups)
        val planks  = ExerciseModel(
           4  , "Planks" ,
            R.drawable.planks ,
            false , false
        )
        excerciseList.add(planks)
        val sidePlanks = ExerciseModel(
            5 , "Side Planks" ,
            R.drawable.side_plank ,
            false , false
        )
        excerciseList.add(sidePlanks)
        val crunches = ExerciseModel(
            6 , "Crunches" ,
            R.drawable.crunches ,
            false , false
        )
        excerciseList.add(crunches)
        val tricepDips = ExerciseModel(
            7 , "Triceps Dips" ,
            R.drawable.triceps_dips_on_chair ,
            false , false
        )
        excerciseList.add(tricepDips)
        val squats = ExerciseModel(
            8 , "Squats" ,
            R.drawable.squats ,
            false , false
        )
        excerciseList.add(squats)
        val lunges = ExerciseModel(
            9 , "Lunges" ,
            R.drawable.lunges_exercise ,
            false , false
        )
        excerciseList.add(lunges)
        val burpees = ExerciseModel(
            10 , "Burpees" ,
            R.drawable.burpees ,
            false , false
        )
        excerciseList.add(burpees)
        val jumpingJacks = ExerciseModel(
            11 , "Jumping Jacks" ,
            R.drawable.jumpingjack ,
            false , false
        )
        excerciseList.add(jumpingJacks)

        return excerciseList
    }
}