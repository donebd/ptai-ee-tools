package com.ptsecurity.appsec.ai.ee.utils.ci.integration.plugin.jenkins.reports;

import com.ptsecurity.appsec.ai.ee.utils.ci.integration.Messages;
import com.ptsecurity.appsec.ai.ee.utils.ci.integration.ptaiserver.v36.Reports;
import hudson.Extension;
import hudson.util.ListBoxModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.Arrays;

@EqualsAndHashCode
@ToString
public class Data extends BaseReport {
    @Getter
    private final String locale;

    @Getter
    private final String format;

    @Getter
    private final String fileName;

    @Getter
    private final String filter;

    @DataBoundConstructor
    public Data(final String format, final String fileName,
                final String locale, final String filter) {
        this.locale = locale;
        this.format = format;
        this.fileName = fileName;
        this.filter = filter;
    }

    @Symbol("Data")
    @Extension
    public static class DataDescriptor extends BaseReportDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.i18n_reporting_data_caption();
        }

        public ListBoxModel doFillFormatItems() {
            ListBoxModel model = new ListBoxModel();
            Arrays.stream(Reports.Data.Format.values())
                    .forEach(f -> model.add(f.getValue().getValue(), f.name()));
            return model;
        }

        public ListBoxModel doFillLocaleItems() {
            ListBoxModel model = new ListBoxModel();
            model.add(Messages.captions_locale_english_displayName(), Reports.Locale.EN.name());
            model.add(Messages.captions_locale_russian_displayName(), Reports.Locale.RU.name());
            return model;
        }
    }
}