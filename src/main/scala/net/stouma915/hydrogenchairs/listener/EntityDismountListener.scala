package net.stouma915.hydrogenchairs.listener

import cats.effect.IO
import net.stouma915.hydrogenchairs.HydrogenChairs
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.{EventHandler, Listener}
import org.spigotmc.event.entity.EntityDismountEvent

final class EntityDismountListener extends Listener {

  import cats.effect.unsafe.implicits.global

  @EventHandler
  def onPlayerDismountFromChair(event: EntityDismountEvent): Unit = {
    event.getEntity match {
      case player: Player
          if HydrogenChairs.stands.contains(player.getUniqueId) =>
        val program = IO {
          Bukkit.getScheduler.scheduleSyncDelayedTask(
            HydrogenChairs.getInstance.unsafeRunSync(),
            (() => {
              val previousLocation =
                HydrogenChairs.previousLocation(player.getUniqueId)
              player.teleport(previousLocation)

              val stand = HydrogenChairs.stands(player.getUniqueId)
              stand.remove()

              HydrogenChairs.stands =
                HydrogenChairs.stands.removed(player.getUniqueId)
              HydrogenChairs.previousLocation =
                HydrogenChairs.previousLocation.removed(player.getUniqueId)
            }): Runnable
          )
        }

        program.unsafeRunSync()
      case _ =>
    }
  }

}
