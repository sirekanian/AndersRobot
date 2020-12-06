package com.sirekanyan.andersrobot.repository

import java.sql.DriverManager
import java.sql.ResultSet

private const val CREATE_TABLE = """
    create table if not exists cities (
        chat bigint,
        city bigint,
        primary key (chat, city)
    );
    """
private const val SELECT_CITIES =
    "select city from cities where chat = ?"
private const val INSERT_CITY =
    "insert into cities (chat, city) values (?, ?) on conflict do nothing"
private const val DELETE_CITY =
    "delete from cities where chat = ? and city = ?"

interface CityRepository {
    fun getCities(chat: Long): List<Long>
    fun putCity(chat: Long, city: Long): Boolean
    fun deleteCity(chat: Long, city: Long): Boolean
}

class CityRepositoryImpl(url: String) : CityRepository {

    private val connection by lazy {
        DriverManager.getConnection(url).also {
            it.createStatement().execute(CREATE_TABLE)
        }
    }

    override fun getCities(chat: Long): List<Long> =
        connection.prepareStatement(SELECT_CITIES)
            .run {
                setLong(1, chat)
                executeQuery()
            }
            .map {
                getLong(1)
            }

    override fun putCity(chat: Long, city: Long): Boolean =
        connection.prepareStatement(INSERT_CITY)
            .run {
                setLong(1, chat)
                setLong(2, city)
                executeUpdate() == 1
            }

    override fun deleteCity(chat: Long, city: Long): Boolean =
        connection.prepareStatement(DELETE_CITY)
            .run {
                setLong(1, chat)
                setLong(2, city)
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