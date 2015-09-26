package models

import com.geirsson.util.Epoch

/**
 * Entity class storing rows of table User
 *  @param id Database column id SqlType(INTEGER), AutoInc, PrimaryKey
 *  @param name Database column name SqlType(VARCHAR), Length(32,true)
 *  @param createdAt Database column createdAt SqlType(TIMESTAMP)
 */
case class UserRow(id: Int, name: String, createdAt: Epoch)
