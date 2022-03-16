package net.stouma915.hydrogenchairs

import cats.effect.IO
import org.bukkit.plugin.java.JavaPlugin

import java.util.logging.Level

final class HydrogenChairs extends JavaPlugin {

  import cats.effect.unsafe.implicits.global

  override def onEnable(): Unit = {
    val program = for {
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
