import grails.util.BuildSettings
import grails.util.Environment

appender('STDOUT', ConsoleAppender) {
	encoder(PatternLayoutEncoder) {
		pattern = '%level %logger - %msg%n'
	}
}

logger 'org.openqa', OFF
logger 'grails.app.taglib.org.grails.plugin.resource', OFF
logger 'org.grails.plugin.resource', OFF

logger 'grails.plugin.springsecurity.rest', DEBUG
logger 'org.pac4j', DEBUG
logger 'grails.app', DEBUG
logger 'org.springframework.security', DEBUG

root ERROR, ['STDOUT']

File targetDir = BuildSettings.TARGET_DIR
if (Environment.developmentMode && targetDir) {

	appender('FULL_STACKTRACE', FileAppender) {
		file = "$targetDir/stacktrace.log"
		append = true
		encoder(PatternLayoutEncoder) {
			pattern = '%level %logger - %msg%n'
		}
	}

	logger 'StackTrace', ERROR, ['FULL_STACKTRACE'], false
}
