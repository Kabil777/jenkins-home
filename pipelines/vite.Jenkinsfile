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
                    // sh '''
                    // echo -e "FROM alpine:3.14\nCMD [\\"echo\\", \\"Hello from configured Docker Agent!\\"]" > Dockerfile
                    // docker build -t my-app-image .
                    // '''
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
}
    post {
        always {
            echo 'Pipeline finished!'
        }
    }

}
