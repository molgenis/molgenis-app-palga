pipeline {
    agent {
        kubernetes {
            label 'molgenis-jdk8'
        }
    }
    stages {
        stage('Retrieve build secrets') {
            steps {
                container('vault') {
                    script {
                        sh "mkdir ${JENKINS_AGENT_WORKDIR}/.m2"
                        sh(script: "vault read -field=value secret/ops/jenkins/maven/settings.xml > ${JENKINS_AGENT_WORKDIR}/.m2/settings.xml")
                        env.GITHUB_TOKEN = sh(script: 'vault read -field=value secret/ops/token/github', returnStdout: true)
                        env.GITHUB_USER = sh(script: 'vault read -field=username secret/ops/token/github', returnStdout: true)
                    }
                }
            }
        }
        stage('Steps [ PR ]') {
            when {
                changeRequest()
            }
            stages {
                stage('Build & Test [ PR ]') {
                    steps {
                        container('maven') {
                            sh "mvn -q -B clean package"
                        }
                    }
                }
            }
        }
        stage('Steps [ master ]') {
            when {
                branch 'master'
            }
            stages {
                stage('Build, Test, Push to Registries [ master ]') {
                    steps {
                        container('maven') {
                            sh "mvn -q -B clean deploy"
                        }
                    }
                }
                stage('Prepare Release [ master ]') {
                    steps {
                        timeout(time: 10, unit: 'MINUTES') {
                            input(message: 'Prepare to release?')
                        }
                        container('maven') {
                            sh "mvn -q -B release:prepare"
                        }
                    }
                }
                stage('Perform release [ master ]') {
                    steps {
                        container('maven') {
                            sh "mvn -q -B release:perform"
                        }
                    }
                }
            }
        }
    }
}