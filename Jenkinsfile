#!groovy
def BN = (BRANCH_NAME == 'master' || BRANCH_NAME.startsWith('releases/')) ? BRANCH_NAME : 'releases/2022-06'

library "knime-pipeline@$BN"

properties([
    pipelineTriggers([upstream('knime-tp/' + env.BRANCH_NAME.replaceAll('/', '%2F'))]),
    buildDiscarder(logRotator(numToKeepStr: '5')),
    disableConcurrentBuilds(),
    parameters([booleanParam(defaultValue: false, description: 'Whether this is a release build', name: 'RELEASE_BUILD')])
])


try {
    parallel 'Tycho Build': {
        // Tycho build for AP
        node('maven && java11') {
            knimetools.defaultTychoBuild(updateSiteProject: 'org.knime.update.shared', disableOWASP: true)
        }
    },
    'Maven Build': {
        // Pure Maven build for SRV and WH
        knimetools.defaultMavenBuild(profiles: ['SRV'], skipSonar: true, exportCoverageData: true)
    }

    stage('Sonarqube analysis') {
        env.lastStage = env.STAGE_NAME
        workflowTests.runSonar(['maven'])
    }

} catch (ex) {
    currentBuild.result = 'FAILURE'
    throw ex
} finally {
    notifications.notifyBuild(currentBuild.result);
}
/* vim: set shiftwidth=4 expandtab smarttab: */
