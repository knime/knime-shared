#!groovy
def BN = (BRANCH_NAME == 'master' || BRANCH_NAME.startsWith('releases/')) ? BRANCH_NAME : 'releases/2024-12'

library "knime-pipeline@$BN"

properties([
    pipelineTriggers([upstream('knime-tp/' + knimetools.CURRENT_BRANCH.replaceAll('/', '%2F'))]),
    buildDiscarder(logRotator(numToKeepStr: '5')),
    disableConcurrentBuilds(),
    parameters([
        booleanParam(defaultValue: false, description: 'Whether this is a release build', name: 'RELEASE_BUILD'),
        p2Tools.getP2pruningParameter()
    ])
])


try {
    parallel 'Tycho Build': {
        // Tycho build for AP
        node('maven && java17') {
            knimetools.defaultTychoBuild(updateSiteProject: 'org.knime.update.shared', disableOWASP: true)
        }
    },
    'Maven Build': {
        // Pure Maven build for SRV and WH
        knimetools.defaultMavenBuild(profiles: ['SRV'], nodeLabel: 'maven && java17')
    }
} catch (ex) {
    currentBuild.result = 'FAILURE'
    throw ex
} finally {
    notifications.notifyBuild(currentBuild.result);
}
/* vim: set shiftwidth=4 expandtab smarttab: */
