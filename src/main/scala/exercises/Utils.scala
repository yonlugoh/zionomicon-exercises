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

  def collectAll[R, E, A](
                           in: Iterable[ZIO[R, E, A]]
                         ): ZIO[R, E, List[A]] =
    ZIO {
      r =>
      def go(effects: List[ZIO[R, E, A]], acc: List[A]): Either[E, List[A]] =
        effects match {
          case Nil  => Right(acc)
          case x::xs => x.run(r).flatMap(h => go(xs, h :: acc))
        }

      go(in.toList, Nil)
    }


}
