package net.stouma915.hydrogenchairs.syntax

trait NullCheckSyntax {

  implicit class AnyRefOps[A <: AnyRef](anyRef: A) {

    def isNull: Boolean = anyRef == null

    def nonNull: Boolean = !isNull

  }

}
