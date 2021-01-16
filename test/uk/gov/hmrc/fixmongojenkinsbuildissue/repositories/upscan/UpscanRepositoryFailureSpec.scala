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

package uk.gov.hmrc.fixmongojenkinsbuildissue.repositories.upscan

import com.typesafe.config.ConfigFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.Configuration
import play.api.test.Helpers._
import uk.gov.hmrc.fixmongojenkinsbuildissue.models.Generators.{sample, _}
import uk.gov.hmrc.fixmongojenkinsbuildissue.models.upscan.{UploadReference, UpscanUpload}
import uk.gov.hmrc.fixmongojenkinsbuildissue.repositories.MongoTestSupport

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{FiniteDuration, SECONDS}

class UpscanRepositoryFailureSpec extends AnyWordSpec with Matchers with MongoTestSupport {
  val config = Configuration(
    ConfigFactory.parseString(
      """
        | mongodb.upscan.expiry-time = 7days
        |""".stripMargin
    )
  )

  val repository: DefaultUpscanRepository = new DefaultUpscanRepository(reactiveMongoComponent, config)

  override def beforeAll(): Unit = {
    super.beforeAll()
   // Thread.sleep(10000) //allow indexing to complete
    reactiveMongoComponent.mongoConnector.helper.driver.close(FiniteDuration(10, SECONDS))
  }

  "Upscan Repository" when {
    "inserting" should {
      "return an error if there is a failure" in {
        val upscanUpload = sample[UpscanUpload]
        await(repository.insert(upscanUpload).value).isLeft shouldBe true
      }
    }

    "updating an upscan upload document" should {
      "return an error if there is a failure" in {
        await(
          repository
            .select(sample[UploadReference])
            .value
        ).isLeft shouldBe true
      }
    }

    "selecting an upscan upload document" should {
      "return an error if there is a failure" in {
        await(
          repository
            .select(sample[UploadReference])
            .value
        ).isLeft shouldBe true
      }
    }

    "selecting all upscan upload documents" should {
      "return an error if there is a failure" in {
        await(
          repository
            .selectAll(List(sample[UploadReference]))
            .value
        ).isLeft shouldBe true
      }
    }
  }

}
