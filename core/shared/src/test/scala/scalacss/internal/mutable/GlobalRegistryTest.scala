package scalacss.internal.mutable

import japgolly.microlibs.testutil.TestUtil._
import scalacss.defaults.DefaultSettings.Dev._
import scalaz.Equal
import scalaz.std.option.optionEqual
import scalaz.std.vector.vectorEqual
import utest._

class BlarrrrStyle extends StyleSheet.Inline {
  import dsl._
  val s = style(borderCollapse.collapse)
}

object GlobalRegistryTest extends TestSuite {

  val gr = new StyleSheetRegistry()

  class S1 extends StyleSheet.Inline
  class S2 extends StyleSheet.Inline

  val s1 = new S1
  val blar = new BlarrrrStyle
  implicit val eqS1: Equal[S1]                = Equal.equalRef
  implicit val eqBL: Equal[BlarrrrStyle]      = Equal.equalRef
  implicit val eqSS: Equal[StyleSheet.Inline] = Equal.equalRef

  var postreg = Vector.empty[StyleSheet.Inline]
  gr.register(s1)
  gr.onRegistration(postreg :+= _)
  gr.register(blar)

  override def tests = Tests {
    "get" - {
      assertEq(gr[S1], Some(s1))
      assertEq(gr[BlarrrrStyle], Some(blar))
    }
    "missing" - {
      assertEq(gr[S2].isEmpty, true)
    }
    "onRegistration" - {
      assertEq(postreg, Vector(s1, blar))
    }
  }
}
