package org.thp.misp.dto

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Tag(
    id: Option[String],
    name: String,
    colour: Option[Int],
    exportable: Option[Boolean]
)

object Tag {
  implicit val reads: Reads[Tag] =
    ((JsPath \ "id").readNullable[String] and
      (JsPath \ "name").read[String] and
      (JsPath \ "colour").readNullable[String].map {
        case Some(c) if c.headOption.contains('#') => Some(Integer.parseUnsignedInt(c.tail, 16))
        case _                                     => None
      } and
      (JsPath \ "exportable").readNullable[Boolean])(Tag.apply _)

  implicit val writes: Writes[Tag] = Writes[Tag] {
    case Tag(Some(id), name, colour, _) => Json.obj("id" -> id, "name" -> name, "colour" -> colour.map(c => f"#$c%06X"))
    case Tag(_, name, _, _)             => JsString(name)
  }
}