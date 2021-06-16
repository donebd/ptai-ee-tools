package com.ptsecurity.appsec.ai.ee.utils.ci.integration.plugin.jenkins.charts;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptsecurity.appsec.ai.ee.ptai.server.v36.projectmanagement.model.IssuesModel;
import com.ptsecurity.appsec.ai.ee.utils.ci.integration.BaseTest;
import com.ptsecurity.appsec.ai.ee.utils.ci.integration.ptaiserver.utils.IssuesModelHelper;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class StackedAreaChartDataModelTest extends BaseTest {
    ObjectMapper mapper;
    protected IssuesModel[] issuesModels;

    @BeforeEach
    public void pre() {
        mapper = IssuesModelHelper.createObjectMapper();
    }

    @Test
    public void testJsonConversion() {
        System.out.println("!!!!!!!!!!!!!!!!!");
        System.out.println(getClass().getClassLoader());
        System.out.println(BaseTest.class.getClassLoader());

        URLClassLoader ucl = (URLClassLoader) ClassLoader.getSystemClassLoader();
        System.out.println(ucl);
        URL[] urls = ucl.getURLs();
        for (URL url : urls) System.out.println(url.toString());



        InputStream is = ucl.getResourceAsStream("json/IssuesModel/issuesModelMap.json.7z");
        Assertions.assertNotNull(is);
        Path issuesFile = getPackedResourceFile("json/IssuesModel/issuesModelMap.json.7z");
        TypeReference<Map<String,IssuesModel>> typeRef = new TypeReference<Map<String,IssuesModel>>() {};
        Map<String, IssuesModel> issuesModelMap = Assertions.assertDoesNotThrow(
                () -> mapper.readValue(new FileInputStream(issuesFile.toFile()), typeRef),
                "IssuesModel test data load failed");
        List<Triple<Integer, LocalDateTime, IssuesModel>> issuesModelList = new ArrayList<>();
        int counter = 0;
        for (String key : issuesModelMap.keySet())
            issuesModelList.add(new ImmutableTriple<>(counter++, LocalDateTime.parse(key, DateTimeFormatter.ISO_DATE_TIME), issuesModelMap.get(key)));
        StackedAreaChartDataModel model = StackedAreaChartDataModel.create(issuesModelList);
        JSONObject jsonObject = BaseJsonChartDataModel.convertObject(model);
        System.out.println(jsonObject);
    }
}