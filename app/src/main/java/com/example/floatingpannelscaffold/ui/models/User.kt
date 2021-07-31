package com.example.floatingpannelscaffold.ui.models

import androidx.annotation.DrawableRes
import com.example.floatingpannelscaffold.R
import java.util.*

data class User(
  val id: String = UUID.randomUUID().toString(),
  @DrawableRes val image: Int,
  val fullName: String,
  val address: String
)

var DefaultUsers = listOf(
  User(image = R.drawable.user_preview, fullName = "Aleksandr Lytvynchuk", address = "Ukraine, Dnipro, Heroiv 10"),
  User(image = R.drawable.user_preview, fullName = "Aleksandr Lytvynchuk", address = "Ukraine, Dnipro, Heroiv 10"),
  User(image = R.drawable.user_preview, fullName = "Aleksandr Lytvynchuk", address = "Ukraine, Dnipro, Heroiv 10"),
  User(image = R.drawable.user_preview, fullName = "Aleksandr Lytvynchuk", address = "Ukraine, Dnipro, Heroiv 10"),
  User(image = R.drawable.user_preview, fullName = "Aleksandr Lytvynchuk", address = "Ukraine, Dnipro, Heroiv 10"),
  User(image = R.drawable.user_preview, fullName = "Aleksandr Lytvynchuk", address = "Ukraine, Dnipro, Heroiv 10"),
  User(image = R.drawable.user_preview, fullName = "Aleksandr Lytvynchuk", address = "Ukraine, Dnipro, Heroiv 10"),
  User(image = R.drawable.user_preview, fullName = "Aleksandr Lytvynchuk", address = "Ukraine, Dnipro, Heroiv 10"),
  User(image = R.drawable.user_preview, fullName = "Aleksandr Lytvynchuk", address = "Ukraine, Dnipro, Heroiv 10"),
  User(image = R.drawable.user_preview, fullName = "Aleksandr Lytvynchuk", address = "Ukraine, Dnipro, Heroiv 10"),
  User(image = R.drawable.user_preview, fullName = "Aleksandr Lytvynchuk", address = "Ukraine, Dnipro, Heroiv 10"),
  User(image = R.drawable.user_preview, fullName = "Aleksandr Lytvynchuk", address = "Ukraine, Dnipro, Heroiv 10"),
  User(image = R.drawable.user_preview, fullName = "Aleksandr Lytvynchuk", address = "Ukraine, Dnipro, Heroiv 10"),
  User(image = R.drawable.user_preview, fullName = "Aleksandr Lytvynchuk", address = "Ukraine, Dnipro, Heroiv 10"),
  User(image = R.drawable.user_preview, fullName = "Aleksandr Lytvynchuk", address = "Ukraine, Dnipro, Heroiv 10"),
  User(image = R.drawable.user_preview, fullName = "Aleksandr Lytvynchuk", address = "Ukraine, Dnipro, Heroiv 10"),
)
