#!groovy
def BN = BRANCH_NAME == "master" || BRANCH_NAME.startsWith("releases/") ? BRANCH_NAME : "master"

library "knime-pipeline@$BN"

properties([
	pipelineTriggers([upstream('knime-tp/' + env.BRANCH_NAME.replaceAll('/', '%2F'))]),
	buildDiscarder(logRotator(numToKeepStr: '5')),
	disableConcurrentBuilds()
])


node('maven') {
     stage('Checkout Sources') {
        checkout scm
	}

	try {
		stage('Tycho Build') {
		        withCredentials([usernamePassword(credentialsId: 'ARTIFACTORY_CREDENTIALS', passwordVariable: 'ARTIFACTORY_PASSWORD', usernameVariable: 'ARTIFACTORY_LOGIN')]) {
				sh '''
					export TEMP="${WORKSPACE}/tmp"
					rm -rf "${TEMP}"; mkdir "${TEMP}"
					mvn -Dmaven.test.failure.ignore=true -Dknime.p2.repo=${P2_REPO} clean verify
					rm -rf "${TEMP}"
				'''
			}

			// junit '**/target/test-reports/*/TEST-*.xml'
		}


		if (currentBuild.result != 'UNSTABLE') {
			stage('Deploy p2') {
				p2Tools.deploy("${WORKSPACE}/org.knime.update.shared/target/repository/")
			}
		} else {
			echo "==============================================\n" +
				 "| Build unstable, not deploying p2 artifacts.|\n" +
				 "=============================================="
		}

		stage('Maven Build') {
			withMaven {
                withCredentials([
					usernamePassword(credentialsId: 'SONAR_CREDENTIALS', passwordVariable: 'SONAR_PASSWORD', usernameVariable: 'SONAR_LOGIN'),
					usernamePassword(credentialsId: 'ARTIFACTORY_CREDENTIALS', passwordVariable: 'ARTIFACTORY_PASSWORD', usernameVariable: 'ARTIFACTORY_LOGIN')
				]) {
					sh '''
						export PATH="$MVN_CMD_DIR:$PATH"
						if [[ "$BRANCH_NAME" == "master" ]]; then
							SONAR_ARGS="sonar:sonar -Dsonar.password=$SONAR_PASSWORD -Dsonar.login=$SONAR_LOGIN"
						fi
						mvn -P SRV -DskipTests=false -Dmaven.test.redirectTestOutputToFile=true clean verify $SONAR_ARGS
					'''
				}
			}

			junit '**/target/surefire-reports/TEST-*.xml'
		}

		if (BRANCH_NAME == "master" || BRANCH_NAME.startsWith("releases/")) {
			if (currentBuild.result != 'UNSTABLE') {
				stage('Deploy') {
					withMaven {
                        withCredentials([
					        usernamePassword(credentialsId: 'ARTIFACTORY_CREDENTIALS', passwordVariable: 'ARTIFACTORY_PASSWORD', usernameVariable: 'ARTIFACTORY_LOGIN')
				        ]) {
						sh '''
							export PATH="$MVN_CMD_DIR:$PATH"
							mvn -P SRV -DskipTests=true -DskipITs=true deploy
						'''
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
 }
/* vim: set ts=4: */
