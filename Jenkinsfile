#!/groovy
def dockerImageRepo = 'anandtest/protest'
def dockerImageTag
def dockerImage
def dockerRegistry = 'hub.docker.com'

pipeline
{
	agent any
	stages
	{
		stage('Cleaning the WorkSpace')
		{
			steps
			{
				deleteDir()
				echo "the build number is ${currentBuild.number}"
				echo 'Cleanup Done'
			}
		}
		
		stage('CheckOut latest Code')
		{
			steps
			{
				checkout scm
				script 
				{
					dockerImageTag="$BUILD_NUMBER"
				}
			}
		}
		
		stage('call the another pipeline')
		{
			build job: 'test-pipeline-child', quietPeriod: 0
		}
		
	}
}
