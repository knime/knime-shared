#!groovy
def BN = BRANCH_NAME == "master" || BRANCH_NAME.startsWith("releases/") ? BRANCH_NAME : "master"

library "knime-pipeline@$BN"

properties([
	pipelineTriggers([upstream('knime-tp/' + env.BRANCH_NAME.replaceAll('/', '%2F'))]),
	buildDiscarder(logRotator(numToKeepStr: '5')),
	disableConcurrentBuilds()
])


try {
	// Tycho build for AP
	knimetools.defaultTychoBuild('org.knime.update.shared')

	stage('Sonarqube analysis') {
		env.lastStage = env.STAGE_NAME
		workflowTests.runSonar([])
	}

	// Pure Maven build for SRV and WH
	node('maven') {
		stage('Checkout Sources') {
			env.lastStage = env.STAGE_NAME
			checkout scm
		}

        def changelist = knimetools.changelistSuffix()

		stage('Maven Build') {
			env.lastStage = env.STAGE_NAME
			withMaven {
				withCredentials([
					usernamePassword(credentialsId: 'SONAR_CREDENTIALS', passwordVariable: 'SONAR_PASSWORD', usernameVariable: 'SONAR_LOGIN'),
					usernamePassword(credentialsId: 'ARTIFACTORY_CREDENTIALS', passwordVariable: 'ARTIFACTORY_PASSWORD', usernameVariable: 'ARTIFACTORY_LOGIN')
				]) {
					sh """
						export PATH="\$MVN_CMD_DIR:\$PATH"
						if [[ "\$BRANCH_NAME" == "master" ]]; then
							SONAR_ARGS="sonar:sonar -Dsonar.password=\$SONAR_PASSWORD -Dsonar.login=\$SONAR_LOGIN"
						fi
						mvn -B -P SRV clean verify \$SONAR_ARGS -DskipTests=false -Dmaven.test.failure.ignore=true -Dmaven.test.redirectTestOutputToFile=true -Dchangelist="${changelist}"
					"""
				}
			}

			junit '**/target/surefire-reports/TEST-*.xml'
		}

		if (currentBuild.result != 'UNSTABLE') {
			stage('Deploy') {
				env.lastStage = env.STAGE_NAME
				withMaven {
					withCredentials([
						usernamePassword(credentialsId: 'ARTIFACTORY_CREDENTIALS', passwordVariable: 'ARTIFACTORY_PASSWORD', usernameVariable: 'ARTIFACTORY_LOGIN')
					]) {
						sh """
                            export PATH="\$MVN_CMD_DIR:\$PATH"
                            mvn -B -P SRV deploy -DskipTests=true -DskipITs=true -Dchangelist="${changelist}"
                        """
					}
				}
			}
		} else {
			echo "===========================================\n" +
				 "| Build unstable, not deploying artifacts.|\n" +
				 "==========================================="
		}
	}
} catch (ex) {
	currentBuild.result = 'FAILED'
	throw ex
} finally {
	notifications.notifyBuild(currentBuild.result);
}
/* vim: set ts=4: */
