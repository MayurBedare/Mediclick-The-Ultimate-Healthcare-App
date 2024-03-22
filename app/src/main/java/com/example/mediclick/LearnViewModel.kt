package com.example.mediclick

import androidx.lifecycle.ViewModel

class LearnViewModel : ViewModel() {
    var videos = ArrayList<Videos>()

    fun fillData() {
        val video1: Videos = Videos("https://firebasestorage.googleapis.com/v0/b/mediclick-416105.appspot.com/o/meditation1.mp4?alt=media&token=bb1abf13-297d-4a10-8aab-0d8fd132ba13\n","How To Meditate For Beginners (Animated)","How To Meditate For Beginners! In this video, I'm going to tell you, where to meditate, how to meditate, how to stop thinking, how long to meditate for, even how long before you start seeing the benefits. ", "05:36", R.drawable.meditate1)
        val video2: Videos = Videos("https://firebasestorage.googleapis.com/v0/b/mediclick-416105.appspot.com/o/meditation2.mp4?alt=media&token=e27987f6-ab8a-4d9a-b9f5-294e51dd9709\n","Alan Watts - Guided Meditation (Awakening The Mind)","This is a 14-minute Alan Watts guided meditation video discussing his method of establishing a meditative state and reaching self-awareness.\n", "14.45", R.drawable.meditation2)
        val video3: Videos = Videos("https://firebasestorage.googleapis.com/v0/b/mediclick-416105.appspot.com/o/meditation3.mp4?alt=media&token=9a79eae2-f61c-410b-97e5-e5b0a98ffc71","Meditation For Children (Calming activity)", "Meditation is a powerful practice. Our children today live in a world so full of constant stimulation and entertainment. Learning to sit still and breathe can help kids to calm themselves when they feel stressed or anxious.","06:13", R.drawable.meditation11)
        val video4: Videos = Videos("https://firebasestorage.googleapis.com/v0/b/mediclick-416105.appspot.com/o/meditation4.mp4?alt=media&token=6f61407c-bd6c-46e6-9ef1-9ca944c85ccd","Meditation: A Beginner's Guide", "Are you new to meditation, and interested in finding out how to start a practice? We'll walk you through the basics! \n","02:00", R.drawable.meditation13)
        val video5: Videos = Videos("https://firebasestorage.googleapis.com/v0/b/mediclick-416105.appspot.com/o/meditation5.mp4?alt=media&token=b4a038e8-f24a-40b2-b596-eb3b7e3bcc4e","The Art of Meditation (Animated video)", "Meditation is probably a bit more complicated than most people think.\n" +
                "\n" +
                "When we meditate we watch our thoughts, while focusing our attention on a certain anchor that keeps us in the present, for example, the breath. While watching the breath we observe how our thoughts come and go, along with our feelings and emotions. And when we get dragged into them, we bring back our attention to our anchor.","06:37", R.drawable.meditation15)
        val video6: Videos = Videos("https://firebasestorage.googleapis.com/v0/b/mediclick-416105.appspot.com/o/meditation6.mp4?alt=media&token=782ad4dc-495e-4c9a-ac21-c35f1db66926","Animation movie on Transcendental Meditation", "Idea, Script and voiceover: Moti Shefi\n" +
                "Animation: Ravid Sandlerman\n" +
                "Mr Jones: Roee Berger\n" +
                "Original Music: Ofra Avni and Yitzhak Yona\n" +
                "Sound effects : freesfx.co.uk","07:46", R.drawable.meditation16)
        videos.add(video1)
        videos.add(video2)
        videos.add(video3)
        videos.add(video4)
        videos.add(video5)
        videos.add(video6)
    }

    fun getData(): ArrayList<Videos> {
        fillData()
        return videos
    }

}