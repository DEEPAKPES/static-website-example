pipeline {
    agent any

    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
                script{
                  echo "Number of threads are : ${THREADS}"
                  echo "The Image ID is : ${IMAGE_ID}"
                  echo "The ranges are : ${RANGES}"
                  echo params.source_mongo
                  echo params.dest_mongo
                }
            }
        }
    }
}
