//package az.ingress.common.logging;
//
//import ch.qos.logback.classic.AsyncAppender;
//import ch.qos.logback.classic.Level;
//import ch.qos.logback.classic.LoggerContext;
//import ch.qos.logback.classic.spi.LoggerContextListener;
//import ch.qos.logback.core.spi.ContextAwareBase;
//import lombok.extern.slf4j.Slf4j;
//import net.logstash.logback.appender.LogstashTcpSocketAppender;
//import net.logstash.logback.encoder.LogstashEncoder;
//import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
//import java.net.InetSocketAddress;
//
//@Slf4j
////@Configuration
//public class LogstashLoggingTcpConfiguration {
//
//    private final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//
//    private final String appName;
//    private final String serverPort;
//    private final String logstashHost;
//    private final int logstashPort;
//
//    private final int queueSize;
//
//    public LogstashLoggingTcpConfiguration(@Value("${server.port}") String serverPort,
//                                           @Value("${logstash.appname}") String appName,
//                                           @Value("${logstash.host}") String logstashHost,
//                                           @Value("${logstash.port}") int logstashPort,
//                                           @Value("${logstash.queue-size}") int queueSize,
//                                           @Value("${logstash.enabled}") boolean isLogstashEnabled) {
//        this.appName = appName;
//        this.serverPort = serverPort;
//        this.logstashHost = logstashHost;
//        this.logstashPort = logstashPort;
//        this.queueSize = queueSize;
//        if (isLogstashEnabled) {
//            addLogstashAppender(context);
//
//            // Add context listener
//            LogbackLoggerContextListener loggerContextListener = new LogbackLoggerContextListener();
//            loggerContextListener.setContext(context);
//            context.addListener(loggerContextListener);
//        }
//    }
//
//    public final void addLogstashAppender(LoggerContext context) {
//        log.info("Initializing Logstash logging");
//
//        LogstashTcpSocketAppender logstashAppender = new LogstashTcpSocketAppender();
//        logstashAppender.setName("LOGSTASH");
//        logstashAppender.setContext(context);
//        String customFields = "{\"app_name\":\"" + appName + "\",\"app_port\":\"" + serverPort + "\"}";
//
//        // Set the Logstash appender config from JHipster properties
//        logstashAppender.addDestinations(new InetSocketAddress(logstashHost, logstashPort));
//
//        LogstashEncoder encoder = new LogstashEncoder();
//        encoder.setCustomFields(customFields);
//
//        // Limit the maximum length of the forwarded stacktrace so that it won't exceed the 8KB UDP limit of logstash
//        ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
//        throwableConverter.setMaxLength(7500);
//        throwableConverter.setRootCauseFirst(true);
//        encoder.setThrowableConverter(throwableConverter);
//
//        logstashAppender.setEncoder(encoder);
//        logstashAppender.start();
//
//        // Wrap the appender in an Async appender for performance
//        AsyncAppender asyncLogstashAppender = new AsyncAppender();
//        asyncLogstashAppender.setContext(context);
//        asyncLogstashAppender.setName("ASYNC_LOGSTASH");
//        asyncLogstashAppender.setQueueSize(queueSize);
//        asyncLogstashAppender.addAppender(logstashAppender);
//        asyncLogstashAppender.start();
//
//        context.getLogger("ROOT").addAppender(asyncLogstashAppender);
//    }
//
//    /**
//     * Logback configuration is achieved by configuration file and API. When configuration file change is detected, the
//     * configuration is reset. This listener ensures that the programmatic configuration is also re-applied after
//     * reset.
//     */
//    class LogbackLoggerContextListener extends ContextAwareBase implements LoggerContextListener {
//
//        @Override
//        public boolean isResetResistant() {
//            return true;
//        }
//
//        @Override
//        public void onStart(LoggerContext context) {
//            addLogstashAppender(context);
//        }
//
//        @Override
//        public void onReset(LoggerContext context) {
//            addLogstashAppender(context);
//        }
//
//        @Override
//        public void onStop(LoggerContext context) {
//            // Nothing to do.
//        }
//
//        @Override
//        public void onLevelChange(ch.qos.logback.classic.Logger logger, Level level) {
//            // Nothing to do.
//        }
//    }
//
//}
//
