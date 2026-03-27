pipeline {
    agent any

    tools {
        // Make sure 'Maven3' and 'JDK17' are configured in Jenkins → Global Tool Config
        maven 'Maven3'
        jdk   'JDK17'
    }

    environment {
        APP_NAME    = 'hello-world'
        WAR_FILE    = 'hello-world.war'
        TOMCAT_URL  = 'http://localhost:8080'         // your Tomcat host
        TOMCAT_CREDS = credentials('tomcat-deployer') // Jenkins credential ID
    }

    stages {

        stage('Checkout') {
            steps {
                echo '--- Pulling source code from repository ---'
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                echo '--- Compiling and running unit tests ---'
                sh 'mvn clean test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package WAR') {
            steps {
                echo '--- Packaging application as WAR ---'
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: "target/${WAR_FILE}", fingerprint: true
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                echo '--- Deploying WAR to Apache Tomcat ---'
                /*
                 * Option A: Tomcat Manager HTTP API (most common)
                 * Requires the Tomcat Manager app and a user with 'manager-script' role.
                 */
                sh """
                    curl -u ${TOMCAT_CREDS_USR}:${TOMCAT_CREDS_PSW} \\
                         -T target/${WAR_FILE} \\
                         "${TOMCAT_URL}/manager/text/deploy?path=/${APP_NAME}&update=true"
                """

                /*
                 * Option B: Copy WAR directly to webapps/ (uncomment if Tomcat is local)
                 *
                 * sh "cp target/${WAR_FILE} /opt/tomcat/webapps/${WAR_FILE}"
                 */
            }
        }

        stage('Smoke Test') {
            steps {
                echo '--- Running post-deploy smoke test ---'
                sh """
                    sleep 10  # give Tomcat time to deploy
                    curl -f ${TOMCAT_URL}/${APP_NAME}/api/health || exit 1
                """
            }
        }
    }

    post {
        success {
            echo "Pipeline SUCCESS — WAR deployed at ${TOMCAT_URL}/${APP_NAME}/"
        }
        failure {
            echo 'Pipeline FAILED — check the logs above.'
        }
    }
}
