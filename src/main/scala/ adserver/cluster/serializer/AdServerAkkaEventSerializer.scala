package adserver.cluster.serializer

import akka.serialization.SerializerWithStringManifest
import scodec._
import scodec.codecs._
import scodec.bits._
import scodec.codecs.implicits._
import org.slf4j.LoggerFactory

class AdServerAkkaEventSerializer extends SerializerWithStringManifest {
  import AdServerAkkaEventSerializer._

  override def identifier: Int = 314324

  override def manifest(o: AnyRef): String = o.getClass.getName

  override def toBinary(o: AnyRef): Array[Byte] =  o match {
    case e: Event => Codec.encode(e).require.toByteArray
    case le: LeaderEvent => Codec.encode(le).require.toByteArray
    case pre: PixalateReloadEvent => Codec.encode(pre).require.toByteArray
    case se: SnapshotEvent => Codec.encode(se).require.toByteArray
    case ie: ImdbEvent => Codec.encode(ie).require.toByteArray
    case dp: DataPoint => Codec.encode(dp).require.toByteArray
    case treq: TagRequest => Codec.encode(treq).require.toByteArray
    case tresp: TagResponse => Codec.encode(tresp).require.toByteArray
    case tresCustom: TagResponseCustom => Codec.encode(tresCustom).require.toByteArray
    case bfe: BidActorFlush => Codec.encode(bfe).require.toByteArray
    case x => throw new Exception(s"unexpected  object $x received which is ${x.getClass} type")
  }

  override def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = manifest match {
    case EventManifest => Codec[Event].decode(BitVector.apply(bytes)).require.value
    case LeaderEventManifest => Codec[LeaderEvent].decode(BitVector.apply(bytes)).require.value
    case SnapshotEventManifest => Codec[SnapshotEvent].decode(BitVector.apply(bytes)).require.value
    case PixalateEventManifest => Codec[PixalateReloadEvent].decode(BitVector.apply(bytes)).require.value
    case ImdbEventManifest => Codec[ImdbEvent].decode(BitVector.apply(bytes)).require.value
    case DataPointManifest => Codec[DataPoint].decode(BitVector.apply(bytes)).require.value
    case TagRequestEventManifest => Codec[TagRequest].decode(BitVector.apply(bytes)).require.value
    case TagResponseEventManifest => Codec[TagResponse].decode(BitVector.apply(bytes)).require.value
    case TagResponseCustomEventManifest => Codec[TagResponseCustom].decode(BitVector.apply(bytes)).require.value
    case BidActorFlushEventManifest => Codec[BidActorFlush].decode(BitVector.apply(bytes)).require.value
    case x => throw new Exception(s"unexpected manifest $x in AdServerAkkaEventSerializer.fromBinary ")
  }

}

object AdServerAkkaEventSerializer {
  final val EventManifest = classOf[Event].getName
  final val LeaderEventManifest = classOf[LeaderEvent].getName
  final val SnapshotEventManifest = classOf[SnapshotEvent].getName
  final val PixalateEventManifest = classOf[PixalateReloadEvent].getName
  final val ImdbEventManifest = classOf[ImdbEvent].getName
  final val DataPointManifest = classOf[DataPoint].getName
  final val TagRequestEventManifest = classOf[TagRequest].getName
  final val TagResponseEventManifest = classOf[TagResponse].getName
  final val TagResponseCustomEventManifest = classOf[TagResponseCustom].getName
  final val BidActorFlushEventManifest = classOf[BidActorFlush].getName

  val  logger = LoggerFactory.getLogger(AdServerAkkaEventSerializer.getClass)

  val strCodec: Codec[String] = variableSizeBytes(uint16, utf8)

  implicit val mapCodec: Codec[Map[String, String]] =  listOfN(int32, strCodec ~ strCodec).xmap(_.toMap, _.toList)
}