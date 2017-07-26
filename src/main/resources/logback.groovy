import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.db.JNDIConnectionSource
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy


import java.nio.charset.Charset

scan()

appender("consoleAppender", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d %-5level %logger{5} - %msg%n"
        //messagePostProcessors = [new CardNumberMaskingPostProcessor(), new SecurityHeaderRemovingPostProcessor()]
    }
}

appender("appFileAppender", RollingFileAppender) {
    def FILE_PATH = "./servers/" + System.getProperty("weblogic.Name") + "/logs/webApp"

    file = FILE_PATH + ".log"
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = FILE_PATH + ".%d{yyyy-MM-dd}.%i.log"
        timeBasedFileNamingAndTriggeringPolicy(SizeAndTimeBasedFNATP) {
            maxFileSize = "2MB"
        }
        maxHistory = 10
    }
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName("UTF-8")
        pattern = "%d %-5level %logger{5} - %msg%n"
        //messagePostProcessors = [new CardNumberMaskingPostProcessor(), new SecurityHeaderRemovingPostProcessor()]
    }
}

//logger("com.datastech", ALL)
//logger("org.eclipse.persistence", DEBUG, ["consoleAppender"], false)

root(INFO, ["consoleAppender", "appFileAppender"])
