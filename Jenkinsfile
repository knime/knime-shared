#!groovy
def BN = BRANCH_NAME == "master" || BRANCH_NAME.startsWith("releases/") ? BRANCH_NAME : "master"

library "knime-pipeline@$BN"

properties([
	pipelineTriggers([upstream('knime-tp/' + env.BRANCH_NAME.replaceAll('/', '%2F'))]),
	buildDiscarder(logRotator(numToKeepStr: '5')),
	disableConcurrentBuilds()
])


try {
	parallel 'Tycho Build': {
		// Tycho build for AP
		knimetools.defaultTychoBuild('org.knime.update.shared')

		stage('Sonarqube analysis') {
			env.lastStage = env.STAGE_NAME
			workflowTests.runSonar([])
		}
	},
	'Maven Build': {
		// Pure Maven build for SRV and WH
		knimetools.defaultMavenBuild({}, 'SRV')
	}
} catch (ex) {
	currentBuild.result = 'FAILED'
	throw ex
} finally {
	notifications.notifyBuild(currentBuild.result);
}
/* vim: set ts=4: */
