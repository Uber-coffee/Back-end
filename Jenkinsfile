#!/usr/bin/env groovy

// Jenkins file for auth_mobile service

node {
    try {
        stage('git') {
            git([
                    url: 'git@github.com:Uber-coffee/Back-end.git',
                    branch: "${env.BRANCH_NAME}",
                    credentialsId: "meshcheryakov_backend"
            ])
        }

        stage('Job started notification') {
            telegram_msg("Build ${env.BRANCH_NAME} started. Build id: ${env.BUILD_ID}")
        }

        dir('auth') {
            docker.image('maven:3.6.3-openjdk-11').inside() {
                stage('Run tests') {
                    sh 'mvn test'
                }

                stage('Build project') {
                    sh 'mvn -DskipTests package spring-boot:repackage'
                }
            }
        }

        if (env.BRANCH_NAME == 'develop' || env.BRANCH_NAME == 'master') {
            dir('auth') {
                stage('Build docker image') {
                    docker.build("auth_mobile:${env.BUILD_ID}")
                }
            }

            stage('Build success notification') {
                telegram_msg("Build ${env.BRANCH_NAME} finished, image: auth_mobile:${env.BUILD_ID}")
            }

            stage('Push to registry and deploy (dev)') {
                if (env.BRANCH_NAME == 'develop') {
                    ansiblePlaybook playbook: 'deploy_dev_playbook.yaml', vaultCredentialsId: 'ansible_vault_password'
                    telegram_msg("Develop has been deployed to dev")
                }

                if (env.BRANCH_NAME == 'master') {
                    ansiblePlaybook playbook: 'deploy_prod_playbook.yaml', vaultCredentialsId: 'ansible_vault_password'
                    telegram_msg("Master has been deployed to production, pray for success :)")
                }
            }
        } else {
            stage('Job success notification') {
                telegram_msg("${env.BRANCH_NAME} has passed all tests")
            }
        }
    } catch (Exception ex) {
        telegram_msg("Build ${env.BRANCH_NAME} failed")
        throw ex
    }
}

def telegram_msg(String msg) {
    telegramSend(
            message: "Auth_mobile service: " + msg,
            chatId: -1001336690990
    )
}