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

    stage('Update Argo cd manifest'){
      steps{
       container('docker') {
        withCredentials([sshUserPrivateKey(credentialsId: 'ssh-privatekey', keyFileVariable: 'SSH_KEY', usernameVariable: 'GIT_USER')]) {
         sh """
            tmp_dir=\$(mktemp -d)
            export GIT_SSH_COMMAND="ssh -i $SSH_KEY -o StrictHostKeyChecking=no"
            git clone git@github.com:Kabil777/argo-repo.git "$tmp_dir"
            cd "$tmp_dir"
            git config user.email "jenkins@kabidev.in"
            git config user.name "Jenkins CI"

            if [[ ! -d "$tmp_dir/apps/$NAME" ]]; then
              mkdir -p "$tmp_dir/apps/$NAME" && touch "$tmp_dir/apps/$NAME/deployment.yaml"
            fi

            sed "s|{{name}}|${NAME}|g; s|{{image}}|${IMAGE_NAME}:${IMAGE_TAG}|g" apps/template/deployment.k8s.yaml > apps/$NAME/deployment.yaml

            git add .
            git commit -m "Updating to newer image"
            git push origin main
          """ 
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
