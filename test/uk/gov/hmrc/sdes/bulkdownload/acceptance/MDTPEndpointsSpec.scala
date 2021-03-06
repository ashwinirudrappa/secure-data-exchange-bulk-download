/*
 * Copyright 2019 HM Revenue & Customs
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

package uk.gov.hmrc.sdes.bulkdownload.acceptance

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}

import scala.io.Source

class MDTPEndpointsSpec extends PlaySpec with GuiceOneAppPerSuite {

  override implicit lazy val app: Application = GuiceApplicationBuilder()
    .configure("microservice.services.service-locator.enabled" -> false,
      "auditing.enabled" -> false)
    .build()

  import app.materializer

  "API definition endpoint" should {
    "respond with definition.json" in {
      val req = FakeRequest(Helpers.GET, "/api/definition")

      val Some(result) = route(app, req)

      status(result) mustBe OK
      contentAsString(result) mustBe readResourceFile("/public/api/definition.json")
    }
  }

  "RAML documentation endpoint" should {
    "respond with requested raml documentation" in {
      val req = FakeRequest(Helpers.GET, "/api/conf/1.0/application.raml")

      val Some(result) = route(app, req)

      status(result) mustBe OK
      contentAsString(result) mustBe readResourceFile("/public/api/conf/1.0/application.raml")
    }
  }

  private def readResourceFile(resourceName: String): String = {
    val is = getClass.getResourceAsStream(resourceName)
    Source.fromInputStream(is).mkString
  }

}
