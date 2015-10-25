package com.geirsson.buddy

object MinCostMaxFlow {

  val SmallConstant = -100000000
  def maxCostBipartiteMatching(adjMatrix: Map[(Int, Int), Int]) = {
    val N: Int = adjMatrix.keys.foldLeft(-1) {
      case (big, (x, y)) => math.max(big, math.max(x, y))
    }
    val M = Vector.tabulate(N, N) {
      case (x, y) =>
        adjMatrix.get(x -> y).map(x => -x).getOrElse {
          if (x > y) 0 else SmallConstant
        }
    }
  }

}
