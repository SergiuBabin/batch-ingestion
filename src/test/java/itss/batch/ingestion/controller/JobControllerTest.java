package itss.batch.ingestion.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class JobControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void postStartJobTestSuccess() throws Exception {
        mockMvc.perform(post("/api/fullIngestionJob")
                        .content("{\n" +
                                "    \"userExternalId\": \"30005\",\n" +
                                "    \"serviceAgrementId\": \"3213124-fsadas-321-bbbsdacasq\",\n" +
                                "    \"skipJobs\": {\n" +
                                "        \"skipContacts\": false,\n" +
                                "        \"skipTransfers\": false,\n" +
                                "        \"skipSavings\": false\n" +
                                "    }\n" +
                                "}")
                        .accept("application/json")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    void postStatusJobTestSuccess() throws Exception {
        mockMvc.perform(post("/api/fullIngestionJob")
                        .content("{\n" +
                                "    \"userExternalId\": \"30005\",\n" +
                                "    \"serviceAgrementId\": \"3213124-fsadas-321-bbbsdacasq\",\n" +
                                "    \"skipJobs\": {\n" +
                                "        \"skipContacts\": false,\n" +
                                "        \"skipTransfers\": true,\n" +
                                "        \"skipSavings\": true\n" +
                                "    }\n" +
                                "}")
                        .accept("application/json")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        mockMvc.perform(post("/api/status")
                        .content("{\n" +
                                "    \"contactsJobId\": \"1\"\n" +
                                "}")
                        .accept("application/json")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    void postStatusJobTestBadRequest() throws Exception {
        mockMvc.perform(post("/api/fullIngestionJob")
                        .content("{\n" +
                                "    \"userExternalId\": \"30005\",\n" +
                                "    \"serviceAgrementId\": \"3213124-fsadas-321-bbbsdacasq\",\n" +
                                "    \"skipJobs\": {\n" +
                                "        \"skipContacts\": false,\n" +
                                "        \"skipTransfers\": true,\n" +
                                "        \"skipSavings\": true\n" +
                                "    }\n" +
                                "}")
                        .accept("application/json")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        mockMvc.perform(post("/api/status")
                        .content("{\n" +
                                "    \"contactsJobId\": \"5\"\n" +
                                "}")
                        .accept("application/json")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}