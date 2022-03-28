package net.stouma915.hydrogenchairs

import cats.effect.IO
import net.stouma915.hydrogenchairs.listener.*
import org.bukkit.entity.ArmorStand
import org.bukkit.plugin.java.JavaPlugin

import java.util.UUID
import java.util.logging.Level

object HydrogenChairs {

  private var instance: HydrogenChairs = _

  private[hydrogenchairs] var stands: Map[UUID, ArmorStand] = Map()

  def getInstance: IO[HydrogenChairs] = IO(instance)

}

final class HydrogenChairs extends JavaPlugin {

  import cats.effect.unsafe.implicits.global

  HydrogenChairs.instance = this

  private val registerListeners = IO {
    Seq(
      new PlayerInteractListener
    ).foreach(getServer.getPluginManager.registerEvents(_, this))
  }

  override def onEnable(): Unit = {
    val program = for {
      _ <- registerListeners
      _ <- IO {
        getLogger.log(Level.INFO, "HydrogenChairs is enabled.")
      }
    } yield ()

    program.unsafeRunSync()
  }

  override def onDisable(): Unit = {
    val program = for {
      _ <- IO {
        getLogger.log(Level.INFO, "HydrogenChairs is disabled.")
      }
    } yield ()

    program.unsafeRunSync()
  }

}
