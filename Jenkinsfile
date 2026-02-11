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

def proxyPort = 8888
def (proxyUser, proxyPassword, proxyStats) = ['knime-proxy', 'knime-proxy-password', 'tinyproxy.stats']
def proxyConfigs = [
    [
        image: 'docker.io/kalaksi/tinyproxy:1.7',
        namePrefix: 'TINYPROXY',
        envArgs: [
            "STAT_HOST=${proxyStats}",
            'MAX_CLIENTS=500',
            'ALLOWED_NETWORKS=0.0.0.0/0',
            'LOG_LEVEL=Info',
            'TIMEOUT=300',
        ],
        ports: [proxyPort]
    ],
    [
        image: 'docker.io/kalaksi/tinyproxy:1.7',
        namePrefix: 'TINYPROXYAUTH',
        envArgs: [
            "STAT_HOST=${proxyStats}",
            'MAX_CLIENTS=500',
            'ALLOWED_NETWORKS=0.0.0.0/0',
            'LOG_LEVEL=Info',
            'TIMEOUT=300',
            "AUTH_USER=${proxyUser}",
            "AUTH_PASSWORD=${proxyPassword}",
        ],
        ports: [proxyPort]
    ]
]

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
                // sidecars for (un-)authenticated proxies
                def (tinyproxy, tinyproxyAuth) = proxyConfigs.collect { cfg ->
                    sidecars.createSideCar(cfg.image, cfg.namePrefix, cfg.envArgs, cfg.ports).start()
                }
                // expose addresses of proxies
                withEnv([
                    "KNIME_TINYPROXY_ADDRESS=http://${tinyproxy.getAddress(proxyPort)}",
                    "KNIME_TINYPROXYAUTH_ADDRESS=http://${proxyUser}:${proxyPassword}@${tinyproxyAuth.getAddress(proxyPort)}",
                    "KNIME_TINYPROXYSTATS=http://${proxyStats}",
                    'KNIME_HTTPBIN_ADDRESS=https://httpbin.testing.knime.com'
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
    notifications.notifyBuild(currentBuild.result)
}
/* vim: set shiftwidth=4 expandtab smarttab: */
