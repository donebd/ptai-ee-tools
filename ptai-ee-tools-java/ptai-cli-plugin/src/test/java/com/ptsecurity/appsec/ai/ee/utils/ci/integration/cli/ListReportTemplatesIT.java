package com.ptsecurity.appsec.ai.ee.utils.ci.integration.cli;

import com.ptsecurity.appsec.ai.ee.utils.ci.integration.cli.commands.BaseCommand;
import org.junit.jupiter.api.*;
import picocli.CommandLine;

@DisplayName("Report templates list read tests")
class ListReportTemplatesIT extends BaseIT {

    @Test
    @DisplayName("Read russian report template names")
    public void testReportTemplatesRu() {
        Integer res = new CommandLine(new Plugin()).execute(
                "list-report-templates",
                "--url", PTAI_URL,
                "--truststore", PEM_PATH.toString(),
                "--token", TOKEN,
                "--locale", "RU");
        Assertions.assertEquals(BaseCommand.ExitCode.SUCCESS.getCode(), res);
    }

    @Test
    @DisplayName("Read english report template names")
    public void testReportTemplatesEn() {
        Integer res = new CommandLine(new Plugin()).execute(
                "list-report-templates",
                "--url", PTAI_URL,
                "--truststore", PEM_PATH.toString(),
                "--token", TOKEN,
                "--locale", "EN");
        Assertions.assertEquals(BaseCommand.ExitCode.SUCCESS.getCode(), res);
    }

    @Test
    @DisplayName("Read korean report template names")
    public void testReportTemplatesKo() {
        Integer res = new CommandLine(new Plugin()).execute(
                "list-report-templates",
                "--url", PTAI_URL,
                "--truststore", PEM_PATH.toString(),
                "--token", TOKEN,
                "--locale", "KO");
        Assertions.assertEquals(BaseCommand.ExitCode.INVALID_INPUT.getCode(), res);
    }

}