/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.stdlib.log;

import io.ballerina.runtime.observability.ObserveUtils;
import org.ballerinalang.logging.BLogManager;
import org.ballerinalang.logging.util.BLogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.logging.LogManager;

/**
 * Base class for the other log functions, containing a getter to retrieve the correct logger, given a package name.
 *
 * @since 0.95.0
 */
public abstract class AbstractLogFunction {

    protected static final BLogManager LOG_MANAGER = (BLogManager) LogManager.getLogManager();

    private static final Logger ballerinaRootLogger = LoggerFactory.getLogger(BLogManager.BALLERINA_ROOT_LOGGER_NAME);

    protected static Logger getLogger(String pkg, String loggerName) {
        if (".".equals(pkg) || pkg == null) {
            return LoggerFactory.getLogger(ballerinaRootLogger.getName() + "." + loggerName);
        } else {
            return LoggerFactory.getLogger(ballerinaRootLogger.getName() + "." + pkg + "." + loggerName);
        }
    }

    /**
     * Execute logging provided message.
     *
     * @param message  log message
     * @param logLevel log level
     * @param pckg package
     * @param consumer log message consumer
     */
    static void logMessage(Object message, BLogLevel logLevel, String pckg,
                           BiConsumer<String, String> consumer) {
        // Create a new log message supplier
        Supplier<String> logMessage = new Supplier<String>() {
            private String msg = null;

            @Override
            public String get() {
                // We should invoke the lambda only once, thus caching return value
                if (msg == null) {
                    Object arg = message;
                    msg = arg.toString();
                }
                return msg;
            }
        };
        consumer.accept(pckg, logMessage.get());
        ObserveUtils.logMessageToActiveSpan(logLevel.name(), logMessage, logLevel == BLogLevel.ERROR);
    }

    static String getPackagePath() {
        String className = Thread.currentThread().getStackTrace()[5].getClassName();
        String[] pkgData = className.split("\\.");
        if (pkgData.length > 1) {
            return pkgData[0] + "/" + pkgData[1];
        }
        return ".";
    }
}
