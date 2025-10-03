import org.yaml.snakeyaml.Yaml

def yaml = new Yaml()
def config = yaml.load(readFileFromWorkspace("repository.yaml"))

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
