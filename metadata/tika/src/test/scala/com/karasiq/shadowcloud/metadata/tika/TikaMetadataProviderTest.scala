package com.karasiq.shadowcloud.metadata.tika

import java.io.File

import scala.concurrent.duration._
import scala.language.postfixOps

import akka.stream.scaladsl.{FileIO, Keep}
import akka.stream.testkit.scaladsl.TestSink
import akka.util.ByteString
import org.apache.commons.io.FileUtils
import org.apache.tika.Tika
import org.scalatest.FlatSpecLike

import com.karasiq.shadowcloud.metadata.Metadata
import com.karasiq.shadowcloud.test.utils.{ActorSpec, ActorSpecImplicits}

class TikaMetadataProviderTest extends ActorSpec with ActorSpecImplicits with FlatSpecLike {
  val testPdfName = "TypeClasses.pdf"
  val testPdfFile = new File(getClass.getClassLoader.getResource(testPdfName).toURI)
  val testPdfBytes = ByteString(FileUtils.readFileToByteArray(testPdfFile))

  val tika = new Tika()
  val detector = TikaMimeDetector(tika)

  "Mime detector" should "detect PDF" in {
    detector.getMimeType("TypeClasses.pdf", testPdfBytes) shouldBe Some("application/pdf")
  }

  val autoParserConfig = system.settings.config.getConfig("shadowcloud.metadata.tika.auto-parser")
  val autoParser = TikaAutoParser(tika, autoParserConfig)

  "Parser" should "extract text" in {
    val output = FileIO.fromPath(testPdfFile.toPath)
      .via(autoParser.parseMetadata(testPdfName, "application/pdf"))
      .toMat(TestSink.probe)(Keep.right)
      .run()

    val metaTable = output.requestNext()
    metaTable.tag shouldBe Some(Metadata.Tag("tika", "auto", Metadata.Tag.Disposition.METADATA))
    println(metaTable)
    assert(metaTable.value.table.exists(_.values("dcterms:created").values == Seq("2010-07-26T09:01:12Z")))

    val textPreview = output.requestNext()
    textPreview.tag shouldBe Some(Metadata.Tag("tika", "auto", Metadata.Tag.Disposition.PREVIEW))
    textPreview.value.text.exists(t ⇒ t.format == "text/plain" && t.data.contains("Type Classes as Objects and Implicits")) shouldBe true

    val text = output.requestNext(1 minute)
    text.tag shouldBe Some(Metadata.Tag("tika", "auto", Metadata.Tag.Disposition.CONTENT))
    text.value.text.exists(t ⇒ t.format == "text/plain" && t.data.contains("Type Classes as Objects and Implicits")) shouldBe true

    val xml = output.requestNext()
    xml.tag shouldBe Some(Metadata.Tag("tika", "auto", Metadata.Tag.Disposition.CONTENT))
    xml.value.text.exists(t ⇒ t.format == "text/html" && t.data.contains("<p>Adriaan Moors Martin Odersky\nEPFL\n</p>")) shouldBe true

    output.request(1)
    output.expectComplete()
  }
}
