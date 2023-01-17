package com.ptsecurity.appsec.ai.ee.server.v411;

import com.ptsecurity.appsec.ai.ee.server.v411.helpers.ApiHelper;
import com.ptsecurity.appsec.ai.ee.server.v411.projectmanagement.model.BaseProjectSettingsModel;
import com.ptsecurity.appsec.ai.ee.server.v411.projectmanagement.model.ProgrammingLanguageGroup;
import com.ptsecurity.appsec.ai.ee.server.v411.projectmanagement.model.WhiteBoxSettingsModel;
import com.ptsecurity.misc.tools.BaseTest;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static com.ptsecurity.appsec.ai.ee.server.helpers.AbstractApiHelper.TokenType.CI;
import static com.ptsecurity.appsec.ai.ee.server.helpers.AbstractApiHelper.TokenType.ROOT;
import static com.ptsecurity.appsec.ai.ee.server.helpers.AbstractApiHelper.checkApiCall;
import static com.ptsecurity.appsec.ai.ee.server.v411.helpers.ApiHelper.PROJECTS;
import static com.ptsecurity.appsec.ai.ee.server.v411.helpers.ApiHelper.STORE;
import static com.ptsecurity.appsec.ai.ee.utils.ci.integration.Project.PHP_SMOKE;
import static com.ptsecurity.misc.tools.helpers.CallHelper.call;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public abstract class AbstractTest extends BaseTest {
    protected static final ApiHelper API = new ApiHelper();
    protected static final String PROJECT_NAME = PHP_SMOKE.getName() + "-" + UUID.randomUUID();
    protected static UUID PROJECT_ID;

    public static void init() {
        BaseTest.init();
    }

    protected static void authenticate() {
        API.authenticate();
    }

    protected static void createTestProject() {
        log.trace("Test get default project settings");
        ApiHelper.setJwt(CI);
        BaseProjectSettingsModel defaultProjectSettings = call(
                PROJECTS::apiProjectsDefaultSettingsGet,
                "Get default project settings API call failed");

        log.trace("Test project creation");
        BaseProjectSettingsModel projectSettings = defaultProjectSettings
                .name(PROJECT_NAME)
                .programmingLanguageGroup(ProgrammingLanguageGroup.PHP)
                .whiteBox(new WhiteBoxSettingsModel()
                        .searchForVulnerableComponentsEnabled(false)
                        .searchForVulnerableSourceCodeEnabled(false)
                        .patternMatchingEnabled(false)
                        .dataFlowAnalysisEnabled(false)
                        .searchForConfigurationFlawsEnabled(true));
        PROJECT_ID = call(
                () -> PROJECTS.apiProjectsBasePost(projectSettings),
                "Base project create API call failed");
        assertNotNull(PROJECT_ID);

        call(
                () -> STORE.apiStoreProjectIdSourcesPost(PROJECT_ID, true, true, PHP_SMOKE.getZip().toFile()),
                "Zipped project sources store API call failed");
    }

    public static void deleteTestProject() {
        log.trace("Check project delete calls");
        assertNotNull(PROJECT_ID);
        checkApiCall(() -> PROJECTS.apiProjectsProjectIdDelete(PROJECT_ID), ROOT);
    }
}