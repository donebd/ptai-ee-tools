package com.ptsecurity.appsec.ai.ee.scan.settings.v11;

import com.ptsecurity.appsec.ai.ee.helpers.aiproj.AiProjHelper;
import com.ptsecurity.appsec.ai.ee.scan.result.ScanBrief;
import com.ptsecurity.appsec.ai.ee.scan.settings.UnifiedAiProjScanSettings;
import com.ptsecurity.appsec.ai.ee.scan.settings.aiproj.*;
import com.ptsecurity.misc.tools.exceptions.GenericException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static com.ptsecurity.appsec.ai.ee.scan.settings.UnifiedAiProjScanSettings.JavaSettings.JavaVersion.v1_11;
import static com.ptsecurity.appsec.ai.ee.scan.settings.UnifiedAiProjScanSettings.JavaSettings.JavaVersion.v1_8;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
public class AiProjScanSettings implements UnifiedAiProjScanSettings {
    @Override
    public @NonNull String getProjectName() {
        return projectName;
    }

    @Override
    public ScanBrief.ScanSettings.@NonNull Language getProgrammingLanguage() {
        return PROGRAMMING_LANGUAGE_MAP.get(programmingLanguage);
    }

    @Accessors(fluent = true)
    @RequiredArgsConstructor
    private enum ScanAppType {
        PHP("Php"),
        JAVA("Java"),
        CSHARP("CSharp"),
        CONFIGURATION("Configuration"),
        FINGERPRINT("Fingerprint"),
        DEPENDENCYCHECK("DependencyCheck"),
        PMTAINT("PmTaint"),
        BLACKBOX("BlackBox"),
        JAVASCRIPT("JavaScript");

        @Getter
        private final String value;
        private static final Map<String, com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType> VALUES = new HashMap<>();

        static {
            for (com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType f : values()) VALUES.put(f.value, f);
        }

        public static com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType from(@NonNull final String value) {
            return VALUES.get(value);
        }
    }

    /**
     * Set of ScanAppType values that support abstract interpretation
     */
    private static final Set<com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType> SCAN_APP_TYPE_AI = new HashSet<>(Arrays.asList(
            com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType.PHP,
            com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType.JAVA,
            com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType.CSHARP,
            com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType.JAVASCRIPT));
    /**
     * Set of programming languages values that support abstract interpretation
     */
    private static final Set<ScanBrief.ScanSettings.Language> LANGUAGE_AI = new HashSet<>(Arrays.asList(
            ScanBrief.ScanSettings.Language.PHP,
            ScanBrief.ScanSettings.Language.JAVA,
            ScanBrief.ScanSettings.Language.CSHARP,
            ScanBrief.ScanSettings.Language.VB,
            ScanBrief.ScanSettings.Language.JAVASCRIPT));

    private static final Map<AiprojLegacy.ProgrammingLanguage, ScanBrief.ScanSettings.Language> PROGRAMMING_LANGUAGE_MAP = new HashMap<>();
    private static final Map<AiprojLegacy.ProjectType, UnifiedAiProjScanSettings.DotNetSettings.ProjectType> DOTNET_PROJECT_TYPE_MAP = new HashMap<>();
    private static final Map<Integer, BlackBoxSettings.ProxySettings.Type> BLACKBOX_PROXY_TYPE_MAP = new HashMap<>();
    private static final Map<AiprojLegacy.Level, BlackBoxSettings.ScanLevel> BLACKBOX_SCAN_LEVEL_MAP = new HashMap<>();
    private static final Map<Integer, UnifiedAiProjScanSettings.BlackBoxSettings.Authentication.Type> BLACKBOX_AUTH_TYPE_MAP = new HashMap<>();


