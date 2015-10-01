import slick.codegen.SourceCodeGenerator
import slick.{ model => m }

object SlickCodegen extends SlickCodegen

trait SlickCodegen {
  case class Config(
    generator: m.Model => SourceCodeGenerator,
    outputDir: String,
    pkg: String,
    fileName: String,
    container: String)
}
