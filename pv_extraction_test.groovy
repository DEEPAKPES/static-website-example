import groovy.json.JsonOutput

def dbname(){
    if (params.ENVIRONMENT == 'INT'){
		return [db_name:'lpesipo1000int.schneider-electric.com:1521:pesv2int' , credentials_id:'5e3e48a8-651d-48bd-813c-a3735c8af832']
    }else if (params.ENVIRONMENT == 'PPR'){
       	return [db_name:'lpesipo1009ppr.schneider-electric.com:1521:pesv2ppr' , credentials_id:'61f393e8-9648-4b7d-b806-ee28cc5befa1']    
    }else if (params.ENVIRONMENT == 'PRD'){
      	return [db_name:'lpesipo1002prd.schneider-electric.com:1521:pesv2prd' , credentials_id:'5e3e48a8-651d-48bd-813c-a3735c8af832']   
    }
}

pipeline {
    agent any
    options {
        disableConcurrentBuilds()
        timestamps()
    }

    stages {
        stage("Display the Env variables") {
            steps {
		    script{
			try {
				data = dbname()
				db_name = data.db_name
				credentials_id = data.credentials_id
			    withCredentials([usernamePassword(credentialsId: credentials_id, passwordVariable: 'CATA_PASS', usernameVariable: 'CATA_USER')])
				{ 
                  if (ALL_CATALOG == 'true') {
                            sh '''
                                cat <<EOF > se-variants-extractor.yml
                                apiVersion: batch/v1
                                kind: Job
                                metadata:
                                  name: se-variants-extractor
                                  namespace: epam
                                spec:
                                  ttlSecondsAfterFinished: 20
                                  template:
                                    metadata:
                                      labels:
                                        job-name: se-variants-extractor
                                    spec:
                                      containers:
                                      - args:
                                        - --spring.data.mongodb.host=se-variants-mongo
                                        - --spring.datasource.url=jdbc:oracle:thin:@'''+db_name+'''``
                                        - --spring.datasource.username=$CATA_USER
                                        - --spring.datasource.password=$CATA_PASS
                                        command:
                                        - java
                                        - -Xms2g
                                        - -Xmx6g
                                        - -XX:+UseG1GC
                                        - -Doracle.jdbc.fanEnabled=false
                                        - -jar
                                        - /app/product-variants-1.0.0.jar
                                        image: registry.us.se.com/epam/product-data-generator
                                        imagePullPolicy: Always
                                        name: se-variants-extractor
                                        resources:
                                          requests:
                                            memory: "4096Mi"
                                            cpu: "1000m"
                                          limits:
                                            memory: "12288Mi"
                                            cpu: "2000m"
                                        securityContext:
                                          allowPrivilegeEscalation: false
                                          capabilities: {}
                                          privileged: false
                                          readOnlyRootFilesystem: false
                                          runAsNonRoot: false
                                      imagePullSecrets:
                                      - name: se
                                      restartPolicy: Never
                                EOF
                            '''.stripIndent()
                        } else {
                            sh '''
                                cat <<EOF > se-variants-extractor.yml
                                apiVersion: batch/v1
                                kind: Job
                                metadata:
                                  name: se-variants-extractor
                                  namespace: epam
                                spec:
                                  ttlSecondsAfterFinished: 20
                                  template:
                                    metadata:
                                      labels:
                                        job-name: se-variants-extractor
                                    spec:
                                      containers:
                                      - args:
                                        - --spring.data.mongodb.host=se-variants-mongo
                                        - --spring.datasource.url=jdbc:oracle:thin:@'''+db_name+'''``
                                        - --spring.datasource.username=$CATA_USER
                                        - --spring.datasource.password=$CATA_PASS
                                        - locales=${LOCALES}
                                        - sourceIds=${SOURCE_IDS}
                                        - type=${TYPE}
                                        command:
                                        - java
                                        - -Xms2g
                                        - -Xmx6g
                                        - -XX:+UseG1GC
                                        - -Doracle.jdbc.fanEnabled=false
                                        - -jar
                                        - /app/product-variants-1.0.0.jar
                                        image: registry.us.se.com/epam/product-data-generator
                                        imagePullPolicy: Always
                                        name: se-variants-extractor
                                        resources:
                                          requests:
                                            memory: "4096Mi"
                                            cpu: "1000m"
                                          limits:
                                            memory: "12288Mi"
                                            cpu: "2000m"
                                        securityContext:
                                          allowPrivilegeEscalation: false
                                          capabilities: {}
                                          privileged: false
                                          readOnlyRootFilesystem: false
                                          runAsNonRoot: false
                                      imagePullSecrets:
                                      - name: se
                                      restartPolicy: Never
                                EOF
                            '''.stripIndent()
                        }  
				}
                } catch (Exception e) {
                    echo 'Exception occurred: ' + e.toString()
                    echo 'Envalid DB selected'
                    currentBuild.result = 'FAILURE'
                }
		    } 
            }
        }
	    stage("File") {
            when {
                expression { currentBuild.result != 'FAILURE' }
            }
            steps {
				script{
					sh 'cat se-variants-extractor.yml'
				}
			}
        }
    }
}
