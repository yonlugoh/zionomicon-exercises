import app.HelloWorld.greet
import exercises.ZIOUtils.goShopping
import zio._
import zio.duration._
import zio.test.Assertion._
import zio.test._
import zio.test.environment._
import zio.test.TestAspect._

object ExampleSpec extends DefaultRunnableSpec {
  def spec = suite("ExampleSpec")(
    test("addition works") {
      assert(1 + 1)(equalTo(2))
    },
    test("subtraction works") {
      assert(2 - 1)(equalTo(1))
    },
    testM("ZIO.succeed succeeds with specified value") {
      assertM(ZIO.succeed(1 + 1))(equalTo(2))
    },
    testM("ZIO.succeed succeeds with greater than") {
      assertM(ZIO.succeed(2 * 2))(isGreaterThan(3))
    },
    testM("testing an effect using map operator") {
      ZIO.succeed(1 + 1).map(n => assert(n)(equalTo(2)))
    },
    testM("testing an effect using a for comprehension") {
      for {
        n <- ZIO.succeed(1 + 1)
      } yield assert(n)(equalTo(2))
    },
    testM("and") {
      for {
        x <- ZIO.succeed(1)
        y <- ZIO.succeed(2)
      } yield assert(x)(equalTo(1)) &&
        assert(y)(equalTo(2))
    },
    test("hasSameElement") {
      assert(List(1, 1, 2, 3))(hasSameElements(List(3, 2, 1, 1)))
    },
    testM("fails") {
      for {
        exit <- ZIO.effect(1 / 0).catchAll(_ => ZIO.fail(())).run
      } yield assert(exit)(fails(isUnit))
    },
    testM("fails with assertM") {
      assertM(ZIO.effect(1 / 0).catchAll(_ => ZIO.fail(())).run)(fails(isUnit))
    },
    testM("greet says hello to the user") {
      for {
        _ <- TestConsole.feedLines("Jane")
        _ <- greet
        value <- TestConsole.output
      } yield assert(value)(equalTo(Vector("Hello, Jane!\n")))
    },
    testM("goShopping delays for one hour") {
      for {
        fiber <- goShopping.fork
        _ <- TestClock.adjust(1.hour)
        _ <- fiber.join
      } yield assertCompletes
    }
  )
}