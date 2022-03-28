package net.stouma915.hydrogenchairs.util

import org.bukkit.block.BlockFace
import org.bukkit.block.data.`type`.Stairs

object Util {

  def getOptimalYaw(stairs: Stairs): Float = {
    import BlockFace.*
    import Stairs.Shape.*

    stairs.getShape match {
      case STRAIGHT =>
        stairs.getFacing match {
          case NORTH => 0.0f
          case EAST  => 90.0f
          case SOUTH => 180.0f
          case WEST  => -90.0f
          case _     => 0.0f
        }
      case INNER_LEFT | OUTER_LEFT =>
        stairs.getFacing match {
          case NORTH => -45.5f
          case SOUTH => 135.5f
          case WEST  => -135.5f
          case EAST  => 45.5f
          case _     => 0.0f
        }
      case INNER_RIGHT | OUTER_RIGHT =>
        stairs.getFacing match {
          case NORTH => 45.5f
          case SOUTH => -135.5f
          case WEST  => -45.5f
          case EAST  => 135.5f
          case _     => 0.0f
        }
    }
  }

}
