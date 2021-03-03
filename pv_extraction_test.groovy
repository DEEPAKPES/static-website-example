import groovy.json.JsonOutput

def dbhostname
def destdb
def dbname(){	
    if (params.ENVIRONMENT == 'INT'){
	db_name = "lpesipo1000int.schneider-electric.com:1521:pesv2int"
	credentials_id = "5e3e48a8-651d-48bd-813c-a3735c8af832"  
    }else if (params.ENVIRONMENT == 'PPR'){
        db_name = "0lpesipo1000int.schneider-electric.com:1521:pesv2int"
	credentials_id = "05e3e48a8-651d-48bd-813c-a3735c8af832"
    }else if (params.ENVIRONMENT == 'PRD'){
      	db_name = "1lpesipo1000int.schneider-electric.com:1521:pesv2int"
	credentials_id = "15e3e48a8-651d-48bd-813c-a3735c8af832"
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
			    sh '''
			    	echo $db_name
				echo $credentials_id
			    '''
		    }
	    }
	}
	    
    }
}
