pipeline {
    agent any

    triggers {
        githubPush()
    }

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        APP_NAME = 'crud-app'
        IMAGE_REPO = 'mahfuzdocker20/crud-app'
        DOCKERHUB_CREDENTIALS = 'dockerhub-credentials'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build And Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn clean test package'
                    } else {
                        bat 'mvn clean test package'
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    env.GIT_SHA = isUnix()
                        ? sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                        : bat(script: '@git rev-parse --short HEAD', returnStdout: true).trim()

                    env.IMAGE_TAG = "${BUILD_NUMBER}-${env.GIT_SHA}"

                    if (isUnix()) {
                        sh "docker build -t ${IMAGE_REPO}:${IMAGE_TAG} -t ${IMAGE_REPO}:latest ."
                    } else {
                        bat "docker build -t ${IMAGE_REPO}:${IMAGE_TAG} -t ${IMAGE_REPO}:latest ."
                    }
                }
            }
        }

        stage('Push To Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: "${DOCKERHUB_CREDENTIALS}",
                        usernameVariable: 'DOCKERHUB_USERNAME',
                        passwordVariable: 'DOCKERHUB_TOKEN'
                    )
                ]) {
                    script {
                        if (isUnix()) {
                            sh 'echo "$DOCKERHUB_TOKEN" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin'
                            sh "docker push ${IMAGE_REPO}:${IMAGE_TAG}"
                            sh "docker push ${IMAGE_REPO}:latest"
                            sh 'docker logout'
                        } else {
                            bat '@echo %DOCKERHUB_TOKEN% | docker login -u %DOCKERHUB_USERNAME% --password-stdin'
                            bat "docker push ${IMAGE_REPO}:${IMAGE_TAG}"
                            bat "docker push ${IMAGE_REPO}:latest"
                            bat 'docker logout'
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        }

        cleanup {
            script {
                if (env.IMAGE_TAG) {
                    if (isUnix()) {
                        sh "docker image rm ${IMAGE_REPO}:${IMAGE_TAG} ${IMAGE_REPO}:latest || true"
                    } else {
                        bat "@docker image rm ${IMAGE_REPO}:${IMAGE_TAG} ${IMAGE_REPO}:latest || exit /b 0"
                    }
                }
            }
        }
    }
}
