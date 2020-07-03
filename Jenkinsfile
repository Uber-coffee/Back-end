#!/usr/bin/env groovy

// Jenkins file for Auth service

node {
    stage('git') {
        git([
                url: 'git@github.com:Uber-coffee/Back-end.git',
                branch: "${env.BRANCH_NAME}",
                credentialsId: "meshcheryakov_backend"
        ])
    }

    stage('Job started notification') {
        telegramSend(
                message: "Build ${env.BRANCH_NAME} started.",
                chatId: -1001336690990
        )
    }

    dir('auth') {
        docker.image('maven:3.6.3-openjdk-11').inside() {
            stage('Directory listing test') {
                sh 'ls -la'
                sh "echo  ${WORKSPACE}"
            }

            stage('Run tests') {
                sh 'mvn test'
            }

            stage('Build project') {
                sh 'mvn -DskipTests package spring-boot:repackage'
            }
        }

        stage('dns resolution debug') {
            sh 'ping -c 3 registry'
        }

        stage('Build docker image') {
            docker.withRegistry('http://registry:5000') {
                def image = docker.build("auth:${env.BUILD_ID}")
                image.push()
            }
        }
    }

    stage('Job success notification') {
        telegramSend(
                message: "Build ${env.BRANCH_NAME} finished, image: auth:${env.BUILD_ID}",
                chatId: -1001336690990
        )
    }

}