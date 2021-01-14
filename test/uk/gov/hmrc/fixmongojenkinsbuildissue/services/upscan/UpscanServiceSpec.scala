/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.fixmongojenkinsbuildissue.services.upscan

import akka.util.Timeout
import cats.data.EitherT
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.test.Helpers.await
import uk.gov.hmrc.fixmongojenkinsbuildissue.models.Error
import uk.gov.hmrc.fixmongojenkinsbuildissue.models.Generators.{sample, _}
import uk.gov.hmrc.fixmongojenkinsbuildissue.models.upscan.{UploadReference, UpscanUpload}
import uk.gov.hmrc.fixmongojenkinsbuildissue.repositories.upscan.UpscanRepository

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

class UpscanServiceSpec extends AnyWordSpec with Matchers with MockFactory {

  implicit val timeout: Timeout                           = Timeout(FiniteDuration(5, TimeUnit.SECONDS))
  implicit val executionContext: ExecutionContextExecutor = ExecutionContext.global
  val mockUpscanRepository                                = mock[UpscanRepository]
  val service                                             = new UpscanServiceImpl(mockUpscanRepository)

  def mockStoreUpscanUpload(upscanUpload: UpscanUpload)(
    response: Either[Error, Unit]
  ) =
    (mockUpscanRepository
      .insert(_: UpscanUpload))
      .expects(upscanUpload)
      .returning(EitherT[Future, Error, Unit](Future.successful(response)))

  def mockReadUpscanUpload(uploadReference: UploadReference)(
    response: Either[Error, Option[UpscanUpload]]
  ) =
    (mockUpscanRepository
      .select(_: UploadReference))
      .expects(uploadReference)
      .returning(EitherT[Future, Error, Option[UpscanUpload]](Future.successful(response)))

  def mockReadUpscanUploads(uploadReferences: List[UploadReference])(
    response: Either[Error, List[UpscanUpload]]
  ) =
    (mockUpscanRepository
      .selectAll(_: List[UploadReference]))
      .expects(uploadReferences)
      .returning(EitherT[Future, Error, List[UpscanUpload]](Future.successful(response)))

  def mockUpdateUpscanUpload(
    uploadReference: UploadReference,
    upscanUpload: UpscanUpload
  )(
    response: Either[Error, Unit]
  ) =
    (mockUpscanRepository
      .update(_: UploadReference, _: UpscanUpload))
      .expects(uploadReference, upscanUpload)
      .returning(EitherT[Future, Error, Unit](Future.successful(response)))

  val upscanUpload = sample[UpscanUpload]

  "Upscan Service" when {

    "it receives a request to store an upscan upload" must {
      "return an error" when {
        "there is a mongo exception" in {
          mockStoreUpscanUpload(upscanUpload)(Left(Error("Connection error")))
          await(service.storeUpscanUpload(upscanUpload).value).isLeft shouldBe true
        }
      }
      "return unit" when {
        "it successfully stores the data" in {
          mockStoreUpscanUpload(upscanUpload)(Right(()))
          await(service.storeUpscanUpload(upscanUpload).value) shouldBe Right(())
        }
      }
    }

    "it receives a request to read a upscan upload" must {
      "return an error" when {
        "there is a mongo exception" in {
          mockReadUpscanUpload(upscanUpload.uploadReference)(Left(Error("Connection error")))
          await(service.readUpscanUpload(upscanUpload.uploadReference).value).isLeft shouldBe true
        }
      }
      "return some upscan upload" when {
        "it successfully reads the data" in {
          mockReadUpscanUpload(upscanUpload.uploadReference)(Right(Some(upscanUpload)))
          await(service.readUpscanUpload(upscanUpload.uploadReference).value) shouldBe Right(Some(upscanUpload))
        }
      }
    }

    "it receives a request to read a upscan uploads" must {
      "return an error" when {
        "there is a mongo exception" in {
          mockReadUpscanUploads(List(upscanUpload.uploadReference))(Left(Error("Connection error")))
          await(service.readUpscanUploads(List(upscanUpload.uploadReference)).value).isLeft shouldBe true
        }
      }
      "return some upscan upload" when {
        "it successfully reads the data" in {
          mockReadUpscanUploads(List(upscanUpload.uploadReference))(Right(List(upscanUpload)))
          await(service.readUpscanUploads(List(upscanUpload.uploadReference)).value) shouldBe Right(List(upscanUpload))
        }
      }
    }

    "it receives a request to update an upscan upload" must {
      "return an error" when {
        "there is a mongo exception" in {
          mockUpdateUpscanUpload(upscanUpload.uploadReference, upscanUpload)(Left(Error("Connection error")))
          await(service.updateUpscanUpload(upscanUpload.uploadReference, upscanUpload).value).isLeft shouldBe true
        }
      }
      "return some upscan upload" when {
        "it successfully stores the data" in {
          mockUpdateUpscanUpload(upscanUpload.uploadReference, upscanUpload)(Right(()))
          await(service.updateUpscanUpload(upscanUpload.uploadReference, upscanUpload).value) shouldBe Right(())
        }
      }
    }
  }
}
