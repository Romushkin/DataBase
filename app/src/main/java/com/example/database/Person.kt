package com.example.database

class Person(val name: String, val role: String, val phone: String) {
    override fun toString(): String {
        return "$name, $role, $phone"
    }
}