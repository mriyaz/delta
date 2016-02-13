package io.flow.delta.api.lib

import io.flow.common.v0.models.Name
import org.specs2.mutable._

class SemverSpec extends Specification {

  "Semver.parse" in {
    Semver.parse("foo") must be(None)
    Semver.parse("0") must be(None)
    Semver.parse("0.1") must be(None)
    Semver.parse("0.1.2") must beEqualTo(Some(Semver(0,1,2)))
  }

  "Semver.isSemver" in {
    Semver.isSemver("foo") must beFalse
    Semver.isSemver("0") must beFalse
    Semver.isSemver("0.1") must beFalse
    Semver.isSemver("0.1.2") must beTrue
  }

  "next" in {
    Semver(0, 1, 2).next must beEqualTo(Semver(0, 1, 3))
  }

  "label" in {
    Semver(0, 1, 2).label must beEqualTo("0.1.2")
  }


}