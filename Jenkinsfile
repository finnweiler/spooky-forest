pipeline {
    agent any
    stages {
        stage("build") {
            steps {
                echo 'Start Building Process'
            }

        }

    }
}

node {
    def app
    if (env.BRANCH_NAME == 'main') {
        stage('Clone repository') {
            checkout scm
        }
        stage('Build image') {
            app = docker.build("corusm/computergrafik-ws")
        }
    } else {
        return
    }
    stage('Push image') {
        docker.withRegistry('https://registry.corusm.de', 'corusm-registry') {
            app.push("${env.BUILD_NUMBER}")
            app.push("latest")
        }
    }
}