pipeline {
    agent { label 'docker-agent' }

    environment {
        DOCKER_CREDENTIALS_ID = "dockerhub-credentials" 
    }

    stages {
        stage('Print Docker Version') {
            steps {
                container('docker') {
                    sh 'docker version'
                }
            }
        }
        stage('clone repository'){
          steps{
               container('docker'){
                   git branch: 'main', url: "${params.REPO_URL}"
              }
           }
        }
      stage('List of files'){
        steps{
           container('docker'){
             sh 'ls -a'
           }
        }
    }
        stage('Build Docker Image') {
            steps {
                container('docker') {
                   sh "docker build -t ${params.IMAGE_NAME}:${params.IMAGE_TAG} ."
                }
            }
        }

        stage('Push to Repository') {
            steps {
                container('docker') {
                    withCredentials([usernamePassword(
                        credentialsId: "$DOCKER_CREDENTIALS_ID",
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )]) {

                 sh """
                    echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                    docker build -t ${params.IMAGE_NAME}:${params.IMAGE_TAG} .
                    docker push ${params.IMAGE_NAME}:${params.IMAGE_TAG}
                    """
               }
            }
        }
    }

 stage('Update Argo CD manifest') {
  steps {
    container('docker') {
      withCredentials([sshUserPrivateKey(credentialsId: 'ssh-privatekey', keyFileVariable: 'SSH_KEY', usernameVariable: 'GIT_USER')]) {
        script {
          def tmpDir = sh(script: 'mktemp -d', returnStdout: true).trim()
          def safeUrl = "${params.URL}".toString()

          sh """
            set -e
            export GIT_SSH_COMMAND="ssh -i $SSH_KEY -o StrictHostKeyChecking=no"

            git clone git@github.com:Kabil777/argo-repo.git ${tmpDir}
            cd ${tmpDir}

            git config user.email "jenkins@kabidev.in"
            git config user.name "Jenkins CI"

            if [[ ! -d "apps/${NAME}" ]]; then
              mkdir -p "apps/${NAME}" && touch "apps/${NAME}/deployment.yaml"
            fi

            sed "s|{{name}}|${NAME}|g; s|{{image}}|${IMAGE_NAME}:${IMAGE_TAG}|g; s|{{url}}|${safeUrl}|g" apps/template/deployment.k8s.yaml > "apps/${NAME}/deployment.yaml"

            git add .
            git commit -m "Updating ${NAME} to ${IMAGE_TAG}" || echo "No changes to commit"
            git push origin main
          """
        }
      }
    }
  }
}

}
    post {
        always {
            echo 'Pipeline finished!'
        }
    }

}
