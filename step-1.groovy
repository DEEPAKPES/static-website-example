pipeline {
    agent any

    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
                script{
                    env_name=${ENV}
                    echo env_name
                    db_1=${DB_NAME}
                    echo db_1
                }
            }
        }
    }
}
