// Copyright (c) 2021 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/io;
import ballerina/jballerina.java;

# Represents a Process error.
public type Error distinct error;

# This object contains information on a process being created from Ballerina.
# This is returned from the `exec` function.
public class Process {

    # Waits for the process to finish its work and exit.
    # ```ballerina
    # int|error exitCode = process.waitForExit();
    # ```
    #
    # + return - Returns the exit code for the process or else an `Error` if a failure occurs
    public isolated function waitForExit() returns int|Error {
        return nativeWaitForExit(self);
    }

    # Returns the exit code of the process when it has finished the execution.
    # Error if the process has not exited yet.
    # ```ballerina
    # int|error exitCode = process.exitCode();
    # ```
    #
    # + return - Returns the exit code of the process or else an `Error` if the process hasn't exited yet
    public isolated function exitCode() returns int|Error {
        return nativeExitCode(self);
    }

    # Provides a channel (to read from), which is made available as the 'standard error' of the process.
    # ```ballerina
    # io:ReadableByteChannel input = process.stderr();
    # ```
    #
    # + return - The `io:ReadableByteChannel`, which represents the process's 'standard error'
    public isolated function stderr() returns io:ReadableByteChannel {
        return nativeStderr(self);
    }
}

isolated function nativeWaitForExit(Process process) returns int | Error = @java:Method {
    name: "waitForExit",
    'class: "org.ballerinalang.stdlib.log.testutils.nativeimpl.WaitForExit"
} external;

isolated function nativeExitCode(Process process) returns int | Error = @java:Method {
    name: "exitCode",
    'class: "org.ballerinalang.stdlib.log.testutils.nativeimpl.ExitCode"
} external;

isolated function nativeStderr(Process process) returns io:ReadableByteChannel = @java:Method {
    name: "stderr",
    'class: "org.ballerinalang.stdlib.log.testutils.nativeimpl.Stderr"
} external;
