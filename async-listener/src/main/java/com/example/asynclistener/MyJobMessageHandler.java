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

import com.fasterxml.jackson.databind.JsonNode;
import org.flowable.job.service.impl.history.async.message.AsyncHistoryJobMessageHandler;
import org.flowable.job.service.impl.persistence.entity.HistoryJobEntity;

public class MyJobMessageHandler implements AsyncHistoryJobMessageHandler {

    @Override
    public boolean handleJob(HistoryJobEntity historyJobEntity, JsonNode historyData) {
        System.out.println("Handling job " + historyJobEntity.getId() + ", data = " + historyData);

        return true;
    }

}
