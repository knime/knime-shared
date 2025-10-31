#!groovy
def BN = (BRANCH_NAME == 'master' || BRANCH_NAME.startsWith('releases/')) ? BRANCH_NAME : 'releases/2026-06'

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
        // Tycho build for AP (no tests)
        node('maven && java21') {
            knimetools.defaultTychoBuild(updateSiteProject: 'org.knime.update.shared', disableOWASP: true)
        }
    },
    'Maven Build': {
        // Pure Maven build for SRV and WH
        node('java21 && maven') {
            def sidecars = dockerTools.createSideCarFactory()
            try {
                def (proxyUser, proxyPassword, proxyStats) = ['knime-proxy', 'knime-proxy-password', 'tinyproxy.stats']
                def tinyproxy = sidecars.createSideCar('docker.io/kalaksi/tinyproxy:1.6', 'tinyproxy',
                    ["STAT_HOST=${proxyStats}", 'MAX_CLIENTS=500', 'ALLOWED_NETWORKS=0.0.0.0/0', 'LOG_LEVEL=Info', 'TIMEOUT=300'], [8888]).start()
                def tinyproxyAuth = sidecars.createSideCar('docker.io/kalaksi/tinyproxy:1.6', 'tinyproxyAuth',
                    ["STAT_HOST=${proxyStats}", 'MAX_CLIENTS=500', 'ALLOWED_NETWORKS=0.0.0.0/0', 'LOG_LEVEL=Info', 'TIMEOUT=300', "AUTH_USER=${proxyUser}", "AUTH_PASSWORD=${proxyPassword}"], [8888]).start()

                withEnv([
                    "KNIME_TINYPROXY=http://${tinyproxy.getAddress(8888)}",
                    "KNIME_TINYPROXYAUTH=http://${proxyUser}:${proxyPassword}@${tinyproxyAuth.getAddress(8888)}",
                    "KNIME_TINYPROXYSTATS=http://${proxyStats}",
                    "KNIME_HTTPBIN=httpbin.testing.knime.com"
                ]) {
                    knimetools.defaultMavenBuild(profiles: ['SRV'], withoutNode: true)
                }
            } finally {
                sidecars.close()
            }
        }
    }
} catch (ex) {
    currentBuild.result = 'FAILURE'
    throw ex
} finally {
    notifications.notifyBuild(currentBuild.result);
}
/* vim: set shiftwidth=4 expandtab smarttab: */