    static {
        PROGRAMMING_LANGUAGE_MAP.put(AiprojLegacy.ProgrammingLanguage.C_PLUS_PLUS, ScanBrief.ScanSettings.Language.CPP);
        PROGRAMMING_LANGUAGE_MAP.put(AiprojLegacy.ProgrammingLanguage.GO, ScanBrief.ScanSettings.Language.GO);
        PROGRAMMING_LANGUAGE_MAP.put(AiprojLegacy.ProgrammingLanguage.JAVA_SCRIPT, ScanBrief.ScanSettings.Language.JAVASCRIPT);
        PROGRAMMING_LANGUAGE_MAP.put(AiprojLegacy.ProgrammingLanguage.C_SHARP, ScanBrief.ScanSettings.Language.CSHARP);
        PROGRAMMING_LANGUAGE_MAP.put(AiprojLegacy.ProgrammingLanguage.JAVA, ScanBrief.ScanSettings.Language.JAVA);
        PROGRAMMING_LANGUAGE_MAP.put(AiprojLegacy.ProgrammingLanguage.KOTLIN, ScanBrief.ScanSettings.Language.KOTLIN);
        PROGRAMMING_LANGUAGE_MAP.put(AiprojLegacy.ProgrammingLanguage.SQL, ScanBrief.ScanSettings.Language.SQL);
        PROGRAMMING_LANGUAGE_MAP.put(AiprojLegacy.ProgrammingLanguage.PYTHON, ScanBrief.ScanSettings.Language.PYTHON);
        PROGRAMMING_LANGUAGE_MAP.put(AiprojLegacy.ProgrammingLanguage.SWIFT, ScanBrief.ScanSettings.Language.SWIFT);
        PROGRAMMING_LANGUAGE_MAP.put(AiprojLegacy.ProgrammingLanguage.VB, ScanBrief.ScanSettings.Language.VB);
        PROGRAMMING_LANGUAGE_MAP.put(AiprojLegacy.ProgrammingLanguage.PHP, ScanBrief.ScanSettings.Language.PHP);
        PROGRAMMING_LANGUAGE_MAP.put(AiprojLegacy.ProgrammingLanguage.OBJECTIVE_C, ScanBrief.ScanSettings.Language.OBJECTIVEC);

        DOTNET_PROJECT_TYPE_MAP.put(AiprojLegacy.ProjectType.NONE, DotNetSettings.ProjectType.NONE);
        DOTNET_PROJECT_TYPE_MAP.put(AiprojLegacy.ProjectType.SOLUTION, DotNetSettings.ProjectType.SOLUTION);
        DOTNET_PROJECT_TYPE_MAP.put(AiprojLegacy.ProjectType.WEB_SITE, DotNetSettings.ProjectType.WEBSITE);

        BLACKBOX_PROXY_TYPE_MAP.put(0, BlackBoxSettings.ProxySettings.Type.HTTP);
        BLACKBOX_PROXY_TYPE_MAP.put(1, BlackBoxSettings.ProxySettings.Type.HTTPNOCONNECT);
        BLACKBOX_PROXY_TYPE_MAP.put(2, BlackBoxSettings.ProxySettings.Type.SOCKS4);
        BLACKBOX_PROXY_TYPE_MAP.put(3, BlackBoxSettings.ProxySettings.Type.SOCKS5);

        BLACKBOX_SCAN_LEVEL_MAP.put(AiprojLegacy.Level.NONE, BlackBoxSettings.ScanLevel.NONE);
        BLACKBOX_SCAN_LEVEL_MAP.put(AiprojLegacy.Level.FAST, BlackBoxSettings.ScanLevel.FAST);
        BLACKBOX_SCAN_LEVEL_MAP.put(AiprojLegacy.Level.NORMAL, BlackBoxSettings.ScanLevel.NORMAL);
        BLACKBOX_SCAN_LEVEL_MAP.put(AiprojLegacy.Level.FULL, BlackBoxSettings.ScanLevel.FULL);

        BLACKBOX_AUTH_TYPE_MAP.put(0, BlackBoxSettings.Authentication.Type.FORM);
        BLACKBOX_AUTH_TYPE_MAP.put(1, BlackBoxSettings.Authentication.Type.HTTP);
        BLACKBOX_AUTH_TYPE_MAP.put(2, BlackBoxSettings.Authentication.Type.NONE);
        BLACKBOX_AUTH_TYPE_MAP.put(3, BlackBoxSettings.Authentication.Type.COOKIE);
    }

