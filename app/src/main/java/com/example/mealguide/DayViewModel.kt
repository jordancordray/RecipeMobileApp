package com.example.mealguide

import androidx.lifecycle.ViewModel

class DayViewModel : ViewModel() {
    var days = listOf<DayCardItem>(
        DayCardItem("Monday", "monday", null),
        DayCardItem("Tuesday", "tuesday", null),
        DayCardItem("Wednesday", "wednesday", null),
        DayCardItem("Thursday", "thursday", null),
        DayCardItem("Friday", "friday", null),
        DayCardItem("Saturday", "saturday", null),
        DayCardItem("Sunday", "sunday", null)
    )

}