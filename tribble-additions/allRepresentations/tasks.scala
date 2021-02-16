package saarland.cispa.se.tribble
package execution

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Arrays

import org.backuity.clist.Command
import org.log4s.getLogger
import saarland.cispa.se.tribble.execution.componentExtraction._
import collection.JavaConverters._

trait Task {
  def execute(): Unit
}

final class GenerateTask extends Command("generate", "Generate sample inputs")
  with Task with ForestGeneratorModule with OutputModule with RandomnessModule with GrammarModule with CacheModule with RegexModule with ReportingModule with HeuristicModule with CloseOffControlModule {
  private[this] val logger = getLogger

  override def execute(): Unit = {
    logger.info(s"Using random seed $randomSeed")
    logger.info(s"Writing generated files to $outputDir")

    val trees = forestGenerator.generateForest()


    for ((tree, i) <- trees.zipWithIndex) {
      reporter.processTree(i + 1, tree)
      logger.debug(tree.depth)


      val input = tree.leaves.mkString

      logger.debug(input)

      val outdir=Files.createDirectories(Paths.get(outputDir+"/plain"))
      val path = Files.write(Files.createTempFile(outdir, f"file${i + 1}%06d_${tree.size()}%d_${tree.depth()}%d_", suffix), input.getBytes(StandardCharsets.UTF_8))
      logger.debug(s"Generated $path")

      val univcomp = new UniversalURLComponentsBuilder();
      val dictExtractor=new DictExtractor(univcomp, 
        Arrays.asList(new FirefoxURLComponentsBuilder(univcomp), new ChromiumURLComponentsBuilder(univcomp)));
      
      val components=dictExtractor.extract(tree).asScala;
      for(comp<-components){
        val comp1=comp.asScala
        val outdir=Files.createDirectories(Paths.get(outputDir+"/"+comp1(0)))
        val path2 = Files.write(Files.createTempFile(outdir, f"components_file${i + 1}%06d_${tree.size()}%d_${tree.depth()}%d_", suffix), comp1(1).getBytes(StandardCharsets.UTF_8))
        logger.debug(s"Generated $path2")
      }
      
    }

  }
}
