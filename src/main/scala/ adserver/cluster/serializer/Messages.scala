package  adserver.cluster.serializer

case class ImdbEvent(msg:String)

case class SnapshotEvent(id:Int)

case class PixalateReloadEvent()

case class LeaderEvent(b:Boolean)
case class DataPoint(ts:Long, name:String, value:Double)
case class Event(etype: Int, cp: Option[Double], userId: Option[String], buyerId: Option[String] = None)

case class TagRequest(impId: String, seatId: String, bidId: String)
case class TagResponse(content: String, contentType: String)
case class TagResponseCustom(code: Int, msg: String, headers: Map[String,String] = Map.empty)
case class BidActorFlush()
