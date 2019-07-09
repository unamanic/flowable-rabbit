/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.asynclistener;


import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.job.service.impl.history.async.message.AsyncHistoryJobMessageReceiver;

public class Receiver {

    private AsyncHistoryJobMessageReceiver asyncHistoryJobMessageReceiver;
    private ProcessEngineConfiguration processEngineConfiguration;

    public void receiveMessage(byte[] messageBytes) {
        receiveMessage(new String(messageBytes));
    }

    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        asyncHistoryJobMessageReceiver.setCommandExecutor(processEngineConfiguration.getCommandExecutor());
        asyncHistoryJobMessageReceiver.messageForJobReceived(message);
    }

    public AsyncHistoryJobMessageReceiver getAsyncHistoryJobMessageReceiver() {
        return asyncHistoryJobMessageReceiver;
    }

    public void setAsyncHistoryJobMessageReceiver(AsyncHistoryJobMessageReceiver asyncHistoryJobMessageReceiver) {
        this.asyncHistoryJobMessageReceiver = asyncHistoryJobMessageReceiver;
    }

    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}
