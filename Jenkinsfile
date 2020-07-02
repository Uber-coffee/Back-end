#!/usr/bin/env groovy

pipeline {
    agent any

    stages {
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
            telegramSend 'Hello from jenkins'
        }

        stage('Build and Test') {
            def maven = docker.image('maven:3.6.3-openjdk-11')

            maven.inside {
                run_sh_command('mvn test')
            }
        }
    }
}