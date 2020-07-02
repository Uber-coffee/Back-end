#!/usr/bin/env groovy

node {
    stage('git') {
        git([
                url: 'git@github.com:Uber-coffee/Back-end.git',
                branch: "${env.BRANCH_NAME}",
                credentialsId: "meshcheryakov_backend"
        ])
    }

    stage('Test ls') {
        sh 'ls -la'
    }

    stage('Telegram test') {
        telegramSend(
                message: 'Hello from jenkins',
                chatId: -1001336690990
        )
    }

    stage('Build and Test') {
        docker.withDockerServer('tcp://ecse005008ef.epam.com:2375') {
            docker.image('maven:3.6.3-openjdk-11').inside {
                sh 'cd auth && mvn test'
            }
        }
    }
}