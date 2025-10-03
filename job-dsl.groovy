@Grab('org.yaml:snakeyaml:1.30')
import org.yaml.snakeyaml.Yaml

// Load repository.yaml
def yaml = new Yaml()
def reposFile = new File("${WORKSPACE}/repository.yaml")
def config = yaml.load(reposFile.text)

config.repos.each { repo ->
    pipelineJob("${repo.name}-pipeline") {
        description("Pipeline for ${repo.name}")
        definition {
            cps {
                // load the right Jenkinsfile template based on type
                script(readFileFromWorkspace("jenkins-configs/piplines/${repo.type}.Jenkinsfile"))
            }
        }
    }
}
