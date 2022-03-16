package net.stouma915.hydrogenchairs.listener

import cats.effect.IO
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.block.data.`type`.Stairs
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.EquipmentSlot

class PlayerInteractListener extends Listener {

  import cats.effect.unsafe.implicits.global

  @EventHandler
  def onPlayerInteract(event: PlayerInteractEvent): Unit = {
    if (event.getHand != EquipmentSlot.HAND)
      return ()
    if (!event.getClickedBlock.getBlockData.isInstanceOf[Stairs])
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
  }

}