    @Override
    public Set<ScanModule> getScanModules() {
        return scanModules
        Set<ScanModule> res = new HashSet<>();
        Set<com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType> scanAppTypes = Arrays.stream(scanAppType.split("[, ]+"))
                .map(com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType::from)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        // See internal wiki pageId=193599549
        // "Vulnerable authentication code" checkbox means that we either enabled AI-supported PHP / Java / C# / JS scan mode ...
        boolean abstractInterpretationCoreUsed = scanAppTypes.stream().anyMatch(SCAN_APP_TYPE_AI::contains);
        // ... or all other languages with PmTaint / UseTaintAnalysis enabled
        boolean taintOnlyLanguageUsed = !LANGUAGE_AI.contains(getProgrammingLanguage())
                && scanAppTypes.contains(com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType.PMTAINT)
                && TRUE.equals(useTaintAnalysis);
        if (abstractInterpretationCoreUsed || taintOnlyLanguageUsed) res.add(ScanModule.VULNERABLESOURCECODE);
        if (TRUE.equals(useTaintAnalysis) && scanAppTypes.contains(com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType.PMTAINT))
            res.add(ScanModule.DATAFLOWANALYSIS);
        if (TRUE.equals(usePmAnalysis) && scanAppTypes.contains(com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType.PMTAINT))
            res.add(ScanModule.PATTERNMATCHING);
        if (scanAppTypes.contains(com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType.CONFIGURATION)) res.add(ScanModule.CONFIGURATION);
        if (scanAppTypes.contains(com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType.BLACKBOX)) res.add(ScanModule.BLACKBOX);
        if (scanAppTypes.contains(com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType.DEPENDENCYCHECK) || scanAppTypes.contains(com.ptsecurity.appsec.ai.ee.scan.settings.legacy.AiProjScanSettings.ScanAppType.FINGERPRINT))
            res.add(ScanModule.COMPONENTS);
        return res;
    }

    private BlackBoxSettings.ProxySettings convert(@NonNull final ProxySettings proxySettings) {
        return BlackBoxSettings.ProxySettings.builder()
                .enabled(TRUE.equals(proxySettings.isEnabled))
                .type(BLACKBOX_PROXY_TYPE_MAP.get(proxySettings.type))
                .host(proxySettings.host)
                .port(proxySettings.port)
                .login(proxySettings.username)
                .password(proxySettings.password)
                .build();
    }

    private BlackBoxSettings.Authentication convert(final Authentication authentication) {
        log.trace("Check if AIPROJ authentication field is defined");
        if (null == authentication || null == authentication.authItem || null == authentication.authItem.credentials)
            return new BlackBoxSettings.Authentication();
        @NonNull AuthItem authItem = authentication.authItem;
        BlackBoxSettings.Authentication.Type authType = BLACKBOX_AUTH_TYPE_MAP.getOrDefault(authItem.credentials.type, BlackBoxSettings.Authentication.Type.NONE);

        if (BlackBoxSettings.Authentication.Type.FORM == authType)
            return isEmpty(authItem.formXpath)
                    ? BlackBoxSettings.FormAuthenticationAuto.builder()
                    .type(authType)
                    .formAddress(authItem.formUrl)
                    .login(null != authItem.credentials.login ? authItem.credentials.login.value : null)
                    .password(null != authItem.credentials.password ? authItem.credentials.password.value : null)
                    .validationTemplate(authItem.regexpOfSuccess)
                    .build()
                    : BlackBoxSettings.FormAuthenticationManual.builder()
                    .type(authType)
                    .formAddress(authItem.formUrl)
                    .xPath(authItem.formXpath)
                    .loginKey(null != authItem.credentials.login ? authItem.credentials.login.name : null)
                    .login(null != authItem.credentials.login ? authItem.credentials.login.value : null)
                    .passwordKey(null != authItem.credentials.password ? authItem.credentials.password.name : null)
                    .password(null != authItem.credentials.password ? authItem.credentials.password.value : null)
                    .validationTemplate(authItem.regexpOfSuccess)
                    .build();
        else if (BlackBoxSettings.Authentication.Type.HTTP == authType)
            return BlackBoxSettings.HttpAuthentication.builder()
                    .login(null != authItem.credentials.login ? authItem.credentials.login.value : null)
                    .password(null != authItem.credentials.password ? authItem.credentials.password.value : null)
                    .validationAddress(authItem.testUrl)
                    .build();
        else if (BlackBoxSettings.Authentication.Type.COOKIE == authType)
            return BlackBoxSettings.CookieAuthentication.builder()
                    .cookie(authItem.credentials.cookie)
                    .validationAddress(authItem.testUrl)
                    .validationTemplate(authItem.regexpOfSuccess)
                    .build();
        else
            return new BlackBoxSettings.Authentication();
    }

    private List<Pair<String, String>> convert(@NonNull final List<List<String>> headers) {
        List<Pair<String, String>> res = new ArrayList<>();
        for (List<String> headerNameAndValues : headers) {
            if (CollectionUtils.isEmpty(headerNameAndValues)) {
                log.trace("Skip empty headers");
                continue;
            }
            if (isEmpty(headerNameAndValues.get(0))) {
                log.trace("Skip header with empty name");
                continue;
            }
            for (int i = 1; i < headerNameAndValues.size(); i++)
                res.add(
                        new ImmutablePair<>(headerNameAndValues.get(0), headerNameAndValues.get(i)));
        }
        return CollectionUtils.isEmpty(res) ? null : res;
    }

