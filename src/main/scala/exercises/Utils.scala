package exercises

object Utils {
  final case class ZIO[-R, +E, +A](run: R => Either[E, A])
  def zipWith[R, E, A, B, C](
                              self: ZIO[R, E, A],
                              that: ZIO[R, E, B]
                            )(f: (A, B) => C): ZIO[R, E, C] =
    ZIO {
      r =>
      val fa = self.run(r)
      val fb = that.run(r)
      for {
      a <- fa
      b <- fb
      } yield f(a, b)
    }
}
