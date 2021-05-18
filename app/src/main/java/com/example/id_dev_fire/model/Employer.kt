package com.example.id_dev_fire.model

class Employer (

    val id : String = "",
    val firstName : String = "",
    val lastName : String = "",
    val email : String = "",
    val phone : Long = 0,
    val gender : String = "",
    val role : String = "",
    val project : String = ""
) {
    fun getEmployerId() : String {
        return id
    }
    fun getEmployerFirstName() : String {
        return firstName
    }

    fun getEmployerLastName() : String {
        return lastName
    }

    fun getEmployerEmail() : String {
        return email
    }

    fun getEmployerPhone() : Long {
        return phone
    }

    fun getEmployerGender() : String {
        return gender
    }

    fun getEmployerRole() : String {
        return role
    }


    fun getEmployerProject() : String {
        return project
    }
}