    @Override
    public BlackBoxSettings getBlackBoxSettings() {
        if (!getScanModules().contains(ScanModule.BLACKBOX)) return null;

        BlackBoxSettings blackBoxSettings = new BlackBoxSettings();

        if (null != level) blackBoxSettings.setScanLevel(BLACKBOX_SCAN_LEVEL_MAP.get(level));
        blackBoxSettings.setRunAutocheckAfterScan(TRUE.equals(runAutocheckAfterScan));

        blackBoxSettings.setSite(site);
        if (null != proxySettings)
            blackBoxSettings.setProxySettings(convert(proxySettings));
        if (null != customHeaders)
            blackBoxSettings.setHttpHeaders(convert(customHeaders));
        if (null != authentication)
            blackBoxSettings.setAuthentication(convert(authentication));

        if (!blackBoxSettings.getRunAutocheckAfterScan()) return blackBoxSettings;

        blackBoxSettings.setAutocheckSite(autocheckSite);
        if (null != autocheckProxySettings)
            blackBoxSettings.setAutocheckProxySettings(convert(autocheckProxySettings));
        if (null != autocheckCustomHeaders)
            blackBoxSettings.setAutocheckHttpHeaders(convert(autocheckCustomHeaders));
        if (null != this.autocheckAuthentication)
            blackBoxSettings.setAutocheckAuthentication(convert(autocheckAuthentication));
        return blackBoxSettings;
    }

    @Override
    public @NonNull Boolean isDownloadDependencies() {
        return TRUE.equals(isDownloadDependencies);
    }

    @Override
    public @NonNull Boolean isUsePublicAnalysisMethod() {
        return TRUE.equals(isUsePublicAnalysisMethod);
    }

    @Override
    public String getCustomParameters() {
        return customParameters;
    }

    @Override
    public DotNetSettings getDotNetSettings() {

        return DotNetSettings.builder()
                .solutionFile(AiProjHelper.fixSolutionFile(solutionFile))
                .webSiteFolder(webSiteFolder)
                .projectType(DOTNET_PROJECT_TYPE_MAP.getOrDefault(projectType, DotNetSettings.ProjectType.NONE))
                .build();
    }

    @Override
    public JavaSettings getJavaSettings() {
        AiProjHelper.JavaParametersParseResult parseResult = AiProjHelper.parseJavaParameters(javaParameters);
        return JavaSettings.builder()
                .unpackUserPackages(TRUE.equals(isUnpackUserPackages))
                .userPackagePrefixes(null == parseResult ? null : parseResult.getPrefixes())
                .javaVersion(AiprojLegacy.JavaVersion._0.equals(javaVersion) ? v1_8 : v1_11)
                .parameters(null == parseResult ? null : parseResult.getOther())
                .build();
    }

    @Override
    public @NonNull Boolean isSkipGitIgnoreFiles() {
        if (CollectionUtils.isEmpty(skipFilesFolders)) return false;
        return skipFilesFolders.contains(".gitignore");
    }

    @Override
    public @NonNull Boolean isUseSastRules() {
        throw GenericException.raise("No custom SAST rules support for legacy AIPROJ schema", new UnsupportedOperationException());
    }

    @Override
    public @NonNull Boolean isUseCustomPmRules() {
        throw GenericException.raise("No custom PM rules support for legacy AIPROJ schema", new UnsupportedOperationException());
    }

    @Override
    public @NonNull Boolean isUseCustomYaraRules() {
        return TRUE.equals(useCustomYaraRules);
    }

    @Override
    public @NonNull Boolean isUseSecurityPolicies() {
        throw GenericException.raise("No security policy support for legacy AIPROJ schema", new UnsupportedOperationException());
    }

    @Override
    public MailingProjectSettings getMailingProjectSettings() {
        throw GenericException.raise("No mail settings support for legacy AIPROJ schema", new UnsupportedOperationException());
    }

    @Override
    public void load(@NonNull String data) throws GenericException {
        Object dataObject = JsonPath.parse(data).read("$[?(@.id == 2)]");
        String dataString = dataObject.toString();

    }

    @Override
    public UnifiedAiProjScanSettings.Version getVersion() {
        return UnifiedAiProjScanSettings.Version.V11;
    }
}
