package net.syrup16g.todo.modules
import com.google.inject.AbstractModule
import net.syrup16g.todo.http.auth.JwtEncoder
import play.api.Configuration

class JwtModule(config: Configuration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[JwtEncoder]).toInstance(new JwtEncoder(config))
  }
}
