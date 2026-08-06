pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    environment {
        COMPOSE_PROJECT_NAME = 'portfolio-manager'
        COMPOSE_CMD_FILE = '.compose_cmd'
        WORK_DIR_FILE = '.workdir'
        ENV_FILE = '.env'

        REPO_URL = 'https://github.com/Neueda-Learning/106-Portfolio-Manager-Runtime-Rangers.git'
        REPO_BRANCH = 'main'
    }


    stages {

        stage('Checkout Source') {
            steps {
                script {

                    def workDir = "repo-${env.BUILD_NUMBER}-${UUID.randomUUID().toString().substring(0, 8)}"

                    def repoUrl = env.REPO_URL?.trim()
                    def branch = env.REPO_BRANCH?.trim()
                    def credentialsId = null


                    sh "rm -rf '${workDir}' 2>/dev/null || true"


                    if (credentialsId) {

                        dir(workDir) {
                            git branch: branch,
                                credentialsId: credentialsId,
                                url: repoUrl
                        }

                    } else {

                        sh "git clone --branch '${branch}' --single-branch '${repoUrl}' '${workDir}'"

                    }


                    writeFile file: env.WORK_DIR_FILE, text: workDir

                    echo "Checked out repository into ${workDir}"
                }
            }
        }


        stage('Validate Agent Tooling') {

            steps {

                script {

                    if (!isUnix()) {
                        error('Linux Jenkins agent required')
                    }


                    sh 'git --version'
                    sh 'docker --version'
                    sh 'curl --version'


                    def composeCmd = sh(
                        script: '''
if docker compose version >/dev/null 2>&1; then
    echo "docker compose"
elif docker-compose version >/dev/null 2>&1; then
    echo "docker-compose"
fi
''',
                        returnStdout: true
                    ).trim()


                    if (!composeCmd) {
                        error('Docker compose not available')
                    }


                    writeFile(
                        file: env.COMPOSE_CMD_FILE,
                        text: composeCmd
                    )


                    sh "${composeCmd} version"
                }
            }
        }



        stage('Test Backend') {

            steps {

                script {

                    def workDir = readFile(env.WORK_DIR_FILE).trim()

                    dir("${workDir}/backend") {

sh 'chmod +x mvnw && ./mvnw -B clean test -Dspring.profiles.active=test'
                    }
                }
            }


            post {

                always {

                    script {

                        def workDir =
                            readFile(env.WORK_DIR_FILE).trim()


                        junit(
                            testResults:
                            "${workDir}/backend/target/surefire-reports/*.xml",
                            allowEmptyResults: true
                        )
                    }
                }
            }
        }




        stage('Build Backend') {

            steps {

                script {

                    def workDir =
                        readFile(env.WORK_DIR_FILE).trim()


                    dir("${workDir}/backend") {

                        sh '''
chmod +x mvnw
./mvnw -B package -DskipTests
'''
                    }
                }
            }
        }




        stage('Validate Frontend') {

            steps {

                script {

                    def workDir =
                        readFile(env.WORK_DIR_FILE).trim()


                    dir("${workDir}/frontend") {

                        sh '''
if command -v npm >/dev/null 2>&1; then
    npm ci
    npm run build
else
    echo "npm not available. Docker will build frontend."
fi
'''
                    }
                }
            }
        }





        stage('Prepare Deployment Env') {

            steps {

                script {

                    def workDir =
                        readFile(env.WORK_DIR_FILE).trim()


                    def envContent = """
MYSQL_ROOT_PASSWORD=${env.MYSQL_ROOT_PASSWORD ?: 'n3u3da!'}
MYSQL_DATABASE=${env.MYSQL_DATABASE ?: 'portfolio_db'}
MYSQL_USER=${env.MYSQL_USER ?: 'portfolio_user'}
MYSQL_PASSWORD=${env.MYSQL_PASSWORD ?: 'portfolio_password'}
""".trim() + "\n"



                    writeFile(
                        file: "${workDir}/${env.ENV_FILE}",
                        text: envContent
                    )
                }
            }
        }





        stage('Deploy MySQL') {

            steps {

                script {

                    def workDir =
                        readFile(env.WORK_DIR_FILE).trim()

                    def composeCmd =
                        readFile(env.COMPOSE_CMD_FILE).trim()


                    dir(workDir) {


                        sh """
${composeCmd} -p ${COMPOSE_PROJECT_NAME} \
--env-file .env pull mysql || true
"""


                        sh """
${composeCmd} -p ${COMPOSE_PROJECT_NAME} \
--env-file .env up -d mysql
"""



                        sh '''
for i in $(seq 1 30)
do

status=$(docker inspect \
-f "{{.State.Health.Status}}" portfolio-mysql \
2>/dev/null || echo starting)


if [ "$status" = "healthy" ]
then
    echo "MySQL is healthy"
    exit 0
fi


echo "Waiting for MySQL ($i/30)"
sleep 5

done


echo "MySQL failed health check"
exit 1
'''
                    }
                }
            }
        }





        stage('Deploy Application') {

            steps {

                script {

                    def workDir =
                        readFile(env.WORK_DIR_FILE).trim()


                    def composeCmd =
                        readFile(env.COMPOSE_CMD_FILE).trim()



                    dir(workDir) {


                        sh """
${composeCmd} -p ${COMPOSE_PROJECT_NAME} \
--env-file .env pull || true
"""


                        sh """
${composeCmd} -p ${COMPOSE_PROJECT_NAME} \
--env-file .env up -d \
--build \
--remove-orphans
"""


                        sh """
${composeCmd} -p ${COMPOSE_PROJECT_NAME} \
--env-file .env ps
"""
                    }
                }
            }
        }





        stage('Health Check') {

            steps {

                script {

                    def workDir =
                        readFile(env.WORK_DIR_FILE).trim()


                    def composeCmd =
                        readFile(env.COMPOSE_CMD_FILE).trim()



                    dir(workDir) {

                        sh """
${composeCmd} -p ${COMPOSE_PROJECT_NAME} \
--env-file .env ps
"""
                    }


                    // Backend
                    sh 'curl -fsS http://localhost:8082/api/market'


                    // Frontend
                    sh 'curl -fsS http://localhost:8085/health'

                }
            }
        }
    }





    post {

        success {

            echo 'Deployment completed successfully.'
        }


        failure {

            echo 'Deployment failed. Check logs.'
        }


        cleanup {

            script {

                if(fileExists(env.WORK_DIR_FILE)) {

                    def workDir =
                        readFile(env.WORK_DIR_FILE).trim()


                    sh """
rm -f '${workDir}/${ENV_FILE}'
"""
                }


                sh '''
rm -f .compose_cmd .workdir
'''
            }
        }
    }
}