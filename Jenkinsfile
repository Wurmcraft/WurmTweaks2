node {
	checkout scm
	sh 'gradle spotlessApply'
	sh 'gradle setupCiWorkspace clean build'
	archive 'build/libs/*'
}