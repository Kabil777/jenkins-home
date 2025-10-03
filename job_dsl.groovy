import org.yaml.snakeyaml.Yaml

def yaml = new Yaml()
def reposFile = new File("${WORKSPACE}/repository.yaml")
def config = yaml.load(reposFile.text)

config.repos.each { repo ->
    pipelineJob("${repo.name}-pipeline") {
        description("Pipeline for ${repo.name}")
        definition {
            cps {
                // load the right Jenkinsfile template based on type
                script(readFileFromWorkspace("pipelines/${repo.type}.Jenkinsfile"))
            }
        }
    }
}
