package com.xfheaderpoc.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;

@Slf4j
@Model(adaptables = {SlingHttpServletRequest.class},
  adapters = {ComponentExporter.class},
  resourceType = FlyoutContent.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
  extensions = ExporterConstants.SLING_MODEL_EXTENSION, options = {
  @ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
  @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false")})
@JsonSerialize(as = FlyoutContent.class)
public class FlyoutContent extends AbstractComponentImpl implements ComponentExporter {
  static final String RESOURCE_TYPE = "xfheaderpoc/components/flyout-content";
  @Getter
  @ValueMapValue
  protected String alt;
  @Getter
  @ValueMapValue
  protected String fileReference;
  @Getter
  @ValueMapValue
  protected String heading;
  @Getter
  @ValueMapValue
  protected String text;

  @Nonnull
  @Override
  public String getExportedType() {
    return RESOURCE_TYPE;
  }

  @PostConstruct
  public void initModel() {
    Document document = Jsoup.parse(text);
    document.select("ul").addClass("flyout__unordered-list");
    text = document.body().html();
  }
}
