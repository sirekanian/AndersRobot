package com.sirekanyan.andersrobot.repository

import java.sql.DriverManager
import java.sql.ResultSet

private const val CREATE_TABLE = """
    create table if not exists cities (
        chat bigint,
        name varchar(200),
        primary key (chat, name)
    );
    """
private const val SELECT_CITIES =
    "select name from cities where chat = ?"
private const val INSERT_CITY =
    "insert into cities (chat, name) values (?, ?) on conflict do nothing"
private const val DELETE_CITY =
    "delete from cities where chat = ? and name = ?"

interface CityRepository {
    fun getCities(chat: Long): List<String>
    fun putCity(chat: Long, name: String): Boolean
    fun deleteCity(chat: Long, name: String): Boolean
}

class CityRepositoryImpl(url: String) : CityRepository {

    private val connection by lazy {
        DriverManager.getConnection(url).also {
            it.createStatement().execute(CREATE_TABLE)
        }
    }

    override fun getCities(chat: Long): List<String> =
        connection.prepareStatement(SELECT_CITIES)
            .run {
                setLong(1, chat)
                executeQuery()
            }
            .map {
                getString(1)
            }

    override fun putCity(chat: Long, name: String): Boolean =
        connection.prepareStatement(INSERT_CITY)
            .run {
                setLong(1, chat)
                setString(2, name)
                executeUpdate() == 1
            }

    override fun deleteCity(chat: Long, name: String): Boolean =
        connection.prepareStatement(DELETE_CITY)
            .run {
                setLong(1, chat)
                setString(2, name)
                executeUpdate() == 1
            }

    private fun <T> ResultSet.map(transform: ResultSet.() -> T): List<T> {
        val list = mutableListOf<T>()
        while (next()) {
            list.add(transform())
        }
        return list
    }

}