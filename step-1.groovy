def source_dbname(){
    if (params.SOURCE_ORACLE_ENV == 'INT_ORACLE'){
        return [db_name:'lpesipo1000int.schneider-electric.com:1521:pesv2int' , credentials_id:'d0d8e9f8-96c4-42dd-a38b-a549a204dbc9']
    }else if (params.SOURCE_ORACLE_ENV == 'PRE_PROD'){
        return [db_name:'lpesipo1009ppr.schneider-electric.com:1521:pesv2ppr' , credentials_id:'3ed29a02-7552-491b-8a4c-d1b1b52d3f71']
    }else if (params.SOURCE_ORACLE_ENV == 'PROD_STANDBY_DB'){
        return [db_name:'lpesipo1010prd.schneider-electric.com:1521:pesv2prdstby' , credentials_id:'50319cf9-32dc-49e8-bec3-70010c89b9bb']
    }
}

def destination_dbname(){
    if (params.PRODUCTS_ML_DB_ENV == 'KAAS_MONGO'){
        return [db_name:'se-variants-mongo']
    }else if (params.PRODUCTS_ML_DB_ENV == 'INT_MONGO'){
        return [db_name:'lpesipo1050sqe.schneider-electric.com']
    }
}

pipeline {
    agent any

    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
                script{

                    data = source_dbname()
                    source_db_name = data.db_name
                    source_credentials_id = data.credentials_id
                    echo source_db_name
                    echo source_credentials_id
                }
            }
        }
    }
}
