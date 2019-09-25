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
		
		def commitHash = sh (
            returnStdout: true,
            script: 'git rev-parse --short HEAD'
        ).trim()
        def commitTs = sh (
            returnStdout: true,
            script: 'date -d @$(git show -s --format=format:%ct) +%Y%m%d-%H%M%S'
        ).trim();

        def changelist
        if (params.RELEASE_BUILD == true) {
            changelist = ".${commitHash}"
        } else if (BRANCH_NAME == "master") {
            changelist = "-beta-${commitTs}-${commitHash}"
        } else if (BRANCH_NAME.startsWith("releases/")) {
            changelist = "-rc-${commitTs}-${commitHash}"
        } else {
            changelist = "-alpha-" + BRANCH_NAME.replace("/", "-") + "-${commitTs}-${commitHash}"
        }

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
						mvn -B -P SRV clean verify \$SONAR_ARGS -DskipTests=false -Dmaven.test.failure.ignore=true -Dmaven.test.redirectTestOutputToFile=true -Dchangelist="${changelist}" ${dockerTag}
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
