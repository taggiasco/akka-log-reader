package ch.taggiasco


/**
 * Some utility functions
 */

package object util {
  
  implicit class With[A](a: A) {
    private def withOption[X](f: => Option[X])(g: (A, X) => A) = f.map(g(a, _)).getOrElse(a)
  }
  
}