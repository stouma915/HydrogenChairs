package net.stouma915.hydrogenchairs.listener

import cats.effect.IO
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.stouma915.hydrogenchairs.HydrogenChairs
import net.stouma915.hydrogenchairs.implicits.*
import net.stouma915.hydrogenchairs.util.Util
import org.bukkit.Bukkit
import org.bukkit.block.data.Bisected
import org.bukkit.block.data.`type`.Stairs
import org.bukkit.entity.{ArmorStand, EntityType}
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.EquipmentSlot

import scala.util.chaining.*

class PlayerInteractListener extends Listener {

  import cats.effect.unsafe.implicits.global

  @EventHandler
  def onPlayerInteract(event: PlayerInteractEvent): Unit = {
    val nullChecks = Set(
      event.getHand.nonNull,
      event.getClickedBlock.nonNull,
      event.getPlayer.nonNull
    )
    if (!nullChecks.forall(x => x))
      return ()

    if (event.getHand != EquipmentSlot.HAND)
      return ()
    if (event.getAction != Action.RIGHT_CLICK_BLOCK)
      return ()
    if (!event.getClickedBlock.getBlockData.isInstanceOf[Stairs])
      return ()
    if (event.getPlayer.isSneaking)
      return ()

    if (!event.getPlayer.hasPermission("hydrogenchairs.use")) {
      val program = for {
        _ <- IO {
          event.getPlayer.sendActionBar(
            Component
              .text("You do not have permission.")
              .color(TextColor.color(255, 85, 85))
          )
        }
      } yield ()

      return program.unsafeRunSync()
    }

    val stairs = event.getClickedBlock.getBlockData.asInstanceOf[Stairs]
    if (stairs.getHalf != Bisected.Half.BOTTOM)
      return ()

    val stairsLocation = event.getClickedBlock.getLocation
    val playerLocation = event.getPlayer.getLocation

    val differenceX =
      math.abs(stairsLocation.getBlockX - playerLocation.getBlockX)
    val differenceY =
      math.abs(stairsLocation.getBlockY - playerLocation.getBlockY)
    val differenceZ =
      math.abs(stairsLocation.getBlockZ - playerLocation.getBlockZ)

    if ((differenceX + differenceZ) >= 3 || differenceY >= 3) {
      val program = for {
        _ <- IO {
          event.getPlayer.sendActionBar(
            Component
              .text("Chair is too far.")
              .color(TextColor.color(255, 85, 85))
          )
        }
      } yield ()

      return program.unsafeRunSync()
    }

    val spawnStandAndRide = for {
      standLocation <- IO {
        stairsLocation.clone().add(0.5d, 0.3d, 0.5d)
      }
      _ <- IO {
        Bukkit.getScheduler.scheduleSyncDelayedTask(
          HydrogenChairs.getInstance.unsafeRunSync(),
          (() => {
            val stand = event.getClickedBlock.getWorld
              .spawnEntity(standLocation, EntityType.ARMOR_STAND)
              .asInstanceOf[ArmorStand]

            val yaw = Util.getOptimalYaw(stairs)

            stand.setRotation(yaw, 0.0f)

            val newPlayerLocation = event.getPlayer.getLocation.clone()
            newPlayerLocation.setYaw(yaw)
            newPlayerLocation.setPitch(0.0f)

            event.getPlayer.teleport(newPlayerLocation)

            stand
              .tap(_.setInvulnerable(true))
              .tap(_.setSmall(true))
              .tap(_.setGravity(false))
              .tap(_.setMarker(true))
              .tap(_.setBasePlate(false))
              .tap(_.setPersistent(false))

            stand.addPassenger(event.getPlayer)

            HydrogenChairs.stands += event.getPlayer.getUniqueId -> stand
          }): Runnable
        )
      }
    } yield ()

    spawnStandAndRide.unsafeRunSync()
  }

}
