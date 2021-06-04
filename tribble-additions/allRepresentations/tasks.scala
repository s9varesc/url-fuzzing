package de.cispa.se.tribble


import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Arrays

import de.cispa.se.tribble.Internal._
//import de.cispa.se.tribble.input.{AlternativeExtraction, ObjectStreamGrammarCache, RuleInlining}
import de.cispa.se.tribble.output.GrammarPrettyPrinter
import org.backuity.clist.{Command, opt}
import org.log4s.getLogger
import de.cispa.se.tribble.componentExtraction._
import scala.collection.JavaConverters._

trait Task {
  def execute(): Unit
}

final class GenerateTask extends Command("generate", "Generate sample inputs")
  with Task with ForestGeneratorModule with OutputModule with RandomnessModule with GrammarModule with CacheModule with RegexModule with ReportingModule with HeuristicModule with CloseOffControlModule with ConstraintModule {
  private val logger = getLogger

  override def execute(): Unit = {
    logger.info(s"Using random seed $randomSeed")
    logger.info(s"Writing generated files to $outputDir")
    val usedSeed = s"$randomSeed"
    val seedPath = Files.write(Files.createTempFile(outputDir, f"seed", suffix), usedSeed.getBytes(StandardCharsets.UTF_8))
    

    val trees = forestGenerator.generateForest()


    for ((tree, i) <- trees.zipWithIndex) {
      reporter.processTree(i + 1, tree)
      
      val input = tree.leaves.mkString
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


