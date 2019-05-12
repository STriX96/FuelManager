package com.example.fuelmanager.data

class FillUps (
    var uid: String,
    var regNum: String,
    var traveledKm: Double,
    var amountOfLiter: Double,
    var price: Double,
    var sum: Double,
    var average: Double,
    var date: String,
    var imageURL: String,
    var thiskey: String
)
{
    constructor(): this("","",0.0,0.0,0.0,0.0,0.0,"","","")
}