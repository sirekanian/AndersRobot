package com.sirekanyan.andersrobot.repository.table

import org.jetbrains.exposed.sql.Table

object Cities : Table() {

    val chat = long("chat")
    val city = long("city")

    override val primaryKey: PrimaryKey =
        PrimaryKey(chat, city)

}