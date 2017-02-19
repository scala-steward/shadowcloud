package com.karasiq.shadowcloud.config.utils

import akka.util.ByteString
import com.karasiq.shadowcloud.index.Chunk

import scala.language.postfixOps

trait ChunkKeyExtractor {
  def apply(chunk: Chunk): ByteString
}

object ChunkKeyExtractor {
  val hash: ChunkKeyExtractor = (chunk: Chunk) ⇒ chunk.checksum.hash
  val encryptedHash: ChunkKeyExtractor = (chunk: Chunk) ⇒ chunk.checksum.encryptedHash
  val doubleHash: ChunkKeyExtractor = (chunk: Chunk) ⇒ chunk.checksum.hash ++ chunk.checksum.encryptedHash

  def fromString(str: String): ChunkKeyExtractor = str match {
    case "hash" ⇒
      hash

    case "encrypted-hash" ⇒
      encryptedHash

    case "double-hash" ⇒
      doubleHash

    case _ ⇒
      Class.forName(str)
        .newInstance()
        .asInstanceOf[ChunkKeyExtractor]
  }
}